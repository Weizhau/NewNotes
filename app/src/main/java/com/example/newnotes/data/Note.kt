package com.example.newnotes.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Note(
    var title: String,
 //   val pubTime: String,
    var text: String,
    var color: String
//    var isImportant: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}