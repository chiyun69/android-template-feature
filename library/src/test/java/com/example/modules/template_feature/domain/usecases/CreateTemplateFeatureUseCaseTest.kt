package com.example.modules.template_feature.domain.usecases

import com.example.modules.template_feature.domain.models.TemplateFeatureModel
import com.example.modules.template_feature.domain.repositories.TemplateFeatureRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CreateTemplateFeatureUseCaseTest {

    private lateinit var repository: TemplateFeatureRepository
    private lateinit var useCase: CreateTemplateFeatureUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = CreateTemplateFeatureUseCase(repository)
    }

    @Test
    fun `invoke should return success result when repository creates feature successfully`() = runTest {
        // Arrange
        val inputFeature = TemplateFeatureModel(
            id = "",
            title = "New Feature",
            description = "New Description",
            isActive = true,
            createdAt = ""
        )
        val createdFeature = inputFeature.copy(
            id = "generated-id",
            createdAt = "2023-01-01T00:00:00Z"
        )
        val expectedResult = Result.success(createdFeature)
        coEvery { repository.createTemplateFeature(inputFeature) } returns expectedResult

        // Act
        val result = useCase(inputFeature)

        // Assert
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(createdFeature)
        coVerify { repository.createTemplateFeature(inputFeature) }
    }

    @Test
    fun `invoke should return failure result when repository fails to create feature`() = runTest {
        // Arrange
        val inputFeature = TemplateFeatureModel(
            id = "",
            title = "New Feature",
            description = "New Description",
            isActive = true,
            createdAt = ""
        )
        val expectedException = Exception("Network error")
        val expectedResult = Result.failure<TemplateFeatureModel>(expectedException)
        coEvery { repository.createTemplateFeature(inputFeature) } returns expectedResult

        // Act
        val result = useCase(inputFeature)

        // Assert
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(expectedException)
        coVerify { repository.createTemplateFeature(inputFeature) }
    }
}