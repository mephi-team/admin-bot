package team.mephi.adminbot.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.mephi.adminbot.model.*;
import team.mephi.adminbot.repository.BroadcastRepository;
import team.mephi.adminbot.repository.DialogRepository;
import team.mephi.adminbot.repository.QuestionRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Configuration
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DialogRepository dialogRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private BroadcastRepository broadcastRepository;

    @Bean
    public ApplicationRunner initTestData() {
        return args -> {
            boolean hasUsers = userRepository.count() > 0;
            boolean hasDialogs = dialogRepository.count() > 0;
            boolean hasQuestions = questionRepository.count() > 0;
            boolean hasBroadcasts = broadcastRepository.count() > 0;

            if (!hasUsers || !hasDialogs || !hasQuestions || !hasBroadcasts) {
                System.out.println("🔁 Предзаполнение БД тестовыми данными...");

                if (!hasUsers) initUsers();
                if (!hasQuestions) initQuestions();
                if (!hasBroadcasts) initBroadcasts();
                if (!hasDialogs) initDialogs(); // зависит от пользователей

                System.out.println("✅ Тестовые данные успешно созданы.");
            }
        };
    }

    private void initUsers() {
        List<User> users = Arrays.asList(
                User.builder().externalId("tg_1001").name("Анна Смирнова").status("active").build(),
                User.builder().externalId("tg_1002").name("Иван Петров").status("active").build(),
                User.builder().externalId("tg_1003").name("Мария Козлова").status("blocked").build(),
                User.builder().externalId("tg_1004").name("Алексей Иванов").status("active").build(),
                User.builder().externalId("tg_1005").name("Екатерина Волкова").status("active").build()
        );
        userRepository.saveAll(users);
        System.out.println("  → Создано 5 пользователей");
    }

    private void initQuestions() {
        List<Question> questions = Arrays.asList(
                Question.builder().questionText("Как поступить в Flexiq?").answerText("Подайте заявку на сайте и пройдите техническое тестирование.").build(),
                Question.builder().questionText("Сколько длится обучение?").answerText("Программы длятся от 3 до 6 месяцев в зависимости от направления.").build(),
                Question.builder().questionText("Есть ли рассрочка?").answerText("Да, мы предлагаем рассрочку до 12 месяцев без процентов.").build(),
                Question.builder().questionText("Нужен ли опыт для поступления?").answerText("Нет, наши курсы рассчитаны на начинающих.").build(),
                Question.builder().questionText("Выдают ли диплом?").answerText("По окончании вы получаете сертификат установленного образца.").build()
        );
        // Устанавливаем createdAt вручную, если в конструкторе не задано
        questions.forEach(q -> q.setCreatedAt(LocalDateTime.now().minusDays(new Random().nextInt(10))));
        questionRepository.saveAll(questions);
        System.out.println("  → Создано 5 вопросов");
    }

    private void initBroadcasts() {
        List<Broadcast> broadcasts = Arrays.asList(
                Broadcast.builder().messageText("Добро пожаловать в Flexiq! Начните обучение уже сегодня.").build(),
                Broadcast.builder().messageText("Напоминаем: завтра стартует новый поток по Java-разработке!").build(),
                Broadcast.builder().messageText("Специальное предложение: скидка 15% на все курсы до конца недели.").build()
        );
        broadcasts.forEach(b -> b.setCreatedAt(LocalDateTime.now().minusDays(new Random().nextInt(5))));
        broadcastRepository.saveAll(broadcasts);
        System.out.println("  → Создано 3 рассылки");
    }

    private void initDialogs() {
        List<User> users = userRepository.findAll();
        Random random = new Random();

        for (User user : users) {
            // Создаём 1–3 диалога на пользователя
            int dialogCount = 1 + random.nextInt(3);
            for (int i = 0; i < dialogCount; i++) {
                Dialog dialog = new Dialog();
                dialog.setUserId(user.getExternalId());
                dialog.setUserName(user.getName());

                // Случайная дата за последние 10 дней
                long randomDays = random.nextInt(10);
                long randomHours = random.nextInt(24);
                LocalDateTime startedAt = LocalDateTime.now()
                        .minusDays(randomDays)
                        .minusHours(randomHours)
                        .truncatedTo(ChronoUnit.MINUTES);
                dialog.setStartedAt(startedAt);

                // Сообщения в диалоге
                List<Message> messages = new ArrayList<>();
                String[] userMessages = {
                        "Как поступить в Flexiq?",
                        "Сколько длится обучение?",
                        "Есть ли рассрочка?"
                };
                String question = userMessages[random.nextInt(userMessages.length)];
                String answer = getAnswerForQuestion(question);

                // Сообщение от пользователя
                Message userMsg = new Message();
                userMsg.setDialog(dialog);
                userMsg.setText(question);
                userMsg.setSender("user");
                userMsg.setTimestamp(startedAt);
                messages.add(userMsg);

                // Ответ от бота (с небольшой задержкой)
                Message botMsg = new Message();
                botMsg.setDialog(dialog);
                botMsg.setText(answer);
                botMsg.setSender("bot");
                botMsg.setTimestamp(startedAt.plusSeconds(2));
                messages.add(botMsg);

                dialog.setMessages(messages);
                dialogRepository.save(dialog);
            }
        }
        System.out.println("  → Созданы диалоги и сообщения");
    }

    private String getAnswerForQuestion(String question) {
        if (question.contains("поступить")) {
            return "Подайте заявку на сайте и пройдите техническое тестирование.";
        } else if (question.contains("длится")) {
            return "Программы длятся от 3 до 6 месяцев в зависимости от направления.";
        } else if (question.contains("рассрочка")) {
            return "Да, мы предлагаем рассрочку до 12 месяцев без процентов.";
        }
        return "Спасибо за ваш вопрос! Наш менеджер свяжется с вами.";
    }
}