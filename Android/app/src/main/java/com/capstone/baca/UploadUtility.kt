package com.capstone.baca

import android.app.Activity
import android.app.ProgressDialog
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.capstone.baca.main.CameraActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UploadUtility(var activity: Activity) {

    var dialog: ProgressDialog? = null
    var serverURL: String = "http://103.119.62.172:5000/test"
    var serverUploadDirectoryPath: String = "test"
    val client = OkHttpClient()

    var cameraActivity = CameraActivity()

    fun uploadFile(sourceFile: File, uploadedFileName: String? = null) {
        Thread {
            val mimeType = getMimeType(sourceFile)
            if (mimeType == null) {
                Log.e("file error", "Not able to get mime type")
                return@Thread
            }
            val fileName: String = if (uploadedFileName == null)  sourceFile.name else uploadedFileName
            toggleProgressDialog(true)

            try {
                val requestBody: RequestBody =
                    MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("image", fileName,sourceFile.asRequestBody(mimeType.toMediaTypeOrNull()))
                        .build()

                val request: Request = Request.Builder().url(serverURL).post(requestBody).build()

                val response: Response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    Log.d("File upload","success, path: $serverUploadDirectoryPath$fileName")
                    val test = response.body!!.string()

                    if (test == cameraActivity.random ) {
                        showToast("SELAMAT KATA YANG ANDA TULIS BENAR")
                    } else {
                        showToast("MAAF KATA YANG ANDA TULIS SALAH")
                    }
                }

                else {
                    Log.d("File upload", "FAILED")

                    showToast("FAILED")
                }

            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.d("File upload", "FAILED")

                showToast("UPLOADING FILE FAILED")

            }
            toggleProgressDialog(false)
        }.start()
    }

    // url = file path or whatever suitable URL you want.
    fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    fun showToast(message: String) {
        activity.runOnUiThread {
            Toast.makeText( activity, message, Toast.LENGTH_LONG ).show()
        }
    }

    fun toggleProgressDialog(show: Boolean) {
        activity.runOnUiThread {
            if (show) {
                dialog = ProgressDialog.show(activity, "", "Processing...", true)
            } else {
                dialog?.dismiss()
            }
        }
    }
}