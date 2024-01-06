CREATE TABLE IF NOT EXISTS notification_task
(
id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
chat_id BIGINT NOT NULL,
notification TEXT,
alarm_date TIMESTAMP

);