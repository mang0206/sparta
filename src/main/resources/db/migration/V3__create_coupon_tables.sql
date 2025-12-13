CREATE TABLE coupon
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '쿠폰 ID',
    name       VARCHAR(100)   NOT NULL COMMENT '쿠폰명',
    discount   DECIMAL(19, 2) NOT NULL COMMENT '할인 금액',
    status     VARCHAR(50)    NOT NULL COMMENT '쿠폰 상태',
    valid_from DATETIME       NOT NULL COMMENT '유효 시작일',
    valid_to   DATETIME       NOT NULL COMMENT '유효 종료일',
    INDEX idx_coupon_name (name)
);

CREATE TABLE coupon_user
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '쿠폰 사용자 ID',
    coupon_id BIGINT      NOT NULL COMMENT '쿠폰 ID',
    user_id   BIGINT      NULL COMMENT '사용자 ID (미발급 시 NULL)',
    code      VARCHAR(50) NOT NULL UNIQUE COMMENT '쿠폰 코드',
    status    VARCHAR(50) NOT NULL COMMENT '쿠폰 사용자 상태',
    FOREIGN KEY (coupon_id) REFERENCES coupon (id),
    INDEX idx_coupon_user_code (code),
    INDEX idx_coupon_user_user_id (user_id)
);
