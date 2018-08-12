# uzabase-rss

Test project that accepts a RSS feed and populate data accordingly

## Technology Stack

- Spring Boot 1.5.x
- Angular > 6
- Mysql > 5.7


## Essential Setups.

Make sure followings are installed.

- Java JDk 1.8.x with Maven
- NodeJs > 8.6

### Setup mysql

```bash
CREATE DATABASE rss_test;
CREATE USER 'root'@'localhost' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON * . * TO 'root'@'localhost';
FLUSH PRIVILEGES;
```