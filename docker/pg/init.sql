
CREATE TABLE question (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  uid VARCHAR(200) NOT NULL DEFAULT 'unassigned',
  question TEXT NOT NULL
);

CREATE TYPE answer_type_enum AS ENUM ('text_response');
CREATE TABLE answer (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    action_type answer_type_enum NOT NULL,
    answer_value JSONB NOT NULL,
    question_fk BIGINT NOT NULL REFERENCES question(id)
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

INSERT INTO question (id, uid, question) VALUES (1, '1', 'тестовый вопрос');
INSERT INTO answer (action_type, answer_value, question_fk) VALUES ('text_response', '{"answer":"тестовый ответ"}', 1);

