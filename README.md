# Subdub Backend

**Subdub** is a subscription tracking backend service, built in **Java with Spring Boot**, that helps users monitor and manage their recurring subscriptions. It notifies users when subscriptions are about to expire so they can cancel before auto-renewal, preventing unwanted charges.

## 🚀 Features

- **User Authentication**

    - Sign up / Sign in with **username & password**
    - **JWT**-based authentication (Access & Refresh tokens)
    - **OAuth2** support (e.g., Google, GitHub)

- **CRUD Operations**

    - **Users**: Create, read, update, delete user profiles
    - **Subscriptions**: Register, view, update, and remove subscriptions

- **Automated Expiry Checks**

    - **Cron-style job** implemented using Spring Boot **scheduling**
    - Daily scans for subscriptions nearing expiry
    - Sends **email notifications** to users

- **Extensible & Secure**
    - Layered architecture for easy feature additions
    - Secure password hashing and token handling
    - Environment-based configuration

## 🏗️ Tech Stack

- **Language:** Java
- **Web Framework:** Spring Boot
- **Authentication:** JWT and OAuth2
- **Database:** PostgreSQL (via Hibernate and Spring Data Jpa)
- **Email:** SMTP via Java’s `JavaEmailSender`
- **Scheduling:** Spring Boot **scheduling**

## 🔄 Scheduled Tasks

A daily job runs at 8am:

- Scans subscriptions expiring in the next 1, 3, 5, 7 days
- Sends reminder emails to users

## 🛡️ Security

- **Password Hashing:** bcrypt
- **Token Storage:** In-memory or persistent store (Redis) for refresh tokens
- **Rate Limiting:** Optional middleware

## 🤝 Related

- **Subdub Frontend:** https://github.com/sangtandoan/subdub_frontend

---

_Happy Tracking!_ 🚀
