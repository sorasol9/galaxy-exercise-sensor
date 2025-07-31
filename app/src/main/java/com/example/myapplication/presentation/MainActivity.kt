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
            checkExerciseCapabilities()
        }
    }

    private suspend fun checkExerciseCapabilities() {
        try {
            val capabilities: ExerciseCapabilities = exerciseClient.getCapabilitiesAsync().await()

            val supportedExerciseTypes = capabilities.supportedExerciseTypes
            Log.d("EXERCISE", "✅ 지원되는 운동 타입 목록:")
            supportedExerciseTypes.forEach { exerciseType ->
                Log.d("EXERCISE", "• ${exerciseType.name}")

                val typeCapabilities = capabilities.getExerciseTypeCapabilities(exerciseType)
                val supportedDataTypes = typeCapabilities.supportedDataTypes

                Log.d("EXERCISE", "  └ ${exerciseType.name}에서 측정 가능한 실시간 데이터:")
                supportedDataTypes.forEach { dataType ->
                    Log.d("EXERCISE", "    - ${dataType.name}")
                }
            }
        } catch (e: Exception) {
            Log.e("EXERCISE", "💥 오류 발생: ${e.message}", e)
        }
    }
}