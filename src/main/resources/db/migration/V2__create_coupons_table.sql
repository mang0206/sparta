CREATE TABLE coupons
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '쿠폰 ID',
    coupon_name         VARCHAR(100) NOT NULL COMMENT '쿠폰 이름',
    discount_type       VARCHAR(20)  NOT NULL COMMENT '할인 유형 (PERCENTAGE, FIXED)',
    discount_value      DECIMAL(19, 2) NOT NULL COMMENT '할인 값 (비율 또는 금액)',
    min_order_amount    DECIMAL(19, 2) NOT NULL DEFAULT 0 COMMENT '최소 주문 금액',
    max_discount_amount DECIMAL(19, 2) NULL COMMENT '최대 할인 금액 (정률 할인 시)',
    start_date          DATETIME     NOT NULL COMMENT '유효 시작 일시',
    end_date            DATETIME     NOT NULL COMMENT '유효 종료 일시',
    usage_limit         INTEGER      NOT NULL DEFAULT 0 COMMENT '발급 한도',
    issue_count         INTEGER      NOT NULL DEFAULT 0 COMMENT '발급된 수',
    used_count          INTEGER      NOT NULL DEFAULT 0 COMMENT '사용된 수',
    is_deleted          BOOLEAN      NOT NULL DEFAULT FALSE COMMENT '삭제 여부',
    created_at          DATETIME     NOT NULL COMMENT '생성 일시',
    updated_at          DATETIME     NOT NULL COMMENT '수정 일시',
    INDEX idx_coupons_date (start_date, end_date),
    INDEX idx_coupons_is_active (end_date, is_deleted)
);
