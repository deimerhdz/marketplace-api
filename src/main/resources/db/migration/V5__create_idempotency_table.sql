CREATE TABLE idempotency_keys
(
    key           VARCHAR(255) PRIMARY KEY,
    endpoint      VARCHAR(255) NOT NULL,
    request_hash  VARCHAR(255) NOT NULL,
    response_body TEXT,
    status_code   INTEGER,
    status        VARCHAR(20) CHECK (status IN ('PROCESSING', 'SUCCESS', 'FAILED')),
    created_at    TIMESTAMP DEFAULT NOW()
);