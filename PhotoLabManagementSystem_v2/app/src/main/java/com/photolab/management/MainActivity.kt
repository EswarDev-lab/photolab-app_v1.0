package com.photolab.management

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.photolab.management.ui.navigation.PhotoLabNavGraph
import com.photolab.management.ui.theme.PhotoLabTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhotoLabApp()
        }
    }
}

@Composable
fun PhotoLabApp() {
    PhotoLabTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            PhotoLabNavGraph()
        }
    }
}
