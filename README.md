# Personal Notes App

A robust and secure web application for managing personal notes, tasks, and events. This application helps users organize their daily activities, create and manage notes, and plan their schedule effectively.

## 🚀 Features

- Secure user authentication and authorization
- Create, read, update, and delete personal notes
- Task management with status tracking
- Event planning and scheduling
- Responsive and user-friendly interface

## 🛠 Technologies

### Backend
- **Java 21**
- **Spring Boot 3.5.0**
  - Spring Security for authentication and authorization
  - Spring Data JPA for data persistence
  - Spring Validation for input validation
  - Spring Actuator for application monitoring
- **PostgreSQL** as the database
- **Resilience4j** for circuit breaking and fault tolerance
- **MapStruct** for object mapping
- **Lombok** for reducing boilerplate code
- **Maven** for dependency management and build automation

### DevOps & Tools
- **Docker** for containerization
- **Docker Compose** for multi-container deployment
- **Spring Boot DevTools** for development efficiency

## 🏗 Architecture

The application follows a modern, scalable architecture based on these principles:

1. **Layered Architecture**
   - Controller Layer (REST APIs)
   - Service Layer (Business Logic)
   - Repository Layer (Data Access)
   - Domain Layer (Entities and DTOs)

2. **Security**
   - JWT-based authentication
   - Role-based access control
   - Secure password handling

3. **Best Practices**
   - Clean Code principles
   - SOLID principles
   - RESTful API design
   - Exception handling
   - Input validation

## 🚦 Getting Started

1. Clone the repository
2. Ensure you have Java 21 and Maven installed
3. Run `docker-compose up` to start the PostgreSQL database
4. Run `mvn spring-boot:run` to start the application
5. Access the application at `http://localhost:8080`

## 🔜 Future Improvements

1. **Enhanced Features**
   - [ ] Note categorization and tagging system
   - [ ] Rich text editor for notes
   - [ ] File attachments support
   - [ ] Calendar integration
   - [ ] Reminder system

2. **Technical Enhancements**
   - [ ] Implementation of caching mechanism
   - [ ] Full-text search functionality
   - [ ] API rate limiting
   - [ ] Enhanced monitoring and logging
   - [ ] Performance optimization

3. **User Experience**
   - [ ] Mobile application development
   - [ ] Dark/Light theme support
   - [ ] Customizable dashboard
   - [ ] Note sharing capabilities
   - [ ] Export/Import functionality

4. **Infrastructure**
   - [ ] CI/CD pipeline implementation
   - [ ] Kubernetes deployment support
   - [ ] Automated testing improvements
   - [ ] Monitoring and alerting system
   - [ ] Backup and recovery system

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
