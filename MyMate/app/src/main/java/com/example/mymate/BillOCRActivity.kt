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
import androidx.core.text.isDigitsOnly
import androidx.core.view.isGone
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
import java.io.File

class BillOCRActivity: AppCompatActivity() {
    lateinit var binding: ActivityBillocrBinding
    private val textRecognizer: TextRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    lateinit var behavior: BottomSheetBehavior<ConstraintLayout>

    private var savedPath: String? = ""

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillocrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val moneyPath = intent.getStringExtra("savedMoney")
        val dayPath = intent.getStringExtra("savedDay")
        savedPath = intent.getStringExtra("savedUri")
        val moneyUri = moneyPath!!.toUri()
        val dayUri = dayPath!!.toUri()

        binding.previewImage.setImageURI(savedPath?.toUri())
        binding.daytemp.setImageURI(moneyUri)
        binding.moneytemp.setImageURI(dayUri)

        binding.daytemp.isGone = true
        binding.moneytemp.isGone = true

        val passmoney = InputImage.fromFilePath(this, moneyUri)
        val passday = InputImage.fromFilePath(this, dayUri)

        val moneytask = ocrProcess(passmoney)
        val daytask = ocrProcess(passday)

        runBlocking {
            moneytask.await()
            daytask.await()

            var day = ""
            var money = ""

            day = daytask.result.text
            money = moneytask.result.text

            if (!day.isDigitsOnly()) {
                for (i in day.indices) {
                    if (!day[i].isDigit()) {
                        day.replace(day[i].toString(), "0")
                    }
                }
            }

            if (day.length < 8) {
                for (i in 0 until (8 - day.length)) {
                    day += "0"
                }
            }

            if (!money.isDigitsOnly()) {
                for (i in money.indices) {
                    if (!money[i].isDigit()) {
                        money.replace(money[i].toString(), "0")
                    }
                }
            }



            /*var daytext = ""

            if (day.isNotEmpty()) {
                daytext = day.substring(0 until 4) + "." + day.substring(4 until 6) + "." + day.substring(6 until 8)
            } else {
                daytext = ""
            }*/

            bottomSheetInit(day, money, moneyPath, dayPath)
        }
    }

    private fun bottomSheetInit(day: String, money: String, moneyPath: String, dayPath: String) {
        behavior = BottomSheetBehavior.from(binding.billpersistent.root)
        behavior.peekHeight = 0
        behavior.isDraggable = false
        behavior.isHideable = false

        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        val daytext = day.substring(2, 4) + "년 " + day.substring(4, 6).toInt().toString() + "월 " + day.substring(6, 8).toInt().toString() + "일"

        binding.billpersistent.duedate.text = daytext
        binding.billpersistent.billamount.text = digitprocessing(money)


        binding.billpersistent.tonext.setOnClickListener {
            val category = intent.getStringExtra("category")
            var listintent = Intent(this, BillDetailOCRActivity::class.java)
            listintent.putExtra("category", category)
            listintent.putExtra("itemday", day.trim())
            listintent.putExtra("itemamount", money.trim())
            listintent.putExtra("savedUri", savedPath)

            if (File(moneyPath.toUri().path!!).exists()) {
                File(moneyPath.toUri().path!!).delete()
            }
            if (File(dayPath.toUri().path!!).exists()) {
                File(dayPath.toUri().path!!).delete()
            }
            startActivity(listintent)
        }

        binding.billpersistent.tobefore.setOnClickListener {
            finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val moneyPath = intent.getStringExtra("savedMoney")
        val dayPath = intent.getStringExtra("savedDay")
        if (File(moneyPath!!.toUri().path!!).exists()) {
            File(moneyPath.toUri().path!!).delete()
        }
        if (File(dayPath!!.toUri().path!!).exists()) {
            File(dayPath.toUri().path!!).delete()
        }
        if (File(savedPath!!.toUri().path!!).exists()) {
            File(savedPath!!.toUri().path!!).delete()
        }

        finish()
    }

    private fun ocrProcess(detectimage: InputImage): Task<Text> {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        var result = "진입불가"
        val money = recognizer.process(detectimage).addOnSuccessListener { visionText ->
            result = "성공"
        }.addOnFailureListener { e ->
            result = "에러"
        }

        return money
    }

    private fun digitprocessing(digits: String): String {
        var textlength = digits.length
        var processed = ""
        while (0 < textlength) {
            var substring1 = ""
            if (textlength == 3) {
                if (processed == "") {
                    processed = digits.substring(0 until 3)
                } else {
                    processed = digits.substring(0 until 3) + "," + processed
                }
            } else if (textlength > 3) {
                substring1 = digits.substring(textlength - 3 until textlength)
                if (processed == "") {
                    processed = substring1
                } else {
                    processed = "$substring1,$processed"
                }
            } else {
                substring1 = digits.substring(0 until textlength)
                processed = "$substring1,$processed"
            }

            textlength -= 3
        }

        return processed
    }
}