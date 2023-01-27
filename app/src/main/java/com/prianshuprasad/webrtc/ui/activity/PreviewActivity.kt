package com.prianshuprasad.webrtc.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.common.util.concurrent.ListenableFuture
import com.prianshuprasad.webrtc.ui.viewmodel.PreviewViewModel
import com.prianshuprasad.webrtc.R
import com.prianshuprasad.webrtc.databinding.ActivityPreviewBinding


class PreviewActivity : AppCompatActivity() {
    private val viewModel: PreviewViewModel = PreviewViewModel()
    private lateinit var binding: ActivityPreviewBinding
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var previewView: PreviewView
    private var preview: Preview? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_preview)
        previewView = binding.previewPreviewView
        setViewEventListeners()
        observeViewModel()
        checkAndRequestPermissions()

    }

    private fun initCameraPreview() {

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun setViewEventListeners() {
        binding.previewEnterButton.setOnClickListener(this::onEnterButtonClicked)
        binding.previewImageViewClose.setOnClickListener { finish() }
    }

    private fun observeViewModel() {
        viewModel.enterResult.observeForever {
            if (it) goToRoomActivity()
            else
                Toast.makeText(this@PreviewActivity, "Some error Occured", Toast.LENGTH_SHORT)
                    .show()

        }
        viewModel.errorMessage.observeForever {

            Toast.makeText(this@PreviewActivity, "$it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onEnterButtonClicked(v: View) {
        val roomId =
            intent.getStringExtra("room") ?: throw IllegalStateException("Room ID must not be null")

        Toast.makeText(this@PreviewActivity, "room = ${roomId}", Toast.LENGTH_SHORT).show()
        val isAudioEnabled = binding.previewAudioCheckbox.isChecked
        val isVideoEnabled = binding.previewVideoCheckbox.isChecked
        viewModel.enter(roomId, isAudioEnabled, isVideoEnabled)
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        preview = Preview.Builder().build()

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        preview?.setSurfaceProvider(previewView.surfaceProvider)

        var camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview)

    }

    private fun goToRoomActivity() {
        val roomId =
            intent.getStringExtra("room") ?: throw IllegalStateException("Room ID must not be null")
        val intent = Intent(this, CallActivity::class.java).apply {
            putExtra("room", roomId)

        }

        startActivity(intent)
        finish()

    }
    val REQUEST_ID_MULTIPLE_PERMISSIONS=10
    private fun checkAndRequestPermissions(): Boolean {
        val camera = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA)

        val audio = ContextCompat.checkSelfPermission(this,
            Manifest.permission.RECORD_AUDIO)

        val listPermissionsNeeded: MutableList<String> = ArrayList()

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }else{
            initCameraPreview()
            binding.previewVideoCheckbox.isChecked= true
        }
        if (audio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO)
        }else{
            binding.previewAudioCheckbox.isChecked= true
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS)
            return false
        }
        return true
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                val perms: MutableMap<String, Int> = HashMap()

                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.RECORD_AUDIO] = PackageManager.PERMISSION_GRANTED

                if ( perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED ) {
                        initCameraPreview()
                    binding.previewVideoCheckbox.isChecked= true
                }
                if ( perms[Manifest.permission.RECORD_AUDIO] == PackageManager.PERMISSION_GRANTED ) {
                    initCameraPreview()
                    binding.previewAudioCheckbox.isChecked= true
                }


            }
        }
    }




}