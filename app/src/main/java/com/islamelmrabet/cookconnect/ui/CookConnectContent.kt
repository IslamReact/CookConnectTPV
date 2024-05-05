package com.islamelmrabet.cookconnect.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.islamelmrabet.cookconnect.ui.theme.CookConnectTheme

@Composable
        /**
         * TODO
         *
         * @param content
         */
fun CookConnectContent(content: @Composable () -> Unit) {
    CookConnectTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}