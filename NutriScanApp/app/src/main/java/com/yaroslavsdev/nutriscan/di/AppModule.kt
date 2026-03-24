package com.yaroslavsdev.nutriscan.di

import com.yaroslavsdev.nutriscan.data.local.TokenManager
import com.yaroslavsdev.nutriscan.data.remote.NetworkModule
import com.yaroslavsdev.nutriscan.data.remote.api.AuthApi
import com.yaroslavsdev.nutriscan.data.repository.AuthRepository
import com.yaroslavsdev.nutriscan.data.repository.ProductRepository
import com.yaroslavsdev.nutriscan.ui.screens.auth.AuthViewModel
import com.yaroslavsdev.nutriscan.ui.screens.product.ProductViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { TokenManager(androidContext()) }

    single { NetworkModule.createService(AuthApi::class.java, get()) }

    single { AuthRepository(get(), get()) }

    viewModel { AuthViewModel(get()) }
}