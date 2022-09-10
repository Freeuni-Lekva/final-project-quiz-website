CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(128) NOT NULL,
    password_hash VARCHAR(512) NOT NULL,
    email VARCHAR(256),
    is_admin BOOLEAN,
    first_name VARCHAR(128),
    last_name VARCHAR(128),
    profile_pic_url VARCHAR(256) DEFAULT 'images/default_profile_pic.jpg',
    bio TEXT,
    CONSTRAINT uq_users_username UNIQUE (username),
    CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS friendships (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_user_id BIGINT,
    second_user_id BIGINT,
    FOREIGN KEY (first_user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (second_user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friend_requests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sender_id BIGINT,
    receiver_id BIGINT,
    send_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS categories(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(64)
);

-- This one needs completion
CREATE TABLE IF NOT EXISTS quizzes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    creator_id BIGINT,
    category_id BIGINT,
    last_question_id BIGINT DEFAULT 0,
    creation_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    quiz_title VARCHAR(64),
    quiz_description VARCHAR(64),
    quiz_timer BIGINT DEFAULT 0,
    FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS questions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    creator_id BIGINT,
    category_id BIGINT DEFAULT NULL,
    creation_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    question_object MEDIUMBLOB,
    question_title VARCHAR(64),
    FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS question_to_quiz (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id BIGINT,
    quiz_id BIGINT,
    local_id BIGINT,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS quiz_comments(
    id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    quiz_id BIGINT,
    user_id BIGINT,
    comment_content VARCHAR(512),
    creation_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS challenges (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sender_id BIGINT,
    receiver_id BIGINT,
    quiz_id BIGINT,
    send_date DATETIME,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_sessions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE,
    quiz_id BIGINT,
    current_local_id BIGINT DEFAULT 0,
    start_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    quiz_time BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS quiz_answers (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT,
  quiz_id BIGINT,
  local_question_id BIGINT,
  score DOUBLE,
  UNIQUE(user_id, quiz_id, local_question_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS quiz_final_scores (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT,
  quiz_id BIGINT,
  score DOUBLE,
  max_score DOUBLE,
  start_time DATETIME,
  end_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS quiz_challenges (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  to_user_id BIGINT,
  from_user_id BIGINT,
  quiz_id BIGINT,
  time_limit BIGINT,
  date_sent DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (from_user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (to_user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS announcements (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    creator_id BIGINT,
    creation_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    title VARCHAR(128),
    text_html TEXT,
    FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS quiz_ratings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    quiz_id BIGINT,
    creator_id BIGINT,
    rating INT,
    FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
    CONSTRAINT rating_ck CHECK (rating BETWEEN 1 AND 5)
);

CREATE TABLE IF NOT EXISTS achievements (
  id BIGINT PRIMARY KEY  AUTO_INCREMENT,
  user_id BIGINT,
  icon_class VARCHAR(64),
  achievement_text VARCHAR(64),
  UNIQUE(user_id, icon_class, achievement_text),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
