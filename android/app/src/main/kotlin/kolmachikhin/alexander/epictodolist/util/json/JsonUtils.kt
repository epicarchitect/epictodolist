package kolmachikhin.alexander.epictodolist.util.json

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken

object JsonUtils {

    fun <M> toJson(m: M) = try {
        Gson().toJson(m)!!
    } catch (t: Throwable) {
        t.printStackTrace()
        ""
    }

    fun <M> fromJson(json: String, cls: Class<M>) = Gson().fromJson(json, cls)!!

    fun intListFromJsonArray(array: JsonArray): ArrayList<Int> {
        val listType = object : TypeToken<ArrayList<Int>>() {}.type
        return ArrayList(Gson().fromJson<Collection<Int>>(array, listType))
    }

    fun boolListFromJsonArray(array: JsonArray): ArrayList<Boolean> {
        val listType = object : TypeToken<ArrayList<Boolean>>() {}.type
        return Gson().fromJson(array, listType)
    }

    fun intListToJsonArray(list: List<Int>): JsonArray {
        val array = JsonArray()
        for (i in list) {
            array.add(i)
        }
        return array
    }

    fun boolListToJsonArray(list: List<Boolean>): JsonArray {
        val array = JsonArray()
        for (i in list) {
            array.add(i)
        }
        return array
    }
}