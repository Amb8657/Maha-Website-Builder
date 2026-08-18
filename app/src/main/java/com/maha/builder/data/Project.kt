package com.maha.builder.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects_table")
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val projectName: String,
    val creationDate: Long,
    val thumbnailPath: String,
    val pageLayoutJson: String // This will hold the Canvas Flexbox structure
)
