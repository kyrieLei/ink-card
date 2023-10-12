package com.example.nfc.Util
import androidx.compose.foundation.layout.*
import android.graphics.Bitmap
import android.graphics.Color
import android.nfc.NfcAdapter
import androidx.compose.foundation.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import com.example.nfc.NFC.NfcUtils
import com.example.nfc.NFC.TransmitBitmapArrayToSTM32
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.nio.charset.StandardCharsets
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun qrcode() {
    val context = LocalContext.current
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var qrCodeBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val NfcUtils= NfcUtils(context)
    fun generateQRCode(text: String) {
        try {
            val hints = mapOf(
                Pair(com.google.zxing.EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name()),
                Pair(com.google.zxing.EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M.name)
            )

            val writer = MultiFormatWriter()
            val bitMatrix: BitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 400, 400, hints)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val pixels = IntArray(width * height)

            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] = if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
                }
            }
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            qrCodeBitmap = bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 70.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = text,
            onValueChange = { newValue ->
                text = newValue

            },
            label = { Text("输入文本") }
        )
        Row {



        Button(
            onClick = {generateQRCode(text.text) },
            enabled = text.text.isNotEmpty() ,
            modifier = Modifier.padding(10.dp)
        ) {
            Text("生成二维码")
        }
        val context = LocalContext.current
        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        if (qrCodeBitmap != null) {
            Button(
                onClick = { NfcUtils.sentBitmap(qrCodeBitmap!!) },
                modifier = Modifier.padding(10.dp)
            ) {
                Text(text = "NFC 传输启动")
            }
        }
    }

        qrCodeBitmap?.let { bitmap ->
            Image(
                bitmap.asImageBitmap(),
                contentDescription = "二维码",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(fraction = 0.90f)
                    .aspectRatio(1f) // 设置长宽比为1:1
                    .align(Alignment.CenterHorizontally) // 居中排列
            )
        }
    }

}
