-- Схема базы данных системы управления поставками

CREATE TABLE app_user (
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL,
    full_name   VARCHAR(150) NOT NULL,
    role        VARCHAR(30)  NOT NULL,
    enabled     BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE supplier (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    inn             VARCHAR(12)  NOT NULL UNIQUE,
    kpp             VARCHAR(9),
    contact_person  VARCHAR(150),
    phone           VARCHAR(30),
    email           VARCHAR(150),
    address         VARCHAR(300),
    rating          NUMERIC(2,1) NOT NULL DEFAULT 5.0 CHECK (rating BETWEEN 0 AND 5),
    active          BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TABLE category (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(300)
);

CREATE TABLE product (
    id            BIGSERIAL PRIMARY KEY,
    sku           VARCHAR(40)   NOT NULL UNIQUE,
    name          VARCHAR(200)  NOT NULL,
    category_id   BIGINT        NOT NULL REFERENCES category(id),
    unit          VARCHAR(20)   NOT NULL DEFAULT 'шт',
    price         NUMERIC(14,2) NOT NULL CHECK (price >= 0),
    min_stock     INTEGER       NOT NULL DEFAULT 0 CHECK (min_stock >= 0),
    description   VARCHAR(500)
);

CREATE TABLE warehouse (
    id            BIGSERIAL PRIMARY KEY,
    code          VARCHAR(20)  NOT NULL UNIQUE,
    name          VARCHAR(150) NOT NULL,
    region        VARCHAR(100) NOT NULL,
    address       VARCHAR(300) NOT NULL,
    manager       VARCHAR(150),
    capacity      INTEGER      NOT NULL DEFAULT 10000 CHECK (capacity > 0)
);

CREATE TABLE stock (
    id            BIGSERIAL PRIMARY KEY,
    product_id    BIGINT  NOT NULL REFERENCES product(id) ON DELETE CASCADE,
    warehouse_id  BIGINT  NOT NULL REFERENCES warehouse(id) ON DELETE CASCADE,
    quantity      INTEGER NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    CONSTRAINT uq_stock UNIQUE (product_id, warehouse_id)
);

CREATE TABLE purchase_order (
    id              BIGSERIAL PRIMARY KEY,
    number          VARCHAR(30)   NOT NULL UNIQUE,
    supplier_id     BIGINT        NOT NULL REFERENCES supplier(id),
    warehouse_id    BIGINT        NOT NULL REFERENCES warehouse(id),
    status          VARCHAR(30)   NOT NULL,
    order_date      DATE          NOT NULL,
    expected_date   DATE,
    total_amount    NUMERIC(16,2) NOT NULL DEFAULT 0,
    comment         VARCHAR(500),
    created_by      VARCHAR(50),
    created_at      TIMESTAMP     NOT NULL DEFAULT now()
);

CREATE TABLE order_item (
    id              BIGSERIAL PRIMARY KEY,
    order_id        BIGINT        NOT NULL REFERENCES purchase_order(id) ON DELETE CASCADE,
    product_id      BIGINT        NOT NULL REFERENCES product(id),
    quantity        INTEGER       NOT NULL CHECK (quantity > 0),
    unit_price      NUMERIC(14,2) NOT NULL CHECK (unit_price >= 0),
    received_qty    INTEGER       NOT NULL DEFAULT 0 CHECK (received_qty >= 0)
);

CREATE TABLE delivery (
    id              BIGSERIAL PRIMARY KEY,
    number          VARCHAR(30)  NOT NULL UNIQUE,
    order_id        BIGINT       NOT NULL REFERENCES purchase_order(id),
    delivery_date   DATE         NOT NULL,
    waybill         VARCHAR(50),
    carrier         VARCHAR(150),
    status          VARCHAR(30)  NOT NULL,
    comment         VARCHAR(500),
    received_by     VARCHAR(50),
    created_at      TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TABLE delivery_item (
    id              BIGSERIAL PRIMARY KEY,
    delivery_id     BIGINT  NOT NULL REFERENCES delivery(id) ON DELETE CASCADE,
    order_item_id   BIGINT  NOT NULL REFERENCES order_item(id),
    quantity        INTEGER NOT NULL CHECK (quantity > 0)
);

CREATE INDEX idx_product_category ON product(category_id);
CREATE INDEX idx_order_supplier   ON purchase_order(supplier_id);
CREATE INDEX idx_order_status     ON purchase_order(status);
CREATE INDEX idx_order_date       ON purchase_order(order_date);
CREATE INDEX idx_item_order       ON order_item(order_id);
CREATE INDEX idx_delivery_order   ON delivery(order_id);
