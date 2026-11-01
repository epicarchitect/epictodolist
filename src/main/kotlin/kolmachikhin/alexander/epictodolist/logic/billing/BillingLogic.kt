package kolmachikhin.alexander.epictodolist.logic.billing

import androidx.lifecycle.MutableLiveData
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.logic.Logic

class BillingLogic(core: Core) : Logic(core) {

//    private lateinit var billingClient: BillingClient
//    private var onPurchaseListener: OnPurchaseListener? = null
//    private var buyWithoutGift = false

    val isAvailable = MutableLiveData(false)

    init {
//        billingClient = BillingClient.newBuilder(context)
//            .enablePendingPurchases()
//            .setListener { billingResult, purchases ->
//                Core.log("BillingLogic::PurchaseUpdatedListener: " + billingResult.responseCode)
//                if (!purchases.isNullOrEmpty()) {
//                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//                        if (!buyWithoutGift) {
//                            val hero = core.heroLogic.hero
//                            hero.crystals += 100
//                            core.heroLogic.update(hero)
//                        }
//
//                        if (onPurchaseListener != null) {
//                            onPurchaseListener!!.onPurchase()
//                            onPurchaseListener = null
//                        }
//                    }
//
//                    val purchase = purchases.first()
//                    val params = ConsumeParams.newBuilder()
//                        .setPurchaseToken(purchase.purchaseToken)
//                        .build()
//
//                    billingClient.consumeAsync(params) { result, _ ->
//                        Core.log("BillingLogic::consumeAsync: " + result.responseCode)
//                    }
//                } else {
//                    Core.log("BillingLogic::PurchaseUpdatedListener, purchases null && purchases.size() > 0 == false")
//                }
//            }.build()
//
//        billingClient.startConnection(object : BillingClientStateListener {
//            override fun onBillingSetupFinished(billingResult: BillingResult) {
//                isAvailable.value = true
//                Core.log("BillingLogic::onBillingSetupFinished: " + billingResult.responseCode)
//            }
//
//            override fun onBillingServiceDisconnected() {
//                isAvailable.value = false
//                Core.log("BillingLogic::onBillingServiceDisconnected")
//            }
//        })
    }

    override fun postInit() {
        ready()
    }

    override fun attachRef() {}

    fun buySupportCreator(onPurchaseListener: OnPurchaseListener?, withoutGift: Boolean) {
//        buyWithoutGift = withoutGift
//        this.onPurchaseListener = onPurchaseListener
//        Core.log("BillingLogic::buySupportCreator")
//
//        val params = QueryProductDetailsParams.newBuilder().setProductList(
//            listOf(
//                QueryProductDetailsParams.Product.newBuilder()
//                    .setProductId(SUPPORT_CREATOR_ID)
//                    .setProductType(BillingClient.ProductType.INAPP)
//                    .build()
//            )
//        ).build()
//
//        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
//            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//                if (productDetailsList.isNotEmpty()) {
//                    val productDetails = productDetailsList.first()
//
//                    val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
//                        .setProductDetails(productDetails)
//                        .build()
//
//                    val billingFlowParams = BillingFlowParams.newBuilder()
//                        .setProductDetailsParamsList(listOf(productDetailsParams))
//                        .build()
//
//                    val responseCode = billingClient.launchBillingFlow(
//                        MainActivity.activity!!,
//                        billingFlowParams
//                    )
//                    Core.log("BillingLogic::launchBillingFlow - responseCode: $responseCode")
//                }
//            } else {
//                onPurchaseListener?.onFailed(billingResult.debugMessage)
//            }
//            Core.log("BillingLogic::querySkuDetailsAsync - responseCode: " + billingResult.responseCode)
//        }
    }

    interface OnPurchaseListener {
        fun onPurchase()
        fun onFailed(message: String)
    }

    companion object {
        const val SUPPORT_CREATOR_ID = "support_creator"
//        const val SUPPORT_CREATOR_ID = "android.test.purchased"
    }
}