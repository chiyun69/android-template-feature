# Architecture Documentation

This document provides detailed technical information about the Template Feature module architecture, design decisions, and implementation patterns.

## 🏗️ Architecture Overview

The Template Feature module implements **Clean Architecture** principles with clear separation of concerns across multiple layers and modules.

```
┌─────────────────────────────────────────────────────────────────────┐
│                        Template Feature Module                      │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌─────────────────┐         ┌──────────────────────────────────┐   │
│  │   API Module    │         │      Implementation Module       │   │
│  │   (Published)   │   ────▶ │         (Internal)               │   │
│  │                 │         │                                  │   │
│  │ • Navigation    │         │ ┌──────────────────────────────┐ │   │
│  │ • Public API    │         │ │     Presentation Layer       │ │   │
│  │ • DI Setup      │         │ │ • ViewModels                 │ │   │
│  └─────────────────┘         │ │ • Compose Screens            │ │   │
│                              │ │ • Navigation                 │ │   │
│                              │ │ • UI State Management        │ │   │
│                              │ └──────────────────────────────┘ │   │
│                              │              ↓                   │   │
│                              │ ┌──────────────────────────────┐ │   │
│                              │ │       Domain Layer           │ │   │
│                              │ │ • Use Cases                  │ │   │
│                              │ │ • Domain Models              │ │   │
│                              │ │ • Repository Interfaces      │ │   │
│                              │ └──────────────────────────────┘ │   │
│                              │              ↓                   │   │
│                              │ ┌──────────────────────────────┐ │   │
│                              │ │        Data Layer            │ │   │
│                              │ │ • Repository Implementations │ │   │
│                              │ │ • Data Sources (Local/Remote)│ │   │
│                              │ │ • DTOs & Entities            │ │   │
│                              │ │ • Data Mappers               │ │   │
│                              │ └──────────────────────────────┘ │   │
│                              └──────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

## 📁 Module Structure

### Dual-Module Architecture

The project uses a **dual-module approach** for clean API boundaries:

```
template-feature/
├── library/api/              # API Module (Published to Maven)
│   └── src/main/java/        
│       └── com/example/modules/template_feature/api/
│           ├── TemplateFeatureModuleApi.kt          # Public interface
│           ├── TemplateFeatureModuleApiImpl.kt      # API implementation  
│           ├── TemplateFeatureModuleBuilder.kt      # Module builder
│           ├── navigation/
│           │   └── TemplateFeatureNavigation.kt     # Public navigation
│           └── di/
│               └── ApiModule.kt                     # Public DI setup
│
└── library/                  # Implementation Module (Internal)
    └── src/main/java/
        └── com/example/modules/template_feature/
            ├── data/                               # Data Layer
            ├── domain/                             # Domain Layer  
            ├── presentation/                       # Presentation Layer
            ├── di/                                 # Dependency Injection
            └── ui/                                 # UI Resources
```

### Benefits of Dual-Module Design

1. **Clear API Surface** - Only intended public functionality exposed
2. **Implementation Hiding** - Internal classes protected from external access
3. **Versioning Control** - API and implementation can evolve independently
4. **Dependency Management** - Consumers get clean dependency graph
5. **ProGuard Protection** - Implementation classes obfuscated in release builds

## 🎯 Clean Architecture Implementation

### Layer Responsibilities

#### 1. Domain Layer (Business Logic Core)

**Location**: `library/src/main/java/.../domain/`

**Responsibilities**:
- Define business models and entities
- Implement business rules through use cases
- Define repository contracts (interfaces)
- No dependencies on external frameworks

**Components**:

```kotlin
// Domain Models - Pure business entities
data class TemplateFeatureModel(
    val id: String,
    val title: String,
    val description: String,
    val isActive: Boolean,
    val createdAt: String
)

// Repository Interfaces - Data access contracts
interface TemplateFeatureRepository {
    suspend fun getAllTemplateFeatures(): Flow<List<TemplateFeatureModel>>
    suspend fun createTemplateFeature(feature: TemplateFeatureModel): Result<TemplateFeatureModel>
    // ... other operations
}

// Use Cases - Business logic operations
class CreateTemplateFeatureUseCase @Inject constructor(
    private val repository: TemplateFeatureRepository
) {
    suspend operator fun invoke(feature: TemplateFeatureModel): Result<TemplateFeatureModel> {
        // Business validation logic
        return repository.createTemplateFeature(feature)
    }
}
```

#### 2. Data Layer (External Data Management)

**Location**: `library/src/main/java/.../data/`

**Responsibilities**:
- Implement repository interfaces from domain layer
- Manage data sources (local database, remote API)
- Handle data transformation and mapping
- Manage caching and synchronization

**Components**:

```kotlin
// DTOs - Network data transfer objects  
@Serializable
data class TemplateFeatureResponseDto(
    val id: String,
    val title: String,
    // ... other fields
)

// Entities - Database representations
@Entity(tableName = "template_features")
data class TemplateFeatureEntity(
    @PrimaryKey val id: String,
    val title: String,
    // ... other fields
)

// Data Sources
interface TemplateFeatureApiService {
    @GET("features")
    suspend fun getAllFeatures(): List<TemplateFeatureResponseDto>
}

@Dao
interface TemplateFeatureDao {
    @Query("SELECT * FROM template_features")
    fun getAllFeatures(): Flow<List<TemplateFeatureEntity>>
}

// Repository Implementation
class TemplateFeatureRepositoryImpl @Inject constructor(
    private val apiService: TemplateFeatureApiService,
    private val dao: TemplateFeatureDao
) : TemplateFeatureRepository {
    // Implementation with local/remote coordination
}
```

#### 3. Presentation Layer (UI and State Management)

**Location**: `library/src/main/java/.../presentation/`

**Responsibilities**:
- Manage UI state and user interactions
- Coordinate with domain use cases
- Handle UI navigation
- Manage loading/error/success states

**Components**:

```kotlin
// UI State Models
data class MainState(
    val isLoading: Boolean = false,
    val templateFeatures: List<TemplateFeatureModel> = emptyList(),
    val error: String? = null
)

// ViewModels - State management and business logic coordination
@HiltViewModel  
class MainViewModel @Inject constructor(
    private val getAllFeaturesUseCase: GetAllTemplateFeaturesUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()
    
    // UI event handling methods
}

// Compose Screens - UI implementation
@Composable
fun MainScreen(
    onNavigateToDetail: (String) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    // UI rendering based on state
}
```

## 🔄 Data Flow Architecture

### Unidirectional Data Flow

The architecture enforces unidirectional data flow following the **MVI (Model-View-Intent)** pattern:

```
┌─────────────┐    User Actions    ┌─────────────┐    Use Cases    ┌─────────────┐
│   Compose   │ ─────────────────▶ │  ViewModel  │ ──────────────▶ │   Domain    │
│   Screen    │                    │             │                 │  Use Cases  │
│             │                    │             │                 │             │
│             │ ◀───── State ───── │             │ ◀─── Result ─── │             │
└─────────────┘                    └─────────────┘                 └─────────────┘
                                          │                               │
                                          │                               ▼
                                          │                    ┌─────────────┐
                                          │                    │ Repository  │
                                          │                    │ Interface   │
                                          │                    │             │
                                          │                    │             │
                                          │                    └─────────────┘
                                          │                               │
                                          │                               ▼
                                          │                    ┌─────────────┐
                                          │                    │ Repository  │
                                          │                    │    Impl     │
                                          │                    │             │
                                          │                    │             │
                                          │                    └─────────────┘
                                          │                          │     │
                                          │                          │     │
                                ┌─────────▼────────┐        ┌────────▼──┐  │
                                │                  │        │   Local   │  │
                                │   Local Cache    │        │ Database  │  │
                                │                  │        │           │  │
                                └──────────────────┘        └───────────┘  │
                                                                           │
                                                                 ┌─────────▼─────────┐
                                                                 │   Remote API      │
                                                                 │                   │
                                                                 │                   │
                                                                 └───────────────────┘
```

### Data Flow Sequence

1. **User Interaction** → Compose UI triggers action
2. **Action Processing** → ViewModel receives and processes action  
3. **Business Logic** → ViewModel invokes appropriate use case
4. **Repository Call** → Use case calls repository interface
5. **Data Retrieval** → Repository implementation coordinates local/remote data
6. **State Update** → Repository returns data, ViewModel updates state
7. **UI Re-composition** → Compose UI re-renders based on new state

## 🧩 Dependency Injection Architecture

### Hilt Module Structure

The project uses **Hilt** for dependency injection with modular configuration:

```kotlin
// Component Hierarchy
@Singleton ApplicationComponent
    ↓
@ActivityRetainedScope ViewModelComponent  
    ↓  
@ActivityScope ActivityComponent
    ↓
@FragmentScope FragmentComponent
```

### Module Organization

```kotlin
// Database Module - Singleton scope
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TemplateFeatureDatabase {
        return Room.databaseBuilder(
            context,
            TemplateFeatureDatabase::class.java,
            "template_feature_db"
        ).build()
    }
    
    @Provides
    fun provideTemplateFeatureDao(database: TemplateFeatureDatabase): TemplateFeatureDao {
        return database.templateFeatureDao()
    }
}

// Network Module - Singleton scope  
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
    
    @Provides
    @Singleton
    fun provideTemplateFeatureApiService(retrofit: Retrofit): TemplateFeatureApiService {
        return retrofit.create(TemplateFeatureApiService::class.java)
    }
}

// Repository Module - Singleton scope
@Module
@InstallIn(SingletonComponent::class) 
abstract class RepositoryModule {
    
    @Binds
    abstract fun bindTemplateFeatureRepository(
        templateFeatureRepositoryImpl: TemplateFeatureRepositoryImpl
    ): TemplateFeatureRepository
}

// Use Case Module - ViewModel scope
@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {
    
    @Provides
    fun provideGetAllTemplateFeaturesUseCase(
        repository: TemplateFeatureRepository
    ): GetAllTemplateFeaturesUseCase {
        return GetAllTemplateFeaturesUseCase(repository)
    }
}
```

## 💾 Data Management Strategy

### Multi-Source Data Strategy

The architecture implements a **multi-source data strategy** with the following priorities:

1. **Local Database** (Primary) - Room database for offline-first experience
2. **Remote API** (Secondary) - REST API for data synchronization  
3. **In-Memory Cache** (Tertiary) - For frequently accessed data

### Repository Pattern Implementation

```kotlin
class TemplateFeatureRepositoryImpl @Inject constructor(
    private val apiService: TemplateFeatureApiService,
    private val dao: TemplateFeatureDao,
    private val preferences: TemplateFeaturePreferences
) : TemplateFeatureRepository {

    override suspend fun getAllTemplateFeatures(): Flow<List<TemplateFeatureModel>> {
        // Always return local data first (offline-first)
        return dao.getAllTemplateFeatures().map { entities ->
            entities.toDomainModelListFromEntity()
        }
    }

    override suspend fun syncWithRemote(): Result<Unit> {
        return try {
            // Fetch fresh data from API
            val remoteFeatures = apiService.getAllTemplateFeatures()
            val entities = remoteFeatures.toEntityListFromDto()
            
            // Replace local data with remote data
            dao.deleteAllTemplateFeatures()
            dao.insertTemplateFeatures(entities)
            
            // Update sync timestamp
            preferences.setLastSyncTime(System.currentTimeMillis())
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createTemplateFeature(
        feature: TemplateFeatureModel
    ): Result<TemplateFeatureModel> {
        return try {
            // Try remote creation first
            val requestDto = feature.toRequestDto()
            val responseDto = apiService.createTemplateFeature(requestDto)
            val domainModel = responseDto.toDomainModel()
            
            // Save to local database
            dao.insertTemplateFeature(responseDto.toEntity())
            
            Result.success(domainModel)
        } catch (e: Exception) {
            // Fallback to local creation with offline indicator
            try {
                val localFeature = feature.copy(
                    id = if (feature.id.isEmpty()) generateLocalId() else feature.id
                )
                dao.insertTemplateFeature(localFeature.toEntity())
                Result.success(localFeature)
            } catch (localException: Exception) {
                Result.failure(localException)
            }
        }
    }
}
```

### Caching Strategy

- **L1 Cache**: Compose State (ViewModels) - Short-term UI state
- **L2 Cache**: Room Database - Persistent local storage  
- **L3 Cache**: Remote API - Source of truth for distributed data

## 🔐 Security Architecture

### API Protection

The module implements several security measures:

#### 1. ProGuard/R8 Obfuscation

**Consumer Rules** (`consumer-rules.pro`):
```proguard
# Keep public API classes
-keep public class com.example.modules.template_feature.api.** { *; }

# Obfuscate implementation classes
-keep,allowobfuscation,allowshrinking class com.example.modules.template_feature.data.** { *; }
-keep,allowobfuscation,allowshrinking class com.example.modules.template_feature.domain.** { *; }
-keep,allowobfuscation,allowshrinking class com.example.modules.template_feature.presentation.** { *; }

# Apply obfuscated package names
-repackageclasses 'obfuscated'
```

#### 2. Module Boundary Protection

- **API Module**: Only exposes necessary public interfaces
- **Implementation Module**: Internal classes are not published
- **ProGuard Rules**: Implementation classes renamed/obfuscated in release builds

#### 3. Data Protection

```kotlin
// Sensitive data encryption at rest
@Entity(tableName = "template_features")
data class TemplateFeatureEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    // Sensitive fields can be encrypted
    @Encrypted val apiKey: String? = null
)

// Network security
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) BODY else NONE
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}
```

## 🧪 Testing Architecture

### Testing Strategy by Layer

The architecture supports comprehensive testing at each layer:

#### 1. Domain Layer Testing

```kotlin
// Pure unit tests - no Android dependencies
class CreateTemplateFeatureUseCaseTest {
    
    @Mock private lateinit var repository: TemplateFeatureRepository
    private lateinit var useCase: CreateTemplateFeatureUseCase
    
    @Test
    fun `should create feature when valid input provided`() = runTest {
        // Given
        val feature = TemplateFeatureModel(...)
        coEvery { repository.createTemplateFeature(feature) } returns Result.success(feature)
        
        // When  
        val result = useCase(feature)
        
        // Then
        assertThat(result.isSuccess).isTrue()
    }
}
```

#### 2. Data Layer Testing

```kotlin
// Integration tests with mocked external dependencies
class TemplateFeatureRepositoryImplTest {
    
    @Mock private lateinit var apiService: TemplateFeatureApiService
    @Mock private lateinit var dao: TemplateFeatureDao
    private lateinit var repository: TemplateFeatureRepositoryImpl
    
    @Test
    fun `should return data from local database`() = runTest {
        // Given
        val entities = listOf(TemplateFeatureEntity(...))
        every { dao.getAllTemplateFeatures() } returns flowOf(entities)
        
        // When
        repository.getAllTemplateFeatures().test {
            // Then
            val result = awaitItem()
            assertThat(result).hasSize(1)
        }
    }
}
```

#### 3. Presentation Layer Testing

```kotlin
// ViewModel testing with coroutine testing utilities  
@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    
    @Mock private lateinit var getAllFeaturesUseCase: GetAllTemplateFeaturesUseCase
    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()
    
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }
    
    @Test
    fun `should load features on initialization`() = runTest {
        // Given
        val features = listOf(TemplateFeatureModel(...))
        coEvery { getAllFeaturesUseCase() } returns flowOf(features)
        
        // When
        viewModel = MainViewModel(getAllFeaturesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.templateFeatures).isEqualTo(features)
        }
    }
}
```

### Test Doubles Strategy

- **Mocks**: For external dependencies (API services, databases)
- **Fakes**: For complex components that need realistic behavior
- **Stubs**: For simple return values
- **Test Doubles**: Repository implementations for testing

## 🚀 Performance Architecture

### Key Performance Optimizations

#### 1. Lazy Loading and Initialization

```kotlin
// Lazy dependency injection
class TemplateFeatureRepositoryImpl @Inject constructor(
    private val apiService: dagger.Lazy<TemplateFeatureApiService>,
    private val dao: TemplateFeatureDao
) : TemplateFeatureRepository {
    
    override suspend fun syncWithRemote(): Result<Unit> {
        return try {
            // API service is created only when needed
            val remoteFeatures = apiService.get().getAllTemplateFeatures()
            // ... rest of implementation
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

#### 2. Database Optimization

```kotlin
@Database(
    entities = [TemplateFeatureEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TemplateFeatureDatabase : RoomDatabase() {
    
    abstract fun templateFeatureDao(): TemplateFeatureDao
    
    companion object {
        @Volatile
        private var INSTANCE: TemplateFeatureDatabase? = null
        
        fun getDatabase(context: Context): TemplateFeatureDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TemplateFeatureDatabase::class.java,
                    "template_feature_database"
                )
                .addMigrations(MIGRATION_1_2)  // Planned migrations
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// Optimized DAO queries
@Dao
interface TemplateFeatureDao {
    
    @Query("""
        SELECT * FROM template_features 
        WHERE isActive = 1 
        ORDER BY createdAt DESC 
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getActiveFeaturesPaged(limit: Int, offset: Int): List<TemplateFeatureEntity>
    
    @Query("SELECT COUNT(*) FROM template_features WHERE isActive = 1")
    suspend fun getActiveFeatureCount(): Int
}
```

#### 3. Memory Management

```kotlin
// ViewModel with proper resource management
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllFeaturesUseCase: GetAllTemplateFeaturesUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()
    
    private var loadJob: Job? = null
    
    fun loadFeatures() {
        loadJob?.cancel()  // Cancel previous loading
        loadJob = viewModelScope.launch {
            // Loading implementation
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        loadJob?.cancel()
    }
}
```

## 🔧 Build Architecture

### Gradle Configuration

#### Multi-Module Build Setup

**Root `build.gradle.kts`:**
```kotlin
buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
    }
}
```

**API Module `build.gradle.kts`:**
```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    `maven-publish`
}

// Source sets to include implementation
sourceSets {
    named("main") {
        java.srcDir("../src/main/java")
        res.srcDir("../src/main/res")
    }
}

// Publishing configuration
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.example.modules"
            artifactId = "template-feature-api"
            version = "1.0.15"
            
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}
```

**Implementation Module `build.gradle.kts`:**
```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

// No publishing - internal only
android {
    compileSdk = 35
    
    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }
}
```

### Version Management

**`gradle/libs.versions.toml`:**
```toml
[versions]
templateFeature = "1.0.15"
kotlin = "2.1.10"
compose-bom = "2024.02.00"
hilt = "2.48"

[libraries]
# Core dependencies
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version = "1.16.0" }
compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }

# Hilt
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-compiler", version.ref = "hilt" }

[bundles]
compose = [
    "androidx-compose-ui",
    "androidx-compose-ui-tooling-preview", 
    "androidx-compose-material3",
    "androidx-activity-compose"
]

[plugins]
android-library = { id = "com.android.library", version = "8.7.3" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
hilt = { id = "dagger.hilt.android.plugin", version.ref = "hilt" }
```

## 🔄 Migration and Versioning Strategy

### API Versioning

The module follows semantic versioning (SemVer):

- **Major Version** (x.0.0): Breaking API changes
- **Minor Version** (0.x.0): New features, backward compatible
- **Patch Version** (0.0.x): Bug fixes, backward compatible

### Database Migration Strategy

```kotlin
// Migration definitions
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE template_features ADD COLUMN category_id TEXT"
        )
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE categories (
                id TEXT PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                description TEXT NOT NULL
            )
            """.trimIndent()
        )
    }
}

// Database configuration
Room.databaseBuilder(context, TemplateFeatureDatabase::class.java, "database")
    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
    .build()
```

## 📈 Monitoring and Analytics Architecture

### Logging Strategy

```kotlin
// Structured logging with different levels
object TemplateFeatureLogger {
    
    private const val TAG = "TemplateFeature"
    
    fun logUserAction(action: String, parameters: Map<String, Any> = emptyMap()) {
        Log.i(TAG, "User Action: $action, Parameters: $parameters")
    }
    
    fun logError(error: Throwable, context: String = "") {
        Log.e(TAG, "Error in $context", error)
    }
    
    fun logPerformance(operation: String, duration: Long) {
        Log.d(TAG, "Performance: $operation took ${duration}ms")
    }
}

// Usage in ViewModels
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllFeaturesUseCase: GetAllTemplateFeaturesUseCase
) : ViewModel() {
    
    fun loadFeatures() {
        viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            
            try {
                TemplateFeatureLogger.logUserAction("load_features")
                // Implementation...
                
                val duration = System.currentTimeMillis() - startTime
                TemplateFeatureLogger.logPerformance("load_features", duration)
            } catch (e: Exception) {
                TemplateFeatureLogger.logError(e, "loadFeatures")
            }
        }
    }
}
```

This architecture provides a solid foundation for scalable, maintainable, and testable Android feature modules following Clean Architecture principles.