package com.lionsgates.notes.data.model

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lionsgates.notes.ui.theme.*

@Entity(tableName = "Notes_table")
data class Note(
    @PrimaryKey(autoGenerate = false)
    val id: Int? = null,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "time")
    val timestamp: Long,

    @ColumnInfo(name = "color")
    val color: Int,

    @ColumnInfo(name = "isOptionalRevealed")
    val isOptionalRevealed: Boolean
) {
    companion object {
        val noteColors = listOf(
            Color.White,
            RedOrange,
            LightGreen,
            LightRed,
            AquaBlue,
            Violet,
            OrangeYellow1,
            ButtonBlue,
            LightGreen1,
            Beige3,
            BrownLight,
            BabyBlue,
            RedPink
        )
    }
}
