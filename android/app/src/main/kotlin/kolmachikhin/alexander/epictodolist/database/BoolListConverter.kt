package kolmachikhin.alexander.epictodolist.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object BoolListConverter {

    
    @TypeConverter
    fun toJson(list: ArrayList<Boolean>): String {
        return Gson().toJson(list)
    }

    
    @TypeConverter
    fun fromList(json: String): ArrayList<Boolean> {
        val type = object : TypeToken<ArrayList<Boolean>>() {}.type
        return Gson().fromJson(json, type)
    }
}