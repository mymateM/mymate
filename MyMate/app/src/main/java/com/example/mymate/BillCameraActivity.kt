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
import kotlinx.coroutines.runBlocking
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
    lateinit var moneyfile: File
    lateinit var dayfile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillcameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 현재 전기요금 고지서에 맞춘 양식(2:1)이므로 오버레이 경우에 따른 수정 필요

        val category = intent.getStringExtra("category")
        if (category == "전기") {
            binding.gas.isGone = true
            binding.water.isGone = true
            binding.electronic.isGone = false
        } else if (category == "수도") {
            binding.gas.isGone = true
            binding.electronic.isGone = true
            binding.water.isGone = false
        } else if (category == "도시가스") {
            binding.gas.isGone = false
            binding.water.isGone = true
            binding.electronic.isGone = true
        }

        binding.guideoverlay.isGone = false

        binding.guideoverlay.setOnClickListener {
            binding.guideoverlay.isGone = true
            binding.guidetext.text = "고지서의 가장 마지막 장을 화면에 맞춰 촬영해주세요"
        }

        permissionCheck()
        val mediadir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        if (mediadir != null && mediadir.exists()) {
            outputDirectory = mediadir
        } else {
            outputDirectory = filesDir
        }
        cameraExcutor = Executors.newSingleThreadExecutor()

        moneyfile = File(outputDirectory, SimpleDateFormat("yy-mm-dd", Locale.KOREA).format(System.currentTimeMillis()) + "1.png")
        dayfile = File(outputDirectory, SimpleDateFormat("yy-mm-dd", Locale.KOREA).format(System.currentTimeMillis()) + "2.png")

        binding.capturebtn.setOnClickListener {
            binding.cover.isGone = false
            binding.indicator.isGone = false
            savePhoto()
        }

        binding.backbtn.setOnClickListener {
            if (savedUri != null) {
                if (File(savedUri!!.path).exists()) {
                    File(savedUri!!.path).delete()
                }
            }
            if (moneyfile.exists()) { moneyfile.delete() }
            if (dayfile.exists()) { dayfile.delete() }
            finish()
        }
    }

    override fun onBackPressed() {
        if (savedUri != null) {
            if (File(savedUri!!.path).exists()) {
                File(savedUri!!.path).delete()
            }
        }
        if (moneyfile.exists()) { moneyfile.delete() }
        if (dayfile.exists()) { dayfile.delete() }
        finish()
    }

    private fun savePhoto() {
        imageCapture = imageCapture ?: return
        val photoFile = File(outputDirectory, SimpleDateFormat("yy-mm-dd", Locale.KOREA).format(System.currentTimeMillis()) + ".png")
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        val category = intent.getStringExtra("category")
        imageCapture?.takePicture(outputOption, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback{
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, photoFile.toUri())) // 이것이 찍은 이미지 (가공 전)
                photoFile.delete() // 가공 전 이미지는 삭제
                val deviceWidth = resources.displayMetrics.widthPixels
                val scaledHeight = deviceWidth * bitmap.height / bitmap.width
                val resizebp = Bitmap.createScaledBitmap(bitmap, deviceWidth, scaledHeight, true)

                if (category == "전기") {

                    val fullwidth = resizebp.width / 2 - dptopx(200.toFloat()) - dptopx(35.toFloat())
                    val fullheight = resizebp.height / 2 - dptopx(100.toFloat())
                    val cropbp = Bitmap.createBitmap(resizebp, fullwidth.toInt(), fullheight.toInt(), dptopx(400.toFloat()).toInt(), dptopx(200.toFloat()).toInt()) //TODO: 이것은 서버로 갑니다
                    val fullfile = File(outputDirectory, SimpleDateFormat("yy-MM-dd", Locale.KOREA).format(System.currentTimeMillis()) + "0.png")
                    if (fullfile.exists()) { fullfile.delete() }
                    val outfull = FileOutputStream(fullfile)
                    cropbp.compress(Bitmap.CompressFormat.PNG, 100, outfull)
                    outfull.close()
                    savedUri = fullfile.toUri()

                    //이하 ML을 위한 가공
                    val onewidth = cropbp.width / 20.2
                    val oneheight = cropbp.height / 10.1

                    val moneybp = Bitmap.createBitmap(cropbp, (onewidth * 15.9).toInt(), (oneheight * 1.7).toInt(), (onewidth * 3.1).toInt(), (oneheight * 0.5).toInt()) // 금액 관심영역 (전기 기준)
                    val daybp = Bitmap.createBitmap(cropbp, (onewidth * 12.0).toInt(), (oneheight * 1.7).toInt(), (onewidth * 2.2).toInt(), (oneheight * 0.5).toInt()) // 날짜 관심영역 (전기 기준)
                    val moneybpresize = Bitmap.createScaledBitmap(moneybp, moneybp.width*2, moneybp.height*2, true)
                    val daybpresize = Bitmap.createScaledBitmap(daybp, daybp.width*2, daybp.height*2, true)
                    if (moneyfile.exists()) { moneyfile.delete() }
                    val outmoney = FileOutputStream(moneyfile)
                    moneybpresize.compress(Bitmap.CompressFormat.PNG, 100, outmoney)
                    if (dayfile.exists()) { dayfile.delete() }
                    val outday = FileOutputStream(dayfile)
                    daybpresize.compress(Bitmap.CompressFormat.PNG, 100, outday)
                } else if (category == "도시가스") {

                    val fullwidth = resizebp.width / 2 - dptopx(162.toFloat()) - dptopx(35.toFloat())
                    val fullheight = resizebp.height / 2 - dptopx(100.toFloat())
                    val cropbp = Bitmap.createBitmap(resizebp, fullwidth.toInt(), fullheight.toInt(), dptopx(323.toFloat()).toInt(), dptopx(200.toFloat()).toInt()) //TODO: 이것은 서버로 갑니다
                    val fullfile = File(outputDirectory, SimpleDateFormat("yy-MM-dd", Locale.KOREA).format(System.currentTimeMillis()) + "0.png")
                    if (fullfile.exists()) { fullfile.delete() }
                    val outfull = FileOutputStream(fullfile)
                    cropbp.compress(Bitmap.CompressFormat.PNG, 100, outfull)
                    outfull.close()
                    savedUri = fullfile.toUri()

                    //이하 ML을 위한 가공
                    val onewidth = cropbp.width / 14.4
                    val oneheight = cropbp.height / 8.9

                    val moneybp = Bitmap.createBitmap(cropbp, (onewidth * 7.6).toInt(), (oneheight * 1.2).toInt(), (onewidth * 3.2).toInt(), (oneheight * 0.5).toInt())
                    val daybp = Bitmap.createBitmap(cropbp, (onewidth * 12.0).toInt(), (oneheight * 1.2).toInt(), (onewidth * 2.2).toInt(), (oneheight * 0.5).toInt())
                    val moneybpresize = Bitmap.createScaledBitmap(moneybp, moneybp.width*2, moneybp.height*2, true)
                    val daybpresize = Bitmap.createScaledBitmap(daybp, daybp.width*2, daybp.height*2, true)
                    if (moneyfile.exists()) { moneyfile.delete() }
                    val outmoney = FileOutputStream(moneyfile)
                    moneybpresize.compress(Bitmap.CompressFormat.PNG, 100, outmoney)
                    if (dayfile.exists()) { dayfile.delete() }
                    val outday = FileOutputStream(dayfile)
                    daybpresize.compress(Bitmap.CompressFormat.PNG, 100, outday)
                } else if (category == "수도") {

                    val fullwidth = resizebp.width / 2 - dptopx(237.toFloat()) - dptopx(35.toFloat())
                    val fullheight = resizebp.height / 2 - dptopx(100.toFloat())
                    val cropbp = Bitmap.createBitmap(resizebp, fullwidth.toInt(), fullheight.toInt(), dptopx(474.toFloat()).toInt(), dptopx(200.toFloat()).toInt()) //TODO: 이것은 서버로 갑니다
                    val fullfile = File(outputDirectory, SimpleDateFormat("yy-MM-dd", Locale.KOREA).format(System.currentTimeMillis()) + "0.png")
                    if (fullfile.exists()) { fullfile.delete() }
                    val outfull = FileOutputStream(fullfile)
                    cropbp.compress(Bitmap.CompressFormat.PNG, 100, outfull)
                    outfull.close()
                    savedUri = fullfile.toUri()

                    //이하 ML을 위한 가공
                    val onewidth = cropbp.width / 21.1
                    val oneheight = cropbp.height / 8.9

                    val moneybp = Bitmap.createBitmap(cropbp, (onewidth * 1.0).toInt(), (oneheight * 6.5).toInt(), (onewidth * 4.1).toInt(), (oneheight * 0.5).toInt())
                    val daybp = Bitmap.createBitmap(cropbp, (onewidth * 17.5).toInt(), (oneheight * 6.2).toInt(), (onewidth * 2.5).toInt(), (oneheight * 0.4).toInt())
                    val moneybpresize = Bitmap.createScaledBitmap(moneybp, moneybp.width*2, moneybp.height*2, true)
                    val daybpresize = Bitmap.createScaledBitmap(daybp, daybp.width*2, daybp.height*2, true)
                    if (moneyfile.exists()) { moneyfile.delete() }
                    val outmoney = FileOutputStream(moneyfile)
                    moneybpresize.compress(Bitmap.CompressFormat.PNG, 100, outmoney)
                    if (dayfile.exists()) { dayfile.delete() }
                    val outday = FileOutputStream(dayfile)
                    daybpresize.compress(Bitmap.CompressFormat.PNG, 100, outday)
                }

                val intentforward = Intent(context, BillOCRActivity::class.java)
                intentforward.putExtra("savedUri", savedUri.toString())
                intentforward.putExtra("savedMoney", moneyfile.toUri().toString())
                intentforward.putExtra("savedDay", dayfile.toUri().toString())
                intentforward.putExtra("category", category)
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