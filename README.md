# Dependency Injector Framework

A lightweight dependency injection framework for **Java** applications with support for component lifecycle management, configuration injection, and task scheduling.

## Features

- Dependency injection via constructor and field injection
- Component lifecycle management with `@PostConstruct` and `@PreDestroy` hooks
- Configuration value injection from files and programmable sources
- Task scheduling with configurable delays and time units
- Lazy component initialization
- Automatic component scanning and registration
- Support for various configuration formats
- Bean definition through `@Configuration` classes
- Hierarchical component initialization with inheritance support

## Installation

Clone and build:

```bash
git clone https://github.com/kaba4cow/dependency-injector.git
cd dependency-injector
mvn clean install
```

Add to your `pom.xml`:

```
xml<dependency>
    <groupId>com.kaba4cow</groupId>
    <artifactId>dependency-injector</artifactId>
    <version>1.1.0</version>
</dependency>
```

Requirements: 

- **Java** version **8** or higher.

## Quick Start

Create your application configuration file (supports various formats):

```yaml
# config.yml
database:
  url: jdbc:mysql://localhost:3306/mydb
  username: root
  password: secret
```

Create a component:

```java
@Component
public class UserService {
    private final DatabaseService databaseService;
    
    @Value("database.url")
    private String databaseUrl;
    
    @Inject
    public UserService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }
    
    @PostConstruct
    public void initialize() {
        // Initialization logic
    }
    
    @PreDestroy
    public void cleanup() {
        // Cleanup logic
    }
}
```

Initialize the application context:

```java
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext("com.example", "config.yml");
        UserService userService = context.getComponent(UserService.class);
        
        // Or without a config file
        // ApplicationContext context = new ApplicationContext("com.example");
    }
}
```

## Core Annotations

### Component Annotations

#### @Component

Marks a class as a managed component:

```java
@Component
public class MyService {
    // Component implementation
}
```

#### @Lazy

Enables lazy initialization of components:

```java
@Component
@Lazy
public class ExpensiveService {
    // This component will only be initialized when first requested
}
```

#### @PostConstruct

Marks methods to be executed after dependency injection is complete:

```java
@Component
public class MyService {
    @PostConstruct
    public void initialize() {
        // Initialization logic
    }
}
```

#### @PreDestroy

Marks methods to be executed before the component is destroyed:

```java
@Component
public class MyService {
    @PreDestroy
    public void cleanup() {
        // Cleanup logic
    }
}
```

### Dependency Annotations

#### @Inject

Injects dependencies through constructors or fields:

```java
@Component
public class UserService {
    @Inject
    private DatabaseService databaseService;
    
    // Or via constructor
    @Inject
    public UserService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }
}
```

#### @Value

Injects configuration values:

```java
@Component
public class ConfigurationService {
    @Value("app.name")
    private String appName;
    
    @Value("app.port")
    private int port;
}
```

#### @Configuration

Marks a class as a configuration provider:

```java
@Configuration
public class AppConfig {
    @Bean
    public DataSource dataSource(@Value("database.url") String url, 
                               @Value("database.username") String username,
                               @Value("database.password") String password) {
        DataSource ds = new SimpleDataSource();
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }
    
    @Value("app.max-threads")
    public int maxThreads() {
        return Runtime.getRuntime().availableProcessors() * 2;
    }
}
```

#### @Bean

Marks a method in a `@Configuration` class as a bean factory:

```java
@Configuration
public class AppConfig {
    @Bean
    public EmailService emailService() {
        return new SMTPEmailService();
    }
}
```

### Scheduler Annotations

#### @Scheduled

Schedules periodic task execution:

```java
@Component
public class ScheduledTasks {
    @Scheduled(initialDelay = 5, fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void performTask() {
        // Task logic
    }
}
```

## Component Lifecycle

1. **Construction**: Components are instantiated using either the default constructor or an `@Inject` annotated constructor
2. **Dependency Injection**: Field-level dependencies are injected
3. **Post-Construction**: `@PostConstruct` methods are called (respecting inheritance hierarchy)
4. **Usage**: Component is ready for use
5. **Pre-Destruction**: `@PreDestroy` methods are called during context shutdown (respecting inheritance hierarchy)

## Configuration Support

The framework supports various configuration formats through the `ConfigLoaderFactory`:

- `Properties`
- `JSON`
- `YAML`

Configuration values can be provided from two sources:

1. External configuration files loaded at startup
2. `@Value`-annotated methods in `@Configuration` classes

When the same key exists in both sources, the value from `@Configuration` classes takes precedence.

Configuration values support the following types:

- Primitive types: `int`, `long`, `float`, `double`, `boolean`
- Wrapper types: `Integer`, `Long`, `Float`, `Double`, `Boolean`
- `String`
- `List`
- `enum` types (using string representation)

## Task Scheduling

The framework includes a built-in task scheduler that supports:

- Fixed-delay execution
- Configurable initial delays
- Custom time units (seconds, minutes, hours, etc.)
- Automatic thread pool sizing based on available processors

## API Reference

### ApplicationContext

The main entry point for application configuration:

```java
// Create with configuration file
ApplicationContext context = new ApplicationContext("com.example", "config.yml");

// Get a component
MyService service = context.getComponent(MyService.class);

// Get a component (optional)
Optional<MyService> service = context.optComponent(MyService.class);

// Get configuration value
String value = context.getConfigValue(String.class, "some.key");

// Get configuration value (optional)
Optional<String> value = context.optConfigValue(String.class, "some.key");

// Properly close the context (automatically registered as shutdown hook)
context.close();
```

## Error Handling

The framework includes error handling for common scenarios:

- Missing dependencies
- Configuration errors
- Lifecycle method exceptions
- Scheduling errors
- Bean creation failures
- Duplicate bean definitions

## License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.