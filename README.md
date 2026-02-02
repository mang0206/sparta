# 🚀 SNS 프로젝트 - MVP

## 📋 프로젝트 개요

**개발 기간**: 4주  
**핵심 기능**: 게시물 CRUD + Kafka 이벤트 스트리밍 + Elasticsearch 검색

### ⚡ MUST HAVE 기능

- ✅ 게시물 생성, 수정, 삭제 (DB 저장 후 Kafka 이벤트 발행)
- ✅ Elasticsearch 실시간 색인 (Kafka Consumer 구독)
- ✅ 단순 조회 (MySQL 직접 쿼리)
- ✅ 키워드 검색 (Elasticsearch 전문 검색)

---

## 🏗️ 시스템 아키텍처

```
┌─────────────┐      ┌─────────────┐      ┌─────────────┐      ┌─────────────┐
│             │      │             │      │             │      │             │
│  API Server │ ⇄    │   MySQL     │  →   │    Kafka    │  →   │Elasticsearch│
│ Spring Boot │      │             │      │             │      │             │
└─────────────┘      └─────────────┘      └─────────────┘      └─────────────┘
```

### 시스템 구성 요소

| 컴포넌트 | 기술 스택 | 역할 |
|---------|----------|------|
| **API Server** | Spring Boot | REST API 제공 |
| **Database** | MySQL | 게시물 데이터 영구 저장 (Source of Truth) |
| **Message Queue** | Apache Kafka | 이벤트 스트리밍 및 비동기 처리 |
| **Search Engine** | Elasticsearch | 전문 검색 및 색인 |

---

## 📝 Event Storming

### 1️⃣ 게시물 생성 플로우

```
게시물 작성 (Command)
    ↓
Post (Aggregate)
    ↓
게시물 생성됨 (Event) - MySQL 저장
    ↓
Kafka 이벤트 발행 정책 (Policy)
    ↓
Kafka Topic: post.created (External)
```

#### 🔄 Kafka Event Flow
```
Kafka Topic: post.created
    ↓
Elasticsearch Consumer (Policy)
    ↓
Elasticsearch 색인
    ↓
색인 완료됨 (Event)
```

---

### 2️⃣ 게시물 수정 플로우

```
게시물 수정 (Command)
    ↓
Post (Aggregate)
    ↓
게시물 수정됨 (Event) - MySQL 업데이트
    ↓
Kafka 이벤트 발행 정책 (Policy)
    ↓
Kafka Topic: post.updated (External)
    ↓
Elasticsearch Consumer → 색인 갱신됨
```

---

### 3️⃣ 게시물 삭제 플로우

```
게시물 삭제 (Command)
    ↓
Post (Aggregate)
    ↓
게시물 삭제됨 (Event) - MySQL 삭제/Soft Delete
    ↓
Kafka 이벤트 발행 정책 (Policy)
    ↓
Kafka Topic: post.deleted (External)
    ↓
Elasticsearch Consumer → 색인 삭제됨
```

---

### 4️⃣ 게시물 단순 조회 플로우

```
게시물 조회 (Command) - ID로 조회
    ↓
Post (Aggregate)
    ↓
MySQL (External)
    ↓
게시물 반환됨 (Event)
```

#### 💡 Implementation
- `GET /api/posts/{id}` - 단건 조회
- `GET /api/posts` - 목록 조회 (Pagination)
- JPA Repository 활용

---

### 5️⃣ 게시물 키워드 검색 플로우

```
키워드 검색 (Command)
    ↓
Search (Aggregate)
    ↓
Elasticsearch (External)
    ↓
검색 결과 반환됨 (Event)
```

#### 💡 Implementation
- `GET /api/posts/search?q={keyword}` - 키워드 검색
- Elasticsearch Query DSL
- Full-text Search
- Match Query

---

## 📨 Kafka Topics 정의

| Topic | 설명 | Payload |
|-------|------|---------|
| `post.created` | 게시물 생성 이벤트 | 게시물 ID, 제목, 내용, 작성자 정보, 타임스탬프 |
| `post.updated` | 게시물 수정 이벤트 | 게시물 ID, 수정된 내용, 타임스탬프 |
| `post.deleted` | 게시물 삭제 이벤트 | 게시물 ID, 타임스탬프 |

---

## 🔗 API Endpoints

| Method | Endpoint | 설명 | 데이터 소스 |
|--------|----------|------|------------|
| `POST` | `/api/posts` | 게시물 생성 | MySQL |
| `GET` | `/api/posts/{id}` | 게시물 단건 조회 | MySQL |
| `GET` | `/api/posts` | 게시물 목록 조회 (페이징) | MySQL |
| `PUT` | `/api/posts/{id}` | 게시물 수정 | MySQL |
| `DELETE` | `/api/posts/{id}` | 게시물 삭제 | MySQL |
| `GET` | `/api/posts/search?q={keyword}` | 키워드 검색 | **Elasticsearch** |

---

## 💡 구현 상세

### ✅ 게시물 생성/수정/삭제
- MySQL 트랜잭션 커밋 후 Kafka 이벤트 발행
- `@TransactionalEventListener` 활용

### ✅ Elasticsearch Consumer
- Kafka 이벤트 구독하여 Elasticsearch 색인 실시간 동기화
- `@KafkaListener` 활용

### ✅ 단순 조회
- MySQL 직접 쿼리 (ID 조회, 목록 조회, 페이징)
- JPA Repository 활용

### ✅ 키워드 검색
- Elasticsearch로 Full-text Search
- 제목, 내용 검색 지원
- 형태소 분석 (Nori Analyzer)

### ✅ 데이터 정합성
- **MySQL**: Source of Truth (원본 데이터)
- **Elasticsearch**: 검색 전용 (읽기 최적화)

---

## 🎯 기술 도입 배경

### 🔥 Why Kafka?

1. **비동기 이벤트 처리로 시스템 결합도 감소**
   - MySQL 저장과 Elasticsearch 색인을 분리
   - Elasticsearch 장애 시에도 게시물 API 정상 동작

2. **데이터 정합성 보장 및 재처리 가능성**
   - 이벤트 영구 저장으로 데이터 복구 가능
   - Consumer 재시작 시 누락된 이벤트 재처리

3. **확장 가능한 이벤트 기반 아키텍처**
   - 향후 알림, 추천, 통계 등 다양한 Consumer 추가 가능
   - 같은 이벤트를 여러 목적으로 재사용

4. **대용량 트래픽 대응**
   - Kafka가 버퍼 역할하여 백프레셔 관리
   - 급격한 트래픽 증가에도 안정적 처리

### 🔍 Why Elasticsearch?

1. **관계형 DB의 Full-text Search 한계 극복**
   - MySQL LIKE 검색은 느리고 비효율적
   - 역색인 구조로 빠른 검색 성능 제공

2. **고급 검색 기능**
   - 형태소 분석 (Nori Analyzer)
   - 연관성 점수 기반 랭킹 (BM25)
   - Fuzzy 검색 (오타 허용)

3. **읽기/쓰기 역할 분리 (CQRS 패턴)**
   - MySQL: 쓰기 및 트랜잭션
   - Elasticsearch: 검색 및 읽기 최적화

---