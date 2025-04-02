package com.trivaris.votechain.app

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.PrintStream

class StdOutInterceptor(val onLine: (String) -> Unit) : OutputStream() {
    private val buffer = StringBuilder()

    override fun write(b: Int) {
        if (b.toChar() == '\n') {
            onLine(buffer.toString())
            buffer.clear()
        } else {
            buffer.append(b.toChar())
        }
    }
}

@Composable
fun StdOutToastListener(context: Context) {
    val ctx = rememberUpdatedState(context)

    DisposableEffect(Unit) {
        val interceptor = StdOutInterceptor { line ->
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(ctx.value, line, Toast.LENGTH_LONG).show()
            }
        }
        System.setOut(PrintStream(interceptor, true))
        onDispose {
            // Restore default if needed
            System.setOut(PrintStream(FileOutputStream(FileDescriptor.out)))
        }
    }
}