package com.janbean.androidperformance

class MyThread: Thread {
    constructor(): super()

    constructor(runnable: Runnable): super(runnable)
}