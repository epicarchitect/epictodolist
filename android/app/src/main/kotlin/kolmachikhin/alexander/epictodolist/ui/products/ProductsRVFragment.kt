package kolmachikhin.alexander.epictodolist.ui.products

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.products.ProductModel
import kolmachikhin.alexander.epictodolist.database.products.ProductModel.Companion.getCountPartsPercent
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.recyclerview.RVFragment
import kolmachikhin.alexander.epictodolist.ui.recyclerview.RVViewHolder
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class ProductsRVFragment(container: ViewGroup) : RVFragment<ProductModel, ProductsRVFragment.ViewHolder>(
    R.layout.product_item,
    RecyclerView.HORIZONTAL,
    container
) {

    var activateListener: ActivateListener? = null
    var tryOnListener: TryOnListener? = null

    fun interface ActivateListener {
        fun onActivate(product: ProductModel?)
    }

    fun interface TryOnListener {
        fun onTryOn(product: ProductModel?)
    }

    override fun newViewHolder(v: View) = ViewHolder(v)

    inner class ViewHolder(v: View) : RVViewHolder<ProductModel>(v) {
        private lateinit var product: ProductModel
        private var progressBar: ProgressBar = find(R.id.progress_bar)
        private var mainLayout: FrameLayout = find(R.id.main_layout)
        private var title: TextView = find(R.id.title)
        private var icon: ImageView = find(R.id.icon)

        init {
            set(mainLayout) {
                ProductDialog.open(list[adapterPosition], ProductDialog.FROM_INVENTORY) { action, model ->
                    list[adapterPosition] = model
                    when (action) {
                        ProductDialog.ACTION_UNLOCK_PART -> {
                            progressBar.progress = getCountPartsPercent(model.countParts, model.needCountParts)
                        }
                        ProductDialog.ACTION_ACTIVATE -> {
                            activateListener?.onActivate(model)
                        }
                        ProductDialog.ACTION_TRY_ON -> {
                            tryOnListener?.onTryOn(model)
                        }
                    }
                }
            }
        }

        override fun setData(m: ProductModel) {
            this.product = m
            title.text = product.title
            icon.setImageResource(product.iconRes)
            progressBar.progress = getCountPartsPercent(product.countParts, product.needCountParts)
            if (adapterPosition == 0) {
                val params = itemView.layoutParams as RecyclerView.LayoutParams
                params.marginStart = MainActivity.ui!!.dp(4)
                itemView.layoutParams = params
            }
        }
    }
}