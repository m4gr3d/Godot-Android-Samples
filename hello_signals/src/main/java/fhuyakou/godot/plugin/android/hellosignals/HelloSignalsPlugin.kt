package fhuyakou.godot.plugin.android.hellosignals

import android.util.Log
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Exposes a [onButtonPressed] method to the game logic. Invoking the method starts a timer which
 * fires a `TikTok` signal every second. Invoking the method a second time stops the timer.
 */
class HelloSignalsPlugin(godot: Godot) : GodotPlugin(godot) {

  companion object {
    val TAG = HelloSignalsPlugin::class.java.simpleName
  }

  private val tikTokSignalInfo = SignalInfo("TikTok")
  private var tikTokTask : ScheduledFuture<*>? = null

  override fun getPluginName() = "HelloSignals"

  override fun getPluginSignals(): Set<SignalInfo> {
    Log.i(TAG, "Registering $tikTokSignalInfo")
    return setOf(tikTokSignalInfo)
  }

  override fun getPluginMethods() = listOf("onButtonPressed")

  private fun startTikTok(): Boolean {
    if (tikTokTask == null || tikTokTask!!.isDone) {
      Log.i(TAG, "Starting tiktok...")
      tikTokTask = Executors.newSingleThreadScheduledExecutor()
        .scheduleAtFixedRate({ emitSignal(tikTokSignalInfo.name) }, 0, 1, TimeUnit.SECONDS)
      return true
    }
    return false
  }

  private fun stopTikTok() {
    if (tikTokTask != null) {
      if (!tikTokTask!!.isDone) {
        Log.i(TAG, "Stopping tiktok...")
        tikTokTask!!.cancel(true)
      }
      tikTokTask = null
    }
  }

  private fun onButtonPressed() {
    Log.i(TAG, "OnButtonPressed from Kotlin")
    if (!startTikTok()) {
      stopTikTok()
    }
  }
}