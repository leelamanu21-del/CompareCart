package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.UserCard
import kotlinx.coroutines.flow.Flow

@Dao
interface UserCardDao {
    @Query("SELECT * FROM user_cards ORDER BY addedTimestamp DESC")
    fun getAllCards(): Flow<List<UserCard>>

    @Query("SELECT * FROM user_cards WHERE isEnabled = 1")
    fun getActiveCards(): Flow<List<UserCard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: UserCard)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<UserCard>)

    @Update
    suspend fun updateCard(card: UserCard)

    @Delete
    suspend fun deleteCard(card: UserCard)

    @Query("UPDATE user_cards SET isEnabled = :isEnabled WHERE id = :id")
    suspend fun toggleCard(id: String, isEnabled: Boolean)

    @Query("SELECT COUNT(*) FROM user_cards")
    suspend fun getCardCount(): Int
}
