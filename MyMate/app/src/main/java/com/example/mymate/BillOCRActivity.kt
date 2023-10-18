package com.example.mymate

import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import com.example.mymate.databinding.ActivityBillocrBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class BillOCRActivity: AppCompatActivity() {
    lateinit var binding: ActivityBillocrBinding
    private val textRecognizer: TextRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    lateinit var behavior: BottomSheetBehavior<ConstraintLayout>

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillocrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val savedPath = intent.getStringExtra("savedUri")
        val moneyPath = intent.getStringExtra("savedMoney")
        val dayPath = intent.getStringExtra("savedDay")
        
        val moneyUri = moneyPath!!.toUri()
        val dayUri = dayPath!!.toUri()

        binding.previewImage.setImageURI(savedPath?.toUri())
        binding.daytemp.setImageURI(moneyUri)
        binding.moneytemp.setImageURI(dayUri)

        /* val money = moneyPath?.let { ImageDecoder.createSource(contentResolver, it.toUri()) }
        val passmoney = money?.let { ImageDecoder.decodeBitmap(it) }
        val day = dayPath?.let { ImageDecoder.createSource(contentResolver, it.toUri()) }
        val passday = day?.let { ImageDecoder.decodeBitmap(it) } */

        val passmoney = InputImage.fromFilePath(this, moneyUri)
        val passday = InputImage.fromFilePath(this, dayUri)

        val moneytask = ocrProcess(passmoney)
        val daytask = ocrProcess(passday)

        runBlocking {
            moneytask.await()
            daytask.await()
            binding.moneytext.text = moneytask.result.text
            binding.daytext.text = daytask.result.text

            var day = ""
            var money = ""

            var temp0 = daytask.result.text.length - 1
            day = daytask.result.text
            money = moneytask.result.text

            while (temp0 != 0) {
                if (day[temp0] < '0' || day[temp0] > '9') {
                    val daybefore = day.substring(0 until temp0) + day.substring(temp0 until day.length)
                    day = daybefore
                }
                temp0--
            }

            if (day.length < 8) {
                var temp1 = day.length
                while (temp1 < 8) {
                    day += "0"
                    temp1++
                }
            }

            var daytext = day.substring(0 until 4) + "." + day.substring(4 until 6) + "." + day.substring(6 until 8)

            var temp2 = moneytask.result.text.length - 1

            while (temp2 != 0) {
                if (money[temp2] < '0' || money[temp2] > '9') {
                    val moneybefore = money.substring(0 until temp2) + money.substring(temp2 until money.length)
                    money = moneybefore
                }
                temp2--
            }
            Log.d("오씨알", daytext)
            Log.d("오씨알", money)
            bottomSheetInit(daytext, money)
        }
    }

    private fun bottomSheetInit(day: String, money: String) {
        behavior = BottomSheetBehavior.from(binding.billpersistent.root)
        behavior.peekHeight = 0
        behavior.isDraggable = false
        behavior.isHideable = false

        Log.d("오씨알 진입", day + " " + money)

        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding.billpersistent.duedate.text = day
        var moneytext = money + "원"
        binding.billpersistent.billamount.text = moneytext


        binding.billpersistent.modaledismiss.setOnClickListener {
            finish()
        }

        binding.billpersistent.tonext.setOnClickListener {
            val category = intent.getStringExtra("category")
            var listintent = Intent(this, BillDetailActivity::class.java)
            listintent.putExtra("category", category)
            val daytext = day.substring(0 until 4) + "년 " + day.substring(5 until 7) + "월 " + day.substring(8 until day.length) + "일"
            listintent.putExtra("itemday", daytext)
            Log.d("오씨알 진입", daytext)
            listintent.putExtra("itemamount", money + "원")
            listintent.putExtra("itemname", " ")
            startActivity(listintent)
        }

        binding.billpersistent.tobefore.setOnClickListener {
            finish()
        }

    }

    private fun ocrProcess(detectimage: InputImage): Task<Text> {
        Log.d("오씨알", "진입")
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        var result = "진입불가"
        val money = recognizer.process(detectimage).addOnSuccessListener { visionText ->
            result = "성공"
            Log.d("오씨알", "성공")
            Log.d("오씨알", visionText.text)
        }.addOnFailureListener { e ->
            result = "에러"
            Log.d("오씨알", "에러")
        }

        return money
    }
}