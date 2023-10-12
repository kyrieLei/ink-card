package com.example.nfc.Util

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.nfc.NfcAdapter
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import coil.compose.rememberImagePainter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.nfc.NFC.NfcUtils
import com.example.nfc.NFC.TransmitBitmapArrayToSTM32


@Composable
fun BinaryImage() {
    val context= LocalContext.current
    val NfcUtils=NfcUtils(context)
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var selectedBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    var processedBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    Column {
        ReadImageFromGallery { uri ->
            selectedImageUri = uri
            selectedBitmap = cropToSquare(context, uri)
            processedBitmap = convertToMono(selectedBitmap!!)

        }
        val nfcAdapter= NfcAdapter.getDefaultAdapter(context)
        if (processedBitmap!=null){
            Button(onClick = {NfcUtils.sentBitmap(processedBitmap!!)}) {
                Text(text = "NFC 传输启动")
            }
        }

        selectedImageUri?.let { uri ->
            Image(
                painter = rememberImagePainter(uri),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(fraction = 0.90f)
                    .aspectRatio(1f) // 设置长宽比为1:1
                    .align(Alignment.CenterHorizontally) // 居中排列
            )
        }

        processedBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Processed Image" ,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(fraction = 0.90f)
                    .aspectRatio(1f) // 设置长宽比为1:1
                    .align(Alignment.CenterHorizontally) // 居中排列
            )
        }
    }

}

fun convertToMono(img: Bitmap): Bitmap {
    val width = img.width                       // 获取位图的宽
    val height = img.height                     // 获取位图的高
    val pixels = IntArray(width * height)        // 通过位图的大小创建像素点数组
    img.getPixels(pixels, 0, width, 0, 0, width, height)   // 将位图的像素数据复制到像素点数组中
    val gray = IntArray(height * width)          // 创建用于存储灰度值的数组

    // 遍历图像每个像素点，获取红色通道值，并将其存储到灰度数组中
    for (i in 0 until height) {
        for (j in 0 until width) {
            val grey = pixels[width * i + j]
            val red = grey and 0x00FF0000 shr 16
            gray[width * i + j] = red
        }
    }

    var e = 0

    // 对每个灰度像素点进行抖动处理
    for (i in 0 until height) {
        for (j in 0 until width) {
            val g = gray[width * i + j]

            // 根据灰度值判断像素点应该设置为黑色或白色
            if (g >= 128) {
                pixels[width * i + j] = -0x1  // 设置像素点为白色
                e = g - 255
            } else {
                pixels[width * i + j] = -0x1000000  // 设置像素点为黑色
                e = g - 0
            }

        }
    }

    val mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)   // 创建新的Bitmap对象用于存储处理后的图像
    mBitmap.setPixels(pixels, 0, width, 0, 0, width, height)   // 将像素点数组的数据设置到新的Bitmap对象中
    return mBitmap   // 返回处理后的灰度图像
}