#  Online Banking System

A traditional web-based banking application built with Spring MVC, Hibernate, and Thymeleaf. This system provides core banking services including user management, debit card operations, auto loans, and mortgage services through a server-side rendered web interface.


##  Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
- [Usage](#usage)
- [Business Rules](#business-rules)
- [Testing](#testing)
- [Contributing](#contributing)

##  Overview

This is a traditional server-side rendered web application for banking operations. The system uses Spring MVC for web layer, Hibernate for ORM, and Thymeleaf for view templating. It follows a classic layered architecture with clear separation of concerns.

**Note**: This is a traditional web application (not REST API), using server-side rendering with Thymeleaf templates.

##  Features

###  User Management
- User registration with passport validation
- User profile management
- Age validation (minimum 18 years)
- Passport number format validation (XX123456)
- Gender and birth date management

###  Debit Card Services
- Card issuance and management
- Balance operations (deposit, transfer)
- Card number uniqueness validation
- CVV and expiration date management
- Money transfer between cards
- Balance inquiry

### Auto Loan Services
- Loan application processing
- Loan payment functionality
- Term-based loan management (months)
- Current amount tracking
- Automatic loan closure when fully paid

### Mortgage Services
- Mortgage applications
- Fixed term options (10, 15, 20 years)
- Payment processing
- Current mortgage balance tracking
- Automatic closure when fully paid

### System Features
- Custom validation framework
- Transaction management
- Custom annotations (@MinAgeUser, @CurrentAmount, @ExpirationDate)
- DTO pattern implementation
- Comprehensive error handling

##  Technology Stack

### Backend Framework
- **Java 17** - Programming language
- **Spring MVC 6.2.6** - Web framework (traditional MVC, not REST)
- **Spring ORM 6.0.11** - Object-relational mapping integration
- **Hibernate 6.5.2** - ORM framework with Jakarta Persistence API

### Database
- **PostgreSQL 42.7.5** - Primary database
- **Hibernate Session Management** - Database session handling

### View Layer
- **Thymeleaf 3.1.3** - Server-side template engine
- **Jakarta Servlet API 6.0.0** - Servlet specifications

### Development & Quality
- **Lombok 1.18.36** - Code generation
- **JetBrains Annotations 24.0.1** - Code quality annotations
- **Custom Validation Framework** - Built-in validation system

### Testing
- **JUnit 5.12.2** - Unit testing framework
- **Mockito 5.10.0** - Mocking framework

### Build & Deployment
- **Maven** - Build automation
- **WAR packaging** - Web application archive deployment

## Architecture

The application follows a traditional 3-tier layered architecture: