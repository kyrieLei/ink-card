package com.example.nfc.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nfc.R
import com.example.nfc.Util.Note


@Composable
fun NoteDisplay(){

    Box(
        modifier = Modifier.background(Color.LightGray).height(50.dp),
        contentAlignment = Alignment.Center
    ) {
        val CustomFont = FontFamily(Font(R.font.font))
        Text(
            text = "记事本",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = Color.DarkGray,
            fontSize = 20.sp,
            style = TextStyle(
                fontFamily = CustomFont,
                fontWeight = FontWeight.Normal
            )
        )
    }
    Note()
}