package kolmachikhin.alexander.epictodolist.logic.dataimport

import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import kolmachikhin.alexander.epictodolist.App.Companion.runOnBackgroundThread
import kolmachikhin.alexander.epictodolist.App.Companion.runOnMainThread
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.logic.Logic
import java.io.BufferedReader
import java.io.InputStreamReader

class DataImportLogic(core: Core) : Logic(core) {

    fun importData(uri: Uri, callback: ImportDataCallback) {
        runOnBackgroundThread {
            try {
                val stream = context.contentResolver.openInputStream(uri)
                val bufferedReader = BufferedReader(InputStreamReader(stream))
                val stringBuilder = StringBuilder()
                var line: String?

                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }

                core.appStateLogic.applyAppStateFromJson(
                    Gson().fromJson(stringBuilder.toString(), JsonObject::class.java)
                )

                runOnMainThread { callback.onCompleted() }
            } catch (throwable: Throwable) {
                Log.d("test123", throwable.stackTraceToString())
                runOnMainThread { callback.onError(throwable) }
            }
        }
    }

    override fun postInit() {}

    override fun attachRef() {
        ready()
    }

    interface ImportDataCallback {
        fun onCompleted()
        fun onError(throwable: Throwable)
    }
}