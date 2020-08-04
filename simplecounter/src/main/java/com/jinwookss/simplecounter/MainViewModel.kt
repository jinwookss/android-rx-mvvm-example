package com.jinwookss.simplecounter

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class MainViewModel {
    private val count = BehaviorSubject.createDefault(0)

    fun getValueStream(): Observable<Int> {
        return count
    }

    fun setValue(n: Int) {
        count.onNext(n)
    }
}