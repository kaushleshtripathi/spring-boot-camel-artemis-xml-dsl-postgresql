# Spring Boot + Camel XML DSL + Artemis + PostgreSQL

Java 21 / Spring Boot / Apache Camel XML DSL / ActiveMQ Artemis / PostgreSQL.

## XML DSL elements included

The route file intentionally demonstrates the requested XML constructs:

- `<beans>`
- `<routeContext>`
- `<route>`
- `<unmarshal>`
- `<setProperty>`
- `<setBody>`
- `<marshal>`
- `<choice>`
- `<when>`
- `<otherwise>`
- `<doTry>`
- `<doCatch>`
- `<onException>`

## Architecture

## Application Flow

```mermaid
flowchart TD
    A[REST] --> B[Spring Boot]
    B --> C[Camel Producer]
    C --> D[Artemis orders.in]

    D --> E[5 Concurrent Camel Consumers]

    E --> F[Idempotency Check]

    F -->|Duplicate| G[Skip / End]
    F -->|New Message| H[(PostgreSQL)]

    H --> I["@Transactional"]

    I --> J[(orders)]
    I --> K[(processed_messages)]

    J --> L[COMMIT]
    K --> L

    L --> M[JMS Acknowledgement]
    M --> N[Artemis orders.processed]

    %% Failure / Retry Flow
    D --> O[Processing Failure]
    O --> P[Retry 1]
    P --> Q[Retry 2]
    Q --> R[Retry 3]
    R --> S[Artemis orders.dlq]

    P -->|Success| M
    Q -->|Success| M
    R -->|Success| M
```

## Retry and DLQ Flow

```mermaid
flowchart TD
    A[Artemis orders.in] --> B[Camel Consumer]
    B --> C[Processing]
    C -->|Failure| D[Retry 1]
    D -->|Failure| E[Retry 2]
    E -->|Failure| F[Retry 3]
    F -->|Failure| G[Artemis orders.dlq]

    D -->|Success| H[orders.processed]
    E -->|Success| H
    F -->|Success| H
    C -->|Success| H
```

## End-to-End Order Processing Flow

```mermaid
flowchart TD

    A[REST Client] --> B[Spring Boot]
    B --> C[Camel Producer]
    C --> D[Artemis orders.in]

    D --> E[5 Concurrent Camel Consumers]

    E --> F{Idempotency Check}

    F -->|Duplicate| G[Skip / End]
    F -->|New Message| H[(PostgreSQL)]

    H --> I["@Transactional"]

    I --> J[(orders)]
    I --> K[(processed_messages)]

    J --> L{Transaction Result}
    K --> L

    L -->|SUCCESS| M[JMS ACK]
    M --> N[Artemis orders.processed]

    L -->|FAILURE| O[Retry 1]
    O -->|FAILURE| P[Retry 2]
    P -->|FAILURE| Q[Retry 3]

    O -->|SUCCESS| M
    P -->|SUCCESS| M
    Q -->|SUCCESS| M

    Q -->|FAILURE| R[Artemis orders.dlq]

    style A fill:#e3f2fd
    style B fill:#e8eaf6
    style C fill:#ede7f6
    style D fill:#fff3e0
    style E fill:#fff8e1
    style F fill:#f3e5f5
    style H fill:#e8f5e9
    style I fill:#e8f5e9
    style N fill:#e8f5e9
    style R fill:#ffebee
    style G fill:#eeeeee
```


```text
REST
  ↓
Spring Boot
  ↓
Camel Producer
  ↓
Artemis orders.in
  ↓
5 Concurrent Camel Consumers
  ↓
Idempotency Check
  ├── Duplicate → Skip
  │
  └── New
       ↓
   PostgreSQL
       ↓
   @Transactional
       ↓
   orders + processed_messages
       ↓
    SUCCESS
       ↓
    JMS ACK
       ↓
orders.processed

    FAILURE
       ↓
    Retry 1
       ↓
    Retry 2
       ↓
    Retry 3
       ├── SUCCESS → orders.processed
       │
       └── FAILURE → orders.dlq
```

## Run

Prerequisites: JDK 21, Maven 3.9+, Docker.

```bash
docker compose up -d
mvn spring-boot:run
```

## Success test

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -H "Idempotency-Key: ORDER-KEY-1" \
  -d '{"orderNumber":"ORD-1001","customerName":"Alice","amount":2500.00}'
```

## Retry + DLQ test

`customerName=FAIL` intentionally throws an exception:

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -H "Idempotency-Key: FAIL-KEY-1" \
  -d '{"orderNumber":"ORD-FAIL-1","customerName":"FAIL","amount":99.99}'
```

Camel retries three times with a 2-second delay and then routes the failed exchange to `orders.dlq`.

## Important transaction note

PostgreSQL work uses Spring `@Transactional`. The JMS consumer is configured as transacted. This example uses at-least-once delivery plus idempotency instead of XA/JTA distributed transactions. That keeps the sample simpler and is a common microservice reliability approach.

```text
Route file: src/main/resources/camel/order-routes.xml
```

```XML
<beans>
    <spring:bean>
    <camelContext>
        <onException>
        <routeContext>
            <route>
                <from>
                <setProperty>
                <setBody>
                <unmarshal>
                <marshal>

                <choice>
                    <when>
                    <otherwise>
                </choice>

                <doTry>
                    <doCatch>
                </doTry>

            </route>
        </routeContext>
    </camelContext>
</beans>
```

