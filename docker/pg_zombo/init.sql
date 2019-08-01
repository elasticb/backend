CREATE EXTENSION zombodb;

CREATE TABLE question (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  uid VARCHAR(200) NOT NULL DEFAULT 'unassigned',
  question TEXT NOT NULL
);

CREATE TABLE answer (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    answer JSONB NOT NULL,
    question_fk BIGINT NOT NULL REFERENCES question(id)
);

CREATE INDEX idxanswer
          ON answer
       USING zombodb ((answer.*))
        WITH (url='172.19.0.3:9200/');
