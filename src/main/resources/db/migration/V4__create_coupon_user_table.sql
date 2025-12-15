CREATE TABLE IF NOT EXISTS coupon_user
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '쿠폰 사용자 ID',
    coupon_id   BIGINT       NOT NULL COMMENT '쿠폰 ID (FK)',
    user_id     BIGINT       NULL     COMMENT '사용자 ID (발급 전 NULL)',
    code        VARCHAR(100) NOT NULL COMMENT '쿠폰 발급 코드 (Unique)',
    status      VARCHAR(20)  NOT NULL DEFAULT 'READY' COMMENT '상태 (READY, ISSUED, USED)',
    created_at  DATETIME     NULL     COMMENT '생성 일시',
    updated_at  DATETIME     NULL     COMMENT '수정 일시',

    CONSTRAINT uk_coupon_user_code UNIQUE (code),
    ) COMMENT '오프라인/대량 발급 쿠폰 지급 내역';