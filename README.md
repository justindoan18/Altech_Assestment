## 📖 Overview
This repository contains a **Spring Boot application** built with Java 21 and Maven.  
It is containerized with Docker and can be easily run using **Docker Compose**.  

Users can choose between two approaches:
- Run with **Docker Compose** (recommended, no local setup required).  
- Run locally with Java & Maven installed.  

---

## 🛠️ Requirements

### Option A: Run with Docker (recommended)
- [Docker](https://docs.docker.com/get-docker/)  
- [Docker Compose](https://docs.docker.com/compose/install/)  

### Option B: Run locally
- [Java 21](https://adoptium.net/)  
- [Maven 3.9+](https://maven.apache.org/install.html)  

---

## 🚀 Getting Started

### ▶️ Run with Docker
1. Clone the repository:
   ```bash
   git clone https://github.com/justindoan18/Altech_Assestment.git
   cd Altech_Assestment
2. Run following command:
    ```bash
   docker compose up --build
2. If we want to shutdown project:
    ```bash
    docker compose stop
