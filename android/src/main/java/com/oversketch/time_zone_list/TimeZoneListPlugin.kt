package com.oversketch.time_zone_list

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.util.*

/** TimeZoneListPlugin  */
class TimeZoneListPlugin : FlutterPlugin, MethodCallHandler {
    private var channel: MethodChannel? = null
    override fun onAttachedToEngine(flutterPluginBinding: FlutterPluginBinding) {
        MethodChannel(flutterPluginBinding.binaryMessenger, "time_zone_list").setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        if (call.method == CALL_GET_TIME_ZONE_LIST) {
            val ids = TimeZone.getAvailableIDs()
            val list: MutableList<Map<String, Any>?> = MutableList(ids.size) { null }
            for (i in ids.indices) {
                val id = ids[i]
                val tz = TimeZone.getTimeZone(id)
                list[i] = mapOf(
                        "tag" to id,
                        "secondsFromGMT" to tz.rawOffset / 1000,
                        "dst" to tz.dstSavings / 1000
                )
            }
            result.success(mapOf("list" to list))
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
        channel!!.setMethodCallHandler(null)
    }

    companion object {
        private const val CALL_GET_TIME_ZONE_LIST = "getTimeZoneList"
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "time_zone_list")
            channel.setMethodCallHandler(TimeZoneListPlugin())
        }
    }
}