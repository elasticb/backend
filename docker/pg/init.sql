CREATE TABLE service_user (
    uid VARCHAR(200) NOT NULL PRIMARY KEY,
    first_name VARCHAR(200) NOT NULL,
    last_name VARCHAR(200) NOT NULL
);

CREATE TABLE question (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  uid VARCHAR(200) REFERENCES service_user(uid),
  question TEXT NOT NULL
);

CREATE TYPE answer_type_enum AS ENUM ('text_response');
CREATE TABLE answer (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    action_type answer_type_enum NOT NULL,
    answer_value JSONB NOT NULL,
    question_fk BIGINT NOT NULL REFERENCES question(id)
);

CREATE TABLE dead_questions (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    uid VARCHAR(200) NOT NULL REFERENCES service_user(uid),
    question TEXT NOT NULL
);

CREATE TABLE notification (
    id BIGSERIAL NOT NULL PRIMARY KEY ,
    notify_date DATE DEFAULT NULL,
    title VARCHAR(150) NOT NULL,
    content TEXT DEFAULT NULL
);

Create or replace function random_string(length integer) returns text as
$$
declare
  chars text[] := '{0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z}';
  result text := '';
  i integer := 0;
begin
  if length < 0 then
    raise exception 'Given length cannot be less than 0';
  end if;
  for i in 1..length loop
    result := result || chars[1+random()*(array_length(chars, 1)-1)];
  end loop;
  return result;
end;
$$ language plpgsql;

CREATE OR REPLACE FUNCTION generate_data()
    RETURNS int AS
$$
BEGIN
    for i in 1..1000000 loop
        INSERT INTO question (question) VALUES (random_string(30));
    end loop;

    RETURN 1;
END;
$$ LANGUAGE 'plpgsql';

CREATE EXTENSION pg_trgm;
CREATE INDEX question_idx ON question USING GIST(question gist_trgm_ops);

INSERT INTO question (id, question) VALUES (1, 'тестовый вопрос');
INSERT INTO answer (action_type, answer_value, question_fk) VALUES ('text_response', '{"answer":"тестовый ответ"}', 1);


CREATE USER docker WITH ENCRYPTED PASSWORD 'docker';
GRANT ALL ON TABLE service_user TO docker;
GRANT ALL ON TABLE question TO docker;
GRANT ALL ON TABLE answer TO docker;
GRANT ALL ON TABLE dead_questions TO docker;
GRANT ALL ON TABLE notification TO docker;
