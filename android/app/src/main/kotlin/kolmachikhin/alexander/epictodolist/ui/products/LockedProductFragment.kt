package kolmachikhin.alexander.epictodolist.ui.products

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.products.ProductModel
import kolmachikhin.alexander.epictodolist.ui.UIFragment
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class LockedProductFragment(container: ViewGroup) : UIFragment(R.layout.locked_product_fragment, container) {

    private lateinit var tvTitle: TextView
    private lateinit var buttonUnlock: Button
    lateinit var product: ProductModel
    var listener: UnlockListener? = null

    override fun findViews() {
        tvTitle = find(R.id.title)
        buttonUnlock = find(R.id.button_unlock)
    }

    override fun start() {
        tvTitle.text = product.title
        set(buttonUnlock) {
            ProductDialog.open(product, ProductDialog.FROM_INVENTORY) { _, _ ->
                listener?.onUnlock(product)
            }
        }
    }

    fun interface UnlockListener {
        fun onUnlock(product: ProductModel)
    }

    companion object {
        
        fun open(product: ProductModel, container: ViewGroup, listener: UnlockListener) {
            val fragment = LockedProductFragment(container)
            fragment.product = product
            fragment.listener = listener
            fragment.add()
        }
    }
}