package com.maha.builder.data

import android.content.Context
import androidx.room.*
import java.util.UUID

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "web_nodes")
data class WebNode(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val projectId: Int,
    val type: String, // "HEADER", "PARAGRAPH", "BUTTON"
    val content: String,
    val cssRules: String
)

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY createdAt DESC")
    suspend fun getAllProjects(): List<Project>
    
    @Insert
    suspend fun insertProject(project: Project): Long
    
    @Query("SELECT * FROM web_nodes WHERE projectId = :projectId")
    suspend fun getNodesForProject(projectId: Int): List<WebNode>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNode(node: WebNode)
}

@Database(entities = [Project::class, WebNode::class], version = 1, exportSchema = false)
abstract class MahaDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    companion object {
        @Volatile private var INSTANCE: MahaDatabase? = null
        fun getDatabase(context: Context): MahaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, MahaDatabase::class.java, "maha_database").build()
                INSTANCE = instance
                instance
            }
        }
    }
}
