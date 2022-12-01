package dev.dk.currency.exchange.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import dev.dk.currency.exchange.android.ui.home.MainHomeRoute

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainHomeRoute()
        }
    }
}

