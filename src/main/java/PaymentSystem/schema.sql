-- Merchants Table
CREATE TABLE merchants(
                       id VARCHAR(36) PRIMARY KEY,
                       merchant_name VARCHAR(100) NOT NULL
                      );


-- Customers Table
CREATE TABLE customers(
                       id BIGINT PRIMARY KEY,
                       email VARCHAR(50) UNIQUE NOT NULL ,
                       full_name VARCHAR(50),
                       created_at TIMESTAMP NOT NULL
                      );


CREATE TABLE payments(
                      id BIGINT PRIMARY KEY,
                      merchant_id VARCHAR(36) NOT NULL,   --FK To merch
                      customer_id BIGINT NOT NULL,   --FK To customers
                      amount DECIMAL(10,2) NOT NULL,
                      currency VARCHAR(10) NOT NULL,
                      status VARCHAR(15) NOT NULL,
                      type VARCHAR(15) NOT NULL,
                      created_at TIMESTAMP NOT NULL,
                      updated_at TIMESTAMP,


                      CONSTRAINT fk_payment_merchant FOREIGN KEY(merchant_id) REFERENCES merchants(id),
                      CONSTRAINT fk_payment_customer FOREIGN KEY(customer_id) REFERENCES customers(id)
                     );

CREATE TABLE refunds(
                      id BIGINT PRIMARY KEY,
                      payment_id BIGINT UNIQUE NOT NULL, --FK TO payments (1:1)
                      amount DECIMAL(10, 2) NOT NULL,
                      created_at TIMESTAMP NOT NULL,

                      CONSTRAINT fk_refund_payment FOREIGN KEY(payment_id) REFERENCES payments(id)
                    );


CREATE INDEX idx_customer_email ON customers(email);  --index on customer email
CREATE INDEX idx_payment_merchant ON payments(merchant_id); --index on payments by merch
CREATE INDEX idx_payment_customer ON payments(customer_id); --index on payments by customer
CREATE INDEX idx_refund_payment ON refunds(payment_id); --index on refunds by payment
