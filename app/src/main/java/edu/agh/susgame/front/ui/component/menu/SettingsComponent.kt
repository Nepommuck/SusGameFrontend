package edu.agh.susgame.front.ui.component.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edu.agh.susgame.front.model.Setting3Value
import edu.agh.susgame.front.providers.interfaces.AppSettingsProvider
import edu.agh.susgame.front.ui.theme.PaddingL
import edu.agh.susgame.front.ui.theme.PaddingM


@Composable
private fun AbstractSettingComponent(
    settingName: String,
    isSelected: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    component: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = { onCheckedChange(!isSelected) }
            )
            .padding(horizontal = PaddingL),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        component()
        Text(
            text = settingName,
            modifier = Modifier.padding(start = PaddingL),
        )
    }
}

@Composable
private fun SwitchSettingComponent(
    settingName: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    AbstractSettingComponent(
        settingName,
        isChecked,
        onCheckedChange,
        component = {
            Switch(
                checked = isChecked,
                onCheckedChange = { onCheckedChange(it) },
            )
        })
}

@Composable
private fun RadioButtonSettingComponent(
    settingName: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    AbstractSettingComponent(
        settingName,
        isSelected,
        onCheckedChange = { onClick() },
        component = {
            RadioButton(
                selected = isSelected,
                onClick = onClick
            )
        })
}

@Composable
fun SettingsComponent(appSettingsProvider: AppSettingsProvider) {
    val currentSettings = appSettingsProvider.getSettings()

    Column(Modifier.padding(all = PaddingL)) {
        SwitchSettingComponent(
            settingName = "Setting 1",
            isChecked = currentSettings.value.setting1,
            onCheckedChange = { newValue -> appSettingsProvider.updateSetting1(newValue) },
        )
        SwitchSettingComponent(
            settingName = "Setting 2",
            isChecked = currentSettings.value.setting2,
            onCheckedChange = { newValue -> appSettingsProvider.updateSetting2(newValue) },
        )

        Text(text = "Setting 3", Modifier.padding(top = PaddingM))
        Column {
            Setting3Value.allValues.forEach { settingValue ->
                RadioButtonSettingComponent(
                    settingName = settingValue.toString(),
                    isSelected = (currentSettings.value.setting3 == settingValue),
                    onClick = { appSettingsProvider.updateSetting3(newValue = settingValue) },
                )
            }
        }
    }
}
