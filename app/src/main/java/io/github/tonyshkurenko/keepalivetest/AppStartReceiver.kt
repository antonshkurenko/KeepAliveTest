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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log

/**
 * Project: KeepAliveTest
 * Follow me: @tonyshkurenko
 *
 * @author Anton Shkurenko
 * @since 7/24/18
 */
class AppStartReceiver : BroadcastReceiver() {

  override fun onReceive(p0: Context, p1: Intent) {

    Log.i(KeepAliveService.TAG, "onReceive with action ${p1.action}")

    Handler(Looper.getMainLooper()).post {

      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        Log.i(KeepAliveService.TAG, "startService in onReceive")
        p0.applicationContext.startService(
            Intent(p0.applicationContext, KeepAliveService::class.java))
      } else {
        Log.w(KeepAliveService.TAG, "Oreo is not supported")
      }
    }
  }
}