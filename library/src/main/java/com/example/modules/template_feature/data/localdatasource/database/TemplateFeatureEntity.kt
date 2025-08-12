package com.example.modules.template_feature.data.localdatasource.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "template_features")
data class TemplateFeatureEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val isActive: Boolean,
    val createdAt: String,
    val lastUpdated: Long = System.currentTimeMillis()
)