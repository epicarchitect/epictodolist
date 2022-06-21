package kolmachikhin.alexander.epictodolist.database.products

import android.content.Context
import androidx.lifecycle.Transformations
import kolmachikhin.alexander.epictodolist.App
import kolmachikhin.alexander.epictodolist.storage.products.ProductsStorage
import kolmachikhin.alexander.epictodolist.database.AppDatabase

class ProductsRepository internal constructor(context: Context) {

    private val dao = AppDatabase.getInstance(context).incompleteProductsDao()
    val incompleteLiveList = Transformations.map(dao.getLiveList()) { ArrayList(it) }
    val liveList = Transformations.map(incompleteLiveList) { models: java.util.ArrayList<IncompleteProductModel> ->
        val list = ProductsStorage.list
        for (product in list) {
            for (model in models) {
                if (product.id == model.id) {
                    product.countParts = model.countParts
                    break
                }
            }
        }
        list
    }

    fun save(model: IncompleteProductModel) {
        App.runOnBackgroundThread { dao.save(model) }
    }

    fun saveList(list: List<IncompleteProductModel>) {
        App.runOnBackgroundThread { dao.saveList(list) }
    }

    fun delete(model: IncompleteProductModel) {
        App.runOnBackgroundThread { dao.delete(model) }
    }

    companion object {
        var instance: ProductsRepository? = null

        
        fun getInstance(context: Context): ProductsRepository {
            if (instance == null) instance = ProductsRepository(context)
            return instance!!
        }
    }
}