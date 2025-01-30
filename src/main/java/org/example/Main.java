package org.example;

import com.github.javafaker.Faker;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.model.*;

import java.time.ZoneId;

public class Main {
    static Faker faker = new Faker();
    static SessionFactory sessionFactory = new Configuration().configure()
            .buildSessionFactory();

    public static void main(String[] args) {
        insertIntoDBTeachers(1000);
        insertIntoDBStudents(30_000);
        insertIntoDBAchievements(200, 20_000);
        insertIntoDBCourses(3_000, 100,4);
        insertIntoDbLessons();
        insertIntoDbSteps();
        insertIntoDbComments();
    }

    private static void insertIntoDbComments() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            NativeQuery<Long> query = session.createNativeQuery("SELECT COUNT(*) FROM steps", Long.class);
            Long countOfSteps = query.uniqueResult();
            query = session.createNativeQuery("SELECT COUNT(*) FROM users", Long.class);
            Long countOfUsers = query.uniqueResult();
            for (int i = 1; i <= countOfSteps; i++) {
                Step step = session.get(Step.class, i);
                for (int j = 0; j < faker.number().numberBetween(0,5); j++) {
                    Comment comment = generateRandomComment();
                    User user = session.get(User.class, faker.number().numberBetween(1, countOfUsers));
                    comment.setUser(user);
                    session.persist(comment);
                    step.getComments().add(comment);
                    session.update(step);
                }
            }
            transaction.commit();
        }
    }

    private static Comment generateRandomComment() {
        Comment comment = new Comment();
        comment.setDate(faker.date().birthday(1,10).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        comment.setText(faker.lorem().sentence());
        return comment;
    }


    private static Attachment generateRandomAttachment() {
        Attachment attachment = new Attachment();
        attachment.setFile(faker.name().title());
        attachment.setPicture(faker.name().title());
        attachment.setVideo(faker.name().title());
        return attachment;
    }

    private static void insertIntoDbSteps() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            NativeQuery<Long> query = session.createNativeQuery("SELECT COUNT(*) FROM lessons", Long.class);
            Long countOfLessons = query.uniqueResult();
            query = session.createNativeQuery("SELECT COUNT(*) FROM attachments", Long.class);
            Long countOfAttachments = query.uniqueResult();
            for (int i = 1; i <= countOfLessons; i++) {
                Lesson lesson = session.get(Lesson.class, i);
                for (int j = 0; j < lesson.countOfSteps; j++) {
                    Step step = generateRandomStep();
                    session.persist(step);
                    if (faker.bool().bool() || countOfAttachments == 0){
                        for (int k = 0; k < faker.number().numberBetween(1,3); k++) {
                            Attachment attachment = generateRandomAttachment();
                            session.persist(attachment);
                            step.getAttachments().add(attachment);
                            session.update(step);
                        }
                    }else {
                        int k = 0;
                        for (; k < faker.number().numberBetween(1,3); k++) {
                            Attachment attachment = session.get(Attachment.class, faker.number().numberBetween(1, countOfAttachments.intValue()));
                            if (step.getAttachments().contains(attachment)){
                                continue;
                            }
                            step.getAttachments().add(attachment);
                            session.update(step);
                            k++;
                        }
                    }
                    lesson.getSteps().add(step);
                    session.update(lesson);
                }
            }
            transaction.commit();
        }
    }

    private static Step generateRandomStep() {
        Step step = new Step();
        step.setName(faker.name().title());
        return step;
    }


    private static void insertIntoDbLessons() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            NativeQuery<Long> query = session.createNativeQuery("SELECT COUNT(*) FROM chapters", Long.class);
            Long countOfChapters = query.uniqueResult();
            for (int i = 1; i <= countOfChapters; i++) {
                Chapter chapter = session.get(Chapter.class, i);
                for (int j = 0; j < chapter.countOfLesson; j++) {
                    Lesson lesson = generateRandomLesson();
                    session.persist(lesson);
                    chapter.getLessons().add(lesson);
                    session.update(chapter);
                }
            }
            transaction.commit();
        }
    }

    private static Lesson generateRandomLesson() {
        Lesson lesson = new Lesson();
        lesson.setName(faker.name().title());
        lesson.setCountOfSteps(faker.number().numberBetween(1,15));
        return lesson;
    }

    private static void insertIntoDBAchievements(int number, int maxNumberOfStudnets) {
        try (Session session = sessionFactory.openSession()) {
            NativeQuery<Long> query = session.createNativeQuery("SELECT COUNT(*) FROM students", Long.class);
            Long countOfStudents = query.uniqueResult();
            for (int i = 0; i < number; i++) {
                Achievement achievement = new Achievement();
                achievement.setDescription(faker.lorem().sentence());
                achievement.setType(faker.number().numberBetween(1,3));
                Transaction transaction = session.beginTransaction();
                session.persist(achievement);
                int j = 0;
                for (; j < faker.number().numberBetween(1,maxNumberOfStudnets); j++) {
                    Student student = session.get(Student.class, faker.number().numberBetween(1, countOfStudents.intValue()));
                    if (student.getAchievements().contains(achievement)){
                        continue;
                    }
                    student.getAchievements().add(achievement);
                    session.update(student);
                    j++;
                }
                transaction.commit();
            }
        }
    }
    private static void insertIntoDBCourses(int number, int maxNumberOfStudents, int maxNumberOfTeachers) {
        try (Session session = sessionFactory.openSession()) {
            NativeQuery<Long> query = session.createNativeQuery("SELECT COUNT(*) FROM teachers", Long.class);
            Long countOfTeachers = query.uniqueResult();
            query = session.createNativeQuery("SELECT COUNT(*) FROM students", Long.class);
            Long countOfStudent = query.uniqueResult();
            for (int i = 0; i < number; i++) {
                Course course = generateRandomCourse();
                Transaction transaction = session.beginTransaction();
                session.persist(course);
                for (int j = 0; j < course.getCountOfChapters(); j++) {
                    Chapter chapter = generateRandomChapter();
                    session.persist(chapter);
                    course.getChapters().add(chapter);
                    session.update(course);
                }
                int j = 0;
                for (; j < faker.number().numberBetween(1,maxNumberOfTeachers); j++) {
                    Teacher teacher = session.get(Teacher.class, faker.number().numberBetween(1, countOfTeachers.intValue()));
                    if (teacher.getCourses().contains(course)){
                        continue;
                    }
                    teacher.setNumberOfCourses(teacher.numberOfCourses + 1);
                    teacher.getCourses().add(course);
                    session.update(teacher);
                    j++;
                }
                j = 0;
                for (; j < faker.number().numberBetween(1,maxNumberOfStudents);) {
                    Student student = session.get(Student.class, faker.number().numberBetween(1, countOfStudent.intValue()));
                    if (student.getCourses().contains(course)){
                        continue;
                    }
                    student.setNumberOfCourses(student.numberOfCourses + 1);
                    student.getCourses().add(course);
                    session.update(student);
                    j++;
                }
                transaction.commit();
            }
        }
    }

    private static Course generateRandomCourse() {
        Course course = new Course();
        course.setName(faker.educator().course());
        course.setDescription(faker.lorem().paragraph(1));
        course.setCountOfChapters(faker.number().numberBetween(1,3));
        return course;
    }

    private static void insertIntoDBStudents(int number) {
        for (int i = 0; i < number; i++) {
            User user = generateRandomUser();
            Student student = new Student();
            student.setUser(user);
            student.setNumberOfCourses(0);
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                session.persist(user);
                session.persist(student);
                transaction.commit();
            }
        }
    }

    private static void insertIntoDBTeachers(int number) {
        for (int i = 0; i < number; i++) {
            User user = generateRandomUser();
            Teacher teacher = new Teacher();
            teacher.setUser(user);
            teacher.setNumberOfCourses(0);
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                session.persist(user);
                session.persist(teacher);
                transaction.commit();
            }
        }
    }

    private static User generateRandomUser() {
        User user = new User();
        user.setCreatedDate(faker.date().birthday(1,10).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        user.setName(faker.name().name());
        user.setEmail(faker.internet().emailAddress());
        return user;
    }

    private static Chapter generateRandomChapter() {
        Chapter chapter = new Chapter();
        chapter.setName(faker.name().title());
        chapter.setCountOfLesson(faker.number().numberBetween(1,5));
        return chapter;
    }
}