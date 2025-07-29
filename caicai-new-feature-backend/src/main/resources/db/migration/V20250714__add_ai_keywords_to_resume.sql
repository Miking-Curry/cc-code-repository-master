-- 为resume表添加ai_keywords字段
ALTER TABLE resume ADD COLUMN ai_keywords TEXT COMMENT 'AI生成的关键词，JSON数组格式'; 