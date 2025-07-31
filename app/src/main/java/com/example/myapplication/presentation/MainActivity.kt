/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.example.myapplication.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.health.services.client.HealthServices
import androidx.health.services.client.ExerciseClient
import androidx.health.services.client.data.ExerciseCapabilities
import androidx.health.services.client.data.ExerciseType
import kotlinx.coroutines.launch

import kotlinx.coroutines.guava.await

class MainActivity : ComponentActivity() {

    private lateinit var exerciseClient: ExerciseClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exerciseClient = HealthServices.getClient(this).exerciseClient

        lifecycleScope.launch {
            val capabilities = exerciseClient.getCapabilitiesAsync().await()

            val dataTypes = capabilities
                .getExerciseTypeCapabilities(ExerciseType.RUNNING)
                .supportedDataTypes

            Log.d("REALTIME_DATA", "=== 실시간으로 수집 가능한 센서 목록 ===")
            for (type in dataTypes) {
                Log.d("REALTIME_DATA", "- ${type.name}")
            }
        }
    }
}