package com.maha.builder.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project)

    @Query("SELECT * FROM projects_table ORDER BY creationDate DESC")
    fun getAllProjects(): Flow<List<Project>>

    @Query("SELECT * FROM projects_table WHERE id = :projectId")
    suspend fun getProjectById(projectId: Int): Project?
    
    @Query("DELETE FROM projects_table WHERE id = :projectId")
    suspend fun deleteProject(projectId: Int)
}
