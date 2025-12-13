CREATE TABLE coupon
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '쿠폰 ID',
    name     VARCHAR(100)   NOT NULL COMMENT '쿠폰명',
    discount DECIMAL(19, 2) NOT NULL COMMENT '할인 금액',
    status   VARCHAR(20)    NOT NULL COMMENT '상태'
);

CREATE TABLE coupon_user
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '쿠폰 발급 ID',
    coupon_id BIGINT       NOT NULL COMMENT '쿠폰 ID',
    user_id   BIGINT       NULL COMMENT '사용자 ID',
    code      VARCHAR(100) NOT NULL UNIQUE COMMENT '쿠폰 코드',
    status    VARCHAR(20)  NOT NULL COMMENT '상태',

    CONSTRAINT fk_coupon_user_coupon FOREIGN KEY (coupon_id) REFERENCES coupon (id)
);
