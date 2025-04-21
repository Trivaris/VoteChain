package com.trivaris.votechain

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.trivaris.votechain.di.candidateModule
import com.trivaris.votechain.di.commonModule
import com.trivaris.votechain.di.dataStoreModule
import com.trivaris.votechain.di.voteModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(this@MainActivity)
            modules(
                commonModule,
                dataStoreModule,
                candidateModule,
                voteModule,
            )
        }

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}