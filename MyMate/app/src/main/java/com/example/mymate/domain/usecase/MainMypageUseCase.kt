package com.example.mymate.domain.usecase

import com.example.mymate.data.dto.setting.UserInfo
import com.example.mymate.data.repository.MainMypageRepository
import java.text.DecimalFormat

class MainMypageUseCase(private val mypageRepo: MainMypageRepository) {
    suspend fun getUserInfo(): UserInfo {
        val response = mypageRepo.getUserInfo()
        response.user_settlement_ratio += "%"
        response.household_settlement_date += "일"
        try {
            response.household_budget_amount = DecimalFormat("#,###").format(response.household_budget_amount).toString() + "원"

        } catch (e: Exception) {
            response.household_budget_amount += "원"
        }
        return response
    }
}