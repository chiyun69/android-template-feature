package com.example.modules.template_feature.data.mappers

import com.example.modules.template_feature.data.dto.TemplateFeatureRequestDto
import com.example.modules.template_feature.data.dto.TemplateFeatureResponseDto
import com.example.modules.template_feature.data.localdatasource.database.TemplateFeatureEntity
import com.example.modules.template_feature.domain.models.TemplateFeatureModel
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TemplateFeatureMappersTest {

    @Test
    fun `TemplateFeatureResponseDto toDomainModel should map all fields correctly`() {
        // Arrange
        val dto = TemplateFeatureResponseDto(
            id = "1",
            title = "Test Feature",
            description = "Test Description",
            isActive = true,
            createdAt = "2023-01-01T00:00:00Z"
        )

        // Act
        val domainModel = dto.toDomainModel()

        // Assert
        assertThat(domainModel.id).isEqualTo("1")
        assertThat(domainModel.title).isEqualTo("Test Feature")
        assertThat(domainModel.description).isEqualTo("Test Description")
        assertThat(domainModel.isActive).isTrue()
        assertThat(domainModel.createdAt).isEqualTo("2023-01-01T00:00:00Z")
    }

    @Test
    fun `List of TemplateFeatureResponseDto toDomainModelListFromDto should map all items`() {
        // Arrange
        val dtoList = listOf(
            TemplateFeatureResponseDto(
                id = "1",
                title = "Feature 1",
                description = "Description 1",
                isActive = true,
                createdAt = "2023-01-01T00:00:00Z"
            ),
            TemplateFeatureResponseDto(
                id = "2",
                title = "Feature 2",
                description = "Description 2",
                isActive = false,
                createdAt = "2023-01-02T00:00:00Z"
            )
        )

        // Act
        val domainList = dtoList.toDomainModelListFromDto()

        // Assert
        assertThat(domainList).hasSize(2)
        assertThat(domainList[0].id).isEqualTo("1")
        assertThat(domainList[0].title).isEqualTo("Feature 1")
        assertThat(domainList[1].id).isEqualTo("2")
        assertThat(domainList[1].isActive).isFalse()
    }

    @Test
    fun `TemplateFeatureModel toRequestDto should map correct fields`() {
        // Arrange
        val domainModel = TemplateFeatureModel(
            id = "1",
            title = "Test Feature",
            description = "Test Description",
            isActive = true,
            createdAt = "2023-01-01T00:00:00Z"
        )

        // Act
        val requestDto = domainModel.toRequestDto()

        // Assert
        assertThat(requestDto.title).isEqualTo("Test Feature")
        assertThat(requestDto.description).isEqualTo("Test Description")
        assertThat(requestDto.isActive).isTrue()
        // Note: id and createdAt are not included in request DTO
    }

    @Test
    fun `TemplateFeatureEntity toDomainModel should map all fields correctly`() {
        // Arrange
        val entity = TemplateFeatureEntity(
            id = "1",
            title = "Test Feature",
            description = "Test Description",
            isActive = true,
            createdAt = "2023-01-01T00:00:00Z"
        )

        // Act
        val domainModel = entity.toDomainModel()

        // Assert
        assertThat(domainModel.id).isEqualTo("1")
        assertThat(domainModel.title).isEqualTo("Test Feature")
        assertThat(domainModel.description).isEqualTo("Test Description")
        assertThat(domainModel.isActive).isTrue()
        assertThat(domainModel.createdAt).isEqualTo("2023-01-01T00:00:00Z")
    }

    @Test
    fun `TemplateFeatureModel toEntity should map all fields correctly`() {
        // Arrange
        val domainModel = TemplateFeatureModel(
            id = "1",
            title = "Test Feature",
            description = "Test Description",
            isActive = true,
            createdAt = "2023-01-01T00:00:00Z"
        )

        // Act
        val entity = domainModel.toEntity()

        // Assert
        assertThat(entity.id).isEqualTo("1")
        assertThat(entity.title).isEqualTo("Test Feature")
        assertThat(entity.description).isEqualTo("Test Description")
        assertThat(entity.isActive).isTrue()
        assertThat(entity.createdAt).isEqualTo("2023-01-01T00:00:00Z")
    }

    @Test
    fun `TemplateFeatureResponseDto toEntity should map all fields correctly`() {
        // Arrange
        val dto = TemplateFeatureResponseDto(
            id = "1",
            title = "Test Feature",
            description = "Test Description",
            isActive = true,
            createdAt = "2023-01-01T00:00:00Z"
        )

        // Act
        val entity = dto.toEntity()

        // Assert
        assertThat(entity.id).isEqualTo("1")
        assertThat(entity.title).isEqualTo("Test Feature")
        assertThat(entity.description).isEqualTo("Test Description")
        assertThat(entity.isActive).isTrue()
        assertThat(entity.createdAt).isEqualTo("2023-01-01T00:00:00Z")
    }

    @Test
    fun `List mappers should handle empty lists correctly`() {
        // Arrange
        val emptyDtoList = emptyList<TemplateFeatureResponseDto>()
        val emptyEntityList = emptyList<TemplateFeatureEntity>()
        val emptyModelList = emptyList<TemplateFeatureModel>()

        // Act
        val domainFromDto = emptyDtoList.toDomainModelListFromDto()
        val domainFromEntity = emptyEntityList.toDomainModelListFromEntity()
        val entityFromDto = emptyDtoList.toEntityListFromDto()
        val entityFromModel = emptyModelList.toEntityListFromModel()

        // Assert
        assertThat(domainFromDto).isEmpty()
        assertThat(domainFromEntity).isEmpty()
        assertThat(entityFromDto).isEmpty()
        assertThat(entityFromModel).isEmpty()
    }
}