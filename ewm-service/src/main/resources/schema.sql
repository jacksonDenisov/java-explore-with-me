DROP TABLE IF EXISTS users, categories, locations, events, compilations, compilation_events, participation_requests, estimations;

CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT uq_categories_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS locations
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL,
    CONSTRAINT pk_locations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    category_id BIGINT NOT NULL,
    description VARCHAR(7000) NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    location_id BIGINT NOT NULL,
    paid BOOLEAN NOT NULL,
    participant_limit INTEGER NOT NULL,
    request_moderation BOOLEAN NOT NULL,
    title VARCHAR(120) NOT NULL,
    confirmed_requests BIGINT NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator_id BIGINT NOT NULL,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    state VARCHAR(30) NOT NULL,
    views BIGINT NOT NULL,
    rating FLOAT,
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT fk_events_to_categories FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_events_to_locations FOREIGN KEY (location_id) REFERENCES locations (id),
    CONSTRAINT fk_events_to_users FOREIGN KEY (initiator_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN NOT NULL,
    title VARCHAR(120) NOT NULL,
    CONSTRAINT pk_compilations PRIMARY KEY (id),
    CONSTRAINT uq_compilations_title UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS compilation_events
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id BIGINT NOT NULL,
    compilation_id BIGINT NOT NULL,
    CONSTRAINT pk_compilation_events PRIMARY KEY (id),
    CONSTRAINT fk_compilation_events_to_events FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_compilation_events_to_compilations FOREIGN KEY (compilation_id) REFERENCES compilations (id)
);

CREATE TABLE IF NOT EXISTS participation_requests
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_participation_requests PRIMARY KEY (id),
    CONSTRAINT fk_participation_requests_to_events FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_participation_requests_to_users FOREIGN KEY (requester_id) REFERENCES users (id),
    CONSTRAINT uq_participation_requests_event_requester UNIQUE (event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS estimations
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    estimation_type VARCHAR(15) NOT NULL,
    CONSTRAINT pk_estimations PRIMARY KEY (id),
    CONSTRAINT fk_estimations_to_users FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_estimations_to_events FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT uq_estimations_user_event UNIQUE (user_id, event_id)
);