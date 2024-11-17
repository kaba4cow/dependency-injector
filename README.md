# Dependency Injector Framework

A lightweight dependency injection framework for **Java** applications with support for component lifecycle management, configuration injection, and task scheduling.

## Features

- Dependency injection via constructor and field injection
- Component lifecycle management with `@PostConstruct` and `@PreDestroy` hooks
- Configuration value injection
- Task scheduling with configurable delays
- Lazy component initialization
- Automatic component scanning and registration
- Support for various configuration formats

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
    }
}
```

## Core Annotations

### @Component

Marks a class as a managed component:

```
@Component
public class MyService {
    // Component implementation
}
```

### @Inject

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

### @Value

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

### @Lazy

Enables lazy initialization of components:

```java
@Component
@Lazy
public class ExpensiveService {
    // This component will only be initialized when first requested
}
```

### @Scheduled

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
3. **Post-Construction**: `@PostConstruct` methods are called
4. **Usage**: Component is ready for use
5. **Pre-Destruction**: `@PreDestroy` methods are called during context shutdown

## Configuration Support

The framework supports various configuration formats through the `ConfigLoaderFactory`:

- `Properties`
- `JSON`
- `YAML`

Configuration values can be injected using the `@Value` annotation and support the following types:

- Primitive types: `int`, `long`, `float`, `double`, `boolean`
- `String`
- `List`
- `enum`

## Task Scheduling

The framework includes a built-in task scheduler that supports:

- Fixed-delay execution
- Configurable initial delays
- Custom time units

## Best Practices

1. **Constructor Injection**: Prefer constructor injection over field injection for required dependencies
2. **Lifecycle Methods**: Use `@PostConstruct` for initialization logic and `@PreDestroy` for cleanup
3. **Configuration**: Keep configuration values in external config files
4. **Component Scanning**: Organize components in a clear package structure
5. **Lazy Loading**: Use `@Lazy` for components that are expensive to create and not always needed

## Error Handling

The framework includes error handling for common scenarios:

- Missing dependencies
- Configuration errors
- Lifecycle method exceptions
- Scheduling errors