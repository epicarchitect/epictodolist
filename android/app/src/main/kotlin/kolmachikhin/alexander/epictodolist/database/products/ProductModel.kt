package kolmachikhin.alexander.epictodolist.database.products

import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.storage.products.ProductsStorage

class ProductModel : IncompleteProductModel {

    
    var title = "..."
    
    var price = Price()
    
    var iconRes = R.drawable.ic_void
    
    var needCountParts = 0

    constructor(
        id: Int,
        title: String,
        priceCoins: Int,
        priceCrystals: Int,
        type: Int,
        iconRes: Int,
        countParts: Int,
        needCountParts: Int
    ) : super(id, type, countParts) {
        this.title = title
        price = Price(priceCoins, priceCrystals)
        this.iconRes = iconRes
        this.needCountParts = needCountParts
    }

    constructor(id: Int, type: Int, countParts: Int) : this(ProductsStorage.get(id, type)) {
        this.countParts = countParts
    }

    constructor(p: ProductModel) : this(p.id, p.title, p.price.coins, p.price.crystals, p.type, p.iconRes, p.countParts, p.needCountParts)

    constructor()

    companion object {

        
        fun isUnlocked(p: ProductModel): Boolean {
            return p.countParts >= p.needCountParts
        }

        
        fun getCountPartsPercent(count: Int, needCountParts: Int): Int {
            return if (count == 0) 0 else count * 100 / needCountParts
        }
    }
}