package com.example.nfc.Util

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color


@Composable
fun ReadImageFromGallery(onImageLoaded: (Uri) -> Unit) {
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { onImageLoaded(it) }
        }

    Box(modifier = Modifier
        .fillMaxWidth(fraction = 1f / 4)
        .height(50.dp)) {
        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Transparent)
        ) {
            Text(text = "Select", color = Color.White, fontSize = 14.sp)
        }
    }
}



