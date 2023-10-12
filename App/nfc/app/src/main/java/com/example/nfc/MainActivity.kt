package com.example.nfc

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.tech.NfcF
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nfc.NFC.TransmitBitmapArrayToSTM32

import com.example.nfc.Screen.GrayScaleImage
import com.example.nfc.Screen.ImageGridScreen
import com.example.nfc.Screen.MonoImage
import com.example.nfc.Screen.NoteDisplay
import com.example.nfc.Screen.QRDisplay
import com.example.nfc.ui.theme.NfcTheme

class MainActivity : ComponentActivity() {
    private lateinit var nfcAdapter: NfcAdapter
    private var nfcBuffer:ByteArray= ByteArray(5000)
    private var bufferIndex:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NfcTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController= rememberNavController()
                    App(navController,intent)
                }
            }
        }

    }

}

@Composable
fun App(navController: NavHostController,intent: Intent?) {
    //val navController = rememberNavController()

    NavHost(navController, startDestination = "grid") {
        composable("grid") { ImageGridScreen(navController) }
        composable("mono") { MonoImage() }
        composable("gray") { GrayScaleImage() }
        composable("note") { NoteDisplay() }
        composable("qr") { QRDisplay() }
    }
}
fun navigateToDetail(navController: NavController, index: Int) {
    when (index) {
        1 -> navController.navigate("mono")
        2 -> navController.navigate("gray")
        3 -> navController.navigate("note")
        4 -> navController.navigate("qr")
    }
}