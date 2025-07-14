
# 🔐 IPFS-Powered Certificate Generator with Kafka Events

A **Spring Boot** based certificate generation system that:

* Generates certificate PDFs  
* Uploads them to **IPFS via Pinata**  
* Generates QR codes linked to the PDF  
* Publishes a **Kafka Event** to notify listeners  
* Persists metadata in PostgreSQL  

> 🎯 Designed for transparency, decentralization, and event-driven systems.

---

## 🚀 Features

* 📄 PDF Certificate Generation  
* 🔗 IPFS File Storage via **Pinata**  
* 📷 QR Code for Verification  
* 📬 Kafka Event Publishing  
* 🗃 Metadata Storage in PostgreSQL  
* 📡 RESTful API for issuing & verifying certificates  

---

## ⚙️ Tech Stack

| Layer        | Tools Used                               |
| ------------ | ---------------------------------------- |
| Backend      | Spring Boot, Spring Web, Spring Data JPA |
| Messaging    | Apache Kafka                             |
| IPFS Storage | Pinata + IPFS                            |
| QR Code      | ZXing                                    |
| PDF          | iText or OpenPDF                         |
| DB           | PostgreSQL                               |
| Build Tool   | Maven                                    |

---

## 🧩 High-Level Architecture (HLD)

```plaintext
             +-----------------------+
             |     Client/API       |
             +----------+------------+
                        |
                        v
         +--------------+--------------+
         |      CertificateController |
         +--------------+--------------+
                        |
                        v
        +---------------+---------------+
        |        CertificateService     |
        +----+------------+-------------+
             |            |        
      +------+      +-----+--------+      
      | PDF Generator | QR Generator |
      +------+      +--------------+      
             |              
      +------+-------------------+             
      |   IPFS Upload (Pinata)   |            
      +-------------+------------+         
                    |                     
          +---------v-----------+        
          |  Kafka Event Sender |        
          +---------------------+         
                    |
                    v
           Kafka Topic: `certificate-events`
````

---

## 🧬 Low-Level Design (LLD)

### 📁 Layers

* **Controller**: Handles incoming API requests
* **Service**: Core business logic including:

  * PDF generation
  * QR code generation
  * Upload to IPFS
  * Kafka event publishing
* **Repository**: Interacts with PostgreSQL to save metadata

### 📦 Modules

| Module                | Responsibility                             |
| --------------------- | ------------------------------------------ |
| `CertificateService`  | Orchestrates full certificate issuing flow |
| `PDFGeneratorService` | Creates PDF from request                   |
| `QRCodeService`       | Generates QR codes from URLs               |
| `PinataService`       | Uploads files to IPFS via Pinata API       |
| `KafkaProducer`       | Publishes certificate events to Kafka      |
| `CertificateRepo`     | Saves metadata in PostgreSQL               |

---

## 🖼️ Flow Diagram

```mermaid
sequenceDiagram
    participant Client
    participant API
    participant Service
    participant PDFGen
    participant QRGen
    participant IPFS
    participant Kafka
    participant DB

    Client->>API: POST /api/certificates/issue
    API->>Service: issueCertificate(request)
    Service->>PDFGen: generateCertificatePDF()
    Service->>IPFS: upload PDF to IPFS
    Service->>QRGen: generate QR Code
    Service->>IPFS: upload QR code
    Service->>DB: save metadata
    Service->>Kafka: publish certificate event
    API->>Client: Return metadata response
```

---

## 🔧 API Endpoints

| Method | Endpoint                        | Description              |
| ------ | ------------------------------- | ------------------------ |
| POST   | `/api/certificates/issue`       | Issue a new certificate  |
| GET    | `/api/certificates/verify/{id}` | Verify certificate by ID |

---

## 📥 Sample Request

```json
{
  "name": "Karan Sahani",
  "course": "Java Spring Boot Backend Development",
  "issuedBy": "Noida International University",
  "date": "2025-07-14"
}
```

---

## 📤 Sample Response

```json
{
  "id": 3,
  "name": "Karan Sahani",
  "course": "Java Spring Boot Backend Development",
  "issuedBy": "Noida International University",
  "issueDate": "2025-07-14",
  "ipfsUrl": "https://ipfs.io/ipfs/QmTBWvcn2d2pGCK6nvxrBNsyueqxu1hnDiyrYaXvtTdj1N",
  "qrCodeUrl": "https://ipfs.io/ipfs/Qme8eYPZtTcbyfUHN9bPjXRTprWSFeaQXJq3JBm8Kbjpow",
  "issuedAt": "2025-07-15T00:47:39.555951"
}
```

* 📄 [View PDF](https://ipfs.io/ipfs/QmTBWvcn2d2pGCK6nvxrBNsyueqxu1hnDiyrYaXvtTdj1N)
* 📷 [View QR Code](https://ipfs.io/ipfs/Qme8eYPZtTcbyfUHN9bPjXRTprWSFeaQXJq3JBm8Kbjpow)

---

## 📦 Kafka Event

```json
{
  "certificateId": "3",
  "name": "Karan Sahani",
  "course": "Java Spring Boot Backend Development",
  "ipfsUrl": "https://ipfs.io/ipfs/QmTBWvcn2d2pGCK6nvxrBNsyueqxu1hnDiyrYaXvtTdj1N",
  "qrCodeUrl": "https://ipfs.io/ipfs/Qme8eYPZtTcbyfUHN9bPjXRTprWSFeaQXJq3JBm8Kbjpow"
}
```

---

## 📁 Repository Name Suggestion

> **`ipfs-pinata-kafka-certificate-generator`**

This clearly highlights all major tech used.

---

## 📤 Deployment Instructions

1. Clone the repo

```bash
git clone https://github.com/karansahani78/ipfs-pinata-kafka-certificate-generator.git
cd ipfs-pinata-kafka-certificate-generator
```

2. Set your environment variables in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/certificate_db
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

pinata.api.key=your_pinata_api_key
pinata.secret.key=your_pinata_secret_key

spring.kafka.bootstrap-servers=localhost:9092
```

3. Run Kafka and PostgreSQL

4. Start your Spring Boot app

```bash
./mvnw clean install
./mvnw spring-boot:run
```

---

## 🧠 Author

**Karan Sahani**
[GitHub](https://github.com/karansahani78) • [LinkedIn](https://www.linkedin.com/in/karan-sahani-70a0ba2b1/)
Java Spring Boot Backend Developer | Blockchain Enthusiast
