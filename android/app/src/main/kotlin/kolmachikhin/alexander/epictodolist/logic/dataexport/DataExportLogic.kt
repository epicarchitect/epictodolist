package kolmachikhin.alexander.epictodolist.logic.dataexport

import android.annotation.SuppressLint
import androidx.core.app.ShareCompat.IntentBuilder
import androidx.core.content.FileProvider
import com.google.gson.GsonBuilder
import kolmachikhin.alexander.epictodolist.App.Companion.runOnBackgroundThread
import kolmachikhin.alexander.epictodolist.App.Companion.runOnMainThread
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.logic.Logic
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import java.io.File
import java.io.FileOutputStream
import java.net.URLConnection
import java.text.DateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class DataExportLogic(core: Core) : Logic(core) {

    fun exportData(callback: ExportDataCallback) {
        runOnBackgroundThread {
            try {
                val formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(Calendar.getInstance().time)

                val json = GsonBuilder()
                    .setPrettyPrinting()
                    .create()
                    .toJson(core.appStateLogic.appStateJson())

                val epicDir = checkNotNull(context.getExternalFilesDir(null)).also {
                    it.mkdirs()
                }

                val file = File(epicDir.path, "Epic State - $formattedDate.json").also {
                    it.createNewFile()
                }

                val fos = FileOutputStream(file)
                fos.write(json.toByteArray())
                fos.close()

                IntentBuilder(MainActivity.activity!!)
                    .setStream(FileProvider.getUriForFile(context, "kolmachikhin.alexander.epictodolist.fileprovider", file))
                    .setType(URLConnection.guessContentTypeFromName(file.name))
                    .startChooser()

                runOnMainThread { callback.onCompleted(file) }
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                runOnMainThread { callback.onError(throwable) }
            }
        }
    }

    override fun postInit() {}

    override fun attachRef() {
        ready()
    }

    interface ExportDataCallback {
        fun onCompleted(file: File?)
        fun onError(throwable: Throwable?)
    }
}