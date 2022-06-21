package kolmachikhin.alexander.epictodolist.logic.sound

import android.content.Context
import android.net.Uri
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.logic.Logic
import kolmachikhin.alexander.epictodolist.logic.Observer
import java.io.File
import java.io.FileOutputStream

class SoundLogic(core: Core) : Logic(core) {

    private val contentResolver = context.contentResolver
    private val preferences = context.getSharedPreferences("SoundLogic", Context.MODE_PRIVATE)
    private val backgroundVolume = MutableLiveData(preferences.getInt("BackgroundVolume", 10))
    private val backgroundSoundPath = getBackgroundSoundFilePath(context)
    val observer = Observer<Any>()

    init {
        val filesDir = context.filesDir
        val soundsDir = File(filesDir.absolutePath + "/sound")
        soundsDir.mkdirs()
    }

    override fun postInit() {}

    override fun attachRef() {
        ready()
    }

    fun updateBackgroundVolume(volume: Int) {
        backgroundVolume.value = volume
        preferences.edit { putInt("BackgroundVolume", volume) }
    }

    fun getBackgroundVolume() = backgroundVolume

    fun updateBackgroundFileFromContentUri(uri: Uri?) {
        val stream = contentResolver.openInputStream(uri!!)!!
        val bytes = ByteArray(stream.available())
        stream.read(bytes)
        val file = File(backgroundSoundPath)
        if (!file.exists()) file.createNewFile()
        val fos = FileOutputStream(file)
        fos.write(bytes)
        fos.close()
        observer.notify(BACKGROUND_SOUND_UPDATED, Any())
    }

    companion object {
        const val BACKGROUND_SOUND_UPDATED = 0

        
        fun getBackgroundSoundFilePath(context: Context): String {
            return context.filesDir.absolutePath + "/sound/background"
        }
    }
}