package com.islamelmrabet.cookconnect.model.localModels

/**
 * Class: UserPreferences
 *
 * Description: This class is used to determine if the user is going to change his password.
 *              Depending on the variable isFirstTime, the database will update the field password with the new password.
 *
 * @property isFirstTime
 */
class UserPreferences(
    val isFirstTime: Boolean = false
)