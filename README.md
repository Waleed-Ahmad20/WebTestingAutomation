# Web Testing Automation

A Software Quality Engineering (SQE) project covering multiple automated testing disciplines, including UI automation, unit testing, Web API testing, performance testing, and automated test case generation.

📹 **Demo Recording:** [Loom](https://www.loom.com/share/6742df16b18748dd88c810b07918724e?sid=f577acbb-a4a5-4231-bb9e-36fe98a42560)

---

## Project Structure

```
SQE_Project/
├── Task1/
│   ├── UIAutomation/                        # Selenium + Cucumber BDD UI tests
│   ├── Unit-Testing-main/                   # JUnit 5 + Mockito unit tests
│   └── WebAPIandPerformanceTestingAutomation/
│       ├── WebAPITestAutomation/            # RestAssured API tests
│       └── PerformanceTestAutomation/       # JMeter load tests
└── Task2/
    └── AITestScript.py                      # Automated test case generation tool
```

---

## Task 1 – Automated Testing Suite

### 1. UI Automation (Selenium + Cucumber BDD)

End-to-end browser tests for [SauceDemo](https://www.saucedemo.com/) using the **Page Object Model** pattern with **Cucumber BDD** scenarios and **Allure** reporting.

**Technologies:** Selenium WebDriver, Cucumber, JUnit 5, Allure, Gradle

**Test scenarios covered:**
- User login
- Add product to cart
- Checkout flow

**Run the tests:**
```bash
cd SQE_Project/Task1/UIAutomation/UI_Automation-main
./gradlew test
```

**View the Allure report:**
```bash
allure serve app/allure-results
```

---

### 2. Unit Testing (JUnit 5 + Mockito)

Unit tests for a file management application following a layered architecture (BL / DAL / DTO / PL) with Mockito mocks for the data access layer.

**Technologies:** Java, JUnit 5, Mockito

**Test classes:**
| File | Description |
|------|-------------|
| `BOTesting.java` | Business Object layer tests (create, read, update, delete, search, transliterate) |
| `DAOTesting.java` | Data Access Object layer tests |
| `POTesting.java` | Presentation Object layer tests |
| `mockitoTest.java` | Mockito integration tests |

---

### 3. Web API Testing (RestAssured)

REST API tests targeting the [reqres.in](https://reqres.in) public API using **RestAssured** with a Swing-based GUI to run individual test cases interactively.

**Technologies:** Java, RestAssured, JUnit 5, Gradle

**Test classes:**
| File | Coverage |
|------|----------|
| `UserApiTests.java` | List users, Create user, Update user, Delete user |
| `ResourceApiTests.java` | List resources, Get single resource |

**Run the tests:**
```bash
cd SQE_Project/Task1/WebAPIandPerformanceTestingAutomation/WebAPITestAutomation/GradleProjects
./gradlew test
```

---

### 4. Performance Testing (JMeter)

JMeter load test plan for the User API endpoint.

**Test plan:** `PerformanceTestAutomation/UserApiLoadTest.jmx`

**Run the test plan:**
```bash
jmeter -n -t UserApiLoadTest.jmx -l results.jtl
```

---

## Task 2 – Test Case Generation Tool

A Python desktop application that reads a Java source file and generates **JUnit 5 test cases** for all functions using a large-language-model inference API.

**Technologies:** Python, Tkinter, Hugging Face Inference API (`Qwen/Qwen2.5-Coder-32B-Instruct`)

**How to use:**
1. Install the required package:
   ```bash
   pip install huggingface_hub
   ```
2. Run the tool:
   ```bash
   python SQE_Project/Task2/AITestScript.py
   ```
3. Click **Browse File**, select a `.java` source file, and the generated test cases will be displayed and saved alongside the original file.

---

## Prerequisites

| Tool | Version |
|------|---------|
| Java | 11 or higher |
| Gradle | Bundled via wrapper (`./gradlew`) |
| Microsoft Edge + EdgeDriver | Matching versions |
| Apache JMeter | 5.x |
| Python | 3.8 or higher |
| Allure CLI | 2.x (for report generation) |

---

## Authors

- **22I2647**
- **22I2642**
