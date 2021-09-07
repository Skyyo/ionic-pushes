package com.skyyo.groupingandreplytonotifications

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

data class UriWithType(val type: String, val uri: Uri?)

const val UNKNOWN = "UNKNOWN"
const val TEXT = "TEXT"
const val IMAGE = "IMAGE"
const val VIDEO = "VIDEO"
const val ZIP = "ZIP"
const val AUDIO = "AUDIO"

class MainActivity : AppCompatActivity() {


    // tested with:
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
                if (uri != null) {
                    val uriWithType = getFileUriWithType(uri)
                    if (uriWithType != null) testCallback(listOf(uriWithType))
                }
            }
            Intent.ACTION_SEND_MULTIPLE -> {
                val parcelables =
                    intent.getParcelableArrayListExtra<Parcelable>(Intent.EXTRA_STREAM)
                val urisWithTypes = arrayListOf<UriWithType>()
                parcelables?.forEach { parcelable ->
                    if (parcelable != null) {
                        val uri = parcelable as? Uri
                        if (uri != null) {
                            val uriWithType = getFileUriWithType(uri)
                            if (uriWithType != null) urisWithTypes.add(uriWithType)
                        }
                    }
                }
                testCallback(urisWithTypes)
            }
        }
    }

    private fun getFileUriWithType(uri: Uri): UriWithType? {
        return when {
            intent.type?.startsWith("*/*") == true -> UriWithType(UNKNOWN, uri)
            intent.type?.startsWith("text/") == true -> UriWithType(TEXT, uri)
            intent.type?.startsWith("application/") == true -> UriWithType(TEXT, uri)
            intent.type?.startsWith("application/zip") == true -> UriWithType(ZIP, uri)
            intent.type?.startsWith("image/") == true -> UriWithType(IMAGE, uri)
            intent.type?.startsWith("audio/") == true -> UriWithType(AUDIO, uri)
            intent.type?.startsWith("video/") == true -> UriWithType(VIDEO, uri)
            else -> null
//                    intent.type == "text/rtf" -> {
//                        Log.d("vovk", "rtf")
//                    }
//                    intent.type?.startsWith("text/plain") == true -> {
//                        Log.d("vovk", "txt")
//                    }
//                    intent.type?.startsWith("text/comma-separated-values") == true -> {
//                        Log.d("vovk", "csv")
//                    }
//                    intent.type?.startsWith("application/pdf") == true -> {
//                        Log.d("vovk", "pdf")
//                    }
//                    intent.type?.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml.document") == true -> {
//                        Log.d("vovk", "docx")
//                    }
//                    intent.type?.startsWith("application/vnd.ms-powerpoint") == true -> {
//                        Log.d("vovk", "ppt")
//                    }
//                    intent.type?.startsWith("application/msword") == true -> {
//                        Log.d("vovk", "doc")
//                    }
//                    intent.type?.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") == true -> {
//                        Log.d("vovk", "xlsx")
//                    }
//                    intent.type?.startsWith("application/vnd.ms-excel") == true -> {
//                        Log.d("vovk", "xls")
//                    }
//                    intent.type?.startsWith("application/vnd.oasis.opendocument.presentation") == true -> {
//                        Log.d("vovk", "odp")
//                    }
//                    intent.type?.startsWith("application/vnd.oasis.opendocument.spreadsheet") == true -> {
//                        Log.d("vovk", "ods")
//                    }
//                    intent.type?.startsWith("application/vnd.oasis.opendocument.text") == true -> {
//                        Log.d("vovk", "odt")
//                    }
        }
    }

    //TODO pass list to ionic
    private fun testCallback(urisWithTypes: List<UriWithType>) {
        urisWithTypes.forEach {
            Log.d("vovk", "uriWithTYpe: $it")
        }
    }
}





