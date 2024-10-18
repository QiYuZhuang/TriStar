DROP TABLE IF EXISTS order_line CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS customer CASCADE;
DROP TABLE IF EXISTS district CASCADE;
DROP TABLE IF EXISTS stock CASCADE;
DROP TABLE IF EXISTS warehouse CASCADE;
DROP TABLE IF EXISTS conflict_s CASCADE;
DROP TABLE IF EXISTS conflict_c CASCADE;

CREATE TABLE warehouse (
    -- WarehouseID
                           w_id       int            NOT NULL,
    -- WarehouseYTD
                           w_ytd      decimal(12, 2) NOT NULL,
    -- WarehouseInfo
                           w_info VARCHAR(255) NULL DEFAULT NULL,

                           PRIMARY KEY (w_id)
);

CREATE TABLE stock (
    -- Stock WarehouseID
                       s_w_id       int           NOT NULL,
    -- Stock ItemID
                       s_i_id       int           NOT NULL,
    -- Stock Quantity
                       s_quantity   int           NOT NULL,

                       FOREIGN KEY (s_w_id) REFERENCES warehouse (w_id) ON DELETE CASCADE,
                       PRIMARY KEY (s_w_id, s_i_id)
);

CREATE TABLE district (
    -- District WarehouseID
                          d_w_id      int            NOT NULL,
    -- District ID
                          d_id        int            NOT NULL,
    -- District YTD
                          d_ytd       decimal(12, 2) NOT NULL,
    -- DistrictID NextOrderID
                          d_next_o_id int            NOT NULL,
    -- District Info
                          d_info      VARCHAR(255)    NULL DEFAULT NULL,

                          FOREIGN KEY (d_w_id) REFERENCES warehouse (w_id) ON DELETE CASCADE,
                          PRIMARY KEY (d_w_id, d_id)
);

CREATE TABLE customer (
    -- warehouse
                          c_w_id         int            NOT NULL,
    -- district
                          c_d_id         int            NOT NULL,
    -- customer
                          c_id           int            NOT NULL,
    -- balance
                          c_balance      decimal(12, 2) NOT NULL,
    -- info
                          c_info         VARCHAR(255)     NULL DEFAULT NULL,

                          FOREIGN KEY (c_w_id, c_d_id) REFERENCES district (d_w_id, d_id) ON DELETE CASCADE,
                          PRIMARY KEY (c_w_id, c_d_id, c_id)
);

CREATE TABLE orders (
                        o_w_id       int       NOT NULL,
                        o_d_id       int       NOT NULL,
                        o_id         int       NOT NULL,
                        o_c_id       int       NOT NULL,
                        o_status     VARCHAR(50)    NULL DEFAULT NULL,

                        PRIMARY KEY (o_w_id, o_d_id, o_id),
                        FOREIGN KEY (o_w_id, o_d_id, o_c_id) REFERENCES customer (c_w_id, c_d_id, c_id) ON DELETE CASCADE,
                        UNIQUE (o_w_id, o_d_id, o_c_id, o_id)
);

CREATE TABLE order_line (
                            ol_w_id        int           NOT NULL,
                            ol_d_id        int           NOT NULL,
                            ol_o_id        int           NOT NULL,
                            ol_number      int           NOT NULL,
                            ol_i_id        int           NOT NULL,
                            ol_delivery_info  VARCHAR(255)    NULL DEFAULT NULL,
                            ol_quantity    int           NOT NULL,

                            FOREIGN KEY (ol_w_id, ol_d_id, ol_o_id) REFERENCES orders (o_w_id, o_d_id, o_id) ON DELETE CASCADE,
                            FOREIGN KEY (ol_w_id, ol_i_id) REFERENCES stock (s_w_id, s_i_id) ON DELETE CASCADE,
                            PRIMARY KEY (ol_w_id, ol_d_id, ol_o_id, ol_number)
);

CREATE TABLE conflict_s (
                            ol_w_id        int           NOT NULL,
                            ol_i_id        int           NOT NULL,

                            FOREIGN KEY (ol_w_id, ol_i_id) REFERENCES stock (s_w_id, s_i_id) ON DELETE CASCADE,
                            PRIMARY KEY (ol_w_id, ol_i_id)
);

CREATE TABLE conflict_c (
    -- warehouse
                            c_w_id         int            NOT NULL,
    -- district
                            c_d_id         int            NOT NULL,
    -- customer
                            c_id           int            NOT NULL,

                            FOREIGN KEY (c_w_id, c_d_id) REFERENCES district (d_w_id, d_id) ON DELETE CASCADE,
                            PRIMARY KEY (c_w_id, c_d_id, c_id)
);


CREATE INDEX idx_customer_name ON customer (c_w_id, c_d_id);