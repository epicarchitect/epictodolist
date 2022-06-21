package kolmachikhin.alexander.epictodolist.ui.drag

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kolmachikhin.alexander.epictodolist.database.Model

class ModelOrderManager<M : Model>(context: Context, tag: String) {

    private var preferences = context.getSharedPreferences(tag, Context.MODE_PRIVATE)


    fun sorted(list: ArrayList<M>, newOnTop: Boolean = true): ArrayList<M> {
        val sortedList = ArrayList<M>()

        for (id in savedOrderIdList) {
            val m = findById(id, list)
            if (m != null) {
                sortedList.add(m)
            }
        }

        if (!newOnTop) list.reverse()

        for (m in list) {
            if (!sortedList.contains(m)) {
                if (newOnTop) {
                    sortedList.add(0, m)
                } else {
                    sortedList.add(m)
                }
            }
        }

        return sortedList
    }

    fun findById(id: Int, list: List<M>): M? {
        for (m in list) if (m.id == id) return m
        return null
    }

    fun save(list: ArrayList<M>) {
        save(toOrderIdList(list))
    }

    fun save(list: List<Int>) {
        preferences.edit().putString(KEY, Gson().toJson(list)).apply()
    }

    val savedOrderIdList: ArrayList<Int>
        get() {
            val json = preferences.getString(KEY, "")
            if (json.isNullOrEmpty()) return ArrayList()
            val type = object : TypeToken<ArrayList<Int>>() {}.type
            return Gson().fromJson(json, type) ?: return ArrayList()
        }

    private fun toOrderIdList(list: ArrayList<M>): ArrayList<Int> {
        val l = ArrayList<Int>()
        for (m in list) l.add(m.id)
        return l
    }

    companion object {
        var KEY = "order"
    }
}