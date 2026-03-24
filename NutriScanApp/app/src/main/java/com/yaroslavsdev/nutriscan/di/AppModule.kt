package com.yaroslavsdev.nutriscan.di

import com.yaroslavsdev.nutriscan.data.local.TokenManager
import com.yaroslavsdev.nutriscan.data.remote.NetworkModule
import com.yaroslavsdev.nutriscan.data.remote.api.AuthApi
import com.yaroslavsdev.nutriscan.data.remote.api.ProductApi
import com.yaroslavsdev.nutriscan.data.repository.AuthRepository
import com.yaroslavsdev.nutriscan.data.repository.FoodDiaryRepository
import com.yaroslavsdev.nutriscan.data.repository.HistoryRepository
import com.yaroslavsdev.nutriscan.data.repository.ProductRepository
import com.yaroslavsdev.nutriscan.ui.screens.allergens.AllergensViewModel
import com.yaroslavsdev.nutriscan.ui.screens.auth.AuthViewModel
import com.yaroslavsdev.nutriscan.ui.screens.product.ProductViewModel
import com.yaroslavsdev.nutriscan.ui.screens.scan.ScanViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { TokenManager(androidContext()) }

    single { NetworkModule.createService(AuthApi::class.java, get()) }
    single { NetworkModule.createService(ProductApi::class.java, get()) }

    single { ProductRepository(get(), get()) }
    single { AuthRepository(get(), get()) }

    single { HistoryRepository }
    single { FoodDiaryRepository }

    viewModelOf(::AuthViewModel)
    viewModelOf(::ProductViewModel)
    viewModelOf(::ScanViewModel)
    viewModelOf(::AllergensViewModel)
}