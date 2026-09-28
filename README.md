# E-Commerce Management System

A Java console application for managing buyers, sellers, products, shopping carts, and orders, with persistent storage in PostgreSQL through JDBC.

Originally built as an object-oriented programming project using in-memory arrays, the application was later adapted to use a relational database. The project combines Java domain models and console interaction with SQL queries and database-backed operations.

## Features

- Register buyers and sellers, including address details for buyers.
- Add products for sale with a price, category, and optional special packaging cost.
- Browse a seller's products and filter products by category.
- Add products to shopping carts and track quantities.
- Calculate cart totals and create orders during checkout.
- View order history, including product details recorded at purchase time.
- Rebuild a shopping cart from a previous order.
- Validate console input and handle database errors.

## Technologies

- **Java** - application logic, domain classes, and console interface
- **PostgreSQL** - relational data storage
- **JDBC** - database connections, SQL execution, and result processing
- **PostgreSQL JDBC driver 42.7.11** - included in `lib/`

## Code Organization

| File | Responsibility |
| --- | --- |
| `Main.java` | Console menus, user input, and application flow |
| `Manager.java` | Database operations for users, products, carts, and orders |
| `DatabaseConnection.java` | PostgreSQL connection configuration |
| `Product.java` | Product model and category enumeration |
| `Address.java` | Buyer address model |
| `lib/postgresql-42.7.11.jar` | PostgreSQL JDBC driver |
| `.vscode/settings.json` | VS Code configuration for the libraries in `lib/` |

## Database

The application uses the following tables:

| Tables | Purpose |
| --- | --- |
| `users`, `buyers`, `sellers` | User accounts and buyer/seller records |
| `addresses` | Buyer addresses |
| `products` | Products offered by sellers |
| `shopping_cart_items` | Cart contents and quantities |
| `orders`, `order_items` | Orders and product details at purchase time |

**Database setup scripts are not yet included.** Running the database features requires the original schema and any associated database logic. Creating an empty database or tables with only the names above is not sufficient.

## Getting Started

### Requirements

- JDK 17 or newer
- A running PostgreSQL instance with the project schema installed
- Git, if cloning from the command line

### 1. Clone the repository

```bash
git clone https://github.com/ofekX/ecommerce-management-system.git
cd ecommerce-management-system
```

### 2. Configure the database connection

The connection settings are in `DatabaseConnection.java`:

```java
String dbUrl = "jdbc:postgresql://localhost:5432/ecommerce_db";
String username = "postgres";
String password = "ENTER_YOUR_PASSWORD_HERE";
```

Adjust these values for your local PostgreSQL instance. Keep real credentials local and do not commit them. The repository contains a password placeholder; environment-based configuration is a possible future improvement.

### 3. Compile and run

Run these commands from the repository root.

**Windows (PowerShell or Command Prompt):**

```shell
javac -cp "lib/*" -d out *.java
java -cp "out;lib/*" Main
```

**macOS / Linux:**

```shell
javac -cp "lib/*" -d out *.java
java -cp "out:lib/*" Main
```

The application displays a numbered menu for registering users, managing products, shopping, checking out, and viewing previous orders.

## Project Scope

This is an academic console application with a simulated checkout flow. It does not process real payments and is not intended for production use. The Java code passes account passwords directly to the database; production use would require password hashing, authentication, and authorization controls.

## Possible Improvements

- Include a reproducible database schema and fictional sample data.
- Move database credentials to environment variables.
- Use Maven or Gradle to manage dependencies.
- Add automated tests for database operations and checkout behavior.
- Introduce secure account authentication and password hashing.
