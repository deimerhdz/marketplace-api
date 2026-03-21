CREATE TABLE IF NOT EXISTS breeds(
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(20) CHECK (type IN ('MEAT', 'MILK')),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    CONSTRAINT uk_breed_name_type UNIQUE (name, type)
)