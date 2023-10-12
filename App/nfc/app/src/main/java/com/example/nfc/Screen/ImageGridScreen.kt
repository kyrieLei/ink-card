package com.example.nfc.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import com.example.nfc.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nfc.navigateToDetail

@Composable
fun ImageGridScreen(navController: NavController) {

    Surface(color = MaterialTheme.colorScheme.background) {
        Column {
            Box(
                modifier = Modifier.background(Color.Transparent).height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                val CustomFont = FontFamily(Font(R.font.font))

                Text(
                    text = "E-INK",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.DarkGray,
                    fontSize = 30.sp,
                    style = TextStyle(
                        fontFamily = CustomFont,
                        fontWeight = FontWeight.Normal
                    )
                )

            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.nfc),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .width(LocalConfiguration.current.screenWidthDp.dp / 2)
                        .aspectRatio(1f)
                )
            }

            Row(modifier=Modifier.padding(20.dp,top = 50.dp)) {
                ImageItem(1, navController, R.drawable.mono)
                ImageItem(2, navController, R.drawable.gray)
            }
            Row(modifier=Modifier.padding(20.dp)) {
                ImageItem(3, navController, R.drawable.note)
                ImageItem(4, navController, R.drawable.qr)
            }
        }
    }
}

@Composable
fun ImageItem(index: Int, navController: NavController, imageResId: Int) {
    val painter: Painter = painterResource(id = imageResId)

    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .padding(5.dp)
            .size(width = 143.5.dp, height = 101.dp)//410:291
            .clickable { navigateToDetail(navController, index) },
        contentScale = ContentScale.Crop
    )
}




