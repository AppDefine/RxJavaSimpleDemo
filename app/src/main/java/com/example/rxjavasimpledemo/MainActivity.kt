package com.example.rxjavasimpledemo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.rxjavasimpledemo.databinding.ActivityMainBinding
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //simpleObserver()
        //createObservable()
        binding.btn.clicks()
            .throttleFirst(1500, TimeUnit.MILLISECONDS)
            .subscribe {
                Log.d("RxJavaButton", "Button Clicked")
            }

        implementNetworkCall()
    }

    private fun implementNetworkCall() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        val productService = retrofit.create(ProductService::class.java)
        productService.getProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
            Log.d("NetworkCall", it.toString())
        }
    }

    private fun createObservable() {
        val observable = Observable.create<String> {
            it.onNext("One")
            it.onNext("Two")
            it.onNext("Three")
            it.onError(IllegalArgumentException("Error in Observable"))
            it.onNext("Four")
            it.onNext("Five")
            it.onComplete()
        }

        observable.subscribe(object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                Log.d("RxJavaSimpleObservable", "onSubscribe is called")
            }

            override fun onNext(t: String) {
                Log.d("RxJavaSimpleObservable", "onNext is called - $t")
            }

            override fun onError(e: Throwable) {
                Log.d("RxJavaSimpleObservable", "onError is called - ${e.message}")
            }

            override fun onComplete() {
                Log.d("RxJavaSimpleObservable", "onComplete is called")
            }

        })
    }

    private fun simpleObserver() {
        val list = listOf<String>("A", "B", "C")
        val observable = Observable.fromIterable(list)

        observable.subscribe(object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                Log.d("RxJavaSimpleTesting", "onSubscribe is called")
            }

            override fun onNext(t: String) {
                Log.d("RxJavaSimpleTesting", "onNext is called - $t")
            }

            override fun onError(e: Throwable) {
                Log.d("RxJavaSimpleTesting", "onError is called - ${e.message}")
            }

            override fun onComplete() {
                Log.d("RxJavaSimpleTesting", "onComplete is called")
            }

        })
    }
}