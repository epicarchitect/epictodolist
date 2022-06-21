package kolmachikhin.alexander.epictodolist.logic

import kolmachikhin.alexander.epictodolist.database.Model

abstract class Logic(protected var core: Core) {

    
    protected var context = core.applicationContext

    var isReady = false
        protected set

    private var onReadyListener: OnReadyListener? = null

    fun interface OnReadyListener {
        fun onReady()
    }

    fun setOnReadyListener(onReadyListener: OnReadyListener?) {
        this.onReadyListener = onReadyListener
    }

    abstract fun postInit()

    fun <T : Model?> findModel(list: ArrayList<T>, id: Int): T? {
        for (m in list) if (m!!.id == id) return m
        return null
    }

    fun getString(res: Int): String {
        return context.getString(res)
    }

    abstract fun attachRef()

    fun ready() {
        isReady = true
        if (onReadyListener != null) {
            onReadyListener!!.onReady()
            onReadyListener = null
        }
    }

    companion object {

        
        fun <T : Model> sort(list: ArrayList<T>, reverse: Boolean) {
            if (reverse) {
                list.sortByDescending { it.id }
            } else {
                list.sortBy { it.id }
            }
        }
    }
}