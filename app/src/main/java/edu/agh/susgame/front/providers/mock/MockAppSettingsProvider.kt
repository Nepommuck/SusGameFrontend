package edu.agh.susgame.front.providers.mock

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import edu.agh.susgame.front.model.AppSettings
import edu.agh.susgame.front.model.Setting3Value
import edu.agh.susgame.front.model.Setting4Value
import edu.agh.susgame.front.providers.interfaces.AppSettingsProvider

class MockAppSettingsProvider : AppSettingsProvider {
    private var state = mutableStateOf(
        AppSettings(
            setting1 = true,
            setting2 = false,
            setting3 = Setting3Value.Val2,
            setting4 = setOf(Setting4Value.Val1, Setting4Value.Val3)
        )
    )

    override fun getSettings(): MutableState<AppSettings> = state

    override fun updateSetting1(newValue: Boolean) {
        state.value = state.value.copy(setting1 = newValue)
    }

    override fun updateSetting2(newValue: Boolean) {
        state.value = state.value.copy(setting2 = newValue)
    }

    override fun updateSetting3(newValue: Setting3Value) {
        state.value = state.value.copy(setting3 = newValue)
    }

    override fun updateSetting4(newValue: Set<Setting4Value>) {
        state.value = state.value.copy(setting4 = newValue)
    }
}