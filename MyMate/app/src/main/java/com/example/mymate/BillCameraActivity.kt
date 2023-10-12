package com.example.mymate

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isGone
import com.example.mymate.databinding.ActivityBillcameraBinding
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BillCameraActivity: AppCompatActivity() {
    lateinit var binding: ActivityBillcameraBinding
    private var imageCapture: ImageCapture? = null
    lateinit var outputDirectory: File
    lateinit var cameraExcutor: ExecutorService
    private var savedUri: Uri? = null // 여기에 파일이 있어!
    private var context: Context = this as Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillcameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 현재 전기요금 고지서에 맞춘 양식(2:1)이므로 오버레이 경우에 따른 수정 필요

        permissionCheck()
        val mediadir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        if (mediadir != null && mediadir.exists()) {
            outputDirectory = mediadir // 얘도 클래스에 넣어둘까?
        } else {
            outputDirectory = filesDir
        }
        cameraExcutor = Executors.newSingleThreadExecutor()

        Log.d("dptopxwidth", dptopx(53.5.toFloat()).toString())
        Log.d("dptopxw", dptopx(20.7.toFloat()).toString())
        Log.d("dptopxheight", dptopx(163.3.toFloat()).toString())
        Log.d("dptopxh", dptopx(44.6.toFloat()).toString())

        binding.capturebtn.setOnClickListener {
            savePhoto()
            //overridePendingTransition(0, 0)
        }
    }

    override fun onBackPressed() {
        if (savedUri != null) {
            if (File(savedUri!!.path).exists()) {
                File(savedUri!!.path).delete()
            }
        }
        finish()
    }

    private fun savePhoto() {
        imageCapture = imageCapture ?: return
        val photoFile = File(outputDirectory, SimpleDateFormat("yy-mm-dd", Locale.KOREA).format(System.currentTimeMillis()) + ".png")
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture?.takePicture(outputOption, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback{
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, photoFile.toUri())) // 이것이 찍은 이미지 (가공 전)
                photoFile.delete() // 가공 전 이미지는 삭제
                val deviceWidth = resources.displayMetrics.widthPixels
                val scaledHeight = deviceWidth * bitmap.height / bitmap.width
                val resizebp = Bitmap.createScaledBitmap(bitmap, deviceWidth, scaledHeight, true)

                //Log.d("bitmapwidth", resizebp.width.toString())
                //Log.d("bitmapheight", resizebp.height.toString())
                //Log.d("bitmapdpwidth", pxtodp(resizebp.width).toString())
                //Log.d("bitmapdpheight", pxtodp(resizebp.height).toString())

                val cropbp = Bitmap.createBitmap(resizebp, 217, 429, 1575, 788) // 가공 후 (전기 기준) - 이것을 저장해서 서버에 업로드
                val fullfile = File(outputDirectory, SimpleDateFormat("yy=mm-dd", Locale.KOREA).format(System.currentTimeMillis()) + "0.png")
                if (fullfile.exists()) { fullfile.delete() }
                val outfull = FileOutputStream(fullfile)
                cropbp.compress(Bitmap.CompressFormat.PNG, 100, outfull)
                outfull.close()
                savedUri = fullfile.toUri() // 캐시로 저장 완료, 객체에 저장
                //binding.overlay.setImageURI(savedUri) // 저장 확인

                //이하 ML을 위한 가공

                val moneybp = Bitmap.createBitmap(cropbp, 1357, 125, 117, 54) // 금액 관심영역 (전기 기준)
                val daybp = Bitmap.createBitmap(cropbp, 936, 125, 164, 54) // 날짜 관심영역 (전기 기준)
                val moneyfile = File(outputDirectory, SimpleDateFormat("yy=mm-dd", Locale.KOREA).format(System.currentTimeMillis()) + "1.png")
                if (moneyfile.exists()) { moneyfile.delete() }
                val outmoney = FileOutputStream(moneyfile)
                moneybp.compress(Bitmap.CompressFormat.PNG, 100, outmoney)
                val dayfile = File(outputDirectory, SimpleDateFormat("yy=mm-dd", Locale.KOREA).format(System.currentTimeMillis()) + "2.png")
                if (dayfile.exists()) { dayfile.delete() }
                val outday = FileOutputStream(dayfile)
                daybp.compress(Bitmap.CompressFormat.PNG, 100, outday)

                val intentforward = Intent(context, BillOCRActivity::class.java)
                intentforward.putExtra("savedUri", savedUri.toString())
                intentforward.putExtra("savedMoney", moneyfile.toUri().toString())
                intentforward.putExtra("savedDay", dayfile.toUri().toString())
                startActivity(intentforward)
            }

            override fun onError(exception: ImageCaptureException) {
                exception.printStackTrace()
                onBackPressed()
            }
        })
    }

    private fun openCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Log.d(TAG, "바인딩 실패")
            }
        }, ContextCompat.getMainExecutor(this))
        
    }

    private fun pxtodp(px: Int): Float {
        val density = resources.displayMetrics.density
        return px / density
    }

    private fun dptopx(dp: Float): Float {
        val density = resources.displayMetrics.density
        return density * dp
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            finish()
        }
    }

    private fun permissionCheck() {
        var permissionList = listOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        for (i: Int in permissionList.indices) {
            if (ContextCompat.checkSelfPermission(this, permissionList[i]) == PackageManager.PERMISSION_DENIED) {
                openCamera()
            } else {
                ActivityCompat.requestPermissions(this, permissionList.toTypedArray(), 10)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 화면 종료 시 삭제
    }
}