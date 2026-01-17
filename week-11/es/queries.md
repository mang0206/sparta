## Elasticsearch에서 검색 및 집계	


## 1. 사용자 활동 분석 (User Activity Analysis)

### 1-1. 가장 활동적인 사용자 Top 5 추출
사용자별로 로그 수를 집계하여 가장 활동량이 많은 사용자

**GET ecommerce-logs-user-activity/_search**
``` json
{
  "size": 0,
  "aggs": {
    "top_active_users": {
      "terms": {
        "field": "user_id",
        "size": 5
      }
    }
  }
}

- 실행 결과
{
    "took": 2,
    "timed_out": false,
    "_shards": {
        "total": 3,
        "successful": 3,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": {
            "value": 8,
            "relation": "eq"
        },
        "max_score": null,
        "hits": []
    },
    "aggregations": {
        "top_active_users": {
            "doc_count_error_upper_bound": 0,
            "sum_other_doc_count": 0,
            "buckets": [
                {
                    "key": "user_001",
                    "doc_count": 4
                },
                {
                    "key": "user_054",
                    "doc_count": 2
                },
                {
                    "key": "guest_999",
                    "doc_count": 1
                },
                {
                    "key": "user_102",
                    "doc_count": 1
                }
            ]
        }
    }
}
```
### 1-2. 시간대별 사용자 트래픽 변화 (Date Histogram)
1시간 단위로 사용자 활동(로그 발생) 추이를 시각화하기 위한 집계

**GET ecommerce-logs-user-activity/_search**
``` json
{
  "size": 0,
  "aggs": {
    "traffic_over_time": {
      "date_histogram": {
        "field": "timestamp",
        "calendar_interval": "1h"
      }
    }
  }
}

-실행 결과
{
    "took": 6,
    "timed_out": false,
    "_shards": {
        "total": 3,
        "successful": 3,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": {
            "value": 8,
            "relation": "eq"
        },
        "max_score": null,
        "hits": []
    },
    "aggregations": {
        "traffic_over_time": {
            "buckets": [
                {
                    "key_as_string": "2024-03-01T08:00:00.000Z",
                    "key": 1709280000000,
                    "doc_count": 1
                },

                ~~~

                {
                    "key_as_string": "2024-03-03T09:00:00.000Z",
                    "key": 1709456400000,
                    "doc_count": 3
                },
                {
                    "key_as_string": "2024-03-03T10:00:00.000Z",
                    "key": 1709460000000,
                    "doc_count": 1
                },
                {
                    "key_as_string": "2024-03-03T11:00:00.000Z",
                    "key": 1709463600000,
                    "doc_count": 2
                }
            ]
        }
    }
}
```

## 2. 주문 데이터 검색 (Advanced Order Search)

### 2-1. 특정 상품명이 포함된 주문 검색 (Nested Query)

**GET ecommerce-logs-orders/_search**
```json
{
  "query": {
    "nested": {
      "path": "items",
      "query": {
        "match": {
          "items.name": "Laptop"
        }
      }
    }
  }
}

-실행 결과
{
    "took": 46,
    "timed_out": false,
    "_shards": {
        "total": 3,
        "successful": 3,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": {
            "value": 1,
            "relation": "eq"
        },
        "max_score": 0.6931471,
        "hits": [
            {
                "_index": "ecommerce-logs-orders",
                "_id": "nvrVypsBvzf98b-heICN",
                "_score": 0.6931471,
                "_source": {
                    "order_id": "ord_998",
                    "user_id": "user_054",
                    "timestamp": "2024-03-03T12:00:00Z",
                    "status": "completed",
                    "total": 1250.00,
                    "items": [
                        {
                            "product_id": "prod_1",
                            "name": "Gaming Laptop",
                            "quantity": 1,
                            "price": 1200.00
                        },
                        {
                            "product_id": "prod_2",
                            "name": "Wireless Mouse",
                            "quantity": 1,
                            "price": 50.00
                        }
                    ]
                }
            }
        ]
    }
}
```

### 2-2. 고액 주문 필터링 (Range Query)
총 주문 금액(total)이 $100 이상인 주문만 필터링하여 조회

**GET ecommerce-logs-orders/_search**

```json
{
  "query": {
    "range": {
      "total": {
        "gte": 100
      }
    }
  }
}

-실행결과
{
    "took": 3,
    "timed_out": false,
    "_shards": {
        "total": 3,
        "successful": 3,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": {
            "value": 2,
            "relation": "eq"
        },
        "max_score": 1.0,
        "hits": [
            {
                "_index": "ecommerce-logs-orders",
                "_id": "n_rVypsBvzf98b-heICN",
                "_score": 1.0,
                "_source": {
                    "order_id": "ord_123",
                    "user_id": "user_001",
                    "timestamp": "2024-03-03T10:00:00Z",
                    "status": "completed",
                    "total": 150.50,
                    "items": [
                        {
                            "product_id": "prod_3",
                            "name": "Mechanical Keyboard",
                            "quantity": 1,
                            "price": 150.50
                        }
                    ]
                }
            },
            {
                "_index": "ecommerce-logs-orders",
                "_id": "nvrVypsBvzf98b-heICN",
                "_score": 1.0,
                "_source": {
                    "order_id": "ord_998",
                    "user_id": "user_054",
                    "timestamp": "2024-03-03T12:00:00Z",
                    "status": "completed",
                    "total": 1250.00,
                    "items": [
                        {
                            "product_id": "prod_1",
                            "name": "Gaming Laptop",
                            "quantity": 1,
                            "price": 1200.00
                        },
                        {
                            "product_id": "prod_2",
                            "name": "Wireless Mouse",
                            "quantity": 1,
                            "price": 50.00
                        }
                    ]
                }
            }
        ]
    }
}
```

## 3. 복합 조건 검색 (Boolean Query)

### 3-1. 특정 사용자의 '실패한' 주문 검색
user_id가 일치하면서 동시에 status가 "failed"인 로그를 찾는 복합 쿼리

**GET ecommerce-logs-orders/_search**
```json
{
  "query": {
    "bool": {
      "must": [
        { "term": { "user_id": "user_001" } },
        { "term": { "status": "failed" } }
      ]
    }
  }
}

-실행결과
{
    "took": 49,
    "timed_out": false,
    "_shards": {
        "total": 3,
        "successful": 3,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": {
            "value": 1,
            "relation": "eq"
        },
        "max_score": 0.8754687,
        "hits": [
            {
                "_index": "ecommerce-logs-orders",
                "_id": "oPrVypsBvzf98b-heICN",
                "_score": 0.8754687,
                "_source": {
                    "order_id": "ord_003",
                    "user_id": "user_001",
                    "timestamp": "2024-03-02T15:30:00Z",
                    "status": "failed",
                    "total": 29.98,
                    "items": [
                        {
                            "product_id": "prod_4",
                            "name": "USB-C Cable",
                            "quantity": 2,
                            "price": 14.99
                        }
                    ]
                }
            }
        ]
    }
}
```