package ru.omsktec.scheduleApp.ui.components.layouts

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import ru.omsktec.scheduleApp.R
import ru.omsktec.scheduleApp.viewmodel.MyViewModel
import ru.omsktec.scheduleApp.ui.components.items.SettingsItem
import ru.omsktec.scheduleApp.ui.theme.customColors
import ru.omsktec.scheduleApp.ui.theme.customTypography

import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun SettingsLayout(
    navController: NavController,
    vm: MyViewModel,
) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    var versionName: String? = null
    var versionCode: Long? = null
    val snackbarHostState = remember { SnackbarHostState() }

    try {
        val packageInfo: PackageInfo = packageManager.getPackageInfo(context.packageName, 0)
        versionName = packageInfo.versionName
        versionCode = packageInfo.longVersionCode
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) {data -> Snackbar(
                snackbarData = data,
                containerColor = customColors.itemPrimary,
                contentColor = customColors.mainText,
                shape = RoundedCornerShape(12.dp),
            ) }
        },
        containerColor = Color.Transparent
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("change_schedule") {
                            popUpTo("main")
                        }
                    }
            ) {
                Text(
                    text = "Выбрать расписания",
                    style = customTypography.robotoRegular16,
                    color = customColors.mainText
                )
                IconButton(
                    onClick = {
                        navController.navigate("change_schedule") {
                            popUpTo("main")
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_right),
                        contentDescription = "Right",
                        tint = customColors.secondaryTextAndIcons,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            SettingsItem(vm, "Учитывать кураторские часы", true, snackbarHostState) { isChecked: Boolean ->
                vm.viewModelScope.launch {
                    vm.updateSettingsData(isCuratorHour = isChecked)
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "version $versionName($versionCode)",
                    style = customTypography.robotoRegular12,
                    color = customColors.secondaryTextAndIcons
                )
            }
        }
    }
}