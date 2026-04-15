CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(128) NOT NULL,
    role VARCHAR(16) NOT NULL,
    status VARCHAR(16) NOT NULL,
    last_login_at DATETIME NULL,
    invited_at DATETIME NULL,
    remark VARCHAR(255) NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(128) NOT NULL,
    code VARCHAR(64) NOT NULL UNIQUE,
    description VARCHAR(255) NULL,
    status VARCHAR(16) NOT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS albums (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_id BIGINT NOT NULL,
    name VARCHAR(128) NOT NULL,
    description VARCHAR(255) NULL,
    status VARCHAR(16) NOT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_album_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT uk_album_category_name UNIQUE (category_id, name)
);

CREATE TABLE IF NOT EXISTS materials (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(128) NOT NULL,
    file_type VARCHAR(16) NOT NULL,
    storage_provider VARCHAR(16) NOT NULL,
    object_key VARCHAR(500) NOT NULL,
    category_id BIGINT NOT NULL,
    album_id BIGINT NULL,
    subtitle VARCHAR(255) NULL,
    summary TEXT NULL,
    cover_url VARCHAR(500) NULL,
    file_size BIGINT NULL,
    tags VARCHAR(255) NULL,
    sort_order INT NOT NULL DEFAULT 0,
    remark VARCHAR(255) NULL,
    ingest_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    publish_time DATETIME NULL,
    publish_status VARCHAR(16) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_material_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT fk_material_album FOREIGN KEY (album_id) REFERENCES albums(id)
);

CREATE TABLE IF NOT EXISTS user_category_permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    created_by BIGINT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_permission_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_permission_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT uk_user_category UNIQUE (user_id, category_id)
);

CREATE TABLE IF NOT EXISTS learning_progress (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    material_id BIGINT NOT NULL,
    read_status VARCHAR(16) NOT NULL,
    completion_status VARCHAR(16) NOT NULL,
    first_accessed_at DATETIME NULL,
    last_accessed_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_progress_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_progress_material FOREIGN KEY (material_id) REFERENCES materials(id),
    CONSTRAINT uk_user_material UNIQUE (user_id, material_id)
);

CREATE TABLE IF NOT EXISTS import_tasks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    import_type VARCHAR(32) NOT NULL,
    status VARCHAR(16) NOT NULL,
    total_count INT NOT NULL DEFAULT 0,
    success_count INT NOT NULL DEFAULT 0,
    fail_count INT NOT NULL DEFAULT 0,
    error_summary TEXT NULL,
    source_provider VARCHAR(16) NULL,
    source_bucket VARCHAR(128) NULL,
    source_prefix VARCHAR(255) NULL,
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_import_user FOREIGN KEY (created_by) REFERENCES users(id)
);

