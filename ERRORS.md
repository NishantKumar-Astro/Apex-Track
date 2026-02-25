# 🧠 Challenges & Solutions Encountered

Throughout the development of **ApexTrack** and **FinanceTracker**, I faced several interesting technical challenges. Here's a summary of the most significant ones and how they were resolved.

---

## 🔐 Security & Authentication

| Issue | Solution |
|-------|----------|
| **JWT tokens never expired** (expiration set to `System.currentTimeMillis() * 10000000`) | Fixed the expiration formula to `System.currentTimeMillis() + 1000 * 60 * 60 * 10` (10 hours). |
| **Password comparison failed in `deleteUser`** | The client was sending a JSON object `{ "password": "..." }` while the controller expected a raw `String`. Introduced a `PasswordRequest` DTO to correctly extract the password. |
| **Swagger UI couldn't send Bearer tokens** | Added an `OpenAPIConfig` with a security scheme of type `http` and scheme `bearer`. This added an **Authorize** button to Swagger – now tokens are included automatically. |
| **Login endpoint returned a plain string on error** | The controller returned `ResponseEntity.ok()` even for failure messages. Changed to return proper HTTP status codes (e.g., 401 Unauthorized) along with the error message. |

---

## 🗄️ Database & JPA

| Issue | Solution |
|-------|----------|
| **Deleting a user failed due to foreign key constraints** | The user had related `Transaction` records. Used `cascade = CascadeType.ALL` on the `@OneToMany` and loaded the collection (`user.getTransactions().size()`) before deletion to ensure Hibernate cascaded the delete. |
| **Deleting a user also deleted categories** | Removed the cascade attribute from the `@OneToMany` relationship between `Users` and `Category` (categories are independent). |
| **Auto‑generated IDs caused duplicate key errors after manual inserts** | Reset each sequence with `SELECT setval('table_id_seq', (SELECT MAX(id) FROM table))` after inserting test data. |
| **`@NotNull` fields were not being validated** | Added `@Valid` to `@RequestBody` parameters in controllers to trigger validation. |
| **Custom repository queries returning wrong results** | Verified that field names in `@Query` matched entity field names (e.g., `typeId` vs `type_id`). Used proper JPQL syntax. |

---

## 🚀 Deployment on Railway

| Issue | Solution |
|-------|----------|
| **Application failed to start with `UnknownHostException`** | The JDBC URL was missing the `jdbc:` prefix. Corrected to `jdbc:postgresql://...`. |
| **Environment variables not being resolved** | Railway provides `DATABASE_URL` without `jdbc:`. Used `spring.datasource.url=jdbc:${DATABASE_URL}` in `application.properties`. |
| **Port binding issues** | Added `server.port=${PORT:8080}` to bind to the port Railway assigns. |
| **Mixed Content error (HTTPS page calling HTTP API)** | Set `server.forward-headers-strategy=framework` and explicitly defined the server URL in OpenAPI config. |
| **Build failed because Railpack couldn't detect the app** | Set the **Root Directory** in Railway service settings to the folder containing `pom.xml` (e.g., `ApexTrack`). |

---

## 📦 API Design & DTOs

| Issue | Solution |
|-------|----------|
| **Inconsistent use of entities vs DTOs** | `createTransaction` used `TransactionRequest` DTO, but `updateTransaction` used the entity. Refactored `updateTransaction` to also accept a DTO, giving better control over updatable fields. |
| **Client had to send full nested objects for category/user** | Modified `TransactionRequest` to accept `categoryName`, `categoryType`, and `userId`. The service looks up the actual entities. |
| **YearMonth parameter in Swagger was not user‑friendly** | Added `@Parameter` annotation with description, example, and schema pattern – now Swagger shows a clear input with example. |

---

## 🧪 Testing & Debugging

| Issue | Solution |
|-------|----------|
| **No unit tests (due to time)** | Instead, I captured **Postman request/response screenshots** for all major endpoints. These serve as proof of functionality and are included in the repository. |
| **Unexpected 403 on root path** | The root `/` was not permit‑all in security config. Added `"/"` to the `requestMatchers`. |

---

## 🧰 Tools & Libraries

| Issue | Solution |
|-------|----------|
| **Springdoc OpenAPI not generating security scheme** | Created a custom `OpenAPI` bean with a `SecurityScheme` of type `http` and scheme `bearer`. |
| **Lombok annotations not working in IDE** | Installed Lombok plugin and enabled annotation processing. |
| **JJWT version conflicts** | Used consistent `0.12.6` for `jjwt-api`, `jjwt-impl`, and `jjwt-jackson`. |

---

## 📌 Final Notes

These challenges taught me a lot about Spring Boot internals, JPA, security, and cloud deployment. Each issue became a learning opportunity and helped me write more robust, maintainable code.
