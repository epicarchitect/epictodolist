package kolmachikhin.alexander.epictodolist.logic

class Observer<M> {

    private val listeners = HashMap<Int, ArrayList<Listener<M>>>()

    fun addListener(event: Int, l: Listener<M>): Observer<M> {
        var list = listeners[event]
        if (list == null) {
            list = ArrayList()
            listeners[event] = list
        }

        list.add(l)
        return this
    }

    private fun getListeners(event: Int): ArrayList<Listener<M>> {
        val list = listeners[event]
        return list ?: ArrayList()
    }

    fun notify(event: Int, m: M) {
        for (l in getListeners(event)) l.notify(m)
    }

    fun interface Listener<M> {
        fun notify(m: M)
    }
}