package kolmachikhin.alexander.epictodolist.database.challenges

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ChallengeTaskListConverter {

    @TypeConverter
    fun toJson(list: ArrayList<ChallengeTaskModel>?): String? = Gson().toJson(list)

    @TypeConverter
    fun fromJson(json: String?): ArrayList<ChallengeTaskModel>? = Gson().fromJson(
        json,
        object : TypeToken<ArrayList<ChallengeTaskModel>>() {}.type
    )
}