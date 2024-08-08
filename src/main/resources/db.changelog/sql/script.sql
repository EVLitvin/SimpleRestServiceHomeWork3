SET
search_path TO public;

CREATE TABLE school
(
    id          BIGSERIAL PRIMARY KEY NOT NULL,
    school_name TEXT                  NOT NULL,
    address     TEXT                  NOT NULL
);

CREATE TABLE teacher
(
    id         BIGSERIAL PRIMARY KEY NOT NULL,
    first_name TEXT                  NOT NULL,
    last_name  TEXT                  NOT NULL,
    school_id  BIGINT                NOT NULL,
    CONSTRAINT fk_post_school FOREIGN KEY (school_id) REFERENCES school (id)
);


CREATE TABLE pupil
(
    id         BIGSERIAL PRIMARY KEY NOT NULL,
    first_name TEXT                  NOT NULL,
    last_name  TEXT                  NOT NULL
);

CREATE TABLE teacher_pupil
(
    teacher_id BIGINT NOT NULL,
    pupil_id   BIGINT NOT NULL,
    CONSTRAINT fk_teacher FOREIGN KEY (teacher_id) REFERENCES teacher (id),
    CONSTRAINT fk_pupil FOREIGN KEY (pupil_id) REFERENCES pupil (id),
    PRIMARY KEY (teacher_id, pupil_id)
);

CREATE TABLE school_teacher
(
    school_id  BIGINT NOT NULL,
    teacher_id BIGINT NOT NULL,
    CONSTRAINT fk_school FOREIGN KEY (school_id) REFERENCES school (id),
    CONSTRAINT fk_teacher FOREIGN KEY (teacher_id) REFERENCES teacher (id),
    PRIMARY KEY (school_id, teacher_id)
);