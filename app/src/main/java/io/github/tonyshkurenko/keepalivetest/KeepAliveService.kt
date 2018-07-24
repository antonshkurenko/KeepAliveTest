/*
 * Copyright 2018 Anton Shkurenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.tonyshkurenko.keepalivetest

import android.app.Service
import android.content.Intent
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import java.util.Timer
import java.util.TimerTask


/**
 * Project: KeepAliveTest
 * Follow me: @tonyshkurenko
 *
 * @author Anton Shkurenko
 * @since 7/24/18
 */
class KeepAliveService : Service() {

  var t: Timer? = null

  companion object {
    val TAG = "KeepAlive"
  }

  override fun onBind(p0: Intent?) = null

  override fun onCreate() {
    super.onCreate()

    Log.i(TAG, "onCreate keep alive service")

    var tick = 0

    t?.cancel()
    t = Timer()
    t!!.scheduleAtFixedRate(object : TimerTask() {
      override fun run() {
        Log.i(TAG, "tick tack: ${tick++}")
      }
    }, 0, 1000)
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_STICKY

  override fun onTaskRemoved(rootIntent: Intent?) {
    super.onTaskRemoved(rootIntent)

    Log.i(TAG, "onTaskRemoved called")
  }

  override fun onDestroy() {
    super.onDestroy()

    t?.cancel()
    Log.i(TAG, "onDestroy keep alive service")

    PreferenceManager.getDefaultSharedPreferences(this).run {

      val broadcast = getBoolean("push_via_broadcast", false)

      if (broadcast) {
        Log.i(TAG, "Send broadcast")
        sendBroadcast(Intent("tonyshkurenko.start"))
      } else {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
          Log.i(KeepAliveService.TAG, "startService in onDestroy")
          startService(
              Intent(applicationContext, KeepAliveService::class.java))
        } else {
          Log.w(KeepAliveService.TAG, "Oreo is not supported")
        }
      }
    }
  }
}