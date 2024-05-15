package com.islamelmrabet.cookconnect.model.localModels

import com.islamelmrabet.cookconnect.navigation.Routes

class SettingItem (
    val title: String,
    val route : String,
)

val settingsList = listOf(
    SettingItem(
        title = "Perfil",
        route = Routes.TableScreen.route,
    ),
    SettingItem(
        title = "Ayuda de Usuario",
        route = Routes.TableScreen.route,
    ),
    SettingItem(
        title = "Contacto",
        route = Routes.TableScreen.route,
    ),
)