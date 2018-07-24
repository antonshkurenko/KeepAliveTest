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

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.CheckBox

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    PreferenceManager.getDefaultSharedPreferences(this@MainActivity).getBoolean(
        "push_via_broadcast", false)
        .apply {
          findViewById<CheckBox>(R.id.switch_control).isChecked = this
        }

    findViewById<CheckBox>(R.id.switch_control).setOnCheckedChangeListener { _, b ->
      PreferenceManager.getDefaultSharedPreferences(this@MainActivity).run {
        edit().putBoolean("push_via_broadcast", b).apply()
      }

      runOnUiThread {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
          Log.i(KeepAliveService.TAG, "stopService in onCreate app")
          stopService(
              Intent(applicationContext, KeepAliveService::class.java))
        } else {
          Log.w(KeepAliveService.TAG, "Oreo is not supported")
        }
      }
    }
  }
}
