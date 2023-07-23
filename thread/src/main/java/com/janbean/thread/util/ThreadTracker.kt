package com.janbean.thread.util

import android.util.Log
import java.util.concurrent.ConcurrentHashMap

object ThreadTracker {
    val map = ConcurrentHashMap<String, Record>()

    @JvmStatic
    fun trace(type: String, name: String): Record {
        val prefix = getPrefix(type, name)
        Log.d("hjianbin", "trace $type $name $prefix")

        val record = map.getOrPut(prefix) {
            Record().apply {
                this.type = type
                this.prefix = prefix
                this.createdAt = System.currentTimeMillis()
            }
        }
        record.startCnt++
        return record
    }

    private fun getPrefix(type: String, name: String): String {
        val index = name.lastIndexOf("-").let {
            if (it < 0) name.lastIndexOf("#")
            else it
        }
        if (index < 0) {
            return type + ": " + name
        }
        return type + ": " + name.substring(0, index)
    }

    @JvmStatic
    fun startRun(name: String?, record: Record?) {
        record ?: return
        record.lastRunAt = System.currentTimeMillis()
        val history = History().apply {
            this.name = name
            this.startAt = System.currentTimeMillis()
        }
        record.history.add(history)
        history.startAt = System.currentTimeMillis()
    }

    @JvmStatic
    fun endRun(name: String?, record: Record?) {
        record ?: return
        record.history.lastOrNull { it.name == name }?.endAt = System.currentTimeMillis()
        record.runCnt++
    }

    class Record {
        var type: String = ""
        var prefix: String = ""
        var startCnt: Int = 0
        var runCnt: Int = 0
        val history = ArrayList<History>()
        var createdAt: Long = 0
        var lastRunAt: Long = 0
        val avgCost
            get() = if (history.size == 0) -1 else history.sumOf { it.cost } / runCnt.coerceAtLeast(
                1
            ).toDouble()

        override fun toString(): String {
            val finalAvgCost = avgCost
            return "Record(type='$type', prefix='$prefix', start_cnt=$startCnt, run_cnt=$runCnt, avg_cost=${if (finalAvgCost == -1) "None" else finalAvgCost})"
        }
    }

    class History {
        var name: String? = null
        var startAt: Long = 0
        var endAt: Long = 0
        val cost: Long
            get() {
                return if (endAt == 0L || startAt == 0L) 0 else (endAt - startAt)
            }

        override fun toString(): String {
            return "History(name=$name, startAt=$startAt, endAt=$endAt, cost=$cost)"
        }
    }
}