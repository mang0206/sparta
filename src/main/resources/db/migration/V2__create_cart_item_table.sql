CREATE TABLE cart_item
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '장바구니 아이템 ID',
    user_id    BIGINT   NOT NULL COMMENT '사용자 ID',
    product_id BIGINT   NOT NULL COMMENT '상품 ID',
    quantity   INT      NOT NULL COMMENT '수량',
    created_at DATETIME NOT NULL COMMENT '생성 일시',
    updated_at DATETIME NOT NULL COMMENT '수정 일시',

    CONSTRAINT fk_cart_item_product FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE INDEX idx_cart_item_user_id ON cart_item (user_id);