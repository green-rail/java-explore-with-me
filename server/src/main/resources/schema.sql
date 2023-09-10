CREATE TABLE IF NOT EXISTS category (
  category_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(50) NOT NULL,
  CONSTRAINT pk_category PRIMARY KEY (category_id)
);

CREATE TABLE IF NOT EXISTS users (
  user_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS event (
  event_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  annotation VARCHAR(2000) NOT NULL,
  category_id BIGINT NOT NULL,
  created_on TIMESTAMP NOT NULL,
  description VARCHAR(7000),
  event_date TIMESTAMP NOT NULL,
  initiator_id BIGINT NOT NULL,
  lat FLOAT NOT NULL,
  lon FLOAT NOT NULL,
  paid BOOLEAN NOT NULL,
  participant_limit INTEGER DEFAULT 0,
  published_on TIMESTAMP,
  request_moderation BOOLEAN DEFAULT TRUE,
  state VARCHAR(15),
  title VARCHAR(255) NOT NULL,
  CONSTRAINT pk_event PRIMARY KEY (event_id),
  CONSTRAINT fk_event_user FOREIGN KEY (initiator_id) REFERENCES PUBLIC.users(user_id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT fk_event_category FOREIGN KEY (category_id) REFERENCES PUBLIC.category(category_id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS compilation (
  compilation_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  title VARCHAR(50) NOT NULL,
  pinned BOOLEAN DEFAULT FALSE,
  CONSTRAINT pk_compilation PRIMARY KEY (compilation_id),
  CONSTRAINT UQ_COMPILATION_TITLE UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS request (
  request_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  created TIMESTAMP NOT NULL,
  event_id BIGINT NOT NULL,
  requester_id BIGINT NOT NULL,
  status VARCHAR(50) NOT NULL,
  CONSTRAINT pk_request PRIMARY KEY (request_id),
  CONSTRAINT fk_request_event FOREIGN KEY (event_id) REFERENCES PUBLIC.event(event_id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT fk_request_user FOREIGN KEY (requester_id) REFERENCES PUBLIC.users(user_id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS event_compilation (
  event_id BIGINT NOT NULL,
  compilation_id BIGINT NOT NULL,
  CONSTRAINT fk_event_compilation_1 FOREIGN KEY (event_id) REFERENCES PUBLIC.event(event_id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT fk_event_compilation_2 FOREIGN KEY (compilation_id) REFERENCES PUBLIC.compilation(compilation_id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT pk_event_compilation PRIMARY KEY (event_id, compilation_id)
);


