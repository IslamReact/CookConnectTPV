package com.islamelmrabet.cookconnect.model.localModels

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.navigation.Routes

/**
 * Class: SettingItem
 *
 * @property title
 * @property route
 */
data class SettingItem(
    val title: String,
    val route: String?,
    val isImplemented: Boolean = true
)

@Composable
fun getSettingsList(): List<SettingItem> {
    return listOf(
        SettingItem(
            title = stringResource(id = R.string.profile),
            route = null,
            isImplemented = false
        ),
        SettingItem(
            title = stringResource(id = R.string.help_user),
            route = null,
            isImplemented = false
        ),
        SettingItem(
            title = stringResource(id = R.string.contact),
            route = Routes.ContactScreen.route
        )
    )
}
