package com.lionsgates.notes.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    @Serializable
    data object Notes : Routes()

    @Serializable
    data class AddEditNote(
        val notId: Int = -1,
        val noteColor: Int = -1
    )
}