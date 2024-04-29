package edu.agh.susgame.front.model

sealed interface Setting3Value {
    data object Val1 : Setting3Value
    data object Val2 : Setting3Value
    data object Val3 : Setting3Value

    companion object {
        val allValues = listOf(Val1, Val2, Val3)
    }
}


sealed interface Setting4Value {
    data object Val1 : Setting4Value
    data object Val2 : Setting4Value
    data object Val3 : Setting4Value
    data object Val4 : Setting4Value
}


data class AppSettings(
    val setting1: Boolean,
    val setting2: Boolean,
    val setting3: Setting3Value,
    val setting4: Set<Setting4Value>,
)
