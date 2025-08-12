package com.example.modules.template_feature.data.repositories

import app.cash.turbine.test
import com.example.modules.template_feature.data.dto.TemplateFeatureResponseDto
import com.example.modules.template_feature.data.localdatasource.database.TemplateFeatureDao
import com.example.modules.template_feature.data.localdatasource.database.TemplateFeatureEntity
import com.example.modules.template_feature.data.localdatasource.preferences.TemplateFeaturePreferences
import com.example.modules.template_feature.data.remotedatasource.api.TemplateFeatureApiService
import com.example.modules.template_feature.domain.models.TemplateFeatureModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TemplateFeatureRepositoryImplTest {

    private lateinit var apiService: TemplateFeatureApiService
    private lateinit var dao: TemplateFeatureDao
    private lateinit var preferences: TemplateFeaturePreferences
    private lateinit var repository: TemplateFeatureRepositoryImpl

    @Before
    fun setUp() {
        apiService = mockk()
        dao = mockk()
        preferences = mockk()
        repository = TemplateFeatureRepositoryImpl(apiService, dao, preferences)
    }

    @Test
    fun `getAllTemplateFeatures should return flow of domain models from DAO`() = runTest {
        // Arrange
        val entities = listOf(
            TemplateFeatureEntity(
                id = "1",
                title = "Feature 1",
                description = "Description 1",
                isActive = true,
                createdAt = "2023-01-01T00:00:00Z"
            ),
            TemplateFeatureEntity(
                id = "2",
                title = "Feature 2",
                description = "Description 2",
                isActive = false,
                createdAt = "2023-01-02T00:00:00Z"
            )
        )
        every { dao.getAllTemplateFeatures() } returns flowOf(entities)

        // Act
        val result = repository.getAllTemplateFeatures()

        // Assert
        result.test {
            val emission = awaitItem()
            assertThat(emission).hasSize(2)
            assertThat(emission[0].id).isEqualTo("1")
            assertThat(emission[0].title).isEqualTo("Feature 1")
            assertThat(emission[1].id).isEqualTo("2")
            assertThat(emission[1].title).isEqualTo("Feature 2")
            awaitComplete()
        }
        verify { dao.getAllTemplateFeatures() }
    }

    @Test
    fun `getTemplateFeatureById should return domain model when entity exists`() = runTest {
        // Arrange
        val entity = TemplateFeatureEntity(
            id = "1",
            title = "Feature 1",
            description = "Description 1",
            isActive = true,
            createdAt = "2023-01-01T00:00:00Z"
        )
        coEvery { dao.getTemplateFeatureById("1") } returns entity

        // Act
        val result = repository.getTemplateFeatureById("1")

        // Assert
        assertThat(result).isNotNull()
        assertThat(result!!.id).isEqualTo("1")
        assertThat(result.title).isEqualTo("Feature 1")
        coVerify { dao.getTemplateFeatureById("1") }
    }

    @Test
    fun `getTemplateFeatureById should return null when entity does not exist`() = runTest {
        // Arrange
        coEvery { dao.getTemplateFeatureById("nonexistent") } returns null

        // Act
        val result = repository.getTemplateFeatureById("nonexistent")

        // Assert
        assertThat(result).isNull()
        coVerify { dao.getTemplateFeatureById("nonexistent") }
    }

    @Test
    fun `createTemplateFeature should return success when remote API succeeds`() = runTest {
        // Arrange
        val inputModel = TemplateFeatureModel(
            id = "",
            title = "New Feature",
            description = "New Description",
            isActive = true,
            createdAt = ""
        )
        val responseDto = TemplateFeatureResponseDto(
            id = "generated-id",
            title = "New Feature",
            description = "New Description",
            isActive = true,
            createdAt = "2023-01-01T00:00:00Z"
        )
        coEvery { apiService.createTemplateFeature(any()) } returns responseDto
        coEvery { dao.insertTemplateFeature(any()) } returns Unit

        // Act
        val result = repository.createTemplateFeature(inputModel)

        // Assert
        assertThat(result.isSuccess).isTrue()
        val createdFeature = result.getOrNull()
        assertThat(createdFeature?.id).isEqualTo("generated-id")
        assertThat(createdFeature?.title).isEqualTo("New Feature")
        coVerify { apiService.createTemplateFeature(any()) }
        coVerify { dao.insertTemplateFeature(any()) }
    }

    @Test
    fun `createTemplateFeature should fallback to local when remote API fails`() = runTest {
        // Arrange
        val inputModel = TemplateFeatureModel(
            id = "",
            title = "New Feature",
            description = "New Description",
            isActive = true,
            createdAt = ""
        )
        coEvery { apiService.createTemplateFeature(any()) } throws Exception("Network error")
        coEvery { dao.insertTemplateFeature(any()) } returns Unit

        // Act
        val result = repository.createTemplateFeature(inputModel)

        // Assert
        assertThat(result.isSuccess).isTrue()
        val createdFeature = result.getOrNull()
        assertThat(createdFeature?.title).isEqualTo("New Feature")
        assertThat(createdFeature?.id).startsWith("local_")
        coVerify { apiService.createTemplateFeature(any()) }
        coVerify { dao.insertTemplateFeature(any()) }
    }

    @Test
    fun `deleteTemplateFeature should return success when both remote and local succeed`() = runTest {
        // Arrange
        coEvery { apiService.deleteTemplateFeature("1") } returns Unit
        coEvery { dao.deleteTemplateFeatureById("1") } returns Unit

        // Act
        val result = repository.deleteTemplateFeature("1")

        // Assert
        assertThat(result.isSuccess).isTrue()
        coVerify { apiService.deleteTemplateFeature("1") }
        coVerify { dao.deleteTemplateFeatureById("1") }
    }

    @Test
    fun `deleteTemplateFeature should fallback to local when remote fails`() = runTest {
        // Arrange
        coEvery { apiService.deleteTemplateFeature("1") } throws Exception("Network error")
        coEvery { dao.deleteTemplateFeatureById("1") } returns Unit

        // Act
        val result = repository.deleteTemplateFeature("1")

        // Assert
        assertThat(result.isSuccess).isTrue()
        coVerify { apiService.deleteTemplateFeature("1") }
        coVerify { dao.deleteTemplateFeatureById("1") }
    }

    @Test
    fun `syncWithRemote should return success when API call succeeds`() = runTest {
        // Arrange
        val remoteDtos = listOf(
            TemplateFeatureResponseDto(
                id = "1",
                title = "Remote Feature",
                description = "Remote Description",
                isActive = true,
                createdAt = "2023-01-01T00:00:00Z"
            )
        )
        coEvery { apiService.getAllTemplateFeatures() } returns remoteDtos
        coEvery { dao.deleteAllTemplateFeatures() } returns Unit
        coEvery { dao.insertTemplateFeatures(any()) } returns Unit
        coEvery { preferences.setLastSyncTime(any()) } returns Unit

        // Act
        val result = repository.syncWithRemote()

        // Assert
        assertThat(result.isSuccess).isTrue()
        coVerify { apiService.getAllTemplateFeatures() }
        coVerify { dao.deleteAllTemplateFeatures() }
        coVerify { dao.insertTemplateFeatures(any()) }
        coVerify { preferences.setLastSyncTime(any()) }
    }
}