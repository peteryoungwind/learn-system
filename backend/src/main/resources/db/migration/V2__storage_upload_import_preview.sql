CREATE TABLE IF NOT EXISTS storage_configs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    provider VARCHAR(32) NOT NULL,
    endpoint VARCHAR(255) NOT NULL,
    bucket VARCHAR(128) NOT NULL,
    region VARCHAR(64) NULL,
    access_key_id VARCHAR(255) NOT NULL,
    access_key_secret VARCHAR(255) NOT NULL,
    domain VARCHAR(255) NULL,
    base_path VARCHAR(255) NULL,
    status VARCHAR(16) NOT NULL DEFAULT 'ACTIVE',
    is_default TINYINT(1) NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS file_type_presets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(32) NOT NULL UNIQUE,
    name VARCHAR(64) NOT NULL,
    extensions VARCHAR(255) NOT NULL,
    mime_types VARCHAR(255) NULL,
    preview_mode VARCHAR(32) NOT NULL,
    is_enabled TINYINT(1) NOT NULL DEFAULT 1,
    sort_order INT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

ALTER TABLE categories
    ADD COLUMN cover_url VARCHAR(500) NULL AFTER description;

ALTER TABLE albums
    ADD COLUMN cover_url VARCHAR(500) NULL AFTER description;

ALTER TABLE materials
    ADD COLUMN original_filename VARCHAR(255) NULL AFTER object_key,
    ADD COLUMN mime_type VARCHAR(128) NULL AFTER original_filename,
    ADD COLUMN source_import_task_id BIGINT NULL AFTER mime_type,
    ADD COLUMN preview_object_key VARCHAR(500) NULL AFTER source_import_task_id,
    ADD COLUMN preview_status VARCHAR(16) NULL AFTER preview_object_key;

ALTER TABLE import_tasks
    ADD COLUMN category_id BIGINT NULL AFTER source_prefix,
    ADD COLUMN album_id BIGINT NULL AFTER category_id,
    ADD COLUMN task_name VARCHAR(128) NULL AFTER album_id;

CREATE TABLE IF NOT EXISTS import_task_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id BIGINT NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    file_type VARCHAR(32) NULL,
    file_size BIGINT NULL,
    storage_path VARCHAR(500) NULL,
    status VARCHAR(16) NOT NULL,
    error_message VARCHAR(500) NULL,
    material_id BIGINT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_import_task_item_task FOREIGN KEY (task_id) REFERENCES import_tasks(id)
);

INSERT INTO file_type_presets (code, name, extensions, mime_types, preview_mode, is_enabled, sort_order)
SELECT 'HTML', 'HTML 页面', 'html,htm', 'text/html', 'INLINE_IFRAME', 1, 10
WHERE NOT EXISTS (SELECT 1 FROM file_type_presets WHERE code = 'HTML');

INSERT INTO file_type_presets (code, name, extensions, mime_types, preview_mode, is_enabled, sort_order)
SELECT 'PDF', 'PDF 文档', 'pdf', 'application/pdf', 'INLINE_IFRAME', 1, 20
WHERE NOT EXISTS (SELECT 1 FROM file_type_presets WHERE code = 'PDF');

INSERT INTO file_type_presets (code, name, extensions, mime_types, preview_mode, is_enabled, sort_order)
SELECT 'DOC', 'Word 文档', 'doc', 'application/msword', 'DOC_CONVERTED_PDF', 1, 30
WHERE NOT EXISTS (SELECT 1 FROM file_type_presets WHERE code = 'DOC');

INSERT INTO file_type_presets (code, name, extensions, mime_types, preview_mode, is_enabled, sort_order)
SELECT 'DOCX', 'Word 文档', 'docx', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 'DOC_CONVERTED_PDF', 1, 40
WHERE NOT EXISTS (SELECT 1 FROM file_type_presets WHERE code = 'DOCX');

INSERT INTO file_type_presets (code, name, extensions, mime_types, preview_mode, is_enabled, sort_order)
SELECT 'MP3', 'MP3 音频', 'mp3', 'audio/mpeg', 'AUDIO', 1, 50
WHERE NOT EXISTS (SELECT 1 FROM file_type_presets WHERE code = 'MP3');
