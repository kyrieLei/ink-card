package com.example.nfc.NFC

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import java.io.ByteArrayOutputStream


fun Context.findActivity(): Activity? {
    var currentContext = this
    while (true) {
        when (currentContext) {
            is Activity -> currentContext
            is ContextWrapper -> currentContext = currentContext.baseContext
            else -> null
        }
    }
}


class NfcUtils(private val context: Context) {
    private val mNfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)

    fun sentBitmap(bitmap: Bitmap) {
        val intent = Intent(context, context.javaClass)
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            val callback = object : NfcAdapter.CreateNdefMessageCallback {
                override fun createNdefMessage(event: NfcEvent): NdefMessage {
                    val record = NdefRecord.createMime("application/octet-stream", byteArray)
                    return NdefMessage(arrayOf(record))
                }
            }

            val acitvity = context.findActivity()
            if (acitvity != null) {
                mNfcAdapter?.setNdefPushMessageCallback(callback, acitvity)
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(context, intent, Bundle())
        } else {
            Toast.makeText(context, "没有检测到NFC标签", Toast.LENGTH_LONG).show()
        }
    }
}