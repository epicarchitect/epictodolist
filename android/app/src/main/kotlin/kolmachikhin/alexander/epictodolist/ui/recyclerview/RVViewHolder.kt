package kolmachikhin.alexander.epictodolist.ui.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class RVViewHolder<M>(v: View) : RecyclerView.ViewHolder(v) {

    fun <V : View?> find(id: Int) = itemView.findViewById<V>(id)

    abstract fun setData(m: M)

}