package kolmachikhin.alexander.epictodolist.ui.drag

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kolmachikhin.alexander.epictodolist.database.Model
import kolmachikhin.alexander.epictodolist.ui.drag.DragHelper.OnMoveListener
import kolmachikhin.alexander.epictodolist.ui.recyclerview.RVFragment
import java.util.*

object DragHelper {

    private fun <M> createHelper(
        rv: RVFragment<M, *>,
        onMoveListener: OnMoveListener = OnMoveListener { _, _ -> }
    ) = ItemTouchHelper(
        object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                Collections.swap(rv.list, from, to)
                rv.rva.notifyItemMoved(from, to)
                onMoveListener.onMove(from, to)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
        }
    )

    fun <M> addDrag(
        rv: RVFragment<M, *>,
        onMoveListener: OnMoveListener = OnMoveListener { _, _ -> }
    ) = createHelper(rv, onMoveListener).apply {
        attachToRecyclerView(rv.rv)
    }

    fun <M : Model> addDrag(
        rv: RVFragment<M, *>,
        manager: ModelOrderManager<M>
    ) = addDrag(rv) { _, _ ->
        manager.save(rv.list)
    }

    fun <M : Model> addDrag(
        rv: RVFragment<M, *>,
        manager: ModelOrderManager<M>,
        onSaveListener: Runnable
    ) = addDrag(rv) { _, _ ->
        manager.save(rv.list)
        onSaveListener.run()
    }

    fun interface OnMoveListener {
        fun onMove(fromPosition: Int, toPosition: Int)
    }
}