# MapperAnalytics
Provides vanity mapping for referral URLs to sites that don't have short and easy to remember URLs

## Getting Started
MapperAnalytics uses Spring Boot, making the additional extension configurable through the configuration.  By default, MySQL is the database, Hikari for connection pooling, and Hibernate for JPA.

For encryption, JSypt is used.  You will want to setup locally in order to encrypt passwords used for Spring Security, API keys, and database connection passwords.
