package com.janbean.androidperformance.activity

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.janbean.androidperformance.R
//import com.janbean.thread.util.ThreadTracker

class RecordActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        val adapter = SimpleAdapter().apply {
//            setData(ThreadTracker.map.map { it.value.toString() })
        }
        findViewById<RecyclerView>(R.id.recycler_view).adapter = adapter
    }

    class SimpleAdapter: RecyclerView.Adapter<SimpleAdapter.VH>() {

        private val data = ArrayList<String>()

        fun setData(data: List<String>) {
            this.data.clear()
            this.data.addAll(data)
            notifyDataSetChanged()
        }

        fun getData(): List<String> {
            return this.data
        }

        class VH(val textView: TextView) : RecyclerView.ViewHolder(textView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            return VH(TextView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            })
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            holder.textView.text = this.data[position]
        }

        override fun getItemCount(): Int {
            return this.data.size

        }
    }
}