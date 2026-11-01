package kolmachikhin.alexander.epictodolist.ui.creator

import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.logic.billing.BillingLogic
import kolmachikhin.alexander.epictodolist.ui.Dialog
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.messages.MessageFragment
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class SupportCreatorDialog : Dialog<Any?>(R.layout.support_creator_dialog) {

    private lateinit var buttonBuy: Button
    private lateinit var checkBoxWithoutGift: CheckBox
    private lateinit var tvGiveCoffee: TextView
    private lateinit var layoutCrystals: LinearLayout
    private var onBuyListener: OnBuyListener? = null

    override fun findViews() {
        super.findViews()
        buttonBuy = find(R.id.button_buy)
        checkBoxWithoutGift = find(R.id.checkbox_without_gift)
        tvGiveCoffee = find(R.id.tv_give_coffee)
        layoutCrystals = find(R.id.layout_crystals)
    }

    override fun start() {
        super.start()
        tvTitle?.text = MainActivity.ui!!.getString(R.string.support_creator)
        checkBoxWithoutGift.setOnCheckedChangeListener { _, isChecked ->
            tvGiveCoffee.isVisible = !isChecked
            layoutCrystals.isVisible = !isChecked
        }

        set(buttonBuy) {
            MainActivity.core!!.billingLogic.buySupportCreator(
                onPurchaseListener = object : BillingLogic.OnPurchaseListener {
                    override fun onPurchase() {
                        closeDialog()
                        onBuyListener?.onBuy()
                    }

                    override fun onFailed(message: String) {
                        MessageFragment.show(
                            message = MainActivity.ui!!.getString(R.string.error) + ":\n$message"
                        )
                    }
                },
                withoutGift = checkBoxWithoutGift.isChecked
            )
        }
    }

    fun interface OnBuyListener {
        fun onBuy()
    }

    companion object {

        fun open(onBuyListener: OnBuyListener?) {
            val d = SupportCreatorDialog()
            d.onBuyListener = onBuyListener
            d.openDialog()
        }
    }
}