-- DB and UTF8 settings as needed

CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE users_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- seed roles
INSERT INTO roles(name) VALUES ('ADMIN'), ('RECRUITER'), ('CANDIDATE');

-- seed admin (password to be updated in Phase 1 after BCrypt hashing)
INSERT INTO users(email, password, full_name) VALUES ('admin@jobportal.local', 'temp', 'System Admin');
INSERT INTO users_roles(user_id, role_id)
SELECT u.id, r.id from users u CROSS JOIN roles r WHERE u.email='admin@jobportal.local' AND r.name='ADMIN';