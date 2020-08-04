package com.jinwookss.simplecounter

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val count = intent.getIntExtra("value", 0)
        editNumber.setText(count.toString(), TextView.BufferType.EDITABLE)

        buttonInput.clicks()
                .subscribe {
                    val intent = Intent()
                    intent.putExtra("value", editNumber.text.toString().toIntOrNull() ?: 0)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                .addTo(compositeDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}