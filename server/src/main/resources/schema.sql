CREATE TABLE IF NOT EXISTS category (
  category_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(512) NOT NULL,
  CONSTRAINT UQ_CATEGORY_NAME UNIQUE (name),
  CONSTRAINT PRIMARY KEY (category_id)
);

CREATE TABLE IF NOT EXISTS users (
  user_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (user_id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS event (
  event_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  annotation VARCHAR(512) NOT NULL,
  category_id BIGINT NOT NULL,
  created_on TIMESTAMP NOT NULL,
  description VARCHAR(512),
  event_date TIMESTAMP NOT NULL,
  initiator_id BIGINT NOT NULL,
  location_lat FLOAT NOT NULL,
  location_lon FLOAT NOT NULL,
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