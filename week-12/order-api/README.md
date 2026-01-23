# MSA Order API

MSA 강좌 중 예제 Order API 소스코드입니다.

# Getting Started

1. 로컬 컴퓨터에서 docker-compose 로 mysql 설치

```shell
docker-compose up --build
```

2. MySQL docker container 접속 후 샘플 데이터 insert

```shell
docker exec -it order-api-order-db-1 /bin/bash

# container
bash-5.1# mysql -u root -p
mysql> insert into orders (user_id, product_id, quantity) values (1, 1, 5);
```

3. Application 실행 및 API 접속 테스트

```shell
$ open http://localhost:8083/api/orders/1
```

# Swagger

```shell
open http://localhost:8083/swagger-ui/index.html
```

# Kafka 테스트 튜토리얼

### 1. 토픽 생성

```shell
chmod +x ./scripts/init-kafka.sh
./init-kafka.sh
```

### 2. 컨슈머 시작 (새 터미널에서)

```shell
chmod +x ./scripts/consumer.sh
./consumer.sh
```

### 3. API 테스트

```sh
curl -X POST http://localhost:8083/api/orders \
-H "Content-Type: application/json" \
-d '{
    "userId": "user123",
    "items": [
        {
            "productId": "PROD-001",
            "quantity": 2,
            "price": 1000
        }
    ]
}'
```
