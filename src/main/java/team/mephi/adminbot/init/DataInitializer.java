package team.mephi.adminbot.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.mephi.adminbot.model.*;
import team.mephi.adminbot.model.enums.*;
import team.mephi.adminbot.model.objects.Filters;
import team.mephi.adminbot.repository.*;
import team.mephi.adminbot.service.CityService;
import team.mephi.adminbot.service.CohortService;
import team.mephi.adminbot.service.DirectionService;
import team.mephi.adminbot.service.RoleService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static team.mephi.adminbot.vaadin.users.tabs.UserTabType.*;

@Configuration
public class DataInitializer {
    private final Long DAY_SECONDS = 86400L;
    private final Long HOUR_SECONDS = 3600L;
    private final Long MINUTE_SECONDS = 3600L;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PdConsentLogRepository pdConsentLogRepository;

    @Autowired
    private DialogRepository dialogRepository;

    @Autowired
    private UserQuestionRepository questionRepository;

    @Autowired
    private UserAnswerRepository answerRepository;

    @Autowired
    private MailingRepository mailingRepository;

    @Autowired
    private MailTemplateRepository mailTemplateRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private TutorDirectionRepository tutorDirectionRepository;

    @Autowired
    private CityService cityService;

    @Autowired
    private CohortService cohortService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DirectionService directionService;

    @Bean
    public ApplicationRunner initTestData() {
        return args -> {
            boolean hasDirections = directionRepository.count() > 0;
            boolean hasRoles = roleRepository.count() > 0;
            boolean hasUsers = userRepository.count() > 0;
            boolean hasPdConsentLog = pdConsentLogRepository.count() > 0;
            boolean hasDialogs = dialogRepository.count() > 0;
            boolean hasQuestions = questionRepository.count() > 0;
            boolean hasAnswers = answerRepository.count() > 0;
            boolean hasBroadcasts = mailingRepository.count() > 0;
            boolean hasTemplates = mailTemplateRepository.count() > 0;
            boolean hasTutors = tutorRepository.count() > 0;
            boolean hasTutorDirections = tutorDirectionRepository.count() > 0;

            if (!hasUsers || !hasDialogs || !hasQuestions || !hasBroadcasts) {
                System.out.println("🔁 Предзаполнение БД тестовыми данными...");

                if (!hasDirections) initDirections();
                if (!hasRoles) initRoles();
                if (!hasUsers) initUsers();
                if (!hasPdConsentLog) initPdConsentLog();
                if (!hasTutors) initTutors();
                if (!hasTutorDirections) initTutorDirections();
                if (!hasQuestions) initQuestions();
                if (!hasAnswers) initAnswers();
                if (!hasBroadcasts) initBroadcasts();
                if (!hasTemplates) initTemplates();
                if (!hasDialogs) initDialogs(); // зависит от пользователей

                System.out.println("✅ Тестовые данные успешно созданы.");
            }
        };
    }

    private void initTutorDirections() {
        var tutors = tutorRepository.findAll();
        var directions = directionRepository.findAll();
        for (var tutor : tutors) {
            // Каждый тьютор работает с 1-3 направлениями
            Collections.shuffle(directions);
            int count = 1 + new Random().nextInt(3);
            for (int i = 0; i < count; i++) {
                TutorDirection td = TutorDirection.builder()
                        .tutor(tutor)
                        .tutorId(tutor.getId())
                        .direction(directions.get(i))
                        .directionId(directions.get(i).getId())
                        .build();
                tutorDirectionRepository.save(td);
            }
        }
    }

    private void initRoles() {
        List<Role> roles = Arrays.asList(
                Role.builder().code(STUDENT.name()).name("Студенты").description("Студенты").build(),
                Role.builder().code(CANDIDATE.name()).name("Кандидаты").description("Кандидаты").build(),
                Role.builder().code(VISITOR.name()).name("Посетители").description("Посетители").build(),
                Role.builder().code(FREE_LISTENER.name()).name("Слушатели").description("Слушатели").build(),
                Role.builder().code(MIDDLE_CANDIDATE.name()).name("Миддл-кандидаты").description("Миддл-кандидаты").build(),
                Role.builder().code(LC_EXPERT.name()).name("Эксперты").description("Эксперты").build(),
                Role.builder().code(TUTOR.name()).name("Кураторы").description("Кураторы").build()
        );
        roleRepository.saveAll(roles);
        System.out.printf("  → Создано %d ролей%n", roles.size());
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
        System.out.printf("  → Создано %d направлений%n", directions.size());
    }

    private void initUsers() {
        // Получаем роли по имени
        Role studentRole = roleRepository.findByCode(STUDENT.name())
                .orElseThrow(() -> new RuntimeException("Роль 'STUDENT' не найдена"));
        Role candidateRole = roleRepository.findByCode(CANDIDATE.name())
                .orElseThrow(() -> new RuntimeException("Роль 'CANDIDATE' не найдена"));
        Role middleCandidateRole = roleRepository.findByCode(MIDDLE_CANDIDATE.name())
                .orElseThrow(() -> new RuntimeException("Роль 'MIDDLE_CANDIDATE' не найдена"));
        Role visitorRole = roleRepository.findByCode(VISITOR.name())
                .orElseThrow(() -> new RuntimeException("Роль 'VISITOR' не найдена"));
        Role freeListenerRole = roleRepository.findByCode(FREE_LISTENER.name())
                .orElseThrow(() -> new RuntimeException("Роль 'FREE_LISTENER' не найдена"));
        Role lcExpertRole = roleRepository.findByCode(LC_EXPERT.name())
                .orElseThrow(() -> new RuntimeException("Роль 'LC_EXPERT' не найдена"));

        Direction java = directionRepository.findById(1L).orElseThrow();
        Direction analytics = directionRepository.findById(2L).orElseThrow();
        Direction python = directionRepository.findById(3L).orElseThrow();

        List<User> users = Arrays.asList(
                User.builder().tgId("tg_1001").tgName("tg_name_1001").email("test1@example.com").userName("Анна Смирнова").firstName("Анна").lastName("Смирнова").role(studentRole).cohort("Весна 2026").direction(java).status(UserStatus.ACTIVE).build(),
                User.builder().tgId("tg_1002").tgName("tg_name_1002").email("test2@example.com").userName("Иван Петров").firstName("Иван").lastName("Петров").role(candidateRole).cohort("Зима 2025").direction(analytics).status(UserStatus.ACTIVE).build(),
                User.builder().tgId("tg_1003").tgName("tg_name_1003").email("test3@example.com").userName("Мария Козлова").firstName("Мария").lastName("Козлова").role(candidateRole).cohort("Зима 2025").direction(python).status(UserStatus.BLOCKED).build(),
                User.builder().tgId("tg_1004").tgName("tg_name_1004").email("test4@example.com").userName("Алексей Иванов").firstName("Алексей").lastName("Иванов").role(middleCandidateRole).cohort("Осень 2025").direction(java).status(UserStatus.ACTIVE).build(),
                User.builder().tgId("tg_1005").tgName("tg_name_1005").email("test5@example.com").userName("Алексей Иванов").firstName("Алексей").lastName("Иванов").role(middleCandidateRole).cohort("Осень 2025").direction(java).status(UserStatus.BLOCKED).build(),
                User.builder().tgId("tg_1006").tgName("tg_name_1006").email("test6@example.com").userName("Алексей Иванов").firstName("Алексей").lastName("Иванов").role(middleCandidateRole).cohort("Осень 2025").direction(java).status(UserStatus.PENDING).build(),
                User.builder().tgId("tg_1007").tgName("tg_name_1007").email("test7@example.com").userName("Алексей Иванов").firstName("Алексей").lastName("Иванов").role(middleCandidateRole).cohort("Осень 2025").direction(java).status(UserStatus.EXPELLED).build(),
                User.builder().tgId("tg_1008").tgName("tg_name_1008").email("test8@example.com").userName("Алексей Иванов").firstName("Алексей").lastName("Иванов").role(middleCandidateRole).cohort("Осень 2025").direction(java).status(UserStatus.INACTIVE).build(),
                User.builder().tgId("tg_1009").tgName("tg_name_1009").email("test9@example.com").userName("Екатерина Волкова").firstName("Екатерина").lastName("Волкова").role(studentRole).cohort("Весна 2026").direction(analytics).status(UserStatus.ACTIVE).build(),
                User.builder().tgId("tg_1010").tgName("tg_name_1010").email("test10@example.com").userName("Анна Козлова").firstName("Анна").lastName("Козлова").role(visitorRole).status(UserStatus.ACTIVE).build(),
                User.builder().tgId("tg_1011").tgName("tg_name_1011").email("test11@example.com").userName("Петр Иванов").firstName("Петр").lastName("Иванов").role(freeListenerRole).direction(python).status(UserStatus.ACTIVE).build(),
                User.builder().tgId("tg_1012").tgName("tg_name_1012").email("test12@example.com").userName("Сергей Смирнов").firstName("Сергей").lastName("Смирнов").role(lcExpertRole).status(UserStatus.ACTIVE).build(),
                User.builder().tgId("tg_1013").tgName("tg_name_1013").email("admin1@example.com").userName("Admin").firstName("Admin").lastName("Admin").role(lcExpertRole).status(UserStatus.ACTIVE).build()
        );
        userRepository.saveAll(users);
        System.out.printf("  → Создано %d пользователей%n", users.size());
    }

    private void initPdConsentLog() {
        Random random = new Random();
        List<ConsentStatus> statuses = Arrays.stream(ConsentStatus.values()).toList();
        List<String> roles = List.of(CANDIDATE.name(), MIDDLE_CANDIDATE.name(), VISITOR.name());
        List<String> sources = List.of("Telegram", "Web", "Mobile App");
        List<User> users = userRepository.findAll().stream().filter(u -> roles.contains(u.getRole().getCode())).toList();
        List<PdConsentLog> logs = new ArrayList<>();
        for (User user : users) {
            int todayDialogs = 1 + random.nextInt(2); // 1 или 2 диалога "сегодня"
            for (int i = 0; i < todayDialogs; i++) {
                logs.add(PdConsentLog.builder()
                        .user(user)
                        .consentedAt(Instant.now().minusSeconds(new Random().nextInt(30) * DAY_SECONDS))
                        .source(sources.get(random.nextInt(sources.size())))
                        .status(statuses.get(random.nextInt(statuses.size())))
                        .build());
            }
        }
        pdConsentLogRepository.saveAll(logs);
        System.out.printf("  → Создано %d записей согласия на обработку ПД%n", logs.size());
    }

    private void initTutors() {
        List<Tutor> tutors = Arrays.asList(
                Tutor.builder().userName("Сергей Иванов").firstName("Сергей").lastName("Иванов").phone("+79991234567").email("test1@example.org").tgId("tg_name_1020").build(),
                Tutor.builder().userName("Николай Александров").firstName("Николай").lastName("Александров").phone("+79997654321").email("test2@example.org").tgId("tg_name_1021").build(),
                Tutor.builder().userName("Екатерина Козлова").firstName("Екатерина").lastName("Козлова").phone("+79991111111").email("test3@example.org").tgId("tg_name_1022").build(),
                Tutor.builder().userName("Петр Петров").firstName("Петр").lastName("Петров").phone("+79992222222").email("test4@example.org").tgId("tg_name_1023").build(),
                Tutor.builder().userName("Иван Иванов").firstName("Иван").lastName("Иванов").phone("+79993333333").email("test5@example.org").tgId("tg_name_1024").build()
        );
        tutorRepository.saveAll(tutors);
        System.out.printf("  → Создано %d кураторов%n", tutors.size());
    }

    private void initQuestions() {
        Random random = new Random();

        Role student = roleRepository.findByCode(STUDENT.name()).get();
        List<User> students = userRepository.findAllByRole(student.getCode());

        List<UserQuestion> questions = Arrays.asList(
                UserQuestion.builder().status(QuestionStatus.NEW).role(student.getName()).direction(students.get(random.nextInt(students.size())).getDirection()).user(students.get(random.nextInt(students.size()))).text("Как поступить в Flexiq?").build(),
                UserQuestion.builder().status(QuestionStatus.ANSWERED).role(student.getName()).direction(students.get(random.nextInt(students.size())).getDirection()).user(students.get(random.nextInt(students.size()))).text("Сколько длится обучение?").build(),
                UserQuestion.builder().status(QuestionStatus.ANSWERED).role(student.getName()).direction(students.get(random.nextInt(students.size())).getDirection()).user(students.get(random.nextInt(students.size()))).text("Есть ли рассрочка?").build(),
                UserQuestion.builder().status(QuestionStatus.ANSWERED).role(student.getName()).direction(students.get(random.nextInt(students.size())).getDirection()).user(students.get(random.nextInt(students.size()))).text("Нужен ли опыт для поступления?").build(),
                UserQuestion.builder().status(QuestionStatus.IN_PROGRESS).role(student.getName()).direction(students.get(random.nextInt(students.size())).getDirection()).user(students.get(random.nextInt(students.size()))).text("Выдают ли диплом?").build(),
                UserQuestion.builder().status(QuestionStatus.NEW).role(student.getName()).direction(students.get(random.nextInt(students.size())).getDirection()).user(students.get(random.nextInt(students.size()))).text("Как поступить в Flexiq?").build(),
                UserQuestion.builder().status(QuestionStatus.ANSWERED).role(student.getName()).direction(students.get(random.nextInt(students.size())).getDirection()).user(students.get(random.nextInt(students.size()))).text("Сколько длится обучение?").build(),
                UserQuestion.builder().status(QuestionStatus.ANSWERED).role(student.getName()).direction(students.get(random.nextInt(students.size())).getDirection()).user(students.get(random.nextInt(students.size()))).text("Есть ли рассрочка?").build(),
                UserQuestion.builder().status(QuestionStatus.ANSWERED).role(student.getName()).direction(students.get(random.nextInt(students.size())).getDirection()).user(students.get(random.nextInt(students.size()))).text("Нужен ли опыт для поступления?").build(),
                UserQuestion.builder().status(QuestionStatus.IN_PROGRESS).role(student.getName()).direction(students.get(random.nextInt(students.size())).getDirection()).user(students.get(random.nextInt(students.size()))).text("Выдают ли диплом?").build(),
                UserQuestion.builder().status(QuestionStatus.NEW).role(student.getName()).direction(students.get(random.nextInt(students.size())).getDirection()).user(students.get(random.nextInt(students.size()))).text("Как поступить в Flexiq?").build(),
                UserQuestion.builder().status(QuestionStatus.ANSWERED).role(student.getName()).direction(students.get(random.nextInt(students.size())).getDirection()).user(students.get(random.nextInt(students.size()))).text("Сколько длится обучение?").build(),
                UserQuestion.builder().status(QuestionStatus.ANSWERED).role(student.getName()).direction(students.get(random.nextInt(students.size())).getDirection()).user(students.get(random.nextInt(students.size()))).text("Есть ли рассрочка?").build(),
                UserQuestion.builder().status(QuestionStatus.ANSWERED).role(student.getName()).direction(students.get(random.nextInt(students.size())).getDirection()).user(students.get(random.nextInt(students.size()))).text("Нужен ли опыт для поступления?").build(),
                UserQuestion.builder().status(QuestionStatus.IN_PROGRESS).role(student.getName()).direction(students.get(random.nextInt(students.size())).getDirection()).user(students.get(random.nextInt(students.size()))).text("Выдают ли диплом?").build()
        );
        // Устанавливаем createdAt вручную, если в конструкторе не задано
        questions.forEach(q -> q.setCreatedAt(Instant.now().minusSeconds(new Random().nextInt(10) * DAY_SECONDS)));
        questionRepository.saveAll(questions);
        System.out.printf("  → Создано %d вопросов%n", questions.size());
    }

    private void initAnswers() {
        Random random = new Random();

        String expert = roleRepository.findByCode(LC_EXPERT.name()).get().getCode();
        List<User> experts = userRepository.findAllByRole(expert);

        List<UserAnswer> answers = Arrays.asList(
                UserAnswer.builder().status(AnswerStatus.SENT).answeredAt(Instant.now()).answeredBy(experts.get(random.nextInt(0, experts.size()))).question(questionRepository.findById(1L).orElseThrow()).answerText("Подайте заявку на сайте и пройдите техническое тестирование.").build(),
                UserAnswer.builder().status(AnswerStatus.SENT).answeredAt(Instant.now()).answeredBy(experts.get(random.nextInt(0, experts.size()))).question(questionRepository.findById(2L).orElseThrow()).answerText("Программы длятся от 3 до 6 месяцев в зависимости от направления.").build(),
                UserAnswer.builder().status(AnswerStatus.SENT).answeredAt(Instant.now()).answeredBy(experts.get(random.nextInt(0, experts.size()))).question(questionRepository.findById(3L).orElseThrow()).answerText("Да, мы предлагаем рассрочку до 12 месяцев без процентов.").build(),
                UserAnswer.builder().status(AnswerStatus.SENT).answeredAt(Instant.now()).answeredBy(experts.get(random.nextInt(0, experts.size()))).question(questionRepository.findById(4L).orElseThrow()).answerText("Нет, наши курсы рассчитаны на начинающих.").build(),
                UserAnswer.builder().status(AnswerStatus.UPDATED).answeredAt(Instant.now()).answeredBy(experts.get(random.nextInt(0, experts.size()))).question(questionRepository.findById(5L).orElseThrow()).answerText("По окончании вы получаете сертификат установленного образца.").build()
        );
        answerRepository.saveAll(answers);
        System.out.printf("  → Создано %d ответов%n", answers.size());
    }

    private void initBroadcasts() {
        Random random = new Random();
        List<MailingStatus> statuses = Arrays.stream(MailingStatus.values()).toList();
        var roles = roleService.getAllRoles();
        var directions = directionService.getAllDirections();
        var cohorts = cohortService.getAllCohorts();
        var cities = cityService.getAllCities();

        List<Mailing> broadcasts = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            var user = userRepository.findById(1L + random.nextLong(userRepository.count())).orElseThrow();
            var curator = tutorRepository.findById(1L + random.nextLong(tutorRepository.count())).orElseThrow();
            broadcasts.add(Mailing.builder()
                    .createdBy(user)
                    .name("Test " + i)
                    .description("Text " + i)
                    .channels(List.of(Channels.Email))
                    .filters(Filters.builder()
                            .users(roles.get(random.nextInt(0, roles.size())).getName())
                            .cohort(cohorts.get(random.nextInt(0, cohorts.size())).getName())
                            .direction(directions.get(random.nextInt(0, directions.size())).getName())
                            .city(cities.get(random.nextInt(0, cities.size())).getName())
                            .curator(curator.getUserName())
                            .build())
                    .status(statuses.get(random.nextInt(statuses.size())))
                    .build());
        }
        broadcasts.forEach(b -> b.setCreatedAt(Instant.now().minusSeconds(new Random().nextInt(5) * DAY_SECONDS)));
        mailingRepository.saveAll(broadcasts);
        System.out.printf("  → Создано %d рассылок%n", broadcasts.size());
    }

    private void initTemplates() {
        List<MailTemplate> templates = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            templates.add(MailTemplate.builder()
                    .name("Name " + i)
                    .subject("Subject " + i)
                    .bodyText("Text " + i)
                    .build());
        }
        mailTemplateRepository.saveAll(templates);
        System.out.printf("  → Создано %d шаблонов%n", templates.size());
    }

    private void initDialogs() {
        List<User> users = userRepository.findAll();
        List<UserQuestion> allQuestions = questionRepository.findAllWithAnswers();
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

    private void createDialogForUser(User user, List<UserQuestion> allQuestions, Random random, boolean forceToday) {
        Dialog dialog = new Dialog();
        dialog.setUser(user);
        dialog.setDirection(directionRepository.findById(1L + random.nextLong(directionRepository.count())).orElseThrow());
        dialog.setStatus(DialogStatus.ACTIVE);

        List<Message> messages = new ArrayList<>();
        Instant currentTimestamp;

        if (forceToday) {
            // === СЛОЖНЫЙ ДИАЛОГ: НАЧИНАЕТСЯ В ПРОШЛОМ, ЗАКАНЧИВАЕТСЯ СЕГОДНЯ ===
            int daysAgo = 2 + random.nextInt(4); // 2–5 дней назад
            currentTimestamp = Instant.now()
                    .minusSeconds(daysAgo * DAY_SECONDS)
                    .plusSeconds(10 * HOUR_SECONDS) // начали утром
                    .truncatedTo(ChronoUnit.MINUTES);

            int initialRounds = 1 + random.nextInt(2); // 1–2 обмена в прошлом
            for (int r = 0; r < initialRounds; r++) {
                UserQuestion q = allQuestions.get(random.nextInt(allQuestions.size()));
                // Пользователь спрашивает
                Message userMsg = createMessage(dialog, user, q.getText(), "user", currentTimestamp);
                messages.add(userMsg);
                currentTimestamp = currentTimestamp.plusSeconds(5 + random.nextInt(10));

                // Бот отвечает
                Message botMsg = createMessage(dialog, null, q.getAnswers().get(random.nextInt(q.getAnswers().size())).getAnswerText(), "bot", currentTimestamp);
                messages.add(botMsg);
                currentTimestamp = currentTimestamp.plusSeconds(10 + random.nextInt(20));
            }

            // Перерыв: несколько дней молчания
            currentTimestamp = Instant.now()
                    .minusSeconds(random.nextInt(3) * HOUR_SECONDS) // сегодня, последние 3 часа
                    .minusSeconds(random.nextInt(60) * MINUTE_SECONDS)
                    .truncatedTo(ChronoUnit.MINUTES);

            // Сегодня: ещё 1–2 сообщения
            int todayRounds = 1 + random.nextInt(2);
            for (int r = 0; r < todayRounds; r++) {
                UserQuestion q = allQuestions.get(random.nextInt(allQuestions.size()));
                Message userMsg = createMessage(dialog, user, q.getText(), "user", currentTimestamp);
                messages.add(userMsg);
                currentTimestamp = currentTimestamp.plusSeconds(3 + random.nextInt(5));

                // Иногда бот не отвечает (последнее сообщение от пользователя)
                if (r < todayRounds - 1 || random.nextBoolean()) {
                    Message botMsg = createMessage(dialog, null, q.getAnswers().get(random.nextInt(q.getAnswers().size())).getAnswerText(), "bot", currentTimestamp);
                    messages.add(botMsg);
                    currentTimestamp = currentTimestamp.plusSeconds(4 + random.nextInt(6));
                }
            }
        } else {
            // === ОБЫЧНЫЙ ДИАЛОГ: ВСЁ ЗА ОДИН ДЕНЬ ===
            currentTimestamp = Instant.now()
                    .minusSeconds(random.nextInt(10) * DAY_SECONDS)
                    .plusSeconds(9 + random.nextInt(10) * HOUR_SECONDS)
                    .truncatedTo(ChronoUnit.MINUTES);

            int rounds = 1 + random.nextInt(3);
            boolean endsWithUserMessage = random.nextBoolean();

            for (int r = 0; r < rounds; r++) {
                UserQuestion q = allQuestions.get(random.nextInt(allQuestions.size()));
                Message userMsg = createMessage(dialog, user, q.getText(), "user", currentTimestamp);
                messages.add(userMsg);
                currentTimestamp = currentTimestamp.plusSeconds(2 + random.nextInt(3));

                if (!(r == rounds - 1 && endsWithUserMessage)) {
                    Message botMsg = createMessage(dialog, null, q.getAnswers().get(random.nextInt(0, q.getAnswers().size())).getAnswerText(), "bot", currentTimestamp);
                    messages.add(botMsg);
                    currentTimestamp = currentTimestamp.plusSeconds(3 + random.nextInt(4));
                }
            }
        }

        if (!messages.isEmpty()) {
            dialog.setLastMessageAt(messages.get(messages.size() - 1).getCreatedAt());
        } else {
            dialog.setLastMessageAt(Instant.now());
        }

        dialog.setMessages(messages);
        dialog.setUnreadCount(messages.size());
        dialogRepository.save(dialog);
    }

    // Вспомогательный метод для создания сообщения
    private Message createMessage(Dialog dialog, User sender, String text, String senderType, Instant createdAt) {
        Message msg = new Message();
        msg.setDialog(dialog);
        msg.setSender(sender);
        msg.setText(text);
        msg.setSenderType(MessageSenderType.valueOf(senderType.toUpperCase()));
        msg.setStatus(MessageStatus.SENT);
        msg.setCreatedAt(createdAt);
        return msg;
    }
}