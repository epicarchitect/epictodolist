package kolmachikhin.alexander.epictodolist.ui.products

import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.Model.Companion.isVoid
import kolmachikhin.alexander.epictodolist.database.products.ProductModel
import kolmachikhin.alexander.epictodolist.database.products.ProductModel.Companion.getCountPartsPercent
import kolmachikhin.alexander.epictodolist.database.products.ProductModel.Companion.isUnlocked
import kolmachikhin.alexander.epictodolist.logic.products.ProductType
import kolmachikhin.alexander.epictodolist.logic.products.ProductType.toString
import kolmachikhin.alexander.epictodolist.storage.products.LocationsStorage
import kolmachikhin.alexander.epictodolist.ui.Dialog
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.messages.MessageFragment
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class ProductDialog : Dialog<ProductModel>(R.layout.product_dialog) {

    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgressCount: TextView
    private lateinit var tvContent: TextView
    private lateinit var tvCoins: TextView
    private lateinit var tvCrystals: TextView
    private lateinit var ivIcon: ImageView
    private lateinit var buttonUnlock: Button
    private lateinit var buttonActivate: Button
    private lateinit var buttonTryOn: Button
    var listener: Listener? = null
    var from = 0

    override fun findViews() {
        super.findViews()
        progressBar = find(R.id.progress_bar)
        tvProgressCount = find(R.id.progress_count)
        tvContent = find(R.id.content)
        tvCoins = find(R.id.coins)
        tvCrystals = find(R.id.crystals)
        ivIcon = find(R.id.icon)
        buttonActivate = find(R.id.button_activate)
        buttonUnlock = find(R.id.button_unlock)
        buttonTryOn = find(R.id.button_try_on)
    }

    @SuppressLint("SetTextI18n")
    override fun start() {
        super.start()
        set(buttonUnlock) { unlockPart() }
        set(buttonActivate) { activate() }
        set(buttonTryOn) { tryOn() }
        tvTitle?.text = toString(model!!.type)
        tvContent.text = model!!.title
        tvCoins.text = model!!.price.coins.toString()
        tvCrystals.text = model!!.price.crystals.toString()
        ivIcon.setImageResource(model!!.iconRes)
        progressBar.progress = getCountPartsPercent(model!!.countParts, model!!.needCountParts)
        if (isUnlocked(model!!)) {
            tvProgressCount.text = MainActivity.ui!!.getString(R.string.unlocked)
        } else {
            tvProgressCount.text = model!!.countParts.toString() + "/" + model!!.needCountParts
        }

        when (from) {
            FROM_INVENTORY -> {
                if (isUnlocked(model!!)) {
                    buttonUnlock.visibility = View.GONE
                    buttonTryOn.visibility = View.GONE

                    if (model!!.type == ProductType.FEATURE) {
                        buttonActivate.visibility = View.GONE
                    } else {
                        buttonActivate.visibility = View.VISIBLE
                    }
                } else {
                    buttonUnlock.visibility = View.VISIBLE
                    if (model!!.type == ProductType.FEATURE) {
                        buttonTryOn.visibility = View.GONE
                    } else {
                        buttonTryOn.visibility = View.VISIBLE
                    }
                    buttonActivate.visibility = View.GONE
                }
            }
            FROM_ACTIVE -> {
                buttonUnlock.visibility = View.GONE
                buttonActivate.visibility = View.VISIBLE
                buttonActivate.text = MainActivity.ui!!.getString(R.string.take_off)
                buttonTryOn.visibility = View.GONE
                set(buttonActivate) { deactivate() }
            }
        }
    }

    private fun unlockPart() {
        if (MainActivity.core!!.productsLogic.unlockPart(model!!)) {
            listener?.onDone(ACTION_UNLOCK_PART, model!!)
        }
        closeDialog()
    }

    fun activate() {
        MainActivity.core!!.productsLogic.activate(model!!)
        listener?.onDone(ACTION_ACTIVATE, model!!)
        closeDialog()
    }

    private fun deactivate() {
        if (model!!.type == ProductType.LOCATION && model!!.id == LocationsStorage.DEFAULT_ID) {
            MessageFragment.show(MainActivity.ui!!.getString(R.string.it_cant_be_removed))
        } else if (isVoid(model!!)) {
            MessageFragment.show(MainActivity.ui!!.getString(R.string.it_cant_be_removed))
        } else {
            MainActivity.core!!.productsLogic.deactivate(model!!)
            listener?.onDone(ACTION_DEACTIVATE, model!!)
        }
        closeDialog()
    }

    private fun tryOn() {
        listener?.onDone(ACTION_TRY_ON, model!!)
        closeDialog()
    }

    override fun closeDialog() {
        buttonActivate.isClickable = false
        buttonUnlock.isClickable = false
        super.closeDialog()
    }

    fun interface Listener {
        fun onDone(action: Int, product: ProductModel)
    }

    companion object {
        const val FROM_INVENTORY = 0
        const val FROM_ACTIVE = 1
        const val ACTION_ACTIVATE = 0
        const val ACTION_DEACTIVATE = 1
        const val ACTION_UNLOCK_PART = 2
        const val ACTION_TRY_ON = 3

        

        fun open(product: ProductModel?, from: Int, listener: Listener? = null) {
            val dialog = ProductDialog()
            dialog.model = product
            dialog.from = from
            dialog.listener = listener
            dialog.openDialog()
            //CreatorLearnDialog.openIfNotLearned(LearnMessageStorage.PRODUCT_DIALOG);
        }
    }
}