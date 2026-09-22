CREATE TABLE IF NOT EXISTS products (
    id VARCHAR(64) PRIMARY KEY,
    name VARCHAR(120) NOT NULL UNIQUE,
    price NUMERIC(12, 2) NOT NULL,
    stock INTEGER NOT NULL CHECK (stock >= 0)
);

CREATE UNIQUE INDEX IF NOT EXISTS products_name_unique ON products (name);