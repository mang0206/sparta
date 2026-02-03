CREATE INDEX idx_author_deleted ON posts(author_id, deleted_at);
CREATE INDEX idx_created_deleted ON posts(created_at DESC, deleted_at);
