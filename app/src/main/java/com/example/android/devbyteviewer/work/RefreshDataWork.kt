/*
 * Copyright 2018, The Android Open Source Project
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
 *
 */

package com.example.android.devbyteviewer.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Logger
import androidx.work.WorkerParameters
import com.example.android.devbyteviewer.database.getDatabase
import com.example.android.devbyteviewer.repository.VideoRepository
import timber.log.Timber
import java.lang.Exception

class RefreshDataWork(appContext: Context, params: WorkerParameters) :
        CoroutineWorker(appContext, params) {

    companion object {

        const val WORK_NAME = "RefreshWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = VideoRepository(database)
        return try {

            repository.refreshVideos()
            Timber.d("refreshVideos() call in worker succeed")
            Result.success()
        } catch (ex: Exception) {
            Timber.d("refreshVideos() call in worker failed, going to retry")
            Result.retry()
        }
    }

}
