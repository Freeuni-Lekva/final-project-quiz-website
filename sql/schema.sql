CREATE TABLE users (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(128) NOT NULL,
    password_hash VARCHAR(128) NOT NULL,
    email VARCHAR(256),
    first_name VARCHAR(128) NOT NULL,
    last_name VARCHAR(128)
);

CREATE TABLE admins (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE friendships (
    id BIGINT PRIMARY KEY auto_increment,
    first_user_id BIGINT,
    second_user_id BIGINT,
    FOREIGN KEY (first_user_id) REFERENCES users(id),
    FOREIGN KEY (second_user_id) REFERENCES users(id)
);
