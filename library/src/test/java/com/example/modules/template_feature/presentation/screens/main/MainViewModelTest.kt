package com.example.modules.template_feature.presentation.screens.main

import app.cash.turbine.test
import com.example.modules.template_feature.domain.models.TemplateFeatureModel
import com.example.modules.template_feature.domain.usecases.GetTemplateFeatureDataUseCase
import com.example.modules.template_feature.domain.usecases.SaveTemplateFeatureDataUseCase
import com.example.modules.template_feature.domain.usecases.SyncTemplateFeatureUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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
class MainViewModelTest {

    private lateinit var getTemplateFeatureDataUseCase: GetTemplateFeatureDataUseCase
    private lateinit var saveTemplateFeatureDataUseCase: SaveTemplateFeatureDataUseCase
    private lateinit var syncTemplateFeatureUseCase: SyncTemplateFeatureUseCase
    private lateinit var viewModel: MainViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getTemplateFeatureDataUseCase = mockk()
        saveTemplateFeatureDataUseCase = mockk()
        syncTemplateFeatureUseCase = mockk()
        
        // Mock default behavior
        coEvery { getTemplateFeatureDataUseCase.getActiveFeatures() } returns flowOf(emptyList())
        coEvery { getTemplateFeatureDataUseCase.getAllFeatures() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should show loading and load active features by default`() = runTest {
        // Arrange
        val testFeatures = listOf(
            TemplateFeatureModel(
                id = "1",
                title = "Active Feature",
                description = "Description",
                isActive = true,
                createdAt = "2023-01-01T00:00:00Z"
            )
        )
        coEvery { getTemplateFeatureDataUseCase.getActiveFeatures() } returns flowOf(testFeatures)

        // Act
        viewModel = MainViewModel(getTemplateFeatureDataUseCase, saveTemplateFeatureDataUseCase, syncTemplateFeatureUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.templateFeatures).isEqualTo(testFeatures)
            assertThat(state.showOnlyActive).isTrue()
            assertThat(state.error).isNull()
        }
    }

    @Test
    fun `loadTemplateFeatures should update state with features when successful`() = runTest {
        // Arrange
        val testFeatures = listOf(
            TemplateFeatureModel(
                id = "1",
                title = "Feature 1",
                description = "Description 1",
                isActive = true,
                createdAt = "2023-01-01T00:00:00Z"
            )
        )
        coEvery { getTemplateFeatureDataUseCase.getActiveFeatures() } returns flowOf(testFeatures)
        viewModel = MainViewModel(getTemplateFeatureDataUseCase, saveTemplateFeatureDataUseCase, syncTemplateFeatureUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.loadTemplateFeatures()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.templateFeatures).isEqualTo(testFeatures)
            assertThat(state.error).isNull()
        }
    }

    @Test
    fun `loadTemplateFeatures should update state with error when exception occurs`() = runTest {
        // Arrange
        val errorMessage = "Network error"
        coEvery { getTemplateFeatureDataUseCase.getActiveFeatures() } throws RuntimeException(errorMessage)
        viewModel = MainViewModel(getTemplateFeatureDataUseCase, saveTemplateFeatureDataUseCase, syncTemplateFeatureUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.error).isEqualTo(errorMessage)
            assertThat(state.templateFeatures).isEmpty()
        }
    }

    @Test
    fun `refreshData should call sync and reload features`() = runTest {
        // Arrange
        coEvery { syncTemplateFeatureUseCase.syncWithRemote() } returns Result.success(Unit)
        coEvery { getTemplateFeatureDataUseCase.getActiveFeatures() } returns flowOf(emptyList())
        viewModel = MainViewModel(getTemplateFeatureDataUseCase, saveTemplateFeatureDataUseCase, syncTemplateFeatureUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.refreshData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        coVerify { syncTemplateFeatureUseCase.syncWithRemote() }
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isRefreshing).isFalse()
        }
    }

    @Test
    fun `searchFeatures should update search query and call search use case`() = runTest {
        // Arrange
        val query = "test query"
        val searchResults = listOf(
            TemplateFeatureModel(
                id = "1",
                title = "Search Result",
                description = "Description",
                isActive = true,
                createdAt = "2023-01-01T00:00:00Z"
            )
        )
        coEvery { getTemplateFeatureDataUseCase.searchFeatures(query) } returns Result.success(searchResults)
        coEvery { getTemplateFeatureDataUseCase.getActiveFeatures() } returns flowOf(emptyList())
        viewModel = MainViewModel(getTemplateFeatureDataUseCase, saveTemplateFeatureDataUseCase, syncTemplateFeatureUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.searchFeatures(query)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.searchQuery).isEqualTo(query)
            assertThat(state.templateFeatures).isEqualTo(searchResults)
            assertThat(state.isLoading).isFalse()
        }
        coVerify { getTemplateFeatureDataUseCase.searchFeatures(query) }
    }

    @Test
    fun `toggleShowOnlyActive should toggle state and reload features`() = runTest {
        // Arrange
        coEvery { getTemplateFeatureDataUseCase.getActiveFeatures() } returns flowOf(emptyList())
        coEvery { getTemplateFeatureDataUseCase.getAllFeatures() } returns flowOf(emptyList())
        viewModel = MainViewModel(getTemplateFeatureDataUseCase, saveTemplateFeatureDataUseCase, syncTemplateFeatureUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.toggleShowOnlyActive()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.showOnlyActive).isFalse() // Should toggle from true to false
        }
    }

    @Test
    fun `deleteFeature should call delete use case and reload features`() = runTest {
        // Arrange
        val featureId = "1"
        coEvery { saveTemplateFeatureDataUseCase.deleteFeature(featureId) } returns Result.success(Unit)
        coEvery { getTemplateFeatureDataUseCase.getActiveFeatures() } returns flowOf(emptyList())
        viewModel = MainViewModel(getTemplateFeatureDataUseCase, saveTemplateFeatureDataUseCase, syncTemplateFeatureUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.deleteFeature(featureId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        coVerify { saveTemplateFeatureDataUseCase.deleteFeature(featureId) }
    }

    @Test
    fun `clearError should set error to null`() = runTest {
        // Arrange
        coEvery { getTemplateFeatureDataUseCase.getActiveFeatures() } throws RuntimeException("Error")
        viewModel = MainViewModel(getTemplateFeatureDataUseCase, saveTemplateFeatureDataUseCase, syncTemplateFeatureUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.clearError()

        // Assert
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.error).isNull()
        }
    }
}