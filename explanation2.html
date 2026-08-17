<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>Complete Project Explanation - Spring Boot Camel Artemis XML DSL</title>
<style>
body{margin:0;background:#f4f6f8;color:#222;font-family:Arial,Helvetica,sans-serif;line-height:1.6}
.container{max-width:1200px;margin:auto;padding:30px}
.card{background:#fff;padding:24px;margin:20px 0;border-radius:12px;box-shadow:0 2px 10px rgba(0,0,0,.08)}
h1,h2,h3{color:#17324d}
pre{background:#17202a;color:#f1f1f1;padding:16px;border-radius:8px;overflow:auto;font-size:13px}
code{font-family:Consolas,monospace}
.flow{background:#eef6ff;border-left:5px solid #3578b3;padding:16px;border-radius:7px}
.note{background:#fff4d6;border-left:5px solid #d89b00;padding:15px}
.ok{background:#e9f7ef;border-left:5px solid #2e8b57;padding:15px}
table{width:100%;border-collapse:collapse}
th,td{border:1px solid #ddd;padding:9px;vertical-align:top}
th{background:#e9eef3}
details{margin-top:15px}
.small{font-size:13px;color:#666}
</style>
</head>
<body>
<div class="container">
<h1>Complete Functionality Explanation</h1>
<p>This document explains the files contained in <code>spring-boot-camel-artemis-xml-dsl.zip</code>.
The explanation is grounded in the actual source/configuration files in the archive. The internal
<code>.git</code> object database is repository metadata rather than application functionality, so it is inventoried separately below.</p>

<div class="card">
<h2>1. Project at a Glance</h2>
<div class="flow">
REST <b>POST /api/orders</b> → Spring Boot <b>OrderController</b> → Artemis <b>orders.in</b> →
5 concurrent Camel consumers → XML Camel route → validation/idempotency/business service →
PostgreSQL <b>orders</b> + <b>processed_messages</b> → Artemis <b>orders.processed</b>.
</div>
<p>Failures from the service are eligible for Camel's global redelivery policy. The XML configures
three redeliveries with a 2-second delay and a DLQ endpoint named <code>orders.dlq</code>.</p>
</div>

<div class="card">
<h2>2. Complete Mermaid Architecture</h2>
<pre><code>flowchart TD
    A[REST Client] -->|POST /api/orders| B[OrderController]
    B -->|ProducerTemplate| C[Artemis orders.in]

    C --> D1[Camel Consumer 1]
    C --> D2[Camel Consumer 2]
    C --> D3[Camel Consumer 3]
    C --> D4[Camel Consumer 4]
    C --> D5[Camel Consumer 5]

    D1 --> E[order-consumer-route]
    D2 --> E
    D3 --> E
    D4 --> E
    D5 --> E

    E --> F[Read Idempotency-Key]
    F --> G[Unmarshal JSON]
    G --> H{orderNumber valid?}

    H -->|No| I[IllegalArgumentException]
    I --> J[doCatch / INVALID]

    H -->|Yes| K[OrderTransactionService.process]
    K --> L{Already processed?}

    L -->|Yes| M[DuplicateMessageException]
    M --> N[doCatch / DUPLICATE]

    L -->|No| O[@Transactional]
    O --> P[(PostgreSQL orders)]
    O --> Q[(processed_messages)]
    P --> R[Commit]
    Q --> R
    R --> S[orders.processed]

    K -->|Other Exception| T[onException]
    T --> U[Retry / Redelivery x3]
    U -->|Still fails| V[(orders.dlq)]</code></pre>
</div>

<div class="card">
<h2>3. Actual Application Data Flow</h2>
<ol>
<li><code>OrderController</code> receives <code>POST /api/orders</code>.</li>
<li>If the request has no <code>Idempotency-Key</code>, the controller creates a UUID.</li>
<li>The controller sends the <code>OrderRequest</code> directly to <code>jms:queue:orders.in</code> with the idempotency key as a JMS header.</li>
<li>The Camel XML consumer reads <code>orders.in</code> with <code>concurrentConsumers=5</code> and <code>transacted=true</code>.</li>
<li>The route copies the header into exchange property <code>messageId</code>.</li>
<li>The message is unmarshalled with Jackson.</li>
<li>The route validates <code>orderNumber</code>.</li>
<li>The service checks both the processed-message key and the business order number.</li>
<li>A new order is saved to <code>orders</code> and its message key is saved to <code>processed_messages</code> in one Spring transaction.</li>
<li>The Camel route marshals the resulting body and sends it to <code>orders.processed</code>.</li>
</ol>
</div>

<div class="card">
<h2>4. REST Request Sequence</h2>
<pre><code>sequenceDiagram
    participant Client
    participant Controller as OrderController
    participant Artemis as Artemis

    Client-&gt;&gt;Controller: POST /api/orders
    Controller-&gt;&gt;Controller: Resolve Idempotency-Key
    Controller-&gt;&gt;Artemis: sendBodyAndHeader(orders.in)
    Artemis--&gt;&gt;Controller: Message accepted by producer call
    Controller--&gt;&gt;Client: HTTP 202 Accepted</code></pre>
<p>The controller source actually sends directly to <code>jms:queue:orders.in</code>; the XML
<code>producer-route</code> starting at <code>direct:orderProducer</code> is also present in the project,
but this controller does not invoke that direct endpoint.</p>
</div>

<div class="card">
<h2>5. Important Flow Detail: Controller vs XML Producer Route</h2>
<div class="note">
The archive contains both a Camel <code>producer-route</code> with
<code>direct:orderProducer</code> and a controller that sends directly to
<code>jms:queue:orders.in</code>. Therefore the controller's current runtime path bypasses the
<code>direct:orderProducer</code> XML route. This is an observation from the actual source, not an assumption.
</div>
</div>

<div class="card">
<h2>6. Consumer and Retry Flow</h2>
<pre><code>flowchart TD
    A[orders.in] --> B[5 concurrent Camel consumers]
    B --> C[Unmarshal JSON]
    C --> D{orderNumber valid?}
    D -->|Invalid| E[IllegalArgumentException]
    D -->|Valid| F[OrderTransactionService]
    F -->|Duplicate| G[DUPLICATE]
    F -->|Success| H[orders.processed]
    F -->|Other exception| I[onException]
    I --> J[Redelivery 1]
    J --> K[Redelivery 2]
    K --> L[Redelivery 3]
    L -->|Still fails| M[orders.dlq]</code></pre>
</div>

<div class="card">
<h2>7. Idempotency and Transaction Flow</h2>
<pre><code>flowchart TD
    A[messageId / Idempotency-Key] --> B[processed_messages exists?]
    B -->|Yes| C[DuplicateMessageException]
    B -->|No| D[orders order_number exists?]
    D -->|Yes| E[DuplicateMessageException]
    D -->|No| F[customerName == FAIL?]
    F -->|Yes| G[SimulatedFailureException]
    F -->|No| H[Create OrderEntity]
    H --> I[Save orders]
    I --> J[Save ProcessedMessage]
    J --> K[COMMIT]</code></pre>
<p>The service method is annotated <code>@Transactional</code>. The source explicitly performs the
processed-message check, order-number check, simulated failure check, order save and processed-message save
inside that method.</p>
</div>

<div class="card">
<h2>8. File-by-File Explanation</h2>
<table>
<tr><th>File</th><th>Functionality</th></tr><tr><td><code>README.md</code></td><td>Project README describing the intended architecture, flows, setup and examples.</td></tr><tr><td><code>apache-camel-masterclass.pdf</code></td><td>PDF reference material included in the archive.</td></tr><tr><td><code>docker-compose.yml</code></td><td>Local infrastructure: PostgreSQL and ActiveMQ Artemis containers, ports, credentials, volumes and database schema mount.</td></tr><tr><td><code>explanation.html</code></td><td>Previously generated HTML explanation included in the archive.</td></tr><tr><td><code>pom.xml</code></td><td>Maven build descriptor: Java/Spring Boot/Camel versions, dependency management and runtime dependencies.</td></tr><tr><td><code>src/main/java/com/example/orderapp/OrderApplication.java</code></td><td>Spring Boot application entry point.</td></tr><tr><td><code>src/main/java/com/example/orderapp/api/OrderController.java</code></td><td>REST endpoint that accepts an order and sends it to Artemis with an Idempotency-Key.</td></tr><tr><td><code>src/main/java/com/example/orderapp/entity/OrderEntity.java</code></td><td>JPA entity mapped to the orders table.</td></tr><tr><td><code>src/main/java/com/example/orderapp/entity/ProcessedMessage.java</code></td><td>JPA entity mapped to processed_messages; the message key is the identifier.</td></tr><tr><td><code>src/main/java/com/example/orderapp/model/OrderRequest.java</code></td><td>Input DTO containing orderNumber, customerName and amount.</td></tr><tr><td><code>src/main/java/com/example/orderapp/repository/OrderRepository.java</code></td><td>Spring Data JPA repository for OrderEntity, including order-number existence checking.</td></tr><tr><td><code>src/main/java/com/example/orderapp/repository/ProcessedMessageRepository.java</code></td><td>Spring Data JPA repository for ProcessedMessage.</td></tr><tr><td><code>src/main/java/com/example/orderapp/service/OrderTransactionService.java</code></td><td>Transactional business service implementing idempotency checks, duplicate checks, simulated failure, order persistence and processed-message persistence.</td></tr><tr><td><code>src/main/resources/application.yml</code></td><td>Spring Boot runtime configuration for application name, PostgreSQL, Artemis, server port and Camel.</td></tr><tr><td><code>src/main/resources/camel/order-routes.xml</code></td><td>Apache Camel Spring XML DSL orchestration: producer route, five concurrent JMS consumers, JSON conversion, validation, service invocation, local catches, global retry and DLQ.</td></tr><tr><td><code>src/main/resources/schema.sql</code></td><td>PostgreSQL schema for the orders and processed_messages tables.</td></tr></table></div><div class="card"><h2>README.md</h2><p>Project README describing the intended architecture, flows, setup and examples.</p><p>The README is documentation for the project and contains Mermaid architecture/data-flow diagrams and usage explanations.</p><details><summary>View source/content</summary><pre><code># Spring Boot + Apache Camel XML DSL + ActiveMQ Artemis + PostgreSQL

A runnable reference application demonstrating an enterprise-style order-processing flow using:

- Spring Boot
- Java 21
- Apache Camel
- Camel XML DSL
- ActiveMQ Artemis
- PostgreSQL
- REST API
- Camel Producer
- Multiple concurrent consumers
- Idempotency
- PostgreSQL transactions
- Camel exception handling
- Retry / redelivery
- Dead Letter Queue (DLQ)
- `orders.in`
- `orders.processed`
- `orders.dlq`

---

# 1. Architecture &amp; Data Flow

The complete application flow is:

```mermaid
flowchart TD

    A[REST Client] --&gt;|POST /api/orders| B[OrderController]

    B --&gt;|ProducerTemplate| C[direct:orderProducer]

    C --&gt; D[Producer Route&lt;br/&gt;order-producer-route]

    D --&gt;|setProperty&lt;br/&gt;Idempotency-Key| E[Marshal JSON]

    E --&gt;|JMS| F[(Artemis&lt;br/&gt;orders.in)]

    F --&gt; G[5 Concurrent Camel Consumers]

    G --&gt; H[Consumer Route&lt;br/&gt;order-consumer-route]

    H --&gt;|setProperty| I[Idempotency Key]

    I --&gt; J[Unmarshal JSON]

    J --&gt; K{choice}

    K --&gt;|Invalid orderNumber| L[Validation Error]

    K --&gt;|Valid Order| M[doTry]

    M --&gt; N[OrderService.process]

    N --&gt; O{Idempotency Check}

    O --&gt;|Duplicate| P[Skip / DUPLICATE]
    O --&gt;|New Message| Q[PostgreSQL Transaction]

    Q --&gt; R[(orders)]
    Q --&gt; S[(processed_messages)]

    R --&gt; T[COMMIT]
    S --&gt; T

    T --&gt; U[JMS ACK]
    U --&gt; V[(Artemis&lt;br/&gt;orders.processed)]

    M --&gt;|Exception| W[onException]
    W --&gt; X{Retry}

    X --&gt;|Retry 1| M
    X --&gt;|Retry 2| M
    X --&gt;|Retry 3| M
    X --&gt;|Retries Exhausted| Y[(Artemis&lt;br/&gt;orders.dlq)]
```

---

# 2. End-to-End Data Flow

```mermaid
flowchart TD

    A[REST Client] --&gt;|POST /api/orders| B[OrderController]

    B --&gt;|ProducerTemplate| C[direct:orderProducer]

    C --&gt; D[setProperty&lt;br/&gt;Idempotency-Key]

    D --&gt; E[setProperty&lt;br/&gt;receivedAt]

    E --&gt; F[marshal JSON]

    F --&gt; G[(Artemis&lt;br/&gt;orders.in)]

    G --&gt; H1[Camel Consumer 1]
    G --&gt; H2[Camel Consumer 2]
    G --&gt; H3[Camel Consumer 3]
    G --&gt; H4[Camel Consumer 4]
    G --&gt; H5[Camel Consumer 5]

    H1 --&gt; I[Consumer Route]
    H2 --&gt; I
    H3 --&gt; I
    H4 --&gt; I
    H5 --&gt; I

    I --&gt; J[setProperty&lt;br/&gt;idempotencyKey]

    J --&gt; K[unmarshal JSON]

    K --&gt; L{choice}

    L --&gt;|Invalid| M[IllegalArgumentException]
    M --&gt; N[doCatch]
    N --&gt; O[INVALID]

    L --&gt;|Valid| P[otherwise]

    P --&gt; Q[doTry]

    Q --&gt; R[OrderService.process]

    R --&gt; S{Idempotency Check}

    S --&gt;|Duplicate Key| T[DuplicateMessageException]
    T --&gt; U[doCatch]
    U --&gt; V[DUPLICATE / Skip]

    S --&gt;|New Message| W[&quot;@Transactional&quot;]

    W --&gt; X[(PostgreSQL orders)]
    W --&gt; Y[(processed_messages)]

    X --&gt; Z{Transaction}
    Y --&gt; Z

    Z --&gt;|SUCCESS| AA[JMS ACK]
    AA --&gt; AB[(Artemis&lt;br/&gt;orders.processed)]

    Q --&gt;|Exception| AC[onException]

    AC --&gt; AD[Retry 1]
    AD --&gt;|Failure| AE[Retry 2]
    AE --&gt;|Failure| AF[Retry 3]

    AD --&gt;|Success| AA
    AE --&gt;|Success| AA
    AF --&gt;|Success| AA

    AF --&gt;|Failure| AG[(Artemis&lt;br/&gt;orders.dlq)]
```

---

# 3. REST → Camel Producer → Artemis

```mermaid
sequenceDiagram

    participant Client as REST Client
    participant Controller as OrderController
    participant Camel as Camel Producer Route
    participant Artemis as Artemis orders.in

    Client-&gt;&gt;Controller: POST /api/orders
    Controller-&gt;&gt;Controller: Read Idempotency-Key

    Controller-&gt;&gt;Camel: ProducerTemplate.sendBodyAndHeader()

    Camel-&gt;&gt;Camel: setProperty(receivedAt)
    Camel-&gt;&gt;Camel: setProperty(idempotencyKey)
    Camel-&gt;&gt;Camel: setBody()
    Camel-&gt;&gt;Camel: marshal JSON

    Camel-&gt;&gt;Artemis: Send JMS message
    Artemis--&gt;&gt;Camel: Message accepted

    Controller--&gt;&gt;Client: HTTP 202 Accepted
```

## What happens?

The REST client sends:

```http
POST /api/orders
Content-Type: application/json
Idempotency-Key: ORDER-1001-KEY
```

with:

```json
{
  &quot;orderNumber&quot;: &quot;ORD-1001&quot;,
  &quot;customerName&quot;: &quot;Alice&quot;,
  &quot;amount&quot;: 2500.00
}
```

The request reaches:

```text
OrderController
```

The controller uses Camel `ProducerTemplate`:

```java
producerTemplate.sendBodyAndHeader(
    &quot;direct:orderProducer&quot;,
    request,
    &quot;Idempotency-Key&quot;,
    key
);
```

The XML producer route then:

1. Reads the idempotency key.
2. Stores properties on the Camel exchange.
3. Converts the Java object to JSON.
4. Sends the JSON message to Artemis `orders.in`.

---

# 4. Camel Producer XML Route

The producer route is defined in:

```text
src/main/resources/camel/order-routes.xml
```

Conceptually:

```xml
&lt;route id=&quot;order-producer-route&quot;&gt;

    &lt;from uri=&quot;direct:orderProducer&quot;/&gt;

    &lt;setProperty name=&quot;receivedAt&quot;&gt;
        ...
    &lt;/setProperty&gt;

    &lt;setProperty name=&quot;idempotencyKey&quot;&gt;
        ...
    &lt;/setProperty&gt;

    &lt;setBody&gt;
        ...
    &lt;/setBody&gt;

    &lt;marshal&gt;
        &lt;json/&gt;
    &lt;/marshal&gt;

    &lt;to uri=&quot;jms:queue:orders.in&quot;/&gt;

&lt;/route&gt;
```

There is no Java `RouteBuilder`.

The routing logic is kept in XML.

---

# 5. Artemis → 5 Concurrent Consumers

```mermaid
flowchart LR

    A[(Artemis&lt;br/&gt;orders.in)]

    A --&gt; C1[Camel Consumer 1]
    A --&gt; C2[Camel Consumer 2]
    A --&gt; C3[Camel Consumer 3]
    A --&gt; C4[Camel Consumer 4]
    A --&gt; C5[Camel Consumer 5]

    C1 --&gt; P[Order Processing]
    C2 --&gt; P
    C3 --&gt; P
    C4 --&gt; P
    C5 --&gt; P
```

The consumer route uses:

```text
concurrentConsumers=5
```

Therefore Camel can process up to five messages concurrently.

The XML configuration is conceptually:

```xml
&lt;from uri=&quot;jms:queue:orders.in?concurrentConsumers=5&amp;amp;transacted=true&quot;/&gt;
```

This gives the application:

- Parallel message consumption
- Higher throughput
- Independent processing of messages
- Transaction-aware JMS consumption

---

# 6. Consumer Processing Flow

```mermaid
flowchart TD

    A[orders.in] --&gt; B[Consume JMS Message]

    B --&gt; C[setProperty&lt;br/&gt;idempotencyKey]

    C --&gt; D[unmarshal JSON]

    D --&gt; E{choice}

    E --&gt;|orderNumber missing| F[IllegalArgumentException]

    F --&gt; G[doCatch]

    G --&gt; H[INVALID]

    E --&gt;|Valid Order| I[otherwise]

    I --&gt; J[doTry]

    J --&gt; K[OrderService.process]

    K --&gt; L{Idempotency Check}

    L --&gt;|Already Processed| M[DuplicateMessageException]

    M --&gt; N[doCatch]

    N --&gt; O[DUPLICATE / Skip]

    L --&gt;|New Message| P[PostgreSQL Transaction]

    P --&gt; Q[(orders)]
    P --&gt; R[(processed_messages)]

    Q --&gt; S[COMMIT]
    R --&gt; S

    S --&gt; T[orders.processed]
```

The consumer route performs:

```text
JMS message
    ↓
setProperty
    ↓
unmarshal JSON
    ↓
choice
    ↓
validation
    ↓
doTry
    ↓
OrderService
    ↓
idempotency
    ↓
PostgreSQL
    ↓
orders.processed
```

---

# 7. XML DSL Components

The application demonstrates the requested Camel XML DSL elements.

## `&lt;beans&gt;`

The XML file starts with the Spring/Camel XML configuration:

```xml
&lt;beans&gt;
```

This is the root container for the XML configuration.

---

## `&lt;bean&gt;`

A Spring bean can be declared directly in XML:

```xml
&lt;bean id=&quot;routeInfo&quot; class=&quot;java.util.HashMap&quot;&gt;
    ...
&lt;/bean&gt;
```

The application services are also available as Spring beans through component scanning.

For example:

```text
OrderService
```

can be referenced by Camel using:

```text
ref=&quot;orderService&quot;
```

---

## `&lt;routeContext&gt;`

The routes are grouped inside:

```xml
&lt;routeContext id=&quot;orderRouteContext&quot;&gt;
```

This keeps the XML routing configuration organized.

---

## `&lt;route&gt;`

Each Camel flow is represented by:

```xml
&lt;route id=&quot;...&quot;&gt;
```

The project contains:

```text
order-producer-route
order-consumer-route
```

---

# 8. `&lt;unmarshal&gt;`

The Artemis message contains JSON.

The consumer converts JSON back to a Java object:

```xml
&lt;unmarshal&gt;
    &lt;json library=&quot;Jackson&quot;/&gt;
&lt;/unmarshal&gt;
```

Data flow:

```text
JSON
 ↓
Jackson
 ↓
OrderRequest
```

---

# 9. `&lt;setProperty&gt;`

Camel exchange properties are used for values that need to travel through the route.

For example:

```xml
&lt;setProperty name=&quot;idempotencyKey&quot;&gt;
    &lt;simple&gt;${header.Idempotency-Key}&lt;/simple&gt;
&lt;/setProperty&gt;
```

The idempotency key is therefore available later in the route:

```text
exchangeProperty.idempotencyKey
```

---

# 10. `&lt;setBody&gt;`

The XML DSL uses `&lt;setBody&gt;` to control the Camel message body.

For example:

```xml
&lt;setBody&gt;
    &lt;simple&gt;${body}&lt;/simple&gt;
&lt;/setBody&gt;
```

It is also used to construct the output message:

```json
{
  &quot;status&quot;: &quot;PROCESSED&quot;,
  &quot;idempotencyKey&quot;: &quot;ORDER-1001-KEY&quot;
}
```

---

# 11. `&lt;marshal&gt;`

The Java object is converted to JSON before being sent to Artemis:

```xml
&lt;marshal&gt;
    &lt;json library=&quot;Jackson&quot;/&gt;
&lt;/marshal&gt;
```

Flow:

```text
OrderRequest
     ↓
Jackson
     ↓
JSON
     ↓
Artemis
```

---

# 12. `&lt;choice&gt;`, `&lt;when&gt;`, `&lt;otherwise&gt;`

The consumer route performs conditional processing:

```mermaid
flowchart TD

    A[Order Message] --&gt; B{choice}

    B --&gt;|Invalid orderNumber| C[when]
    C --&gt; D[Validation Error]

    B --&gt;|Valid order| E[otherwise]
    E --&gt; F[Process Order]
```

The `&lt;when&gt;` branch handles invalid input.

The `&lt;otherwise&gt;` branch performs normal processing.

---

# 13. `&lt;doTry&gt;` and `&lt;doCatch&gt;`

The main processing is protected by:

```xml
&lt;doTry&gt;
    ...
    &lt;doCatch&gt;
        ...
    &lt;/doCatch&gt;
&lt;/doTry&gt;
```

The application uses catches for:

### Duplicate messages

```text
DuplicateMessageException
```

The duplicate is logged and skipped.

### Validation errors

```text
IllegalArgumentException
```

The message is treated as invalid rather than being repeatedly retried.

---

# 14. Idempotency

Idempotency prevents the same message from creating duplicate database records.

```mermaid
flowchart TD

    A[JMS Message] --&gt; B[Read Idempotency-Key]

    B --&gt; C[(processed_messages)]

    C --&gt; D{Key Exists?}

    D --&gt;|YES| E[Duplicate Message]
    E --&gt; F[Skip Processing]

    D --&gt;|NO| G[Continue Processing]

    G --&gt; H[(orders)]

    H --&gt; I[(processed_messages)]

    I --&gt; J[COMMIT]

    J --&gt; K[Message Successfully Processed]
```

The application uses two levels of duplicate protection.

## Technical idempotency

The table:

```text
processed_messages
```

contains:

```text
message_key
processed_at
```

`message_key` is the primary key.

Therefore:

```text
ORDER-1001-KEY
```

cannot be inserted twice.

## Business idempotency

The `orders` table has:

```sql
UNIQUE(order_number)
```

This prevents the same business order from being inserted twice.

---

# 15. PostgreSQL Transaction

The database operation is performed inside Spring:

```java
@Transactional
public void process(...)
```

The transaction covers:

```text
Check processed_messages
        ↓
Check orders
        ↓
Insert orders
        ↓
Insert processed_messages
        ↓
COMMIT
```

Diagram:

```mermaid
flowchart TD

    A[OrderService.process] --&gt; B[&quot;@Transactional&quot;]

    B --&gt; C[Check processed_messages]

    C --&gt; D{Already Exists?}

    D --&gt;|Yes| E[Duplicate]

    D --&gt;|No| F[Check orders]

    F --&gt; G{Order Exists?}

    G --&gt;|Yes| H[Duplicate]

    G --&gt;|No| I[Insert orders]

    I --&gt; J[Insert processed_messages]

    J --&gt; K{Transaction}

    K --&gt;|Success| L[COMMIT]
    K --&gt;|Exception| M[ROLLBACK]

    L --&gt; N[Continue Camel Route]
    M --&gt; O[Exception Handling / Retry]
```

If the transaction fails:

```text
ROLLBACK
```

is performed.

This prevents partial database updates.

---

# 16. Success Flow

```mermaid
sequenceDiagram

    participant REST as REST Client
    participant API as Spring Boot
    participant Camel as Camel
    participant JMS as Artemis
    participant DB as PostgreSQL
    participant OUT as orders.processed

    REST-&gt;&gt;API: POST Order
    API-&gt;&gt;Camel: ProducerTemplate
    Camel-&gt;&gt;JMS: orders.in

    JMS-&gt;&gt;Camel: Consume
    Camel-&gt;&gt;Camel: Unmarshal JSON
    Camel-&gt;&gt;Camel: Idempotency Check

    Camel-&gt;&gt;DB: BEGIN Transaction
    Camel-&gt;&gt;DB: INSERT orders
    Camel-&gt;&gt;DB: INSERT processed_messages
    DB--&gt;&gt;Camel: COMMIT

    Camel-&gt;&gt;OUT: Publish processed event
    Camel--&gt;&gt;JMS: ACK
```

---

# 17. Exception Handling, Retry and DLQ

```mermaid
flowchart TD

    A[orders.in] --&gt; B[Camel Consumer]

    B --&gt; C[OrderService]

    C --&gt;|Success| D[(orders.processed)]

    C --&gt;|Exception| E[onException]

    E --&gt; F[Retry 1]

    F --&gt;|Failure| G[Retry 2]

    G --&gt;|Failure| H[Retry 3]

    F --&gt;|Success| D
    G --&gt;|Success| D
    H --&gt;|Success| D

    H --&gt;|Failure| I[(orders.dlq)]
```

The global Camel exception handler is responsible for retries:

```xml
&lt;onException&gt;
    ...
    &lt;redeliveryPolicy
        maximumRedeliveries=&quot;3&quot;
        redeliveryDelay=&quot;2000&quot;/&gt;
    ...
    &lt;to uri=&quot;jms:queue:orders.dlq&quot;/&gt;
&lt;/onException&gt;
```

The configured behavior is:

```text
Initial attempt
      ↓
Failure
      ↓
Retry 1
      ↓
Failure
      ↓
Retry 2
      ↓
Failure
      ↓
Retry 3
      ↓
Failure
      ↓
orders.dlq
```

The retry delay in this example is:

```text
2000 ms = 2 seconds
```

---

# 18. Retry Test

The application contains a deliberate failure condition for testing.

If:

```json
{
  &quot;customerName&quot;: &quot;FAIL&quot;
}
```

is sent, the service throws:

```text
RetryableOrderException
```

Example:

```bash
curl -X POST http://localhost:8080/api/orders \
  -H &quot;Content-Type: application/json&quot; \
  -H &quot;Idempotency-Key: FAIL-KEY-1&quot; \
  -d &quot;{\&quot;orderNumber\&quot;:\&quot;ORD-FAIL-1\&quot;,\&quot;customerName\&quot;:\&quot;FAIL\&quot;,\&quot;amount\&quot;:99.99}&quot;
```

The expected flow is:

```text
orders.in
   |
   v
Processing
   |
   X
   |
Retry 1
   |
   X
   |
Retry 2
   |
   X
   |
Retry 3
   |
   X
   |
orders.dlq
```

---

# 19. Complete Application Flow

```mermaid
flowchart TD

    %% REST / PRODUCER

    A[REST Client] --&gt;|POST /api/orders| B[OrderController]

    B --&gt;|ProducerTemplate| C[direct:orderProducer]

    C --&gt; D[setProperty&lt;br/&gt;Idempotency-Key]

    D --&gt; E[setProperty&lt;br/&gt;receivedAt]

    E --&gt; F[marshal JSON]

    F --&gt; G[(Artemis&lt;br/&gt;orders.in)]

    %% CONSUMERS

    G --&gt; H1[Camel Consumer 1]
    G --&gt; H2[Camel Consumer 2]
    G --&gt; H3[Camel Consumer 3]
    G --&gt; H4[Camel Consumer 4]
    G --&gt; H5[Camel Consumer 5]

    H1 --&gt; I[Consumer Route]
    H2 --&gt; I
    H3 --&gt; I
    H4 --&gt; I
    H5 --&gt; I

    %% TRANSFORMATION

    I --&gt; J[setProperty&lt;br/&gt;idempotencyKey]
    J --&gt; K[unmarshal JSON]
    K --&gt; L{choice}

    %% VALIDATION

    L --&gt;|Invalid| M[IllegalArgumentException]
    M --&gt; N[doCatch]
    N --&gt; O[INVALID]

    %% PROCESSING

    L --&gt;|Valid| P[otherwise]
    P --&gt; Q[doTry]
    Q --&gt; R[OrderService.process]

    %% IDEMPOTENCY

    R --&gt; S{Idempotency Check}

    S --&gt;|Duplicate| T[DuplicateMessageException]
    T --&gt; U[doCatch]
    U --&gt; V[DUPLICATE / Skip]

    S --&gt;|New Message| W[&quot;@Transactional&quot;]

    %% DATABASE

    W --&gt; X[(PostgreSQL orders)]
    W --&gt; Y[(processed_messages)]

    X --&gt; Z{Transaction}
    Y --&gt; Z

    Z --&gt;|SUCCESS| AA[JMS ACK]
    AA --&gt; AB[(Artemis&lt;br/&gt;orders.processed)]

    %% FAILURE

    Q --&gt;|Exception| AC[onException]

    AC --&gt; AD[Retry 1]
    AD --&gt;|Failure| AE[Retry 2]
    AE --&gt;|Failure| AF[Retry 3]

    AD --&gt;|Success| AA
    AE --&gt;|Success| AA
    AF --&gt;|Success| AA

    AF --&gt;|Failure| AG[(Artemis&lt;br/&gt;orders.dlq)]
```

---

# 20. Code-to-Flow Mapping

| Flow | Source |
|---|---|
| REST API | `OrderController.java` |
| Camel Producer | `direct:orderProducer` |
| Producer route | `order-routes.xml` |
| Artemis input | `orders.in` |
| Multiple consumers | `concurrentConsumers=5` |
| JSON conversion | `&lt;marshal&gt;` / `&lt;unmarshal&gt;` |
| Exchange properties | `&lt;setProperty&gt;` |
| Message body | `&lt;setBody&gt;` |
| Conditional routing | `&lt;choice&gt;` / `&lt;when&gt;` / `&lt;otherwise&gt;` |
| Local exception handling | `&lt;doTry&gt;` / `&lt;doCatch&gt;` |
| Global exception handling | `&lt;onException&gt;` |
| Retry | `&lt;redeliveryPolicy&gt;` |
| Database transaction | `OrderService.process()` + `@Transactional` |
| Idempotency | `processed_messages` |
| Business duplicate protection | `orders.order_number UNIQUE` |
| Successful output | `orders.processed` |
| Failed messages | `orders.dlq` |

---

# 21. Project Structure

```text
spring-camel-artemis-postgres-xml-dsl/
│
├── pom.xml
├── docker-compose.yml
├── README.md
│
└── src/
    └── main/
        ├── java/
        │   └── com/example/orders/
        │       ├── OrderApplication.java
        │       │
        │       ├── api/
        │       │   └── OrderController.java
        │       │
        │       ├── model/
        │       │   └── OrderRequest.java
        │       │
        │       ├── entity/
        │       │   ├── OrderEntity.java
        │       │   └── ProcessedMessage.java
        │       │
        │       ├── repository/
        │       │   ├── OrderRepository.java
        │       │   └── ProcessedMessageRepository.java
        │       │
        │       └── service/
        │           └── OrderService.java
        │
        └── resources/
            ├── application.yml
            ├── schema.sql
            │
            └── camel/
                └── order-routes.xml
```

---

# 22. Main XML Route

The complete Camel routing configuration is located at:

```text
src/main/resources/camel/order-routes.xml
```

The route contains the requested XML DSL structure:

```xml
&lt;beans&gt;

    &lt;bean/&gt;

    &lt;camelContext&gt;

        &lt;onException&gt;
            ...
        &lt;/onException&gt;

        &lt;routeContext&gt;

            &lt;route&gt;

                &lt;from/&gt;

                &lt;setProperty/&gt;

                &lt;setBody/&gt;

                &lt;marshal/&gt;

                &lt;unmarshal/&gt;

                &lt;choice&gt;

                    &lt;when&gt;
                        ...
                    &lt;/when&gt;

                    &lt;otherwise&gt;
                        ...
                    &lt;/otherwise&gt;

                &lt;/choice&gt;

                &lt;doTry&gt;

                    ...

                    &lt;doCatch&gt;
                        ...
                    &lt;/doCatch&gt;

                &lt;/doTry&gt;

            &lt;/route&gt;

        &lt;/routeContext&gt;

    &lt;/camelContext&gt;

&lt;/beans&gt;
```

The project uses **unprefixed Camel XML elements** as requested.

---

# 23. Running the Application

## Prerequisites

Install:

- JDK 21
- Maven 3.9+
- Docker Desktop

Check Java:

```bash
java -version
```

Check Maven:

```bash
mvn -version
```

---

## Start PostgreSQL and Artemis

From the project root:

```bash
docker compose up -d
```

Check containers:

```bash
docker ps
```

You should have:

```text
orders-postgres
orders-artemis
```

---

## Start Spring Boot

```bash
mvn spring-boot:run
```

The application starts on:

```text
http://localhost:8080
```

---

# 24. Test Successful Order

```bash
curl -X POST http://localhost:8080/api/orders \
  -H &quot;Content-Type: application/json&quot; \
  -H &quot;Idempotency-Key: ORDER-1001-KEY&quot; \
  -d &quot;{\&quot;orderNumber\&quot;:\&quot;ORD-1001\&quot;,\&quot;customerName\&quot;:\&quot;Alice\&quot;,\&quot;amount\&quot;:2500.00}&quot;
```

Expected flow:

```text
REST
 ↓
Spring Boot
 ↓
Camel Producer
 ↓
orders.in
 ↓
Camel Consumer
 ↓
Idempotency
 ↓
PostgreSQL
 ↓
COMMIT
 ↓
orders.processed
```

---

# 25. Test Idempotency

Send the same request again:

```bash
curl -X POST http://localhost:8080/api/orders \
  -H &quot;Content-Type: application/json&quot; \
  -H &quot;Idempotency-Key: ORDER-1001-KEY&quot; \
  -d &quot;{\&quot;orderNumber\&quot;:\&quot;ORD-1001\&quot;,\&quot;customerName\&quot;:\&quot;Alice\&quot;,\&quot;amount\&quot;:2500.00}&quot;
```

The application checks:

```text
processed_messages
```

and detects:

```text
ORDER-1001-KEY
```

as already processed.

The duplicate is not inserted into the database.

---

# 26. Test Retry + DLQ

Send:

```bash
curl -X POST http://localhost:8080/api/orders \
  -H &quot;Content-Type: application/json&quot; \
  -H &quot;Idempotency-Key: FAIL-KEY-1&quot; \
  -d &quot;{\&quot;orderNumber\&quot;:\&quot;ORD-FAIL-1\&quot;,\&quot;customerName\&quot;:\&quot;FAIL\&quot;,\&quot;amount\&quot;:99.99}&quot;
```

Because:

```text
customerName = FAIL
```

the service intentionally throws an exception.

The message goes through the configured retry policy.

After the configured retries are exhausted:

```text
orders.dlq
```

receives the failed message.

---

# 27. Artemis Queues

The application uses these queues:

| Queue | Purpose |
|---|---|
| `orders.in` | Incoming orders |
| `orders.processed` | Successfully processed orders/events |
| `orders.dlq` | Messages that failed after retries |

---

# 28. PostgreSQL Tables

## orders

```text
id
order_number
customer_name
amount
status
created_at
```

The important constraint is:

```sql
UNIQUE(order_number)
```

## processed_messages

```text
message_key
processed_at
```

The important constraint is:

```text
PRIMARY KEY(message_key)
```

---

# 29. Important Transaction Behavior

This sample uses:

```text
JMS transacted consumer
+
Spring @Transactional PostgreSQL processing
+
Idempotency
```

The application intentionally does **not** use XA/JTA distributed transactions.

Instead, it relies on:

```text
At-least-once JMS delivery
+
Database transaction
+
Idempotency
+
Unique database constraints
```

This is a common practical approach for microservices because it avoids the operational complexity of distributed XA transactions.

For stronger database/event atomicity, consider the **Outbox Pattern**.

---

# 30. Why Idempotency Is Important

JMS messaging can result in a message being delivered again.

For example:

```text
Message received
       ↓
Database transaction
       ↓
Application crashes before ACK
       ↓
Artemis redelivers message
```

Without idempotency:

```text
orders
ORD-1001
ORD-1001
ORD-1001
```

could potentially be created.

With idempotency:

```text
Message
   ↓
processed_messages
   ↓
Already exists?
   ↓
YES
   ↓
Skip
```

Therefore duplicate delivery does not create duplicate business data.

---

# 31. Production Considerations

For a production implementation, consider adding:

- Correlation ID
- Structured logging
- Distributed tracing
- Micrometer metrics
- Prometheus
- Grafana
- Artemis monitoring
- PostgreSQL connection pool tuning
- HikariCP tuning
- Retry backoff
- Exponential retry policy
- DLQ monitoring
- DLQ replay mechanism
- Outbox Pattern
- Schema migrations using Flyway or Liquibase
- Authentication and authorization
- API validation
- OpenAPI documentation
- Graceful shutdown
- Kubernetes readiness/liveness probes

---

# 32. Summary

The complete flow is:

```text
REST
 ↓
Spring Boot Controller
 ↓
Camel ProducerTemplate
 ↓
Camel XML Producer Route
 ↓
Artemis orders.in
 ↓
5 Concurrent Camel Consumers
 ↓
Camel XML Consumer Route
 ↓
Unmarshal JSON
 ↓
Idempotency Check
 ↓
choice / validation
 ↓
doTry
 ↓
@Transactional OrderService
 ↓
PostgreSQL
 ├── orders
 └── processed_messages
 ↓
COMMIT
 ↓
JMS ACK
 ↓
Artemis orders.processed
```

Failure flow:

```text
orders.in
 ↓
Camel Processing
 ↓
Exception
 ↓
onException
 ↓
Retry 1
 ↓
Retry 2
 ↓
Retry 3
 ↓
Still Failed
 ↓
orders.dlq
```

This provides a complete **REST → Camel → Artemis → concurrent consumers → idempotency → PostgreSQL transaction → processed event / retry → DLQ** reference architecture.
</code></pre></details></div><div class="card"><h2>apache-camel-masterclass.pdf</h2><p>PDF reference material included in the archive.</p><p>This file is binary/non-text; its presence is documented, but its binary content is not reproduced.</p></div><div class="card"><h2>docker-compose.yml</h2><p>Local infrastructure: PostgreSQL and ActiveMQ Artemis containers, ports, credentials, volumes and database schema mount.</p><h3>Infrastructure</h3>
<ul>
<li>PostgreSQL 17 is exposed on port <code>5432</code>.</li>
<li>PostgreSQL database/user/password are <code>ordersdb / orders / orders</code>.</li>
<li>Artemis 2.40.0 is exposed on <code>61616</code> for messaging and <code>8161</code> for its web console.</li>
<li>Artemis credentials are <code>admin / admin</code>.</li>
<li><code>schema.sql</code> is mounted into PostgreSQL initialization.</li>
<li>Named volumes persist PostgreSQL and Artemis data.</li>
</ul><details><summary>View source/content</summary><pre><code>services:
  postgres:
    image: postgres:17
    environment:
      POSTGRES_DB: ordersdb
      POSTGRES_USER: orders
      POSTGRES_PASSWORD: orders
    ports: [&quot;5432:5432&quot;]
    volumes:
      - pgdata:/var/lib/postgresql/data
      - ./src/main/resources/schema.sql:/docker-entrypoint-initdb.d/01-schema.sql:ro
  artemis:
    image: apache/activemq-artemis:2.40.0
    environment:
      ARTEMIS_USER: admin
      ARTEMIS_PASSWORD: admin
      ANONYMOUS_LOGIN: &quot;false&quot;
    ports: [&quot;61616:61616&quot;, &quot;8161:8161&quot;]
    volumes:
      - artemisdata:/var/lib/artemis-instance
volumes:
  pgdata:
  artemisdata:
</code></pre></details></div><div class="card"><h2>explanation.html</h2><p>Previously generated HTML explanation included in the archive.</p><p>This is an earlier generated explanation page that is itself included in the archive.</p><details><summary>View source/content</summary><pre><code>&lt;!DOCTYPE html&gt;
&lt;html lang=&quot;en&quot;&gt;
&lt;head&gt;
&lt;meta charset=&quot;UTF-8&quot;&gt;
&lt;meta name=&quot;viewport&quot; content=&quot;width=device-width,initial-scale=1&quot;&gt;
&lt;title&gt;order-routes.xml - Functionality Explanation&lt;/title&gt;
&lt;style&gt;
body{font-family:Arial,sans-serif;line-height:1.6;margin:0;background:#f4f6f8;color:#222}
.container{max-width:1100px;margin:auto;padding:30px}
.card{background:#fff;border-radius:10px;padding:22px;margin:20px 0;box-shadow:0 2px 8px #0001}
h1,h2{color:#17324d} pre{background:#17202a;color:#eee;padding:16px;border-radius:8px;overflow:auto}
.flow{background:#eef6ff;border-left:5px solid #3578b3;padding:15px}
table{width:100%;border-collapse:collapse}th,td{border:1px solid #ddd;padding:9px;text-align:left}th{background:#e9eef3}
.warn{background:#fff4d6;border-left:5px solid #d89b00;padding:15px}
.ok{background:#e9f7ef;border-left:5px solid #2e8b57;padding:15px}
&lt;/style&gt;
&lt;/head&gt;
&lt;body&gt;&lt;div class=&quot;container&quot;&gt;
&lt;h1&gt;order-routes.xml — Functionality Explanation&lt;/h1&gt;

&lt;div class=&quot;card&quot;&gt;&lt;h2&gt;1. Purpose&lt;/h2&gt;
&lt;p&gt;This uploaded XML is an Apache Camel Spring XML DSL configuration. It defines a producer route, a consumer route,
global exception handling, retries and a DLQ.&lt;/p&gt;
&lt;ul&gt;
&lt;li&gt;&lt;b&gt;producer-route&lt;/b&gt;: receives a Camel message from &lt;code&gt;direct:orderProducer&lt;/code&gt;, adds a timestamp,
marshals it to JSON and sends it to Artemis &lt;code&gt;orders.in&lt;/code&gt;.&lt;/li&gt;
&lt;li&gt;&lt;b&gt;order-consumer-route&lt;/b&gt;: consumes from &lt;code&gt;orders.in&lt;/code&gt; using five concurrent consumers, reads an
idempotency key, unmarshals JSON, validates &lt;code&gt;orderNumber&lt;/code&gt;, calls &lt;code&gt;orderTransactionService&lt;/code&gt;,
and sends successful results to &lt;code&gt;orders.processed&lt;/code&gt;.&lt;/li&gt;
&lt;/ul&gt;&lt;/div&gt;

&lt;div class=&quot;card&quot;&gt;&lt;h2&gt;2. Overall Data Flow&lt;/h2&gt;
&lt;div class=&quot;flow&quot;&gt;REST/API or another Camel component → &lt;code&gt;direct:orderProducer&lt;/code&gt; → JSON → Artemis
&lt;code&gt;orders.in&lt;/code&gt; → 5 concurrent consumers → idempotency key → JSON unmarshal → validation →
&lt;code&gt;orderTransactionService.process(...)&lt;/code&gt; → JSON → &lt;code&gt;orders.processed&lt;/code&gt;.&lt;/div&gt;
&lt;p&gt;Unhandled exceptions are subject to the configured global redelivery policy and DLQ endpoint.&lt;/p&gt;&lt;/div&gt;

&lt;div class=&quot;card&quot;&gt;&lt;h2&gt;3. Camel Context and Namespaces&lt;/h2&gt;
&lt;p&gt;The root uses the Camel Spring namespace as the default namespace and the Spring Beans namespace with the
&lt;code&gt;spring&lt;/code&gt; prefix. The Camel context is &lt;code&gt;orderCamelContext&lt;/code&gt;.&lt;/p&gt;
&lt;pre&gt;&amp;lt;beans xmlns=&amp;quot;http://camel.apache.org/schema/spring&amp;quot;
       xmlns:spring=&amp;quot;http://www.springframework.org/schema/beans&amp;quot;
       xmlns:xsi=&amp;quot;http://www.w3.org/2001/XMLSchema-instance&amp;quot;
       xsi:schemaLocation=&amp;quot;http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd http://camel.apache.org/schema/spring https://camel.apache.org/schema/spring/camel-spring.xsd&amp;quot;&amp;gt;
  &amp;lt;camelContext id=&amp;quot;orderCamelContext&amp;quot;&amp;gt;
    &amp;lt;onException useOriginalMessage=&amp;quot;true&amp;quot;&amp;gt;&lt;/pre&gt;&lt;/div&gt;

&lt;div class=&quot;card&quot;&gt;&lt;h2&gt;4. Global Exception Handling&lt;/h2&gt;
&lt;p&gt;The &lt;code&gt;onException&lt;/code&gt; block catches &lt;code&gt;java.lang.Exception&lt;/code&gt;.&lt;/p&gt;
&lt;table&gt;&lt;tr&gt;&lt;th&gt;Setting&lt;/th&gt;&lt;th&gt;Value&lt;/th&gt;&lt;th&gt;Function&lt;/th&gt;&lt;/tr&gt;
&lt;tr&gt;&lt;td&gt;useOriginalMessage&lt;/td&gt;&lt;td&gt;true&lt;/td&gt;&lt;td&gt;Uses the original message for exception handling.&lt;/td&gt;&lt;/tr&gt;
&lt;tr&gt;&lt;td&gt;maximumRedeliveries&lt;/td&gt;&lt;td&gt;3&lt;/td&gt;&lt;td&gt;Configures three redelivery attempts.&lt;/td&gt;&lt;/tr&gt;
&lt;tr&gt;&lt;td&gt;redeliveryDelay&lt;/td&gt;&lt;td&gt;2000&lt;/td&gt;&lt;td&gt;Two seconds between attempts.&lt;/td&gt;&lt;/tr&gt;
&lt;tr&gt;&lt;td&gt;retryAttemptedLogLevel&lt;/td&gt;&lt;td&gt;WARN&lt;/td&gt;&lt;td&gt;Retry attempts are logged at WARN.&lt;/td&gt;&lt;/tr&gt;
&lt;tr&gt;&lt;td&gt;handled&lt;/td&gt;&lt;td&gt;false&lt;/td&gt;&lt;td&gt;The global handler does not mark the exception handled.&lt;/td&gt;&lt;/tr&gt;
&lt;tr&gt;&lt;td&gt;DLQ&lt;/td&gt;&lt;td&gt;orders.dlq&lt;/td&gt;&lt;td&gt;Configured JMS destination for failed processing.&lt;/td&gt;&lt;/tr&gt;&lt;/table&gt;
&lt;pre&gt;    &amp;lt;onException useOriginalMessage=&amp;quot;true&amp;quot;&amp;gt;
      &amp;lt;exception&amp;gt;java.lang.Exception&amp;lt;/exception&amp;gt;
      &amp;lt;redeliveryPolicy maximumRedeliveries=&amp;quot;3&amp;quot; redeliveryDelay=&amp;quot;2000&amp;quot; retryAttemptedLogLevel=&amp;quot;WARN&amp;quot;/&amp;gt;
      &amp;lt;handled&amp;gt;&amp;lt;constant&amp;gt;false&amp;lt;/constant&amp;gt;&amp;lt;/handled&amp;gt;
      &amp;lt;to uri=&amp;quot;jms:queue:orders.dlq&amp;quot;/&amp;gt;
    &amp;lt;/onException&amp;gt;&lt;/pre&gt;&lt;/div&gt;

&lt;div class=&quot;card&quot;&gt;&lt;h2&gt;5. Producer Route&lt;/h2&gt;
&lt;p&gt;The route starts at &lt;code&gt;direct:orderProducer&lt;/code&gt;. It creates a &lt;code&gt;receivedAt&lt;/code&gt; property,
marshals the body using Jackson and sends it to &lt;code&gt;jms:queue:orders.in&lt;/code&gt;.&lt;/p&gt;
&lt;pre&gt;      &amp;lt;route id=&amp;quot;producer-route&amp;quot;&amp;gt;
        &amp;lt;from uri=&amp;quot;direct:orderProducer&amp;quot;/&amp;gt;
        &amp;lt;setProperty name=&amp;quot;receivedAt&amp;quot;&amp;gt;&amp;lt;simple&amp;gt;${date:now:yyyy-MM-dd&amp;#x27;T&amp;#x27;HH:mm:ss.SSS}&amp;lt;/simple&amp;gt;&amp;lt;/setProperty&amp;gt;
        &amp;lt;marshal&amp;gt;&amp;lt;json library=&amp;quot;Jackson&amp;quot;/&amp;gt;&amp;lt;/marshal&amp;gt;
        &amp;lt;to uri=&amp;quot;jms:queue:orders.in&amp;quot;/&amp;gt;
      &amp;lt;/route&amp;gt;&lt;/pre&gt;
&lt;div class=&quot;flow&quot;&gt;direct:orderProducer → receivedAt → Marshal JSON → orders.in&lt;/div&gt;&lt;/div&gt;

&lt;div class=&quot;card&quot;&gt;&lt;h2&gt;6. Consumer Route and Five Consumers&lt;/h2&gt;
&lt;p&gt;The consumer endpoint is:&lt;/p&gt;
&lt;pre&gt;jms:queue:orders.in?concurrentConsumers=5&amp;amp;transacted=true&lt;/pre&gt;
&lt;p&gt;This requests five concurrent consumers and transacted JMS consumption.&lt;/p&gt;
&lt;pre&gt;Artemis orders.in
       |
  +----+----+----+----+----+
  |    |    |    |    |
 C1   C2   C3   C4   C5
  |    |    |    |    |
  +----+----+----+----+----+
             |
       Order processing&lt;/pre&gt;&lt;/div&gt;

&lt;div class=&quot;card&quot;&gt;&lt;h2&gt;7. Idempotency Key&lt;/h2&gt;
&lt;p&gt;The route reads the JMS header &lt;code&gt;Idempotency-Key&lt;/code&gt; and stores it as the Camel exchange property
&lt;code&gt;messageId&lt;/code&gt;.&lt;/p&gt;
&lt;pre&gt;&amp;lt;setProperty name=&quot;messageId&quot;&amp;gt;
  &amp;lt;simple&amp;gt;${header.Idempotency-Key}&amp;lt;/simple&amp;gt;
&amp;lt;/setProperty&amp;gt;&lt;/pre&gt;
&lt;p&gt;The property is then passed to &lt;code&gt;orderTransactionService.process&lt;/code&gt;. The XML does not contain the
implementation of the service or its database idempotency logic, so that part cannot be confirmed from this file alone.&lt;/p&gt;&lt;/div&gt;

&lt;div class=&quot;card&quot;&gt;&lt;h2&gt;8. JSON Unmarshal and Validation&lt;/h2&gt;
&lt;p&gt;The consumer converts JSON to an object representation using Jackson:&lt;/p&gt;
&lt;pre&gt;&amp;lt;unmarshal&amp;gt;&amp;lt;json library=&quot;Jackson&quot;/&amp;gt;&amp;lt;/unmarshal&amp;gt;&lt;/pre&gt;
&lt;p&gt;The &lt;code&gt;choice&lt;/code&gt; checks whether &lt;code&gt;orderNumber&lt;/code&gt; is null or empty.&lt;/p&gt;
&lt;ul&gt;&lt;li&gt;Invalid: throws &lt;code&gt;IllegalArgumentException&lt;/code&gt; with &lt;code&gt;orderNumber is required&lt;/code&gt;.&lt;/li&gt;
&lt;li&gt;Valid: continues through &lt;code&gt;otherwise&lt;/code&gt; to the transaction service.&lt;/li&gt;&lt;/ul&gt;&lt;/div&gt;

&lt;div class=&quot;card&quot;&gt;&lt;h2&gt;9. Transaction Service&lt;/h2&gt;
&lt;pre&gt;&amp;lt;spring:bean ref=&quot;orderTransactionService&quot;
 method=&quot;process(${exchangeProperty.messageId}, ${body})&quot;/&amp;gt;&lt;/pre&gt;
&lt;p&gt;The XML proves that the Camel route delegates processing to the Spring bean
&lt;code&gt;orderTransactionService&lt;/code&gt;. The Java implementation is not part of the uploaded XML, so its internal
database transaction behavior is not inferred here.&lt;/p&gt;&lt;/div&gt;

&lt;div class=&quot;card&quot;&gt;&lt;h2&gt;10. Successful Processing&lt;/h2&gt;
&lt;p&gt;After the service call, the result is marshalled to JSON and sent to:&lt;/p&gt;
&lt;pre&gt;jms:queue:orders.processed?transacted=true&lt;/pre&gt;
&lt;div class=&quot;flow&quot;&gt;Valid message → transaction service → Marshal JSON → orders.processed&lt;/div&gt;&lt;/div&gt;

&lt;div class=&quot;card&quot;&gt;&lt;h2&gt;11. Duplicate Message Handling&lt;/h2&gt;
&lt;p&gt;The first local &lt;code&gt;doCatch&lt;/code&gt; catches
&lt;code&gt;OrderTransactionService$DuplicateMessageException&lt;/code&gt;. It logs the duplicate and replaces the body with:&lt;/p&gt;
&lt;pre&gt;{&quot;status&quot;:&quot;DUPLICATE&quot;,&quot;messageKey&quot;:&quot;...&quot;}&lt;/pre&gt;
&lt;p&gt;The duplicate exception is therefore handled by the local catch. The XML does not show how the service decides
that a message is duplicate.&lt;/p&gt;&lt;/div&gt;

&lt;div class=&quot;card&quot;&gt;&lt;h2&gt;12. Validation Error Handling&lt;/h2&gt;
&lt;p&gt;The second &lt;code&gt;doCatch&lt;/code&gt; catches &lt;code&gt;IllegalArgumentException&lt;/code&gt;, logs the validation failure,
marks it handled, and creates:&lt;/p&gt;
&lt;pre&gt;{&quot;status&quot;:&quot;INVALID&quot;,&quot;message&quot;:&quot;...&quot;}&lt;/pre&gt;
&lt;p&gt;Because this catch explicitly sets &lt;code&gt;handled=true&lt;/code&gt;, this local validation exception is considered handled.&lt;/p&gt;&lt;/div&gt;

&lt;div class=&quot;card&quot;&gt;&lt;h2&gt;13. Complete Flow&lt;/h2&gt;
&lt;pre&gt;
direct:orderProducer
        |
        v
 Producer Route
        |
   receivedAt
        |
   Marshal JSON
        |
        v
 Artemis orders.in
        |
 +------+------+------+------+ 
 |      |      |      |      |
 C1     C2     C3     C4     C5
 +------+------+------+------+ 
        |
        v
 Idempotency-Key
        |
        v
 Unmarshal JSON
        |
        v
      choice
      /   \
 invalid  valid
   |       |
   v       v
throw   orderTransactionService
   |       |
doCatch   Marshal JSON
   |       |
INVALID    v
         orders.processed

Duplicate:
service -&gt; DuplicateMessageException -&gt; doCatch -&gt; DUPLICATE

Unhandled exception:
exception -&gt; onException -&gt; up to 3 redeliveries -&gt; orders.dlq
&lt;/pre&gt;&lt;/div&gt;

&lt;div class=&quot;card&quot;&gt;&lt;h2&gt;14. Line-by-Line Reference&lt;/h2&gt;
&lt;table&gt;&lt;tr&gt;&lt;th&gt;Lines&lt;/th&gt;&lt;th&gt;Function&lt;/th&gt;&lt;/tr&gt;
&lt;tr&gt;&lt;td&gt;1–5&lt;/td&gt;&lt;td&gt;XML declaration, namespaces and schema locations.&lt;/td&gt;&lt;/tr&gt;
&lt;tr&gt;&lt;td&gt;6&lt;/td&gt;&lt;td&gt;Creates Camel context.&lt;/td&gt;&lt;/tr&gt;
&lt;tr&gt;&lt;td&gt;7–12&lt;/td&gt;&lt;td&gt;Global exception handler, retry policy and DLQ endpoint.&lt;/td&gt;&lt;/tr&gt;
&lt;tr&gt;&lt;td&gt;14&lt;/td&gt;&lt;td&gt;Creates route context.&lt;/td&gt;&lt;/tr&gt;
&lt;tr&gt;&lt;td&gt;15–20&lt;/td&gt;&lt;td&gt;Producer route: timestamp → JSON → orders.in.&lt;/td&gt;&lt;/tr&gt;
&lt;tr&gt;&lt;td&gt;22–24&lt;/td&gt;&lt;td&gt;Consumer route, five consumers, transacted input and messageId property.&lt;/td&gt;&lt;/tr&gt;
&lt;tr&gt;&lt;td&gt;25–37&lt;/td&gt;&lt;td&gt;Try block: unmarshal → validation → service → marshal → orders.processed.&lt;/td&gt;&lt;/tr&gt;
&lt;tr&gt;&lt;td&gt;38–42&lt;/td&gt;&lt;td&gt;Duplicate exception catch.&lt;/td&gt;&lt;/tr&gt;
&lt;tr&gt;&lt;td&gt;43–48&lt;/td&gt;&lt;td&gt;Validation exception catch.&lt;/td&gt;&lt;/tr&gt;
&lt;tr&gt;&lt;td&gt;49–53&lt;/td&gt;&lt;td&gt;End of routes and Camel context.&lt;/td&gt;&lt;/tr&gt;&lt;/table&gt;&lt;/div&gt;

&lt;div class=&quot;card warn&quot;&gt;&lt;b&gt;Important:&lt;/b&gt; This explanation is based on the uploaded &lt;code&gt;order-routes.xml&lt;/code&gt;.
The file does not contain the Java implementation of &lt;code&gt;orderTransactionService&lt;/code&gt;, database schema,
Spring Boot configuration, or Artemis broker configuration. Those details cannot be established from this XML alone.&lt;/div&gt;

&lt;div class=&quot;card ok&quot;&gt;&lt;b&gt;Key takeaway:&lt;/b&gt; the XML is the Camel orchestration layer connecting the producer endpoint,
Artemis input queue, five concurrent consumers, JSON conversion, validation, the transaction service,
success queue, local exception handling, retry policy and DLQ.&lt;/div&gt;
&lt;/div&gt;&lt;/body&gt;&lt;/html&gt;</code></pre></details></div><div class="card"><h2>pom.xml</h2><p>Maven build descriptor: Java/Spring Boot/Camel versions, dependency management and runtime dependencies.</p><h3>Build and dependency stack</h3>
<ul>
<li>Spring Boot parent: <code>3.5.6</code></li>
<li>Java: <code>21</code></li>
<li>Apache Camel: <code>4.14.0</code></li>
<li>Spring Web</li>
<li>Spring Data JPA</li>
<li>Spring Boot Artemis</li>
<li>Camel Spring Boot</li>
<li>Camel JMS</li>
<li>Camel Jackson</li>
<li>Camel XML IO DSL</li>
<li>PostgreSQL JDBC driver</li>
</ul><details><summary>View source/content</summary><pre><code>&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;
&lt;project xmlns=&quot;http://maven.apache.org/POM/4.0.0&quot;&gt;
  &lt;modelVersion&gt;4.0.0&lt;/modelVersion&gt;
  &lt;parent&gt;&lt;groupId&gt;org.springframework.boot&lt;/groupId&gt;&lt;artifactId&gt;spring-boot-starter-parent&lt;/artifactId&gt;&lt;version&gt;3.5.6&lt;/version&gt;&lt;relativePath/&gt;&lt;/parent&gt;
  &lt;groupId&gt;com.example&lt;/groupId&gt;&lt;artifactId&gt;camel-artemis-xml-full-dsl&lt;/artifactId&gt;&lt;version&gt;1.0.0&lt;/version&gt;
  &lt;properties&gt;&lt;java.version&gt;21&lt;/java.version&gt;&lt;camel.version&gt;4.14.0&lt;/camel.version&gt;&lt;/properties&gt;
  &lt;dependencyManagement&gt;&lt;dependencies&gt;&lt;dependency&gt;&lt;groupId&gt;org.apache.camel.springboot&lt;/groupId&gt;&lt;artifactId&gt;camel-spring-boot-bom&lt;/artifactId&gt;&lt;version&gt;${camel.version}&lt;/version&gt;&lt;type&gt;pom&lt;/type&gt;&lt;scope&gt;import&lt;/scope&gt;&lt;/dependency&gt;&lt;/dependencies&gt;&lt;/dependencyManagement&gt;
  &lt;dependencies&gt;
    &lt;dependency&gt;&lt;groupId&gt;org.springframework.boot&lt;/groupId&gt;&lt;artifactId&gt;spring-boot-starter-web&lt;/artifactId&gt;&lt;/dependency&gt;
    &lt;dependency&gt;&lt;groupId&gt;org.springframework.boot&lt;/groupId&gt;&lt;artifactId&gt;spring-boot-starter-data-jpa&lt;/artifactId&gt;&lt;/dependency&gt;
    &lt;dependency&gt;&lt;groupId&gt;org.springframework.boot&lt;/groupId&gt;&lt;artifactId&gt;spring-boot-starter-artemis&lt;/artifactId&gt;&lt;/dependency&gt;
    &lt;dependency&gt;&lt;groupId&gt;org.apache.camel.springboot&lt;/groupId&gt;&lt;artifactId&gt;camel-spring-boot-starter&lt;/artifactId&gt;&lt;/dependency&gt;
    &lt;dependency&gt;&lt;groupId&gt;org.apache.camel.springboot&lt;/groupId&gt;&lt;artifactId&gt;camel-jms-starter&lt;/artifactId&gt;&lt;/dependency&gt;
    &lt;dependency&gt;&lt;groupId&gt;org.apache.camel.springboot&lt;/groupId&gt;&lt;artifactId&gt;camel-jackson-starter&lt;/artifactId&gt;&lt;/dependency&gt;
    &lt;dependency&gt;&lt;groupId&gt;org.apache.camel.springboot&lt;/groupId&gt;&lt;artifactId&gt;camel-xml-io-dsl-starter&lt;/artifactId&gt;&lt;/dependency&gt;
    &lt;dependency&gt;&lt;groupId&gt;org.postgresql&lt;/groupId&gt;&lt;artifactId&gt;postgresql&lt;/artifactId&gt;&lt;scope&gt;runtime&lt;/scope&gt;&lt;/dependency&gt;
  &lt;/dependencies&gt;
  &lt;build&gt;&lt;plugins&gt;&lt;plugin&gt;&lt;groupId&gt;org.springframework.boot&lt;/groupId&gt;&lt;artifactId&gt;spring-boot-maven-plugin&lt;/artifactId&gt;&lt;/plugin&gt;&lt;/plugins&gt;&lt;/build&gt;
&lt;/project&gt;
</code></pre></details></div><div class="card"><h2>src/main/java/com/example/orderapp/OrderApplication.java</h2><p>Spring Boot application entry point.</p><h3>Startup</h3><p><code>SpringApplication.run(OrderApplication.class,args)</code> starts the Spring Boot application.</p><details><summary>View source/content</summary><pre><code>package com.example.orderapp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication public class OrderApplication { public static void main(String[] args){SpringApplication.run(OrderApplication.class,args);} }
</code></pre></details></div><div class="card"><h2>src/main/java/com/example/orderapp/api/OrderController.java</h2><p>REST endpoint that accepts an order and sends it to Artemis with an Idempotency-Key.</p><h3>Runtime behavior</h3>
<pre><code>POST /api/orders
    ↓
Read Idempotency-Key
    ↓
If missing: generate UUID
    ↓
producer.sendBodyAndHeader("jms:queue:orders.in", order, key)
    ↓
HTTP 202 Accepted</code></pre><details><summary>View source/content</summary><pre><code>package com.example.orderapp.api;
import com.example.orderapp.model.OrderRequest;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
@RestController @RequestMapping(&quot;/api/orders&quot;) public class OrderController {
 private final ProducerTemplate producer; public OrderController(ProducerTemplate producer){this.producer=producer;}
 @PostMapping public ResponseEntity&lt;String&gt; create(@RequestHeader(value=&quot;Idempotency-Key&quot;,required=false) String key,@RequestBody OrderRequest order){
  String id=(key==null||key.isBlank())?UUID.randomUUID().toString():key;
  producer.sendBodyAndHeader(&quot;jms:queue:orders.in&quot;,order,&quot;Idempotency-Key&quot;,id);
  return ResponseEntity.accepted().body(&quot;Accepted. Idempotency-Key=&quot;+id);
 }
}
</code></pre></details></div><div class="card"><h2>src/main/java/com/example/orderapp/entity/OrderEntity.java</h2><p>JPA entity mapped to the orders table.</p><h3>Persistence mapping</h3>
<p><code>OrderEntity</code> maps to <code>orders</code>. It has an auto-generated Long ID,
a unique/non-null order number, required customer name and amount, status, and creation timestamp.</p><details><summary>View source/content</summary><pre><code>package com.example.orderapp.entity;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.LocalDateTime;
@Entity @Table(name=&quot;orders&quot;,uniqueConstraints=@UniqueConstraint(name=&quot;uk_order_number&quot;,columnNames=&quot;order_number&quot;))
public class OrderEntity { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @Column(name=&quot;order_number&quot;,nullable=false,unique=true) private String orderNumber; @Column(name=&quot;customer_name&quot;,nullable=false) private String customerName; @Column(nullable=false,precision=14,scale=2) private BigDecimal amount; @Column(nullable=false) private String status; @Column(name=&quot;created_at&quot;,nullable=false) private LocalDateTime createdAt;
public void setOrderNumber(String v){orderNumber=v;} public void setCustomerName(String v){customerName=v;} public void setAmount(BigDecimal v){amount=v;} public void setStatus(String v){status=v;} public void setCreatedAt(LocalDateTime v){createdAt=v;} }
</code></pre></details></div><div class="card"><h2>src/main/java/com/example/orderapp/entity/ProcessedMessage.java</h2><p>JPA entity mapped to processed_messages; the message key is the identifier.</p><h3>Persistence mapping</h3>
<p><code>ProcessedMessage</code> maps to <code>processed_messages</code>. Its
<code>messageKey</code> is the JPA identifier and <code>processedAt</code> records processing time.</p><details><summary>View source/content</summary><pre><code>package com.example.orderapp.entity;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name=&quot;processed_messages&quot;) public class ProcessedMessage { @Id @Column(name=&quot;message_key&quot;,length=200) private String messageKey; @Column(name=&quot;processed_at&quot;,nullable=false) private LocalDateTime processedAt; protected ProcessedMessage(){} public ProcessedMessage(String k){messageKey=k;processedAt=LocalDateTime.now();} }
</code></pre></details></div><div class="card"><h2>src/main/java/com/example/orderapp/model/OrderRequest.java</h2><p>Input DTO containing orderNumber, customerName and amount.</p><h3>Input fields</h3><ul><li><code>orderNumber</code></li><li><code>customerName</code></li><li><code>amount</code> as <code>BigDecimal</code></li></ul><details><summary>View source/content</summary><pre><code>package com.example.orderapp.model;
import java.math.BigDecimal;
public class OrderRequest { private String orderNumber; private String customerName; private BigDecimal amount;
public String getOrderNumber(){return orderNumber;} public void setOrderNumber(String v){orderNumber=v;}
public String getCustomerName(){return customerName;} public void setCustomerName(String v){customerName=v;}
public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;} }
</code></pre></details></div><div class="card"><h2>src/main/java/com/example/orderapp/repository/OrderRepository.java</h2><p>Spring Data JPA repository for OrderEntity, including order-number existence checking.</p><h3>Repository API</h3><p>Extends <code>JpaRepository&lt;OrderEntity,Long&gt;</code> and provides
<code>existsByOrderNumber</code> for business duplicate detection.</p><details><summary>View source/content</summary><pre><code>package com.example.orderapp.repository;
import com.example.orderapp.entity.OrderEntity; import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderRepository extends JpaRepository&lt;OrderEntity,Long&gt;{boolean existsByOrderNumber(String orderNumber);}
</code></pre></details></div><div class="card"><h2>src/main/java/com/example/orderapp/repository/ProcessedMessageRepository.java</h2><p>Spring Data JPA repository for ProcessedMessage.</p><h3>Repository API</h3><p>Extends <code>JpaRepository&lt;ProcessedMessage,String&gt;</code>, allowing
lookup by the idempotency/message key.</p><details><summary>View source/content</summary><pre><code>package com.example.orderapp.repository;
import com.example.orderapp.entity.ProcessedMessage; import org.springframework.data.jpa.repository.JpaRepository;
public interface ProcessedMessageRepository extends JpaRepository&lt;ProcessedMessage,String&gt;{}
</code></pre></details></div><div class="card"><h2>src/main/java/com/example/orderapp/service/OrderTransactionService.java</h2><p>Transactional business service implementing idempotency checks, duplicate checks, simulated failure, order persistence and processed-message persistence.</p><h3>Business processing</h3>
<pre><code>@Transactional
process(key, request)
    |
    +-- processed_messages contains key? → Duplicate
    |
    +-- orders contains orderNumber? → Duplicate
    |
    +-- customerName == FAIL? → SimulatedFailureException
    |
    +-- save OrderEntity
    |
    +-- save ProcessedMessage
    |
    +-- commit</code></pre><details><summary>View source/content</summary><pre><code>package com.example.orderapp.service;
import com.example.orderapp.entity.*; import com.example.orderapp.model.OrderRequest; import com.example.orderapp.repository.*; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.time.LocalDateTime;
@Service(&quot;orderTransactionService&quot;) public class OrderTransactionService { private final OrderRepository orders; private final ProcessedMessageRepository messages;
 public OrderTransactionService(OrderRepository o,ProcessedMessageRepository m){orders=o;messages=m;}
 @Transactional public void process(String key,OrderRequest request){
  if(messages.existsById(key)) throw new DuplicateMessageException(&quot;Duplicate idempotency key: &quot;+key);
  if(orders.existsByOrderNumber(request.getOrderNumber())) throw new DuplicateMessageException(&quot;Duplicate order: &quot;+request.getOrderNumber());
  if(&quot;FAIL&quot;.equalsIgnoreCase(request.getCustomerName())) throw new SimulatedFailureException(&quot;Simulated processing failure&quot;);
  OrderEntity e=new OrderEntity(); e.setOrderNumber(request.getOrderNumber()); e.setCustomerName(request.getCustomerName()); e.setAmount(request.getAmount()); e.setStatus(&quot;PROCESSED&quot;); e.setCreatedAt(LocalDateTime.now()); orders.save(e); messages.save(new ProcessedMessage(key));
 }
 public static class DuplicateMessageException extends RuntimeException{public DuplicateMessageException(String m){super(m);}}
 public static class SimulatedFailureException extends RuntimeException{public SimulatedFailureException(String m){super(m);}}
}
</code></pre></details></div><div class="card"><h2>src/main/resources/application.yml</h2><p>Spring Boot runtime configuration for application name, PostgreSQL, Artemis, server port and Camel.</p><h3>Runtime configuration</h3>
<ul>
<li>Application name: <code>camel-artemis-xml-full-dsl</code></li>
<li>PostgreSQL URL: <code>jdbc:postgresql://localhost:5432/ordersdb</code></li>
<li>Artemis broker: <code>tcp://localhost:61616</code></li>
<li>Spring Boot HTTP port: <code>8080</code></li>
<li>JPA schema mode: <code>validate</code></li>
<li>Camel Spring Boot main-run-controller: <code>true</code></li>
</ul><details><summary>View source/content</summary><pre><code>spring:
  application:
    name: camel-artemis-xml-full-dsl
  datasource:
    url: jdbc:postgresql://localhost:5432/ordersdb
    username: orders
    password: orders
  jpa:
    hibernate:
      ddl-auto: validate
    open-in-view: false
  artemis:
    mode: native
    broker-url: tcp://localhost:61616
    user: admin
    password: admin
server:
  port: 8080
camel:
  springboot:
    main-run-controller: true
</code></pre></details></div><div class="card"><h2>src/main/resources/camel/order-routes.xml</h2><p>Apache Camel Spring XML DSL orchestration: producer route, five concurrent JMS consumers, JSON conversion, validation, service invocation, local catches, global retry and DLQ.</p><h3>XML route behavior</h3>
<pre><code>Global onException
    ├─ Exception: java.lang.Exception
    ├─ maximumRedeliveries: 3
    ├─ redeliveryDelay: 2000 ms
    └─ to: jms:queue:orders.dlq

producer-route
    direct:orderProducer
        ↓
    receivedAt property
        ↓
    Jackson marshal
        ↓
    orders.in

order-consumer-route
    orders.in?concurrentConsumers=5&amp;transacted=true
        ↓
    messageId = Idempotency-Key
        ↓
    Jackson unmarshal
        ↓
    choice: orderNumber required
        ↓
    orderTransactionService.process(...)
        ↓
    Jackson marshal
        ↓
    orders.processed

Local catches:
    DuplicateMessageException → DUPLICATE response
    IllegalArgumentException  → INVALID response + handled=true</code></pre><details><summary>View source/content</summary><pre><code>&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;
&lt;beans xmlns=&quot;http://camel.apache.org/schema/spring&quot;
       xmlns:spring=&quot;http://www.springframework.org/schema/beans&quot;
       xmlns:xsi=&quot;http://www.w3.org/2001/XMLSchema-instance&quot;
       xsi:schemaLocation=&quot;http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd http://camel.apache.org/schema/spring https://camel.apache.org/schema/spring/camel-spring.xsd&quot;&gt;
  &lt;camelContext id=&quot;orderCamelContext&quot;&gt;
    &lt;onException useOriginalMessage=&quot;true&quot;&gt;
      &lt;exception&gt;java.lang.Exception&lt;/exception&gt;
      &lt;redeliveryPolicy maximumRedeliveries=&quot;3&quot; redeliveryDelay=&quot;2000&quot; retryAttemptedLogLevel=&quot;WARN&quot;/&gt;
      &lt;handled&gt;&lt;constant&gt;false&lt;/constant&gt;&lt;/handled&gt;
      &lt;to uri=&quot;jms:queue:orders.dlq&quot;/&gt;
    &lt;/onException&gt;

    &lt;routeContext id=&quot;orderRouteContext&quot;&gt;
      &lt;route id=&quot;producer-route&quot;&gt;
        &lt;from uri=&quot;direct:orderProducer&quot;/&gt;
        &lt;setProperty name=&quot;receivedAt&quot;&gt;&lt;simple&gt;${date:now:yyyy-MM-dd&#x27;T&#x27;HH:mm:ss.SSS}&lt;/simple&gt;&lt;/setProperty&gt;
        &lt;marshal&gt;&lt;json library=&quot;Jackson&quot;/&gt;&lt;/marshal&gt;
        &lt;to uri=&quot;jms:queue:orders.in&quot;/&gt;
      &lt;/route&gt;

      &lt;route id=&quot;order-consumer-route&quot;&gt;
        &lt;from uri=&quot;jms:queue:orders.in?concurrentConsumers=5&amp;amp;transacted=true&quot;/&gt;
        &lt;setProperty name=&quot;messageId&quot;&gt;&lt;simple&gt;${header.Idempotency-Key}&lt;/simple&gt;&lt;/setProperty&gt;
        &lt;doTry&gt;
          &lt;unmarshal&gt;&lt;json library=&quot;Jackson&quot;/&gt;&lt;/unmarshal&gt;
          &lt;choice&gt;
            &lt;when&gt;&lt;simple&gt;${body[orderNumber]} == null || ${body[orderNumber]} == &#x27;&#x27;&lt;/simple&gt;
              &lt;throwException exceptionType=&quot;java.lang.IllegalArgumentException&quot; message=&quot;orderNumber is required&quot;/&gt;
            &lt;/when&gt;
            &lt;otherwise&gt;
              &lt;setBody&gt;&lt;simple&gt;${body}&lt;/simple&gt;&lt;/setBody&gt;
              &lt;spring:bean ref=&quot;orderTransactionService&quot; method=&quot;process(${exchangeProperty.messageId}, ${body})&quot;/&gt;
            &lt;/otherwise&gt;
          &lt;/choice&gt;
          &lt;marshal&gt;&lt;json library=&quot;Jackson&quot;/&gt;&lt;/marshal&gt;
          &lt;to uri=&quot;jms:queue:orders.processed?transacted=true&quot;/&gt;
          &lt;doCatch&gt;
            &lt;exception&gt;com.example.orderapp.service.OrderTransactionService$DuplicateMessageException&lt;/exception&gt;
            &lt;log message=&quot;Duplicate ignored: ${exchangeProperty.messageId}&quot;/&gt;
            &lt;setBody&gt;&lt;simple&gt;{&quot;status&quot;:&quot;DUPLICATE&quot;,&quot;messageKey&quot;:&quot;${exchangeProperty.messageId}&quot;}&lt;/simple&gt;&lt;/setBody&gt;
          &lt;/doCatch&gt;
          &lt;doCatch&gt;
            &lt;exception&gt;java.lang.IllegalArgumentException&lt;/exception&gt;
            &lt;log message=&quot;Validation failure: ${exception.message}&quot;/&gt;
            &lt;handled&gt;&lt;constant&gt;true&lt;/constant&gt;&lt;/handled&gt;
            &lt;setBody&gt;&lt;simple&gt;{&quot;status&quot;:&quot;INVALID&quot;,&quot;message&quot;:&quot;${exception.message}&quot;}&lt;/simple&gt;&lt;/setBody&gt;
          &lt;/doCatch&gt;
        &lt;/doTry&gt;
      &lt;/route&gt;
    &lt;/routeContext&gt;
  &lt;/camelContext&gt;
&lt;/beans&gt;
</code></pre></details></div><div class="card"><h2>src/main/resources/schema.sql</h2><p>PostgreSQL schema for the orders and processed_messages tables.</p><h3>Database model</h3>
<pre><code>orders
  id BIGSERIAL PRIMARY KEY
  order_number VARCHAR(100) NOT NULL UNIQUE
  customer_name VARCHAR(150) NOT NULL
  amount NUMERIC(14,2) NOT NULL
  status VARCHAR(40) NOT NULL
  created_at TIMESTAMP NOT NULL

processed_messages
  message_key VARCHAR(200) PRIMARY KEY
  processed_at TIMESTAMP NOT NULL</code></pre><details><summary>View source/content</summary><pre><code>CREATE TABLE IF NOT EXISTS orders (id BIGSERIAL PRIMARY KEY, order_number VARCHAR(100) NOT NULL UNIQUE, customer_name VARCHAR(150) NOT NULL, amount NUMERIC(14,2) NOT NULL, status VARCHAR(40) NOT NULL, created_at TIMESTAMP NOT NULL);
CREATE TABLE IF NOT EXISTS processed_messages (message_key VARCHAR(200) PRIMARY KEY, processed_at TIMESTAMP NOT NULL);
</code></pre></details></div><div class="card">
<h2>9. Repository Metadata</h2>
<p>The archive also contains a <code>.git/</code> directory with Git metadata, hooks, refs, logs and object files.
Those files describe version-control history/state rather than application runtime behavior. They are therefore not
treated as executable application components in this explanation.</p>
</div>

<div class="card">
<h2>10. Important Implementation Observation</h2>
<div class="note">
The source contains both a Camel producer XML route starting at <code>direct:orderProducer</code> and a REST controller
that directly sends to <code>jms:queue:orders.in</code>. Therefore, as the uploaded code stands, the REST controller
does not call the <code>direct:orderProducer</code> XML route. This is important when tracing the actual runtime path.
</div>
</div>

<div class="card">
<h2>11. Failure Scenario</h2>
<p>The service intentionally throws <code>SimulatedFailureException</code> when
<code>customerName</code> equals <code>FAIL</code>, case-insensitively. That exception is not caught by either of
the two local <code>doCatch</code> blocks shown in the XML, so it can reach the global <code>onException</code> policy.</p>
<pre><code>orders.in
   ↓
consumer
   ↓
OrderTransactionService
   ↓
customerName == FAIL
   ↓
SimulatedFailureException
   ↓
global onException
   ↓
up to 3 redeliveries
   ↓
orders.dlq</code></pre>
</div>

<div class="card ok">
<h2>12. Final Summary</h2>
<p>This project is a Spring Boot + Java 21 + Apache Camel + ActiveMQ Artemis + PostgreSQL order-processing sample.
The Java layer exposes the REST endpoint and implements persistence/business processing. The XML layer orchestrates
Camel messaging, JSON transformation, validation, concurrent Artemis consumption, local exception handling and
global retry/DLQ behavior. Docker Compose supplies PostgreSQL and Artemis locally.</p>
</div>

<p class="small">Generated from the uploaded archive: spring-boot-camel-artemis-xml-dsl.zip</p>
</div></body></html>