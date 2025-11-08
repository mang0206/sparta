package com.sparta.demo.domain.User.entity;

import com.sparta.demo.domain.Purcahse.entity.Purchase;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity // 이 클래스는 JPA가 관리하는 엔티티입니다.
@Getter // Lombok: Getter 메서드를 자동 생성합니다.
@DynamicInsert // 값이 null이 아닌 필드만으로 INSERT 쿼리를 생성합니다.
@DynamicUpdate // 변경된 필드만으로 UPDATE 쿼리를 생성합니다.
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA는 기본 생성자를 필요로 합니다.
@Table(name = "user") // 'user'라는 이름의 테이블과 매핑됩니다.
public class User {

    @Id // 이 필드가 테이블의 Primary Key(기본 키)입니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 생성을 DB의 AUTO_INCREMENT에 위임합니다.
    private Long id;

    @Column(name = "username", nullable = false, length = 50) // 'username' 컬럼, NOT NULL, 길이 50
    private String username;

    @Column(nullable = false, unique = true) // 'email' 컬럼, NOT NULL, UNIQUE
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @CreationTimestamp // 엔티티가 생성될 때의 시간이 자동으로 기록됩니다.
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // 엔티티가 수정될 때의 시간이 자동으로 기록됩니다.
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Purchase> purchases;

    @Builder // 빌더 패턴으로 객체를 생성할 수 있게 합니다.
    public User(
            String username,
            String email,
            String passwordHash
    ) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }
}