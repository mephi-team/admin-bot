package team.mephi.adminbot.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.mephi.adminbot.model.*;
import team.mephi.adminbot.model.enums.MailingStatus;
import team.mephi.adminbot.model.enums.SenderType;
import team.mephi.adminbot.repository.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Configuration
public class DataInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DialogRepository dialogRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private MailingRepository mailingRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Bean
    public ApplicationRunner initTestData() {
        return args -> {
            boolean hasDirections = directionRepository.count() > 0;
            boolean hasRoles = roleRepository.count() > 0;
            boolean hasUsers = userRepository.count() > 0;
            boolean hasDialogs = dialogRepository.count() > 0;
            boolean hasQuestions = questionRepository.count() > 0;
            boolean hasBroadcasts = mailingRepository.count() > 0;
            boolean hasTutors = tutorRepository.count() > 0;

            if (!hasUsers || !hasDialogs || !hasQuestions || !hasBroadcasts) {
                System.out.println("🔁 Предзаполнение БД тестовыми данными...");

                if (!hasDirections) initDirections();
                if (!hasRoles) initRoles();
                if (!hasUsers) initUsers();
                if (!hasQuestions) initQuestions();
                if (!hasBroadcasts) initBroadcasts();
                if (!hasDialogs) initDialogs(); // зависит от пользователей
                if (!hasTutors) initTutors();

                System.out.println("✅ Тестовые данные успешно созданы.");
            }
        };
    }

    private void initRoles() {
        List<Role> roles = Arrays.asList(
                Role.builder().name("student").description("Студенты").build(),
                Role.builder().name("candidate").description("Кандидаты").build(),
                Role.builder().name("visitor").description("Посетитель").build(),
                Role.builder().name("free_listener").description("Слушатели").build(),
                Role.builder().name("middle_candidate").description("Миддл-кандидаты").build(),
                Role.builder().name("lc_expert").description("Эксперты").build(),
                Role.builder().name("extuser").description("Внешний пользователь").build()
        );
        roleRepository.saveAll(roles);
        System.out.println("  → Создано 5 ролей");
    }

    private void initDirections() {
        List<Direction> directions = Arrays.asList(
                Direction.builder().code("java").name("Java").build(),
                Direction.builder().code("analytics").name("Analytics").build(),
                Direction.builder().code("python").name("Python").build(),
                Direction.builder().code("cpp").name("C++").build(),
                Direction.builder().code("js").name("JavaScrypt").build()
        );
        directionRepository.saveAll(directions);
        System.out.println("  → Создано 5 направлений");
    }

    private void initUsers() {
        // Получаем роли по имени
        Role studentRole = roleRepository.findByName("student")
                .orElseThrow(() -> new RuntimeException("Роль 'student' не найдена"));
        Role candidateRole = roleRepository.findByName("candidate")
                .orElseThrow(() -> new RuntimeException("Роль 'candidate' не найдена"));
        Role visitorRole = roleRepository.findByName("visitor")
                .orElseThrow(() -> new RuntimeException("Роль 'visitor' не найдена"));
        Role freeListenerRole = roleRepository.findByName("free_listener")
                .orElseThrow(() -> new RuntimeException("Роль 'free_listener' не найдена"));

        List<User> users = Arrays.asList(
                User.builder().tgId("tg_1001").userName("Анна Смирнова").firstName("Анна").lastName("Смирнова").role(studentRole).status("active").build(),
                User.builder().tgId("tg_1002").userName("Иван Петров").firstName("Иван").lastName("Петров").role(candidateRole).status("active").build(),
                User.builder().tgId("tg_1003").userName("Мария Козлова").firstName("Мария").lastName("Козлова").role(studentRole).status("blocked").build(),
                User.builder().tgId("tg_1004").userName("Алексей Иванов").firstName("Алексей").lastName("Иванов").role(candidateRole).status("active").build(),
                User.builder().tgId("tg_1005").userName("Екатерина Волкова").firstName("Екатерина").lastName("Волкова").role(studentRole).status("active").build(),
                User.builder().tgId("tg_1006").userName("Анна Козлова").firstName("Анна").lastName("Козлова").role(visitorRole).status("active").build(),
                User.builder().tgId("tg_1007").userName("Петр Иванов").firstName("Петр").lastName("Иванов").role(freeListenerRole).status("active").build()
        );
        userRepository.saveAll(users);
        System.out.println("  → Создано 5 пользователей");
    }

    private void initTutors() {
        List<Tutor> tutors = Arrays.asList(
                Tutor.builder().userName("test1").firstName("Сергей").lastName("Иванов").phoneNumber("+79991234567").email("test1@example.com").build(),
                Tutor.builder().userName("test2").firstName("Николай").lastName("Александров").phoneNumber("+79997654321").email("test2@example.com").build()
        );
        tutorRepository.saveAll(tutors);
        System.out.println("  → Создано 2 куратора");
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
        Random random = new Random();

        List<Mailing> broadcasts = Arrays.asList(
                Mailing.builder()
                        .createdBy(userRepository.findById(1L + random.nextLong(userRepository.count())).orElseThrow())
                        .name("Test1")
                        .status(MailingStatus.DRAFT)
                        .build(),
                Mailing.builder()
                        .createdBy(userRepository.findById(1L + random.nextLong(userRepository.count())).orElseThrow())
                        .name("Test2")
                        .status(MailingStatus.DRAFT)
                        .build(),
                Mailing.builder()
                        .createdBy(userRepository.findById(1L + random.nextLong(userRepository.count())).orElseThrow())
                        .name("Test3")
                        .status(MailingStatus.DRAFT)
                        .build()
        );
        broadcasts.forEach(b -> b.setCreatedAt(LocalDateTime.now().minusDays(new Random().nextInt(5))));
        mailingRepository.saveAll(broadcasts);
        System.out.println("  → Создано 3 рассылки");
    }

    private void initDialogs() {
        List<User> users = userRepository.findAll();
        List<Question> allQuestions = questionRepository.findAll();
        Random random = new Random();

        // --- 1. Создаём обычные диалоги (как раньше) ---
        for (User user : users) {
            int dialogCount = 1 + random.nextInt(2); // 1-2 диалога
            for (int d = 0; d < dialogCount; d++) {
                createDialogForUser(user, allQuestions, random, false); // обычные
            }
        }

        // --- 2. ДОБАВЛЯЕМ 1–2 ДИАЛОГА С СЕГОДНЯШНЕЙ ДАТОЙ ---
        if (!users.isEmpty()) {
            // Выбираем случайного пользователя
            User todayUser = users.get(random.nextInt(users.size()));
            int todayDialogs = 1 + random.nextInt(2); // 1 или 2 диалога "сегодня"
            for (int i = 0; i < todayDialogs; i++) {
                createDialogForUser(todayUser, allQuestions, random, true); // сегодняшние
            }
        }

        System.out.println("  → Созданы диалоги, включая с сегодняшней датой");
    }

    private void createDialogForUser(User user, List<Question> allQuestions, Random random, boolean forceToday) {
        Dialog dialog = new Dialog();
        dialog.setUser(user);
        dialog.setDirection(directionRepository.findById(1L + random.nextLong(directionRepository.count())).orElseThrow());
        dialog.setStatus("active");

        List<Message> messages = new ArrayList<>();
        LocalDateTime currentTimestamp;

        if (forceToday) {
            // === СЛОЖНЫЙ ДИАЛОГ: НАЧИНАЕТСЯ В ПРОШЛОМ, ЗАКАНЧИВАЕТСЯ СЕГОДНЯ ===
            int daysAgo = 2 + random.nextInt(4); // 2–5 дней назад
            currentTimestamp = LocalDateTime.now()
                    .minusDays(daysAgo)
                    .plusHours(10) // начали утром
                    .truncatedTo(ChronoUnit.MINUTES);

            int initialRounds = 1 + random.nextInt(2); // 1–2 обмена в прошлом
            for (int r = 0; r < initialRounds; r++) {
                Question q = allQuestions.get(random.nextInt(allQuestions.size()));
                // Пользователь спрашивает
                Message userMsg = createMessage(dialog, user, q.getQuestionText(), "user", currentTimestamp);
                messages.add(userMsg);
                currentTimestamp = currentTimestamp.plusSeconds(5 + random.nextInt(10));

                // Бот отвечает
                Message botMsg = createMessage(dialog, null, q.getAnswerText(), "bot", currentTimestamp);
                messages.add(botMsg);
                currentTimestamp = currentTimestamp.plusSeconds(10 + random.nextInt(20));
            }

            // Перерыв: несколько дней молчания
            currentTimestamp = LocalDateTime.now()
                    .minusHours(random.nextInt(3)) // сегодня, последние 3 часа
                    .minusMinutes(random.nextInt(60))
                    .truncatedTo(ChronoUnit.MINUTES);

            // Сегодня: ещё 1–2 сообщения
            int todayRounds = 1 + random.nextInt(2);
            for (int r = 0; r < todayRounds; r++) {
                Question q = allQuestions.get(random.nextInt(allQuestions.size()));
                Message userMsg = createMessage(dialog, user, q.getQuestionText(), "user", currentTimestamp);
                messages.add(userMsg);
                currentTimestamp = currentTimestamp.plusSeconds(3 + random.nextInt(5));

                // Иногда бот не отвечает (последнее сообщение от пользователя)
                if (r < todayRounds - 1 || random.nextBoolean()) {
                    Message botMsg = createMessage(dialog, null, q.getAnswerText(), "bot", currentTimestamp);
                    messages.add(botMsg);
                    currentTimestamp = currentTimestamp.plusSeconds(4 + random.nextInt(6));
                }
            }
        } else {
            // === ОБЫЧНЫЙ ДИАЛОГ: ВСЁ ЗА ОДИН ДЕНЬ ===
            currentTimestamp = LocalDateTime.now()
                    .minusDays(random.nextInt(10))
                    .plusHours(9 + random.nextInt(10))
                    .truncatedTo(ChronoUnit.MINUTES);

            int rounds = 1 + random.nextInt(3);
            boolean endsWithUserMessage = random.nextBoolean();

            for (int r = 0; r < rounds; r++) {
                Question q = allQuestions.get(random.nextInt(allQuestions.size()));
                Message userMsg = createMessage(dialog, user, q.getQuestionText(), "user", currentTimestamp);
                messages.add(userMsg);
                currentTimestamp = currentTimestamp.plusSeconds(2 + random.nextInt(3));

                if (!(r == rounds - 1 && endsWithUserMessage)) {
                    Message botMsg = createMessage(dialog, null, q.getAnswerText(), "bot", currentTimestamp);
                    messages.add(botMsg);
                    currentTimestamp = currentTimestamp.plusSeconds(3 + random.nextInt(4));
                }
            }
        }

        if (!messages.isEmpty()) {
            dialog.setLastMessageAt(messages.get(messages.size() - 1).getCreatedAt());
        } else {
            dialog.setLastMessageAt(LocalDateTime.now());
        }

        dialog.setMessages(messages);
        dialog.setUnreadCount(messages.size());
        dialogRepository.save(dialog);
    }

    // Вспомогательный метод для создания сообщения
    private Message createMessage(Dialog dialog, User sender, String text, String senderType, LocalDateTime createdAt) {
        Message msg = new Message();
        msg.setDialog(dialog);
        msg.setSender(sender);
        msg.setText(text);
        msg.setSenderType(SenderType.valueOf(senderType.toUpperCase()));
        msg.setStatus("active");
        msg.setCreatedAt(createdAt);
        return msg;
    }
}