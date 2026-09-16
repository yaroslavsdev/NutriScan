package com.yaroslavsdev.nutriscan.di

import com.yaroslavsdev.nutriscan.data.local.TokenManager
import com.yaroslavsdev.nutriscan.data.remote.NetworkModule
import com.yaroslavsdev.nutriscan.data.remote.api.AuthApi
import com.yaroslavsdev.nutriscan.data.remote.api.DiaryApi
import com.yaroslavsdev.nutriscan.data.remote.api.ProductsApi
import com.yaroslavsdev.nutriscan.data.repository.AuthRepository
import com.yaroslavsdev.nutriscan.data.repository.DiaryRepository
import com.yaroslavsdev.nutriscan.data.repository.ProductsRepository
import com.yaroslavsdev.nutriscan.ui.screens.addProduct.AddProductViewModel
import com.yaroslavsdev.nutriscan.ui.screens.allergens.AllergensViewModel
import com.yaroslavsdev.nutriscan.ui.screens.auth.AuthViewModel
import com.yaroslavsdev.nutriscan.ui.screens.diary.FoodDiaryViewModel
import com.yaroslavsdev.nutriscan.ui.screens.history.CheckHistoryViewModel
import com.yaroslavsdev.nutriscan.ui.screens.nutrition.NutritionViewModel
import com.yaroslavsdev.nutriscan.ui.screens.product.ProductViewModel
import com.yaroslavsdev.nutriscan.ui.screens.profile.ProfileViewModel
import com.yaroslavsdev.nutriscan.ui.screens.scan.ScanViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { TokenManager(androidContext()) }

    single { NetworkModule.createService(AuthApi::class.java, get()) }
    single { NetworkModule.createService(ProductsApi::class.java, get()) }
    single { NetworkModule.createService(DiaryApi::class.java, get()) }

    single { AuthRepository(get(), get()) }
    single { ProductsRepository(get()) }
    single { DiaryRepository(get()) }

    viewModelOf(::AuthViewModel)
    viewModelOf(::ProductViewModel)
    viewModelOf(::ScanViewModel)
    viewModelOf(::AllergensViewModel)
    viewModelOf(::NutritionViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::CheckHistoryViewModel)
    viewModelOf(::AddProductViewModel)
    viewModelOf(::FoodDiaryViewModel)
}