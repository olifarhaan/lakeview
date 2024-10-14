# Starter Security Application

## Overview

A starter template for building secure and scalable backend services using Spring Boot, JWT, and MySQL. You can use this project as a starting point for your own backend services. It has a Python testing suite for writing and running tests.

The Starter Security Application is a robust, Spring Boot-based backend service designed to provide secure user authentication and authorization. It features a RESTful API, JWT-based security, and seamless integration with a MySQL database. The application also supports email notifications for password resets and is fully containerized for easy deployment.

## Why Should You Use This Project as a Starter Template?

- **Comprehensive Security**: Implements industry-standard JWT-based authentication, ensuring secure user sessions.
- **Scalable Architecture**: Built with Spring Boot, allowing for easy scaling and integration with other services.
- **Dockerized Environment**: Simplifies deployment and testing with Docker and Docker Compose, making it easy to set up and run in any environment.
- **Python Testing Suite**: Includes a Python-based testing framework, providing flexibility and ease of use for writing and running tests.
- **OpenAPI Documentation**: Automatically generated API documentation helps developers understand and interact with the API efficiently.
- **Modular Design**: The codebase is organized into clear modules, making it easy to extend and maintain.

## Prerequisites

- Docker and Docker Compose


## Getting Started

### Clone the Repository

```bash
git clone https://github.com/yourusername/boot-starter-security.git
cd boot-starter-security
```

### Environment Configuration

Create a `.env` file in the root directory with the following content:

```
DB_URL=jdbc:mysql://mysqlhost:3306/boot_starter_security_db
DB_USERNAME=root
DB_PASSWORD=password
CORS_ALLOWED_ORIGINS=http://localhost:5173
AUTH_TOKEN_JWT_SECRET=your_jwt_secret
AUTH_TOKEN_EXPIRATION_IN_MILS=3600000
AUTH_TOKEN_PASSWORD_RESET_EXPIRATION_IN_MILS=10000000
EMAIL_USERNAME=your_email@gmail.com
EMAIL_PASSWORD=your_email_password
PRODUCTION_URL=https://starter-security-api.azurewebsites.net
```

### Build and Run with Docker

1. **Build the Docker images and start the containers:**

   ```bash
   docker-compose up --build
   ```

   This will start the MySQL database, the Spring Boot application, and a Python container for testing.

### Accessing the Application

- The application will be accessible at `http://localhost:8080`.
- The MySQL database will be accessible at `localhost:3307`.


## Project Structure

- **src/main/java/com/olifarhaan**: Contains the main application code, including configuration, controllers, services, and models.
- **src/test/python**: Contains Python scripts for testing the application.
- **Dockerfile**: Defines the Docker image for the Spring Boot application.
- **docker-compose.yml**: Defines the services for Docker Compose, including the application, MySQL, and Python testing environment.
- **build.gradle**: Gradle build configuration file.

## Key Components

### Spring Boot Application

- **Main Application**: `StarterSecurityApplication.java`
- **Security Configuration**: `WebSecurityConfig.java`
- **User Service**: `UserService.java`
- **Email Service**: `EmailService.java`

### Python Testing

- **Setup Helper**: `setup_helper.py`
- **API Helpers**: `api_helpers.py`
- **User Controller Tests**: `user_controller_test.py`

For the tests to run you have to install the vscode extension `littlefoxteam.vscode-python-test-adapter` & `hbenl.vscode-test-explorer`
After installing the extension you can run the tests by clicking on the test explorer button on the left sidebar and then clicking on the run button.

## OpenAPI Documentation

The application includes OpenAPI documentation, which can be accessed at `http://localhost:8080/swagger-ui.html`.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request for any improvements or bug fixes.

## License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.

## Contact

For any inquiries, please contact M. Ali Farhan at [alifarhan231087@gmail.com](mailto:alifarhan231087@gmail.com).