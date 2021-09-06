package com.skyyo.groupingandreplytonotifications

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {


    // tested "types"
    // txt, csv, xlsx, xls, odp, odt, ods, pdf, doc, docx, rtf, ppt
    // zip
    // jpg, svg, jpeg, webp, png, gif, tiff
    // mp4, mkv, wmw, avi,
    // ogg, mp3, wav,


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (intent?.action) {
            Intent.ACTION_SEND -> {
                val uri = intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri
                when {
                    intent.type == "text/rtf" -> {
                        Log.d("vovk", "rtf")
                    }
                    intent.type?.startsWith("text/plain") == true -> {
                        Log.d("vovk", "txt")
                    }
                    intent.type?.startsWith("text/comma-separated-values") == true -> {
                        Log.d("vovk", "csv")
                    }
                    intent.type?.startsWith("image/") == true -> {
                        Log.d("vovk", "image")
                    }
                    intent.type?.startsWith("application/pdf") == true -> {
                        Log.d("vovk", "pdf")
                    }
                    intent.type?.startsWith("application/zip") == true -> {
                        Log.d("vovk", "zip")
                    }
                    intent.type?.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml.document") == true -> {
                        Log.d("vovk", "docx")
                    }
                    intent.type?.startsWith("application/vnd.ms-powerpoint") == true -> {
                        Log.d("vovk", "ppt")
                    }
                    intent.type?.startsWith("application/msword") == true -> {
                        Log.d("vovk", "doc")
                    }
                    intent.type?.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") == true -> {
                        Log.d("vovk", "xlsx")
                    }
                    intent.type?.startsWith("application/vnd.ms-excel") == true -> {
                        Log.d("vovk", "xls")
                    }
                    intent.type?.startsWith("application/vnd.oasis.opendocument.presentation") == true -> {
                        Log.d("vovk", "odp")
                    }
                    intent.type?.startsWith("application/vnd.oasis.opendocument.spreadsheet") == true -> {
                        Log.d("vovk", "ods")
                    }
                    intent.type?.startsWith("application/vnd.oasis.opendocument.text") == true -> {
                        Log.d("vovk", "odt")
                    }
                    intent.type?.startsWith("audio/") == true -> {
                        Log.d("vovk", "audio")
                    }
                    intent.type?.startsWith("video/") == true -> {
                        Log.d("vovk", "video")
                    }
                    intent.type != null -> {
                        Log.d("vovk", "${intent.type}")
                    }
                }
            }
            Intent.ACTION_SEND_MULTIPLE -> {
                val uris = intent.getParcelableArrayListExtra<Parcelable>(Intent.EXTRA_STREAM)
                Log.d("vovk", "ACTION_SEND_MULTIPLE image $uris")
            }
        }
    }
}





