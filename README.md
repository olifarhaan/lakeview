# LakeView - Enterprise Hotel Management System

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-green)

## Overview

LakeView is a robust, enterprise-grade hotel management system built with Spring Boot 3.3.4 and Java 21. It provides comprehensive APIs for hotel operations, room management, and booking services with a focus on scalability, security, and performance.

## Key Features

### 🏗️ Architecture
- Implemented a modular, scalable architecture using Spring Boot 3.x
- Achieved clean separation of concerns with layered architecture
- Integrated comprehensive logging and monitoring capabilities

### 🔒 Security
- Implemented JWT-based authentication with role-based access control
- Secured endpoints with Spring Security
- Integrated email verification system
- Protected against concurrent booking conflicts using distributed locking

### 💪 Performance
- Implemented Redis-based distributed locking for concurrent bookings
- Optimized database queries for room availability checks
- Utilized connection pooling for improved database performance
- Implemented asynchronous operations for non-blocking operations

### 🏢 Enterprise Features

#### Room Management
- Hierarchical room classification system
- Dynamic pricing models
- Real-time availability tracking
- Multi-floor support

#### Booking System
- Concurrent booking handling
- Automated confirmation emails
- Flexible payment status tracking
- Guest management

### 🔧 Technical Stack
- **Backend**: Spring Boot 3.3.4
- **Database**: MySQL 8.0
- **Cache**: Redis with Redisson
- **Documentation**: OpenAPI/Swagger
- **Testing**: Python Testing Suite
- **Container**: Docker & Docker Compose
- **Build**: Gradle

## Getting Started

### Prerequisites
- Docker and Docker Compose
- JDK 21
- Python 3.x (for testing)

### Quick Start

1. Clone the repository
    ```bash
    git clone https://github.com/yourusername/lakeview.git
    cd lakeview
    ```

2. Configure environment variables
    ```bash
    cp .env.example .env
    # Edit .env with your configurations
    ```

3. Start the application
    ```bash
    docker-compose up --build
    ```

Access points:
- API: http://localhost:8080
- API Documentation: http://localhost:8080/swagger-ui.html

## Documentation

Comprehensive API documentation is available through Swagger UI after starting the application. The documentation includes:
- Detailed endpoint descriptions
- Request/Response schemas
- Authentication requirements
- Testing examples

## Testing

The application includes a comprehensive Python testing suite. For VS Code users, install:
- `littlefoxteam.vscode-python-test-adapter`
- `hbenl.vscode-test-explorer`

## Deployment

The application is containerized and can be deployed to any Docker-compatible environment. Production deployment uses:
- Render [https://render.com/]
- Redis.io [https://redis.io/]
- Aiven for MySQL [https://aiven.io/]

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Author

**M. Ali Farhan**
- LinkedIn: [olifarhaan](https://linkedin.com/in/olifarhaan)
- Email: [alifarhan231087@gmail.com](mailto:alifarhan231087@gmail.com)

---

<p align="center">Made with ❤️ by M. Ali Farhan</p>