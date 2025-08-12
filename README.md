# Template Feature Module

A modular Android library template implementing Clean Architecture with Jetpack Compose, designed for scalable feature development in multi-module applications.

## 🏗️ Architecture Overview

This module demonstrates a complete Clean Architecture implementation with:

- **API Module** (`library/api/`) - Public interface for external consumption
- **Implementation Module** (`library/`) - Full feature implementation with Clean Architecture layers
- **Sample App** (`sample-app/`) - Demonstration of module integration

```
template-feature/
├── library/api/          # Published API (consumer-facing)
├── library/              # Implementation (Clean Architecture)
│   ├── data/            # Data layer (repositories, data sources)
│   ├── domain/          # Domain layer (models, use cases)
│   ├── presentation/    # Presentation layer (ViewModels, UI)
│   └── di/              # Dependency injection
├── sample-app/          # Integration example
└── docs/                # Documentation
```

## 🚀 Quick Start

### For API Consumers

1. **Add dependency** to your app's `build.gradle.kts`:
```kotlin
implementation("com.example.modules:template-feature-api:1.0.15")
```

2. **Integrate navigation** in your app:
```kotlin
@Composable
fun AppNavigation() {
    NavHost(...) {
        // Your existing navigation
        
        // Add template feature navigation
        composable("template_feature") {
            TemplateFeatureNavigation()
        }
    }
}
```

### For Module Development

1. **Clone and setup**:
```bash
git clone <repository>
cd template-feature
./gradlew build
```

2. **Run sample app**:
```bash
./gradlew :sample-app:assembleDebug
./gradlew :sample-app:installDebug
```

3. **Run tests**:
```bash
./gradlew :library:testDebugUnitTest
```

## 📁 Module Structure

### Implementation Module (`library/`)

```
library/src/main/java/com/example/modules/template_feature/
├── data/                               # Data Layer
│   ├── dto/                           # Network DTOs
│   ├── localdatasource/               # Local data sources
│   │   ├── database/                  # Room database
│   │   └── preferences/               # SharedPreferences
│   ├── remotedatasource/              # Remote data sources
│   ├── mappers/                       # Data mappers
│   └── repositories/                  # Repository implementations
├── domain/                             # Domain Layer
│   ├── models/                        # Domain models
│   ├── repositories/                  # Repository interfaces
│   └── usecases/                      # Business logic use cases
├── presentation/                       # Presentation Layer
│   ├── screens/                       # Screen implementations
│   │   ├── main/                      # Main screen (list view)
│   │   └── detail/                    # Detail screen (CRUD)
│   ├── sharedcomponents/              # Reusable UI components
│   └── navigation/                    # Internal navigation
├── di/                                 # Dependency Injection
│   ├── DatabaseModule.kt              # Database dependencies
│   ├── NetworkModule.kt               # Network dependencies
│   ├── RepositoryModule.kt            # Repository bindings
│   └── UseCaseModule.kt               # Use case dependencies
└── ui/theme/                          # UI theming
```

### API Module (`library/api/`)

```
api/src/main/java/com/example/modules/template_feature/api/
├── TemplateFeatureModuleApi.kt         # Public API interface
├── TemplateFeatureModuleApiImpl.kt     # API implementation
├── TemplateFeatureModuleBuilder.kt     # Module builder
├── navigation/
│   └── TemplateFeatureNavigation.kt   # Public navigation entry
└── di/
    └── ApiModule.kt                    # Public DI module
```

## 🧪 Testing

Comprehensive unit test coverage includes:

- **Domain Layer**: Use case business logic
- **Data Layer**: Repository implementations with fallback scenarios  
- **Presentation Layer**: ViewModel state management
- **Data Mapping**: DTO/Entity/Model transformations

```bash
# Run all tests
./gradlew :library:testDebugUnitTest

# Run specific test categories
./gradlew :library:testDebugUnitTest --tests "*UseCase*"
./gradlew :library:testDebugUnitTest --tests "*Repository*" 
./gradlew :library:testDebugUnitTest --tests "*ViewModel*"
```

Test report available at: `library/build/reports/tests/testDebugUnitTest/index.html`

## 📦 Publishing

### Local Publishing
```bash
./gradlew :library:api:publishToMavenLocal
```

### Version Management
Update version in `library/api/build.gradle.kts`:
```kotlin
version = "1.0.16"  # Increment version
```

## 🔧 Development Workflow

1. **Feature Development**: Work in `library/` implementation module
2. **API Design**: Expose functionality through `library/api/`
3. **Testing**: Add comprehensive unit tests
4. **Integration**: Test with `sample-app/`
5. **Publishing**: Publish API module for consumption

## 📖 Documentation

- **[Development Guide](DEVELOPMENT.md)** - Step-by-step feature addition
- **[Architecture Details](ARCHITECTURE.md)** - Technical implementation details  
- **[API Documentation](API.md)** - Consumer integration guide

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Architecture**: Clean Architecture + MVVM
- **DI**: Hilt/Dagger
- **Database**: Room with KSP
- **Networking**: Retrofit + OkHttp
- **Testing**: JUnit 4, MockK, Turbine, Truth
- **Async**: Coroutines + Flow

## 🤝 Contributing

1. Follow existing Clean Architecture patterns
2. Add comprehensive unit tests for new features
3. Update API module when adding public functionality
4. Follow existing code conventions and naming

## 📄 License

[Add your license here]# android-template-feature
