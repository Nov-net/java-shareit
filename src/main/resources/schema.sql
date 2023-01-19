DROP TABLE IF EXISTS comments, bookings, items, requests, users;

CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests (
   id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   description VARCHAR(1000) NOT NULL,
   requestor_id INTEGER NOT NULL REFERENCES users (id),
   created TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS items (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    available BOOLEAN NOT NULL,
    owner_id INTEGER NOT NULL REFERENCES users (id),
    request_id INTEGER REFERENCES requests(id)
);

CREATE TABLE IF NOT EXISTS bookings (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date  TIMESTAMP WITHOUT TIME ZONE,
    end_date  TIMESTAMP WITHOUT TIME ZONE,
    item_id INTEGER NOT NULL REFERENCES items (id),
    booker_id INTEGER NOT NULL REFERENCES users (id),
    status VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS comments (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text VARCHAR(1000) NOT NULL,
    item_id INTEGER NOT NULL REFERENCES items (id),
    author_id INTEGER NOT NULL REFERENCES users (id),
    created  TIMESTAMP WITHOUT TIME ZONE
);