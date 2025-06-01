# 🧭 Travel-Bank_Service Registry (v6.0)

This project is part of the **Travel Bank Microservices** suite. It implements a **Service Registry** using **Spring Cloud Netflix Eureka** and supports **client-side service discovery and load balancing** via **Spring Cloud LoadBalancer**.

---

## 📚 Project Lineage

This is the sixth version of Travel Bank Microservices. Explore earlier phases here:

- 🔗 [TRAVEL-BANK_Microservices_v.1.0](https://github.com/vinaysteja2/TRAVEL-BANK_Micorservices_v_1.git)
- 🔗 [TRAVEL-BANK_Docker_v.2.0](https://github.com/vinaysteja2/TRAVEL-BANK_Docker_v_2.git)
- 🔗 [TRAVEL-BANK_ConfigManagement_v.3.0](https://github.com/vinaysteja2/Travel-Bank_Config_Management_v_3.git)
- 🔗 [TRAVEL-BANK_ConfigServer_v.4,0](https://github.com/vinaysteja2/Travel-Bank_ConfigServer_v_4.git)
- 🔗 [Travel-Bank_MySqlDB_v5.0](https://github.com/vinaysteja2/Travel-Bank_MySqlDB_v_5.git)

---

## 🌐 Client-Side Discovery Pattern

In client-side service discovery:

- Each service **registers** itself with Eureka on startup.
- Clients **query the registry** to locate service instances.
- Load balancing is done **on the client side** using `Spring Cloud LoadBalancer`.

---

## ⚙️ Technologies Used

| Component            | Purpose                                  |
|---------------------|-------------------------------------------|
| Spring Boot          | Microservices framework                  |
| Eureka Server        | Service Registry                         |
| Eureka Client        | Registers services with Eureka           |
| Spring Cloud LoadBalancer | Client-side load balancing          |
| OpenFeign (optional) | Declarative REST client for service-to-service calls |

---

## 🔧 Setting Up Eureka Server

### Step 1: Add Dependencies

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

### Step 2: Configure `application.yml`

```yaml
server:
  port: 8070

eureka:
  instance:
    hostname: localhost
  client:
    fetchRegistry: false
    registerWithEureka: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
```

### Step 3: Enable Eureka Server

```java
@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceRegistryApplication.class, args);
    }
}
```

---

## 🔗 Registering Eureka Clients

### Step 1: Add Dependencies

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

### Step 2: Configure `application.yml`

```yaml
spring:
  application:
    name: accounts  # Replace with your service name

eureka:
  instance:
    prefer-ip-address: true
  client:
    registerWithEureka: true
    fetchRegistry: true
    serviceUrl:
      defaultZone: http://localhost:8070/eureka/
```

---

## 🔁 Load Balancing with Spring Cloud LoadBalancer

### Feign Client Example

```java
@FeignClient(name = "accounts") // service name from Eureka
public interface AccountsClient {
    @GetMapping("/accounts")
    List<Account> getAccounts();
}
```

Spring will handle service discovery and automatically balance the load across available instances.

---

## ✅ Benefits of Client-side Discovery

- No need for a central load balancer
- Auto-scaling support
- Reduced latency with direct client-to-service communication
- Built-in fault tolerance

---

## 📅 Roadmap for v7.x

- Switch to Kubernetes-based server-side discovery
- Add HTTPS/TLS for Eureka
- Health checks and resilience monitoring
- Advanced circuit breakers with Resilience4j

---

## 🙌 Contribution

Feel free to fork the repo, open issues, or submit pull requests to improve this project.

---

## 📜 License

MIT License © [Vinay Steja](https://github.com/vinaysteja2)

> Built with 💙 using Spring Cloud, Netflix Eureka, and open-source technologies.
