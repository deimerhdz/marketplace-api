-- ==========================================
-- SCRIPT CONSOLIDADO COMPLETO
-- Orden de creación por dependencias:
-- 1. users
-- 2. breeds
-- 3. suppliers   (FK → users)
-- 4. bulls       (FK → breeds, suppliers)
-- 5. straws      (FK → bulls)
-- 6. inventory   (FK → straws)
-- 7. orders      (FK → users, suppliers)
-- 8. order_item  (FK → orders, straws)
-- 9. payments    (FK → orders)
-- ==========================================


-- ==========================================
-- TABLE: users
-- ==========================================
CREATE TABLE users (
                       id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
                       email           VARCHAR(255)    NOT NULL,
                       name            VARCHAR(255)    NOT NULL,
                       last_name       VARCHAR(255)    NOT NULL,
                       cognito_sub       VARCHAR(255)    NOT NULL,
                       role       VARCHAR(255)    NOT NULL,
                       active      BOOLEAN     NOT NULL DEFAULT false,
                       created_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
                       updated_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),

                       CONSTRAINT chk_roles   CHECK (role IN ('ADMIN', 'SUPPLIER','CUSTOMER'))
);

CREATE UNIQUE INDEX idx_user_email ON users (email);
CREATE UNIQUE INDEX idx_user_cognito_id ON users (cognito_sub);

-- ==========================================
-- TABLE: suppliers
-- ==========================================
CREATE TABLE suppliers (
                           id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
                           nit             VARCHAR(255)    NOT NULL,
                           email           VARCHAR(255)    NOT NULL,
                           phone           VARCHAR(50)     NOT NULL,
                           user_id         UUID            NOT NULL,
                           image           JSONB,
                           legal_name      VARCHAR(255)    NOT NULL,

                           created_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
                           updated_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),

                           CONSTRAINT fk_supplier_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE UNIQUE INDEX idx_supplier_email  ON suppliers (email);
CREATE UNIQUE INDEX idx_supplier_nit    ON suppliers (nit);
CREATE INDEX        idx_supplier_user   ON suppliers (user_id);


-- ==========================================
-- TABLE: bulls
-- ==========================================
CREATE TABLE bulls (
                       id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
                       name            VARCHAR(255)    NOT NULL,
                       slug            VARCHAR(255)    NOT NULL,
                       stud            VARCHAR(255)    NOT NULL,
                       breed_id        UUID            NOT NULL,
                       supplier_id     UUID            NOT NULL,
                       image           JSONB           NOT NULL,
                       video           JSONB           NOT NULL,
                       pedigree        JSONB,
                       description     TEXT            NOT NULL,
                       birth_date      DATE            NOT NULL,
                       gallery         JSONB,

                       created_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
                       updated_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),


                       CONSTRAINT fk_bull_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers (id)
);

CREATE UNIQUE INDEX idx_bull_slug       ON bulls (slug);
CREATE INDEX        idx_bull_breed      ON bulls (breed_id);
CREATE INDEX        idx_bull_supplier   ON bulls (supplier_id);
CREATE INDEX        idx_bull_name       ON bulls (name);

ALTER TABLE bulls
    ADD CONSTRAINT fk_bull_breed FOREIGN KEY (breed_id) REFERENCES breeds (id);
-- ==========================================
-- TABLE: straws
-- ==========================================
CREATE TABLE straws (
                        id          UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
                        bull_id     UUID            NOT NULL,
                        sku         VARCHAR(255)    NOT NULL,
                        type        VARCHAR(50)     NOT NULL,
                        price       DECIMAL(10,2)   NOT NULL,
                        min_order   INTEGER         NOT NULL,
                        created_at  TIMESTAMPTZ     NOT NULL DEFAULT now(),
                        updated_at  TIMESTAMPTZ     NOT NULL DEFAULT now(),

                        CONSTRAINT fk_straw_bull    FOREIGN KEY (bull_id) REFERENCES bulls (id),
                        CONSTRAINT chk_straw_type   CHECK (type IN ('CONVENTIONAL', 'SEXED')),
                        CONSTRAINT chk_straw_price  CHECK (price > 0),
                        CONSTRAINT chk_straw_min_order CHECK (min_order > 0)
);

CREATE INDEX        idx_straw_bull          ON straws (bull_id);
CREATE UNIQUE INDEX uk_straw_bull_type      ON straws (bull_id, type);
CREATE UNIQUE INDEX uk_straw_sku            ON straws (sku);


-- ==========================================
-- TABLE: inventory
-- ==========================================
CREATE TABLE inventory (
                           id              UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
                           straw_id        UUID        NOT NULL,
                           stock           INTEGER     NOT NULL,
                           min_stock       INTEGER     NOT NULL,
                           is_available    BOOLEAN     NOT NULL,
                           created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
                           updated_at      TIMESTAMPTZ NOT NULL DEFAULT now(),

                           CONSTRAINT fk_inventory_straw   FOREIGN KEY (straw_id) REFERENCES straws (id),
                           CONSTRAINT chk_inventory_stock  CHECK (stock >= 0),
                           CONSTRAINT chk_inventory_min    CHECK (min_stock >= 0)
);

CREATE UNIQUE INDEX uk_inventory_straw              ON inventory (straw_id);
CREATE INDEX        idx_inventory_available         ON inventory (is_available);
CREATE INDEX        idx_inventory_available_straw   ON inventory (is_available, straw_id);


-- ==========================================
-- TABLE: orders
-- ==========================================
CREATE TABLE orders (
                        id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
                        customer_id     UUID            NOT NULL,
                        supplier_id     UUID            NOT NULL,
                        total           DECIMAL(10,2)   NOT NULL,
                        order_status    VARCHAR(50)     NOT NULL,
                        created_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),

                        CONSTRAINT fk_order_customer    FOREIGN KEY (customer_id) REFERENCES users     (id),
                        CONSTRAINT fk_order_supplier    FOREIGN KEY (supplier_id) REFERENCES suppliers (id),
                        CONSTRAINT chk_order_total      CHECK (total >= 0),
                        CONSTRAINT chk_order_status     CHECK (order_status IN (
                                                                                'PENDING', 'PAID', 'PROCESSING', 'SHIPPED', 'COMPLETED', 'CANCELLED'
                            ))
);

CREATE INDEX idx_order_customer         ON orders (customer_id);
CREATE INDEX idx_order_supplier         ON orders (supplier_id);
CREATE INDEX idx_order_customer_created ON orders (customer_id, created_at);
CREATE INDEX idx_order_supplier_status  ON orders (supplier_id, order_status);
CREATE INDEX idx_order_status_created   ON orders (order_status, created_at);


-- ==========================================
-- TABLE: order_item
-- ==========================================
CREATE TABLE order_item (
                            id          UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
                            sku         VARCHAR(255)    NOT NULL,
                            order_id    UUID,
                            straw_id    UUID,
                            type        VARCHAR(50)     NOT NULL,
                            bull_name   VARCHAR(255)    NOT NULL,
                            quantity    INTEGER         NOT NULL,
                            price       DECIMAL(10,2)   NOT NULL,
                            subtotal    DECIMAL(10,2)   NOT NULL,

                            CONSTRAINT fk_order_item_order  FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
                            CONSTRAINT fk_order_item_straw  FOREIGN KEY (straw_id) REFERENCES straws (id) ON DELETE SET NULL,
                            CONSTRAINT chk_order_item_type      CHECK (type IN ('CONVENTIONAL', 'SEXED')),
                            CONSTRAINT chk_order_item_qty       CHECK (quantity > 0),
                            CONSTRAINT chk_order_item_price     CHECK (price > 0),
                            CONSTRAINT chk_order_item_subtotal  CHECK (subtotal > 0)
);

CREATE INDEX        idx_order_item_order        ON order_item (order_id);
CREATE UNIQUE INDEX uk_order_item_order_sku     ON order_item (order_id, sku);
CREATE INDEX        idx_order_item_straw        ON order_item (straw_id);
CREATE INDEX        idx_order_item_sku          ON order_item (sku);


-- ==========================================
-- TABLE: payments
-- ==========================================
CREATE TABLE payments (
                          id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
                          amount          DECIMAL(10,2)   NOT NULL,
                          method          VARCHAR(100)    NOT NULL,
                          transaction_ref VARCHAR(255)    NOT NULL,
                          order_id        UUID            NOT NULL,
                          created_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),

                          CONSTRAINT fk_payment_order     FOREIGN KEY (order_id) REFERENCES orders (id),
                          CONSTRAINT chk_payment_amount   CHECK (amount > 0)
);

CREATE INDEX idx_payment_order_id ON payments (order_id);