## Elasticsearch 인덱스 설계

1. 인덱스 템플릿 설정

PUT _template/ecommerce-logs-template
{
  "index_patterns": ["ecommerce-logs-*"],
  "settings": {
    "number_of_shards": 3,
    "number_of_replicas": 1
  },
  "mappings": {
    "properties": {
      "timestamp": { "type": "date" },
      "service": { "type": "keyword" },
      "level": { "type": "keyword" }
    }
  }
}

2. User Activity 로그 인덱스 생성
PUT ecommerce-logs-user-activity
{
  "settings": {
    "number_of_shards": 3,
    "number_of_replicas": 1
  },
  "mappings": {
    "properties": {
      "user_id": { "type": "keyword" },
      "action": { "type": "keyword" },
      "page": { "type": "keyword" },
      "ip": { "type": "ip" },
      "user_agent": { "type": "text" },
      "timestamp": { "type": "date" }
    }
  }
}

3. Orders (주문) 로그 인덱스 생성
PUT ecommerce-logs-orders
{
  "settings": {
    "number_of_shards": 3,
    "number_of_replicas": 1
  },
  "mappings": {
    "properties": {
      "order_id": { "type": "keyword" },
      "user_id": { "type": "keyword" },
      "items": {
        "type": "nested", 
        "properties": {
          "product_id": { "type": "keyword" },
          "name": { "type": "text" },
          "quantity": { "type": "integer" },
          "price": { "type": "double" }
        }
      },
      "total": { "type": "double" },
      "status": { "type": "keyword" },
      "timestamp": { "type": "date" }
    }
  }
}

4. 테스트 로그 생성

```json
{ "index": {} }
{ "user_id": "user_001", "action": "login", "page": "/login", "ip": "192.168.0.1", "user_agent": "Mozilla/5.0 (Windows)", "timestamp": "2024-03-03T09:55:00Z" }
{ "index": {} }
{ "user_id": "user_001", "action": "view_product", "page": "/product/123", "ip": "192.168.0.1", "user_agent": "Mozilla/5.0 (Windows)", "timestamp": "2024-03-03T09:58:00Z" }
{ "index": {} }
{ "user_id": "user_001", "action": "add_to_cart", "page": "/product/123", "ip": "192.168.0.1", "user_agent": "Mozilla/5.0 (Windows)", "timestamp": "2024-03-03T09:59:00Z" }
{ "index": {} }
{ "user_id": "user_054", "action": "view_product", "page": "/product/998", "ip": "10.0.0.5", "user_agent": "Mozilla/5.0 (Macintosh)", "timestamp": "2024-03-03T11:50:00Z" }
{ "index": {} }
{ "user_id": "user_054", "action": "add_to_cart", "page": "/product/998", "ip": "10.0.0.5", "user_agent": "Mozilla/5.0 (Macintosh)", "timestamp": "2024-03-03T11:55:00Z" }
{ "index": {} }
{ "user_id": "user_102", "action": "view_home", "page": "/home", "ip": "172.16.0.10", "user_agent": "Mozilla/5.0 (iPhone)", "timestamp": "2024-03-01T08:50:00Z" }
{ "index": {} }
{ "user_id": "user_001", "action": "purchase", "page": "/checkout", "ip": "192.168.0.1", "user_agent": "Mozilla/5.0 (Windows)", "timestamp": "2024-03-03T10:05:00Z" }
{ "index": {} }
{ "user_id": "guest_999", "action": "view_product", "page": "/product/555", "ip": "203.0.113.5", "user_agent": "Mozilla/5.0 (Android)", "timestamp": "2024-03-03T14:00:00Z" }


{ "index": {} }
{ "order_id": "ord_998", "user_id": "user_054", "timestamp": "2024-03-03T12:00:00Z", "status": "completed", "total": 1250.00, "items": [ { "product_id": "prod_1", "name": "Gaming Laptop", "quantity": 1, "price": 1200.00 }, { "product_id": "prod_2", "name": "Wireless Mouse", "quantity": 1, "price": 50.00 } ] }
{ "index": {} }
{ "order_id": "ord_123", "user_id": "user_001", "timestamp": "2024-03-03T10:00:00Z", "status": "completed", "total": 150.50, "items": [ { "product_id": "prod_3", "name": "Mechanical Keyboard", "quantity": 1, "price": 150.50 } ] }
{ "index": {} }
{ "order_id": "ord_003", "user_id": "user_001", "timestamp": "2024-03-02T15:30:00Z", "status": "failed", "total": 29.98, "items": [ { "product_id": "prod_4", "name": "USB-C Cable", "quantity": 2, "price": 14.99 } ] }
{ "index": {} }
{ "order_id": "ord_004", "user_id": "user_102", "timestamp": "2024-03-01T09:00:00Z", "status": "completed", "total": 45.00, "items": [ { "product_id": "prod_2", "name": "Wireless Mouse", "quantity": 3, "price": 15.00 } ] }

```