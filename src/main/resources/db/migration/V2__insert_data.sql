INSERT INTO breeds (id, name, type, created_at, updated_at)
VALUES
--  Carne
(gen_random_uuid(), 'Brahman', 'MEAT', NOW(), NOW()),
(gen_random_uuid(), 'Nelore', 'MEAT', NOW(), NOW()),
(gen_random_uuid(), 'Gyr', 'MEAT', NOW(), NOW()),
(gen_random_uuid(), 'Angus', 'MEAT', NOW(), NOW()),
(gen_random_uuid(), 'Brangus', 'MEAT', NOW(), NOW()),

--  Leche
(gen_random_uuid(), 'Holstein', 'MILK', NOW(), NOW()),
(gen_random_uuid(), 'Jersey', 'MILK', NOW(), NOW()),
(gen_random_uuid(), 'Pardo Suizo', 'MILK', NOW(), NOW()),
(gen_random_uuid(), 'Guernsey', 'MILK', NOW(), NOW()),
(gen_random_uuid(), 'Normando', 'MILK', NOW(), NOW());