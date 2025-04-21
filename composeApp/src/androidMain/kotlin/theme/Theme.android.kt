package com.trivaris.votechain.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.trivaris.votechain.models.datastore.PreferenceKeys
import kotlinx.coroutines.flow.Flow

@Composable
actual fun AppTheme(
    darkTheme: Flow<Boolean>,
    materialTheme: Flow<Boolean>,
    content: @Composable () -> Unit
) {
    val isDarkTheme by darkTheme.collectAsState(
        initial = PreferenceKeys.DARK_MODE.defaultValue
    )
    val isMaterialTheme by materialTheme.collectAsState(
        initial = PreferenceKeys.DYNAMIC_COLOR.defaultValue
    )

    val context = LocalContext.current

    val colorScheme = remember(isDarkTheme, isMaterialTheme) {
        when {
            isMaterialTheme && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                if (isDarkTheme)
                    dynamicDarkColorScheme(context)
                else dynamicLightColorScheme(context)
            }
            isDarkTheme -> darkScheme
            else -> lightScheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}