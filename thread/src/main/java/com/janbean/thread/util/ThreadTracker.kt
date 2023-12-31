package com.janbean.thread.util

import android.util.Log
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

object ThreadTracker {
    var DEBUG = true
    val map by lazy { ConcurrentHashMap<String, Record>() }

    @JvmStatic
    fun getCurrentThread(): List<ThreadInfo> {
        val allStackTraces = Thread.getAllStackTraces()
        return allStackTraces.map {
            val thread = it.key
            ThreadInfo(thread.id, thread.name, thread.state)
        }
    }

    @JvmStatic
    fun trace(type: String, name: String): Record? {
        if (!DEBUG) return null
        val prefix = getPrefix(type, name)
        Log.d("hjianbin", "trace $type $name $prefix")

        val record = map.getOrPut("$type:$prefix") {
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
        if (type == "Thread") {
            return name
        }
        return name.replace(Regex("-\\d+$"), "").replace(Regex("#\\d+$"), "")
    }
    @JvmStatic
    fun startRun(type: String, name: String): Record? {
        val key = "$type:${getPrefix(type, name)}"
        return map[key]?.apply {
            startRun(name, this)
        }
    }

    @JvmStatic
    fun startRun(name: String?, record: Record?) {
        Log.d("hjianbin", "startRun $name ${record?.type} ${record?.prefix}")
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
        val history = CopyOnWriteArrayList<History>()
        var createdAt: Long = 0
        var lastRunAt: Long = 0
        var corePoolSize: Int? = null
        var maximumPoolSize: Int? = null
        var keepAliveTime: Long? = null
        var allowCoreThreadTimeout: Boolean? = null

        val avgCost
            get() = if (history.size == 0) -1 else history.sumOf { it.cost } / runCnt.coerceAtLeast(
                1
            ).toDouble()

        override fun toString(): String {
            val finalAvgCost = avgCost
            return if (corePoolSize == null) {
                "Record(type='$type', prefix='$prefix', start_cnt=$startCnt, run_cnt=$runCnt, avg_cost=${if (finalAvgCost == -1) "None" else finalAvgCost})"
            } else {
                "Record(type='$type', prefix='$prefix', start_cnt=$startCnt, run_cnt=$runCnt, avg_cost=${if (finalAvgCost == -1) "None" else finalAvgCost} corePoolSize=$corePoolSize maximumPoolSize=$maximumPoolSize keepAliveTime=$keepAliveTime allowCoreThreadTimeout=$allowCoreThreadTimeout)"
            }
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