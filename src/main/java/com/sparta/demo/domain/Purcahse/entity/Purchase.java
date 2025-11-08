package com.sparta.demo.domain.Purcahse.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.sparta.demo.domain.User.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table
@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(nullable = false)
    BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    PurchaseStatus status;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseItem> purchaseItems = new ArrayList<>();

    @Builder
    public Purchase(
            User user,
            BigDecimal totalPrice,
            PurchaseStatus status
    ) {
        this.user = user;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public void addPurchaseItem(PurchaseItem purchaseItem) { // addOrderItem -> addPurchaseItem
        purchaseItems.add(purchaseItem);
        purchaseItem.setPurchase(this); // setOrder -> setPurchase
    }

    //== 비즈니스 로직 ==//
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void changeStatus(PurchaseStatus status) {
        this.status = status;
    }

}