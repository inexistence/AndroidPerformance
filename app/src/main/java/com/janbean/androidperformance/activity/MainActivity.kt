package com.janbean.androidperformance.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.janbean.androidperformance.R
import com.janbean.androidperformance.Test2
import com.janbean.androidperformance.test.DebugFileLogger
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class MainActivity : AppCompatActivity() {
    val executorSynchronousQueue = ThreadPoolExecutor(
        0,
        Integer.MAX_VALUE,
        60L,
        TimeUnit.SECONDS,
        SynchronousQueue<Runnable>(),
        object : ThreadFactory {
            private val counter = AtomicInteger(1)

            override fun newThread(r: Runnable?): Thread {
                val threadName = "SynchronousQueue-${counter.getAndIncrement()}"
                Log.i("test", "newThread $threadName")
                return Thread(r).apply { name = threadName }
            }
        }
    )

    val executorLinkedBlockingQueue = ThreadPoolExecutor(
        0,
        Integer.MAX_VALUE,
        60L,
        TimeUnit.SECONDS,
        LinkedBlockingQueue<Runnable>(),
        object : ThreadFactory {
            private val counter = AtomicInteger(1)

            override fun newThread(r: Runnable?): Thread {
                val threadName = "LinkedBlockingQueue-${counter.getAndIncrement()}"
                Log.i("test", "newThread $threadName")
                return Thread(r).apply { name = threadName }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        createAndAddFeatureButton("startThread") {
            Test2.test()
        }

        createAndAddFeatureButton("executorSynchronousQueue") {
            executorSynchronousQueue.execute {
                Log.i("test", "run SynchronousQueue ${Thread.currentThread().name}")
                try {
                    Thread.sleep(3000)
                } catch (e: Exception) {

                }
            }
        }

        createAndAddFeatureButton("executorLinkedBlockingQueue") {
            executorLinkedBlockingQueue.execute {
                Log.i("test", "run LinkedBlockingQueue ${Thread.currentThread().name}")
                try {
                    Thread.sleep(3000)
                } catch (e: Exception) {

                }
            }
        }

        DebugFileLogger.getInstance()

        createAndAddFeature("showRecord", RecordActivity::class.java)
    }

    private fun createAndAddFeature(text: String, targetActivity: Class<*>) {
        val container = findViewById<ViewGroup>(R.id.feature_btn_container)
        container.addView(createFeatureButton(text, targetActivity))
    }

    private fun createFeatureButton(text: String, targetActivity: Class<*>): View {
        return createFeatureButton(text) {
            goActivity(targetActivity)
        }
    }

    private fun createAndAddFeatureButton(text: String, action: (() -> Unit)? = null) {
        val container = findViewById<ViewGroup>(R.id.feature_btn_container)
        container.addView(createFeatureButton(text, action))
    }

    private fun createFeatureButton(text: String, action: (() -> Unit)? = null): View {
        val button = Button(this)
        button.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        button.text = text
        button.setOnClickListener {
            action?.invoke()
        }
        // 文本是否全大写
        button.isAllCaps = false
        return button
    }

    private fun goActivity(targetActivity: Class<*>) {
        startActivity(Intent(this, targetActivity))
    }
}