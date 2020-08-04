package com.jinwookss.simplecounter

import android.app.Activity
import android.content.Intent
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

        buttonSecond.clicks().subscribe { goSecondActivity() }.addTo(compositeDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    fun goSecondActivity() {
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("value", viewModel.getValue())
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                val value = data?.getIntExtra("value", 0) ?: 0
                viewModel.setValue(value)
            }
        }
    }
}
