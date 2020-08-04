package com.jinwookss.simplecounter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel = MainViewModel()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.getValueStream()
                .map { it.toString() }
                .subscribe(textviewCount::setText)
                .addTo(compositeDisposable)

        Observable.merge(
                buttonMinus.clicks().map { -1 },
                buttonPlus.clicks().map { 1 }
        ).withLatestFrom(viewModel.getValueStream(), BiFunction { value: Int, new: Int -> value + new })
                .subscribe(viewModel::setValue)
                .addTo(compositeDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
