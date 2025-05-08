package ru.omsktec.scheduleApp.ui.components.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.omsktec.scheduleApp.R
import ru.omsktec.scheduleApp.ui.theme.customColors
import ru.omsktec.scheduleApp.ui.theme.customTypography
import ru.omsktec.scheduleApp.viewmodel.MyViewModel

@Composable
fun SettingsItem(
    viewModel: MyViewModel,
    title: String,
    isCurator: Boolean = false,
    snackbarHostState: SnackbarHostState,
    action: (Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = customTypography.robotoRegular16,
                color = customColors.mainText
            )
            if (isCurator) {
                IconButton(onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Смещает расписание после второй пары по понедельникам",
                            duration = SnackbarDuration.Short
                        )
                    }
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_help),
                        tint = customColors.secondaryTextAndIcons,
                        contentDescription = "help"
                    )
                }
            }
        }
        Switch(
            checked = viewModel.settingsData.isCuratorHour,
            onCheckedChange = {
                action(it)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = customColors.mainText,
                checkedTrackColor = customColors.itemPrimary,
                checkedBorderColor = Color.Transparent,
                uncheckedThumbColor = customColors.secondaryTextAndIcons,
                uncheckedBorderColor = Color.Transparent,
                uncheckedTrackColor = customColors.itemPrimary
            )
        )
    }
}