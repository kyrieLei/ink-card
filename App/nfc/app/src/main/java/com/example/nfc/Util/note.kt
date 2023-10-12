package com.example.nfc.Util
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.nfc.NfcAdapter
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nfc.NFC.NfcUtils
import com.example.nfc.NFC.TransmitBitmapArrayToSTM32

private val smallFontStyle = TextStyle(fontSize = 14.sp)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Note() {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val NfcUtils=NfcUtils(context)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 70.dp)
    ) {
        val textState = remember { mutableStateOf(TextFieldValue()) }

        TextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            label = { Text("输入文本(建议不超过143字)") },
            textStyle = smallFontStyle // 应用自定义的小字体样式
        )

        Spacer(modifier = Modifier.height(16.dp))


        Row {


        Button(
            onClick = {
                bitmap = textToBitmap(context, textState.value.text)
            },
            modifier = Modifier.padding(10.dp)
        ) {
            Text("转为图片")
        }
        val nfcAdapter=NfcAdapter.getDefaultAdapter(context)
        if (bitmap!=null){
            Button(onClick = { NfcUtils.sentBitmap(bitmap!!)
            },
                modifier=Modifier.padding(10.dp)
                ) {
                Text(text = "NFC 传输启动")
            }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (bitmap != null) {
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = "Converted Image",
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(fraction = 1f)
                    .aspectRatio(1f) // 设置长宽比为1:1
                    .align(Alignment.CenterHorizontally) // 居中排列
            )
        }

    }


}

fun textToBitmap(context: Context, text: String): Bitmap {
    val width = 200
    val height = 200

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    canvas.drawColor(Color.White.toArgb())

    val paint = TextPaint().apply {
        color = Color.Black.toArgb()
        textSize = 15f
    }

    val textLayout = StaticLayout.Builder.obtain(
        text,
        0,
        text.length,
        paint,
        width
    ).setAlignment(Layout.Alignment.ALIGN_CENTER) // 设置对齐方式为居中对齐
        .setLineSpacing(0f, 1f) // 设置行间距
        .build()

    canvas.save()
    canvas.translate(0f, ((height - textLayout.height) / 2).toFloat())
    textLayout.draw(canvas)
    canvas.restore()

    return bitmap
}
