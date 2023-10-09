package com.shino72.wallet.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.shino72.wallet.db.entity.OttDB

@Dao
interface OttDao {
    // 전부 가져오기
    @Query("SELECT * FROM 'Plan'")
    suspend fun getAll() : List<OttDB>

    @Query("DELETE FROM 'Plan'")
    suspend fun deleteAll()

    @Query("DELETE FROM 'Plan' WHERE uid = :userId")
    suspend fun deleteByUserId(userId: Int)

    // 삽입 하기
    @Insert
    suspend fun insertDB(plan: OttDB)

    // 삭제 하기
    @Delete
    suspend fun delete(plan: OttDB)
}