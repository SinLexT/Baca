package com.capstone.baca.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.capstone.baca.R
import com.capstone.baca.UploadUtility
import com.capstone.baca.databinding.ActivityCameraBinding
import java.io.File
import java.util.*

class CameraActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    companion object{
        private const val FILE_NAME = "photo.jpg"
    }

    private lateinit var binding: ActivityCameraBinding
    private val cameraRequest = 10
    private lateinit var imageView: ImageView
    private lateinit var photoFile : File
    private var btnSound : Button? = null
    private var textToSpeech : TextToSpeech? = null
    private var array : Array<String>? = null
    private var random : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraRequest)

        btnSound = this.findViewById(R.id.btn_sound)

        array = resources.getStringArray(R.array.kosakata)
        random = array!![Random().nextInt(array!!.size)]

        btnSound!!.isEnabled = false
        textToSpeech = TextToSpeech(this, this)

        btnSound!!.setOnClickListener {
            sound()
        }

        imageView = findViewById(R.id.ivImage)
        val photoButton: Button = findViewById(R.id.btn_camera)

        photoButton.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)

            val fileProvider = FileProvider.getUriForFile(this, "com.capstone.baca.fileprovider", photoFile)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            startActivityForResult(cameraIntent, cameraRequest)
        }
    }

    private fun sound() {
        textToSpeech?.speak(random, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val output = textToSpeech?.setLanguage(Locale("id", "ID"))

            if (output == TextToSpeech.LANG_MISSING_DATA || output == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.d("Text-To-Speech", "The Language is not supported")
            } else {
                btnSound?.isEnabled = true
            }
        } else {
            Log.d("Text-To-Speech", "Initialization Failed!")
        }
    }

    public override fun onDestroy() {
        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
        }
        super.onDestroy()
    }

    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequest) {
            val photo = BitmapFactory.decodeFile(photoFile.absolutePath)

            val upload = UploadUtility(this)
            upload.uploadFile(sourceFile = photoFile)

            imageView.setImageBitmap(photo)
        }
    }
}

