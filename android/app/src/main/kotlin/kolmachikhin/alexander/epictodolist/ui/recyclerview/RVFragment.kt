package kolmachikhin.alexander.epictodolist.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.animations.Animations
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.UIFragment

abstract class RVFragment<M, VH : RVViewHolder<M>>  constructor(
    var itemLayoutRes: Int,
    var orientation: Int,
    container: ViewGroup,
    var bottomPadding: Int = 0
) : UIFragment(R.layout.rv_fragment, container) {

    lateinit var tvOnEmptyMessage: TextView
    lateinit var rv: RecyclerView
    lateinit var rva: RecyclerView.Adapter<VH>
    
    var list = ArrayList<M>()
    var onEmptyMessage = MainActivity.ui!!.getString(R.string.empty)

    fun size() = list.size

    override fun findViews() {
        rv = find(R.id.rv)
        tvOnEmptyMessage = find(R.id.tv_on_empty_message)
    }

    override fun start() {
        setRv()
        tvOnEmptyMessage.text = onEmptyMessage
    }

    open fun setRv() {
        rv.layoutManager = LinearLayoutManager(context, orientation, false)
        rva = RVA()
        rv.adapter = rva
        if (orientation == RecyclerView.VERTICAL) {
            rv.clipToPadding = false
            rv.setPadding(0, 0, 0, bottomPadding)
        }
        checkEmptyMessage()
    }

    fun checkEmptyMessage() {
        if (list.size == 0) {
            Animations.showView(tvOnEmptyMessage, null)
        } else {
            if (tvOnEmptyMessage.alpha > 0) {
                Animations.hideView(tvOnEmptyMessage, null)
            }
        }
    }

    abstract fun newViewHolder(v: View): VH

    inner class RVA : RecyclerView.Adapter<VH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = newViewHolder(
            LayoutInflater.from(context).inflate(itemLayoutRes, parent, false)
        )

        override fun onBindViewHolder(holder: VH, position: Int) = holder.setData(list[holder.adapterPosition])

        override fun getItemCount() = list.size
    }


    fun addItem(m: M, position: Int = 0) {
        try {
            list.add(position, m)
            rva.notifyItemInserted(position)
            rva.notifyItemRangeChanged(position, rva.itemCount)
            rv.scrollToPosition(position)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        checkEmptyMessage()
    }

    fun updateItem(m: M, position: Int) {
        try {
            list[position] = m
            rva.notifyItemChanged(position)
            rv.scrollToPosition(position)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        checkEmptyMessage()
    }

    fun deleteItem(position: Int) {
        try {
            list.removeAt(position)
            rva.notifyItemRemoved(position)
            rva.notifyItemRangeChanged(position, rva.itemCount)
            rv.scrollToPosition(position)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        checkEmptyMessage()
    }
}