package com.skyyo.groupingandreplytonotifications

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {


    //    csv, doc, docx, pdf, ppt, pptx, rtf, txt, xls and xlsx,
//    MP4, mov, wmv, flv, Avi, hevc,
//    Mp3
//    Jpeg, jpg, PNG, gif, tiff, raw, heic, heif, mpeg, bmp, webp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        when {
            //single URI
            intent?.action == Intent.ACTION_SEND -> {
                when {
                    "text/plain" == intent.type -> {
                        Log.d("vovk", "text")
                        handleSendText(intent)
                    }
                    intent.type?.startsWith("image/") == true -> {
                        val uri = intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri

                        Log.d("vovk", "name ${uri!!.lastPathSegment}")
                        Log.d("vovk", "name2 ${uri.encodedPath}")

                        Log.d("vovk", "send image intent")
                        handleSendImage(intent)
                    }
                    intent.type?.startsWith("application/pdf") == true -> {
                        Log.d("vovk", "pdf")
                        val uri = intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri

                        Log.d("vovk", "name ${uri!!.lastPathSegment}")
                        Log.d("vovk", "name2 ${uri.encodedPath}")
                    }
                }
            }
            intent?.action == Intent.ACTION_SEND_MULTIPLE && intent.type?.startsWith("image/") == true -> {
                Log.d("vovk", "ACTION_SEND_MULTIPLE image")
                handleSendMultipleImages(intent) // Handle multiple images being sent
            }
            else -> {
                Log.d("vovk", "else")
                // Handle other intents, such as being started from the home screen
            }
        }
    }

    private fun handleSendText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            // Update UI to reflect text being shared
        }
    }

    private fun handleSendImage(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            // Update UI to reflect image being shared
        }
    }

    private fun handleSendMultipleImages(intent: Intent) {
        intent.getParcelableArrayListExtra<Parcelable>(Intent.EXTRA_STREAM)?.let {
            // Update UI to reflect multiple images being shared
        }
    }
}





