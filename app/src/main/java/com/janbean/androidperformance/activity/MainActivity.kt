package com.janbean.androidperformance.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.janbean.androidperformance.R
import com.janbean.androidperformance.Test2

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        createAndAddFeatureButton("startThread") {
            Test2.test()
        }

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