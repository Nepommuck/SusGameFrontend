package edu.agh.susgame.front.providers.interfaces

import androidx.compose.runtime.MutableState
import edu.agh.susgame.front.model.AppSettings
import edu.agh.susgame.front.model.Setting3Value
import edu.agh.susgame.front.model.Setting4Value

interface AppSettingsProvider {
    fun getSettings(): MutableState<AppSettings>

    fun updateSetting1(newValue: Boolean)
    fun updateSetting2(newValue: Boolean)
    fun updateSetting3(newValue: Setting3Value)
    fun updateSetting4(newValue: Set<Setting4Value>)
}

