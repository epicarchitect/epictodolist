package kolmachikhin.alexander.epictodolist.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object IntListConverter {

    @TypeConverter
    fun toJson(list: ArrayList<Int>): String {
        return Gson().toJson(list)
    }

    
    @TypeConverter
    fun fromJson(json: String): ArrayList<Int> {
        val type = object : TypeToken<ArrayList<Int>>() {}.type
        return Gson().fromJson(json, type)
    }
}