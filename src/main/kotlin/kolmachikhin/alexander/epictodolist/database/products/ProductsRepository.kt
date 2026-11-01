package kolmachikhin.alexander.epictodolist.database.products

import android.content.Context
import androidx.lifecycle.map
import kolmachikhin.alexander.epictodolist.App
import kolmachikhin.alexander.epictodolist.database.AppDatabase
import kolmachikhin.alexander.epictodolist.storage.products.ProductsStorage

class ProductsRepository internal constructor(context: Context) {

    private val dao = AppDatabase.getInstance(context).incompleteProductsDao()
    val incompleteLiveList = dao.getLiveList().map { ArrayList(it) }
    val liveList = incompleteLiveList.map { models: ArrayList<IncompleteProductModel> ->
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