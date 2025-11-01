# RabbitMQ Dynamic TTL Demo

This project demonstrates how to implement **dynamic message delays in RabbitMQ** without relying on
external plugins (like `rabbitmq_delayed_message_exchange`) or creating new queues at runtime.
Instead, it leverages **native RabbitMQ features** such as **per-message TTL** and **dead-letter routing**.

The project is built with **Spring Boot** and provides two flows:

- **Simple flow**: Messages are published and consumed immediately.
- **Delayed flow**: Messages are published with a runtime-defined delay (in milliseconds) and processed
after the delay expires.

---

## 🚀 Problem Statement

By default, RabbitMQ supports message delaying using **queue-level TTL**. However:

- Changing the TTL requires creating a new queue with updated arguments.
This is not flexible for runtime scenarios where each message may require a different delay.

This project solves the problem by:

- Using **per-message TTL** (expiration property).
- Routing expired messages via a **dead-letter routing** into the final processing queue.
- Allowing **runtime delay definition** without creating new queues.

---

## ⚠️ Known Side Effects

- RabbitMQ queues are **FIFO**.
- If you decrease the TTL at runtime, messages published earlier with a higher TTL may still block newer messages with a shorter TTL.
- This behavior is expected and should be considered when designing systems with mixed TTL values.

---

## 🛠️ Architecture

```
        [Producer REST API]
                  |
                  v
      -------------------------
       |                     |
       v                     v
simple-exchange       delayed-exchange
       |                     |
       v                     v
 simple-queue        delayed-wait-queue (per-message TTL)
                             |
                             v
                       delayed-queue (final consumer)

```


- **Simple flow:**  
`simple-exchange → simple-queue → consumer`

- **Delayed flow:**  
`delayed-exchange → delayed-wait-queue (per-message TTL) → delayed-queue → consumer`

### Key Configuration
The link between `delayed-wait-queue` and `delayed-queue` is established using **dead-letter routing**. 

When a message expires in `delayed-wait-queue`, RabbitMQ automatically forwards it to `delayed-queue`:

```java
@Bean
Queue delayedWaitQueue() {
  Map<String, Object> queueArgs = Map.of(
      "x-dead-letter-exchange", "",
      "x-dead-letter-routing-key", "delayed-queue"
  );
  return new Queue("delayed-wait-queue", true, false, false, queueArgs);
}
```
> This configuration ensures that expired messages are re-routed into the final `delayed-queue`, enabling **per-message TTL** delays without creating new queues or using plugins.
---

## 📦 Running with Docker

The project is fully dockerized. It uses **Docker Compose** to start both RabbitMQ and the Spring Boot application.

```bash
mvn clean package && docker-compose up --build
```
- RabbitMQ Management UI: http://localhost:15672
    - Username: `admin`
    - Password: `s3cret`

---

## 🔥 Example Requests
### Simple Flow
```bash
curl --location 'http://localhost:8080/message/simple' \
--header 'Content-Type: application/json' \
--data '{
"message": "Test Message",
"delayMs": 5000
}'
```
### Delayed Flow
```bash
curl --location 'http://localhost:8080/message/delayed' \
--header 'Content-Type: application/json' \
--data '{
"message": "Test Message",
"delayMs": 5000
}'
```

---

## 📑 Sample Logs

**Delayed consumer:**
```
[DELAYED_QUEUE_CONSUMER] [PUBLISH_DATE=Sat Nov 01 12:55:26 UTC 2025] [MESSAGE=Test Message] [DELAY=5000]
```

**Simple consumer:**
```
[SIMPLE_QUEUE_CONSUMER] [PUBLISH_DATE=Sat Nov 01 12:55:26 UTC 2025] [MESSAGE=Test Message]
```

### Measuring the Actual Delay

Each log entry contains both the **publish date** (when the message was sent) and the **log timestamp** (when the consumer processed it).  
By comparing these two values, you can verify the effective delay.


--- 

## ✨ Key Takeaways
- Dynamic per-message TTL allows flexible delayed messaging without plugins.
- FIFO ordering can cause unexpected waiting when mixing high and low TTL values.
- This approach is lightweight, portable, and works with **vanilla RabbitMQ**.

---

## 🤝 Contributing
Contributions are welcome! Feel free to fork the repo, submit pull requests or open issues.

---

## 📜 License
This project is licensed under the MIT License.
