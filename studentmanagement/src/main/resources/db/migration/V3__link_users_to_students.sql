ALTER TABLE users ADD COLUMN student_id BIGINT;
ALTER TABLE users ADD CONSTRAINT fk_users_student FOREIGN KEY (student_id) REFERENCES students(id);
ALTER TABLE users ADD CONSTRAINT uk_users_student UNIQUE  (student_id);
