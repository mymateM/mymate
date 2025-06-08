package com.example.mymate.util

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.example.mymate.R

object FontManager {
    lateinit var montserratHeavy: Typeface
        private set
    lateinit var montserratExtraBold: Typeface
        private set
    lateinit var montserratBold: Typeface
        private set
    lateinit var montserratSemiBold: Typeface
        private set
    lateinit var montserratMedium: Typeface
        private set
    lateinit var montserratRegular: Typeface
        private set
    lateinit var montserratLight: Typeface
        private set
    lateinit var montserratThin: Typeface
        private set

    lateinit var suitHeavy: Typeface
        private set
    lateinit var suitExtraBold: Typeface
        private set
    lateinit var suitBold: Typeface
        private set
    lateinit var suitSemiBold: Typeface
        private set
    lateinit var suitMedium: Typeface
        private set
    lateinit var suitRegular: Typeface
        private set
    lateinit var suitLight: Typeface
        private set
    lateinit var suitExtraLight: Typeface
        private set
    lateinit var suitThin: Typeface
        private set



    fun init(inputContext: Context) {
        val context = inputContext.applicationContext
        //montserrat Font init
        montserratHeavy = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_heavy), Typeface.NORMAL)
        montserratExtraBold = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_extrabold), Typeface.NORMAL)
        montserratBold = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_bold), Typeface.NORMAL)
        montserratSemiBold = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_semibold), Typeface.NORMAL)
        montserratMedium = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_medium), Typeface.NORMAL)
        montserratRegular = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_regular), Typeface.NORMAL)
        montserratLight = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_light), Typeface.NORMAL)
        montserratThin = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_thin), Typeface.NORMAL)

        //suit Font init
        suitHeavy = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_heavy), Typeface.NORMAL)
        suitExtraBold = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_extrabold), Typeface.NORMAL)
        suitBold = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_bold), Typeface.NORMAL)
        suitSemiBold = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_semibold), Typeface.NORMAL)
        suitMedium = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_medium), Typeface.NORMAL)
        suitRegular = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_regular), Typeface.NORMAL)
        suitLight = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_light), Typeface.NORMAL)
        suitExtraLight = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_extralight), Typeface.NORMAL)
        suitThin = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_thin), Typeface.NORMAL)
    }


}