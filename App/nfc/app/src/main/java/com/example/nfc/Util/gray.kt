package com.example.nfc.Util
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import coil.compose.rememberImagePainter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.nfc.NfcAdapter
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.nfc.NFC.NfcUtils
import com.example.nfc.NFC.TransmitBitmapArrayToSTM32


@Composable
fun GrayImage() {
    val context= LocalContext.current
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var selectedBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    var processedBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    val NfcUtils=NfcUtils(context)
    Column {

        ReadImageFromGallery { uri ->
            selectedImageUri = uri
            selectedBitmap = cropToSquare(context, uri)
            processedBitmap = convertToGray(selectedBitmap!!)
        }
        val nfcAdapter=NfcAdapter.getDefaultAdapter(context)
        if (processedBitmap!=null){
            Button(onClick = { NfcUtils.sentBitmap(processedBitmap!!) }) {
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
                contentDescription = "Processed Image",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(fraction = 0.90f)
                    .aspectRatio(1f) // 设置长宽比为1:1
                    .align(Alignment.CenterHorizontally) // 居中排列
            )
        }
    }

}

fun convertToGray(img: Bitmap): Bitmap {
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

            // 执行误差扩散部分的抖动处理
            if (j < width - 1 && i < height - 1) {
                // 右边像素处理
                gray[width * i + j + 1] += 3 * e / 8
                // 下方像素处理
                gray[width * (i + 1) + j] += 3 * e / 8
                // 右下角像素处理
                gray[width * (i + 1) + j + 1] += e / 4
            } else if (j == width - 1 && i < height - 1) {
                // 下方像素处理
                gray[width * (i + 1) + j] += 3 * e / 8
            } else if (j < width - 1 && i == height - 1) {
                // 右边像素处理
                gray[width * i + j + 1] += e / 4
            }
        }
    }

    val mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)   // 创建新的Bitmap对象用于存储处理后的图像
    mBitmap.setPixels(pixels, 0, width, 0, 0, width, height)   // 将像素点数组的数据设置到新的Bitmap对象中
    return mBitmap   // 返回处理后的灰度图像
}

fun cropToSquare(context: Context,imageUri: Uri): Bitmap {
    // 下载图片到Bitmap对象
    val contentResolver = context.contentResolver
    val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
    val width = bitmap.width
    val height = bitmap.height

    // 计算裁剪正方形区域的大小和位置
    val size = if (width > height) height else width
    val x = if (width > height) (width - height) / 2 else 0
    val y = if (height > width) (height - width) / 2 else 0

    // 裁剪为正方形
    val squareBitmap = Bitmap.createBitmap(bitmap, x, y, size, size)

    // 缩放为200x200分辨率
    val scaledBitmap = Bitmap.createScaledBitmap(squareBitmap, 200, 200, false)

    return scaledBitmap
}