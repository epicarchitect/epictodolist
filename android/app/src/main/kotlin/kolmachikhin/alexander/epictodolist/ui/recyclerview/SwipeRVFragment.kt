package kolmachikhin.alexander.epictodolist.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.MainActivity

abstract class SwipeRVFragment<M, VH : RVViewHolder<M>>  constructor(
    itemLayoutRes: Int,
    orientation: Int,
    container: ViewGroup,
    bottomPadding: Int = 0
) : RVFragment<M, VH>(itemLayoutRes, orientation, container, bottomPadding) {

    override fun setRv() {
        rv.layoutManager = LinearLayoutManager(context)
        rva = SwipeRVA()
        rv.adapter = rva

        if (orientation == RecyclerView.VERTICAL) {
            rv.clipToPadding = false
            rv.setPadding(0, 0, 0, MainActivity.ui!!.dp(80))
        }

        checkEmptyMessage()
    }

    fun closeAllItems() {
        (rva as SwipeRVFragment<*, *>.SwipeRVA).mItemManger.closeAllItems()
    }

    fun bindSwipeView(holder: VH) {
        (rva as SwipeRVFragment<*, *>.SwipeRVA).mItemManger.bindView(holder.itemView, holder.adapterPosition)
    }

    inner class SwipeRVA : RecyclerSwipeAdapter<VH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = newViewHolder(
            LayoutInflater.from(context).inflate(itemLayoutRes, parent, false)
        )

        override fun onBindViewHolder(holder: VH, position: Int) = holder.setData(list[holder.adapterPosition])

        override fun getItemCount() = list.size

        override fun getSwipeLayoutResourceId(position: Int) = R.id.swipe_layout

    }
}