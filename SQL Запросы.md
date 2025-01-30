# SQL Запросы: Описание и Примеры

Этот документ содержит описание различных SQL запросов с примерами их использования. Каждый запрос сопровождается кратким объяснением его назначения.

### Описание
Id учителя у которого больше всего комментариев под его уроками

### Пример
SELECT t.id
FROM teachers t
JOIN course_teacher ct ON t.id = ct.teacher_id
JOIN courses c ON ct.course_id = c.id
JOIN chapters ch ON c.id = ch.course_id
JOIN lessons l ON ch.id = l.chapter_id
JOIN steps s ON l.id = s.lesson_id
JOIN comments cm ON s.id = cm.step_id
GROUP BY t.id
ORDER BY COUNT(cm.id) DESC
LIMIT 1;

### Описание
Анализ курсов, у которых малый охват( мало преподавателей занимается этим курсом, мало студентов, мало комментариев)

### Пример
SELECT c.id, c.name, c.count_of_chapters,
COUNT(DISTINCT ct.teacher_id) AS teacher_count,
COUNT(DISTINCT cs.student_id) AS student_count,
COUNT(cm.id) AS comment_count
FROM courses c
LEFT JOIN course_teacher ct ON c.id = ct.course_id
LEFT JOIN course_student cs ON c.id = cs.course_id
LEFT JOIN chapters ch ON c.id = ch.course_id
LEFT JOIN lessons l ON ch.id = l.chapter_id
LEFT JOIN steps s ON l.id = s.lesson_id
LEFT JOIN comments cm ON s.id = cm.step_id
GROUP BY c.id
HAVING COUNT(DISTINCT ct.teacher_id) < 2
AND COUNT(DISTINCT cs.student_id) < 30
AND COUNT(cm.id) < 100;

### Описание
Получить таблицу в которой видно лучших студентов ( есть все достижения,записаны на много курсов, пишут много комментариев с большой длинной)

### Пример
SELECT s.id AS student_id,
s.user_id AS user_id,
COUNT(DISTINCT a.id) AS achievement_count,
COUNT(DISTINCT c.id) AS course_count,
SUM(LENGTH(cm.text)) AS total_comment_length
FROM students s
LEFT JOIN achievements_student a_s ON s.id = a_s.student_id
LEFT JOIN achievements a ON a_s.achievement_id = a.id
LEFT JOIN course_student c_s ON s.id = c_s.student_id
LEFT JOIN courses c ON c_s.course_id = c.id
LEFT JOIN comments cm ON s.user_id = cm.user_id
GROUP BY s.id, s.user_id
ORDER BY achievement_count DESC, course_count DESC, total_comment_length DESC;


### Описание
Выявление пользователей которые, которые записаны на много курсов, но имеют мало достижений и которые пишут мало комментариев с маленькой длинной

### Пример
SELECT s.id AS student_id,
s.user_id AS user_id,
COUNT(DISTINCT c.id) AS course_count,
COUNT(DISTINCT a.id) AS achievement_count,
COUNT(cm.id) AS comment_count,
SUM(LENGTH(cm.text)) AS total_comment_length
FROM students s
LEFT JOIN course_student c_s ON s.id = c_s.student_id
LEFT JOIN courses c ON c_s.course_id = c.id
LEFT JOIN achievements_student a_s ON s.id = a_s.student_id
LEFT JOIN achievements a ON a_s.achievement_id = a.id
LEFT JOIN comments cm ON s.user_id = cm.user_id
GROUP BY s.id, s.user_id
HAVING COUNT(DISTINCT c.id) > 3 -- Пример: записаны на больше 5 курсов
AND COUNT(DISTINCT a.id) < 2 -- Пример: имеют меньше 3 достижений
AND COUNT(cm.id) < 10 -- Пример: пишут меньше 10 комментариев
AND SUM(LENGTH(cm.text)) < 500; -- Пример: общая длина комментариев меньше 500 символов