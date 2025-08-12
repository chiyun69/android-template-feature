# Development Guide - Adding New Features

This guide walks you through adding new features to the template-feature module following Clean Architecture principles.

## 🎯 Overview

When adding a new feature, you'll work through these layers in order:

1. **Domain Layer** - Define business models and contracts
2. **Data Layer** - Implement data access and storage
3. **Presentation Layer** - Build UI components and state management
4. **Dependency Injection** - Wire up all dependencies
5. **Testing** - Add comprehensive unit tests
6. **API Integration** - Expose functionality publicly

## 📋 Prerequisites

- Understand Clean Architecture principles
- Familiarity with Kotlin Coroutines and Flow
- Experience with Jetpack Compose
- Knowledge of Hilt dependency injection

## 🏗️ Step-by-Step Feature Addition

Let's walk through adding a **"Category"** feature as an example.

### Step 1: Domain Layer

Start with the business logic and contracts.

#### 1.1 Create Domain Model

**File**: `library/src/main/java/com/example/modules/template_feature/domain/models/CategoryModel.kt`

```kotlin
package com.example.modules.template_feature.domain.models

data class CategoryModel(
    val id: String,
    val name: String,
    val description: String,
    val color: String,
    val isActive: Boolean,
    val featureCount: Int,
    val createdAt: String,
    val updatedAt: String
) {
    companion object {
        fun empty() = CategoryModel(
            id = "",
            name = "",
            description = "",
            color = "#000000",
            isActive = true,
            featureCount = 0,
            createdAt = "",
            updatedAt = ""
        )
    }
}
```

#### 1.2 Create Repository Interface

**File**: `library/src/main/java/com/example/modules/template_feature/domain/repositories/CategoryRepository.kt`

```kotlin
package com.example.modules.template_feature.domain.repositories

import com.example.modules.template_feature.domain.models.CategoryModel
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun getAllCategories(): Flow<List<CategoryModel>>
    suspend fun getCategoryById(id: String): CategoryModel?
    suspend fun getActiveCategories(): Flow<List<CategoryModel>>
    suspend fun createCategory(category: CategoryModel): Result<CategoryModel>
    suspend fun updateCategory(category: CategoryModel): Result<CategoryModel>
    suspend fun deleteCategory(id: String): Result<Unit>
    suspend fun syncCategories(): Result<Unit>
}
```

#### 1.3 Create Use Cases

**File**: `library/src/main/java/com/example/modules/template_feature/domain/usecases/GetAllCategoriesUseCase.kt`

```kotlin
package com.example.modules.template_feature.domain.usecases

import com.example.modules.template_feature.domain.models.CategoryModel
import com.example.modules.template_feature.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(): Flow<List<CategoryModel>> {
        return repository.getAllCategories()
    }
}
```

**File**: `library/src/main/java/com/example/modules/template_feature/domain/usecases/CreateCategoryUseCase.kt`

```kotlin
package com.example.modules.template_feature.domain.usecases

import com.example.modules.template_feature.domain.models.CategoryModel
import com.example.modules.template_feature.domain.repositories.CategoryRepository
import javax.inject.Inject

class CreateCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(category: CategoryModel): Result<CategoryModel> {
        // Add business logic validation
        if (category.name.isBlank()) {
            return Result.failure(IllegalArgumentException("Category name cannot be empty"))
        }
        
        return repository.createCategory(category)
    }
}
```

### Step 2: Data Layer

Implement data access, storage, and mapping.

#### 2.1 Create DTOs (Data Transfer Objects)

**File**: `library/src/main/java/com/example/modules/template_feature/data/dto/CategoryRequestDto.kt`

```kotlin
package com.example.modules.template_feature.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoryRequestDto(
    val name: String,
    val description: String,
    val color: String,
    val isActive: Boolean
)
```

**File**: `library/src/main/java/com/example/modules/template_feature/data/dto/CategoryResponseDto.kt`

```kotlin
package com.example.modules.template_feature.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponseDto(
    val id: String,
    val name: String,
    val description: String,
    val color: String,
    val isActive: Boolean,
    val featureCount: Int,
    val createdAt: String,
    val updatedAt: String
)
```

#### 2.2 Create Database Entity

**File**: `library/src/main/java/com/example/modules/template_feature/data/localdatasource/database/CategoryEntity.kt`

```kotlin
package com.example.modules.template_feature.data.localdatasource.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val color: String,
    val isActive: Boolean,
    val featureCount: Int,
    val createdAt: String,
    val updatedAt: String
)
```

#### 2.3 Create DAO (Database Access Object)

**File**: `library/src/main/java/com/example/modules/template_feature/data/localdatasource/database/CategoryDao.kt`

```kotlin
package com.example.modules.template_feature.data.localdatasource.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>
    
    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: String): CategoryEntity?
    
    @Query("SELECT * FROM categories WHERE isActive = 1 ORDER BY name ASC")
    fun getActiveCategories(): Flow<List<CategoryEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)
    
    @Update
    suspend fun updateCategory(category: CategoryEntity)
    
    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteCategoryById(id: String)
    
    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()
}
```

#### 2.4 Update Database Class

Add CategoryDao to your existing database:

**File**: `library/src/main/java/com/example/modules/template_feature/data/localdatasource/database/TemplateFeatureDatabase.kt`

```kotlin
// Add to existing database
@Entity
import com.example.modules.template_feature.data.localdatasource.database.CategoryEntity
import com.example.modules.template_feature.data.localdatasource.database.CategoryDao

@Database(
    entities = [
        TemplateFeatureEntity::class,
        CategoryEntity::class  // Add this
    ],
    version = 2,  // Increment version
    exportSchema = false
)
abstract class TemplateFeatureDatabase : RoomDatabase() {
    abstract fun templateFeatureDao(): TemplateFeatureDao
    abstract fun categoryDao(): CategoryDao  // Add this
}
```

#### 2.5 Create API Service

**File**: `library/src/main/java/com/example/modules/template_feature/data/remotedatasource/api/CategoryApiService.kt`

```kotlin
package com.example.modules.template_feature.data.remotedatasource.api

import com.example.modules.template_feature.data.dto.CategoryRequestDto
import com.example.modules.template_feature.data.dto.CategoryResponseDto
import retrofit2.http.*

interface CategoryApiService {
    
    @GET("categories")
    suspend fun getAllCategories(): List<CategoryResponseDto>
    
    @GET("categories/{id}")
    suspend fun getCategoryById(@Path("id") id: String): CategoryResponseDto
    
    @POST("categories")
    suspend fun createCategory(@Body request: CategoryRequestDto): CategoryResponseDto
    
    @PUT("categories/{id}")
    suspend fun updateCategory(
        @Path("id") id: String,
        @Body request: CategoryRequestDto
    ): CategoryResponseDto
    
    @DELETE("categories/{id}")
    suspend fun deleteCategory(@Path("id") id: String)
}
```

#### 2.6 Create Data Mappers

**File**: `library/src/main/java/com/example/modules/template_feature/data/mappers/CategoryMapper.kt`

```kotlin
package com.example.modules.template_feature.data.mappers

import com.example.modules.template_feature.data.dto.CategoryRequestDto
import com.example.modules.template_feature.data.dto.CategoryResponseDto
import com.example.modules.template_feature.data.localdatasource.database.CategoryEntity
import com.example.modules.template_feature.domain.models.CategoryModel

// DTO to Domain Model
fun CategoryResponseDto.toDomainModel(): CategoryModel {
    return CategoryModel(
        id = this.id,
        name = this.name,
        description = this.description,
        color = this.color,
        isActive = this.isActive,
        featureCount = this.featureCount,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun List<CategoryResponseDto>.toDomainModelList(): List<CategoryModel> {
    return this.map { it.toDomainModel() }
}

// Domain Model to Request DTO
fun CategoryModel.toRequestDto(): CategoryRequestDto {
    return CategoryRequestDto(
        name = this.name,
        description = this.description,
        color = this.color,
        isActive = this.isActive
    )
}

// Entity to Domain Model
fun CategoryEntity.toDomainModel(): CategoryModel {
    return CategoryModel(
        id = this.id,
        name = this.name,
        description = this.description,
        color = this.color,
        isActive = this.isActive,
        featureCount = this.featureCount,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun List<CategoryEntity>.toDomainModelListFromEntity(): List<CategoryModel> {
    return this.map { it.toDomainModel() }
}

// Domain Model to Entity
fun CategoryModel.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        color = this.color,
        isActive = this.isActive,
        featureCount = this.featureCount,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

// Response DTO to Entity
fun CategoryResponseDto.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        color = this.color,
        isActive = this.isActive,
        featureCount = this.featureCount,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun List<CategoryResponseDto>.toEntityList(): List<CategoryEntity> {
    return this.map { it.toEntity() }
}
```

#### 2.7 Create Repository Implementation

**File**: `library/src/main/java/com/example/modules/template_feature/data/repositories/CategoryRepositoryImpl.kt`

```kotlin
package com.example.modules.template_feature.data.repositories

import com.example.modules.template_feature.data.localdatasource.database.CategoryDao
import com.example.modules.template_feature.data.mappers.*
import com.example.modules.template_feature.data.remotedatasource.api.CategoryApiService
import com.example.modules.template_feature.domain.models.CategoryModel
import com.example.modules.template_feature.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val apiService: CategoryApiService,
    private val dao: CategoryDao
) : CategoryRepository {

    override suspend fun getAllCategories(): Flow<List<CategoryModel>> {
        return dao.getAllCategories().map { entities ->
            entities.toDomainModelListFromEntity()
        }
    }

    override suspend fun getCategoryById(id: String): CategoryModel? {
        return dao.getCategoryById(id)?.toDomainModel()
    }

    override suspend fun getActiveCategories(): Flow<List<CategoryModel>> {
        return dao.getActiveCategories().map { entities ->
            entities.toDomainModelListFromEntity()
        }
    }

    override suspend fun createCategory(category: CategoryModel): Result<CategoryModel> {
        return try {
            val requestDto = category.toRequestDto()
            val responseDto = apiService.createCategory(requestDto)
            val domainModel = responseDto.toDomainModel()
            
            // Save to local database
            dao.insertCategory(responseDto.toEntity())
            
            Result.success(domainModel)
        } catch (e: Exception) {
            // Fallback to local creation
            try {
                val localCategory = category.copy(
                    id = generateLocalId(),
                    createdAt = getCurrentTimestamp(),
                    updatedAt = getCurrentTimestamp()
                )
                dao.insertCategory(localCategory.toEntity())
                Result.success(localCategory)
            } catch (localException: Exception) {
                Result.failure(localException)
            }
        }
    }

    override suspend fun updateCategory(category: CategoryModel): Result<CategoryModel> {
        return try {
            val requestDto = category.toRequestDto()
            val responseDto = apiService.updateCategory(category.id, requestDto)
            val domainModel = responseDto.toDomainModel()
            
            // Update local database
            dao.updateCategory(responseDto.toEntity())
            
            Result.success(domainModel)
        } catch (e: Exception) {
            // Fallback to local update
            try {
                val updatedCategory = category.copy(updatedAt = getCurrentTimestamp())
                dao.updateCategory(updatedCategory.toEntity())
                Result.success(updatedCategory)
            } catch (localException: Exception) {
                Result.failure(localException)
            }
        }
    }

    override suspend fun deleteCategory(id: String): Result<Unit> {
        return try {
            apiService.deleteCategory(id)
            dao.deleteCategoryById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            // Fallback to local deletion
            try {
                dao.deleteCategoryById(id)
                Result.success(Unit)
            } catch (localException: Exception) {
                Result.failure(localException)
            }
        }
    }

    override suspend fun syncCategories(): Result<Unit> {
        return try {
            val remoteCategories = apiService.getAllCategories()
            val entities = remoteCategories.toEntityList()
            
            // Replace all local data with remote data
            dao.deleteAllCategories()
            dao.insertCategories(entities)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun generateLocalId(): String {
        return "local_category_${System.currentTimeMillis()}_${(1000..9999).random()}"
    }

    private fun getCurrentTimestamp(): String {
        return java.time.Instant.now().toString()
    }
}
```

### Step 3: Presentation Layer

Build UI components and state management.

#### 3.1 Create State Data Classes

**File**: `library/src/main/java/com/example/modules/template_feature/presentation/screens/categories/CategoriesState.kt`

```kotlin
package com.example.modules.template_feature.presentation.screens.categories

import com.example.modules.template_feature.domain.models.CategoryModel

data class CategoriesState(
    val isLoading: Boolean = false,
    val categories: List<CategoryModel> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val showOnlyActive: Boolean = true,
    val selectedCategory: CategoryModel? = null
) {
    val hasData: Boolean get() = categories.isNotEmpty()
    val hasError: Boolean get() = error != null
    val isEmpty: Boolean get() = !isLoading && !hasError && categories.isEmpty()
}

data class CategoryDetailState(
    val isLoading: Boolean = false,
    val category: CategoryModel = CategoryModel.empty(),
    val error: String? = null,
    val isSaving: Boolean = false,
    val isEditMode: Boolean = false
) {
    val hasError: Boolean get() = error != null
    val canSave: Boolean get() = category.name.isNotBlank() && !isSaving
}
```

#### 3.2 Create ViewModel

**File**: `library/src/main/java/com/example/modules/template_feature/presentation/screens/categories/CategoriesViewModel.kt`

```kotlin
package com.example.modules.template_feature.presentation.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modules.template_feature.domain.models.CategoryModel
import com.example.modules.template_feature.domain.usecases.CreateCategoryUseCase
import com.example.modules.template_feature.domain.usecases.GetAllCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CategoriesState())
    val state: StateFlow<CategoriesState> = _state.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            try {
                getAllCategoriesUseCase()
                    .catch { exception ->
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = exception.message ?: "Unknown error occurred"
                        )
                    }
                    .collect { categories ->
                        val filteredCategories = if (_state.value.showOnlyActive) {
                            categories.filter { it.isActive }
                        } else {
                            categories
                        }
                        
                        _state.value = _state.value.copy(
                            isLoading = false,
                            categories = filteredCategories,
                            error = null
                        )
                    }
            } catch (exception: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun createCategory(category: CategoryModel) {
        viewModelScope.launch {
            createCategoryUseCase(category)
                .onSuccess {
                    // Refresh the list after creation
                    loadCategories()
                }
                .onFailure { exception ->
                    _state.value = _state.value.copy(
                        error = exception.message ?: "Failed to create category"
                    )
                }
        }
    }

    fun toggleShowOnlyActive() {
        _state.value = _state.value.copy(
            showOnlyActive = !_state.value.showOnlyActive
        )
        loadCategories()
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
```

#### 3.3 Create Compose Screen

**File**: `library/src/main/java/com/example/modules/template_feature/presentation/screens/categories/CategoriesScreen.kt`

```kotlin
package com.example.modules.template_feature.presentation.screens.categories

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CategoriesScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Switch(
                checked = state.showOnlyActive,
                onCheckedChange = { viewModel.toggleShowOnlyActive() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            state.hasError -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.error ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.loadCategories() }) {
                        Text("Retry")
                    }
                }
            }
            
            state.isEmpty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No categories found")
                }
            }
            
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.categories) { category ->
                        CategoryCard(
                            category = category,
                            onClick = { onNavigateToDetail(category.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(
    category: CategoryModel,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = category.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${category.featureCount} features",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
```

### Step 4: Dependency Injection

Wire up all dependencies using Hilt.

#### 4.1 Create Database Module Updates

**File**: `library/src/main/java/com/example/modules/template_feature/di/DatabaseModule.kt`

Add CategoryDao to your existing DatabaseModule:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {
    
    // Existing TemplateFeatureDao binding...
    
    @Binds
    abstract fun bindCategoryDao(
        database: TemplateFeatureDatabase
    ): CategoryDao
}
```

#### 4.2 Create Network Module Updates

**File**: `library/src/main/java/com/example/modules/template_feature/di/NetworkModule.kt`

Add CategoryApiService:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    // Existing Retrofit setup...
    
    @Provides
    @Singleton
    fun provideCategoryApiService(retrofit: Retrofit): CategoryApiService {
        return retrofit.create(CategoryApiService::class.java)
    }
}
```

#### 4.3 Create Repository Module Updates

**File**: `library/src/main/java/com/example/modules/template_feature/di/RepositoryModule.kt`

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    // Existing repository bindings...
    
    @Binds
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository
}
```

#### 4.4 Create Use Case Module Updates

**File**: `library/src/main/java/com/example/modules/template_feature/di/UseCaseModule.kt`

```kotlin
@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {
    
    // Existing use case providers...
    
    @Provides
    fun provideGetAllCategoriesUseCase(
        repository: CategoryRepository
    ): GetAllCategoriesUseCase = GetAllCategoriesUseCase(repository)
    
    @Provides
    fun provideCreateCategoryUseCase(
        repository: CategoryRepository
    ): CreateCategoryUseCase = CreateCategoryUseCase(repository)
}
```

### Step 5: Testing

Add comprehensive unit tests.

#### 5.1 Create Use Case Tests

**File**: `library/src/test/java/com/example/modules/template_feature/domain/usecases/CreateCategoryUseCaseTest.kt`

```kotlin
package com.example.modules.template_feature.domain.usecases

import com.example.modules.template_feature.domain.models.CategoryModel
import com.example.modules.template_feature.domain.repositories.CategoryRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CreateCategoryUseCaseTest {

    private lateinit var repository: CategoryRepository
    private lateinit var useCase: CreateCategoryUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = CreateCategoryUseCase(repository)
    }

    @Test
    fun `invoke should return success when repository creates category successfully`() = runTest {
        // Arrange
        val inputCategory = CategoryModel.empty().copy(name = "Test Category")
        val createdCategory = inputCategory.copy(id = "generated-id")
        coEvery { repository.createCategory(inputCategory) } returns Result.success(createdCategory)

        // Act
        val result = useCase(inputCategory)

        // Assert
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(createdCategory)
        coVerify { repository.createCategory(inputCategory) }
    }

    @Test
    fun `invoke should return failure when category name is blank`() = runTest {
        // Arrange
        val invalidCategory = CategoryModel.empty().copy(name = "")

        // Act
        val result = useCase(invalidCategory)

        // Assert
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(IllegalArgumentException::class.java)
    }
}
```

#### 5.2 Create Repository Tests

**File**: `library/src/test/java/com/example/modules/template_feature/data/repositories/CategoryRepositoryImplTest.kt`

```kotlin
package com.example.modules.template_feature.data.repositories

import app.cash.turbine.test
import com.example.modules.template_feature.data.dto.CategoryResponseDto
import com.example.modules.template_feature.data.localdatasource.database.CategoryDao
import com.example.modules.template_feature.data.localdatasource.database.CategoryEntity
import com.example.modules.template_feature.data.remotedatasource.api.CategoryApiService
import com.example.modules.template_feature.domain.models.CategoryModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CategoryRepositoryImplTest {

    private lateinit var apiService: CategoryApiService
    private lateinit var dao: CategoryDao
    private lateinit var repository: CategoryRepositoryImpl

    @Before
    fun setUp() {
        apiService = mockk()
        dao = mockk()
        repository = CategoryRepositoryImpl(apiService, dao)
    }

    @Test
    fun `getAllCategories should return flow of domain models from DAO`() = runTest {
        // Arrange
        val entities = listOf(
            CategoryEntity(
                id = "1",
                name = "Category 1",
                description = "Description 1",
                color = "#FF0000",
                isActive = true,
                featureCount = 5,
                createdAt = "2023-01-01T00:00:00Z",
                updatedAt = "2023-01-01T00:00:00Z"
            )
        )
        every { dao.getAllCategories() } returns flowOf(entities)

        // Act & Assert
        repository.getAllCategories().test {
            val emission = awaitItem()
            assertThat(emission).hasSize(1)
            assertThat(emission[0].id).isEqualTo("1")
            assertThat(emission[0].name).isEqualTo("Category 1")
            awaitComplete()
        }
    }

    @Test
    fun `createCategory should return success when remote API succeeds`() = runTest {
        // Arrange
        val inputModel = CategoryModel.empty().copy(name = "New Category")
        val responseDto = CategoryResponseDto(
            id = "generated-id",
            name = "New Category",
            description = "",
            color = "#000000",
            isActive = true,
            featureCount = 0,
            createdAt = "2023-01-01T00:00:00Z",
            updatedAt = "2023-01-01T00:00:00Z"
        )
        coEvery { apiService.createCategory(any()) } returns responseDto
        coEvery { dao.insertCategory(any()) } returns Unit

        // Act
        val result = repository.createCategory(inputModel)

        // Assert
        assertThat(result.isSuccess).isTrue()
        coVerify { apiService.createCategory(any()) }
        coVerify { dao.insertCategory(any()) }
    }
}
```

#### 5.3 Create ViewModel Tests

**File**: `library/src/test/java/com/example/modules/template_feature/presentation/screens/categories/CategoriesViewModelTest.kt`

```kotlin
package com.example.modules.template_feature.presentation.screens.categories

import app.cash.turbine.test
import com.example.modules.template_feature.domain.models.CategoryModel
import com.example.modules.template_feature.domain.usecases.CreateCategoryUseCase
import com.example.modules.template_feature.domain.usecases.GetAllCategoriesUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CategoriesViewModelTest {

    private lateinit var getAllCategoriesUseCase: GetAllCategoriesUseCase
    private lateinit var createCategoryUseCase: CreateCategoryUseCase
    private lateinit var viewModel: CategoriesViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getAllCategoriesUseCase = mockk()
        createCategoryUseCase = mockk()
        
        coEvery { getAllCategoriesUseCase() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should load categories`() = runTest {
        // Arrange
        val testCategories = listOf(
            CategoryModel.empty().copy(id = "1", name = "Category 1")
        )
        coEvery { getAllCategoriesUseCase() } returns flowOf(testCategories)

        // Act
        viewModel = CategoriesViewModel(getAllCategoriesUseCase, createCategoryUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.categories).isEqualTo(testCategories)
            assertThat(state.isLoading).isFalse()
        }
    }
}
```

### Step 6: API Integration

Expose the new functionality through the public API.

#### 6.1 Update API Interface

**File**: `library/api/src/main/java/com/example/modules/template_feature/api/TemplateFeatureModuleApi.kt`

```kotlin
interface TemplateFeatureModuleApi {
    
    // Existing methods...
    
    @Composable
    fun CategoriesNavigation(navController: NavHostController? = null)
}
```

#### 6.2 Update API Implementation

**File**: `library/api/src/main/java/com/example/modules/template_feature/api/TemplateFeatureModuleApiImpl.kt`

```kotlin
class TemplateFeatureModuleApiImpl : TemplateFeatureModuleApi {
    
    // Existing implementations...
    
    @Composable
    override fun CategoriesNavigation(navController: NavHostController?) {
        // Delegate to internal navigation
        com.example.modules.template_feature.presentation.navigation.CategoriesNavigation(
            navController ?: rememberNavController()
        )
    }
}
```

## 🧪 Testing Strategy

### Test Coverage Checklist

For each new feature, ensure you have:

- [ ] **Domain Layer Tests**
  - [ ] Use case business logic tests
  - [ ] Input validation tests
  - [ ] Error handling tests

- [ ] **Data Layer Tests** 
  - [ ] Repository implementation tests
  - [ ] Remote/local fallback scenarios
  - [ ] Data mapper tests
  - [ ] DAO tests (if needed)

- [ ] **Presentation Layer Tests**
  - [ ] ViewModel state management tests
  - [ ] User interaction tests
  - [ ] Error state tests

### Running Tests

```bash
# Run all tests for your new feature
./gradlew :library:testDebugUnitTest --tests "*Category*"

# Run specific test type
./gradlew :library:testDebugUnitTest --tests "*CategoryUseCase*"
./gradlew :library:testDebugUnitTest --tests "*CategoryRepository*"
./gradlew :library:testDebugUnitTest --tests "*CategoryViewModel*"
```

## 🚀 Integration Checklist

Before considering your feature complete:

- [ ] All layers implemented (Domain → Data → Presentation)
- [ ] Dependency injection properly configured
- [ ] Comprehensive unit tests written and passing
- [ ] API module updated (if feature should be public)
- [ ] Navigation integrated
- [ ] Error handling implemented
- [ ] Local/remote data sync working
- [ ] UI responsive and accessible

## 📝 Best Practices

1. **Start with Domain** - Always begin with business models and use cases
2. **Test-Driven Development** - Write tests alongside implementation
3. **Single Responsibility** - Each class should have one clear purpose
4. **Dependency Inversion** - Depend on interfaces, not implementations
5. **Error Handling** - Always provide fallback mechanisms
6. **Documentation** - Document complex business logic
7. **Code Review** - Follow existing patterns and conventions

## 🔄 Common Patterns

### Repository Pattern
- Always provide local/remote fallback
- Use Flow for reactive data streams
- Handle network errors gracefully

### Use Case Pattern
- Single responsibility per use case
- Validate inputs before calling repository
- Return Result types for error handling

### ViewModel Pattern
- Expose UI state through StateFlow
- Handle loading/error/success states
- Keep UI logic minimal

### Testing Pattern
- Use AAA structure (Arrange, Act, Assert)
- Mock external dependencies
- Test both success and failure scenarios

---

This guide provides a complete workflow for adding new features while maintaining Clean Architecture principles and comprehensive testing coverage.