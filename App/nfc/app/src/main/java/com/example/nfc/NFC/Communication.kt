package com.example.nfc.NFC


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import java.nio.ByteBuffer


fun TransmitBitmapArrayToSTM32(nfcAdapter: NfcAdapter,bitmap: Bitmap,context: Context) {
    val intent = Intent(context, context.javaClass)
    if(NfcAdapter.ACTION_TAG_DISCOVERED==intent.action){
        val bitmapArray = getBitmapArray(bitmap)
        val byteArray = convertBitmapArrayToByteArray(bitmapArray)


        val callback = object : NfcAdapter.CreateNdefMessageCallback {
            override fun createNdefMessage(event: NfcEvent): NdefMessage {
                val record = NdefRecord.createMime("application/octet-stream", byteArray)
                return NdefMessage(arrayOf(record))
            }
        }

        nfcAdapter?.setNdefPushMessageCallback(callback, context as Activity)
        nfcAdapter?.setNdefPushMessage(null, context as Activity)


        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(context, intent, Bundle())
    }
    else{
        Toast.makeText(context,"没有检测到NFC标签",Toast.LENGTH_LONG).show()
    }
}

fun getBitmapArray(bitmap: Bitmap): Array<Bitmap> {
    return arrayOf(bitmap)
}
fun convertBitmapArrayToByteArray(bitmapArray: Array<Bitmap>): ByteArray {
    // 计算字节数组的总长度
    var totalLength = 0
    for (bitmap in bitmapArray) {
        totalLength += bitmap.byteCount
    }

    // 创建字节数组
    val byteArray = ByteArray(totalLength)
    val buffer = ByteBuffer.wrap(byteArray)

    // 将每个位图的字节数据复制到字节数组中
    for (bitmap in bitmapArray) {
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        for (pixel in pixels) {
            buffer.putInt(pixel)
        }
    }

    return byteArray
}


