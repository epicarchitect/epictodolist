package kolmachikhin.alexander.epictodolist.database.products

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface IncompleteProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(model: IncompleteProductModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveList(list: List<IncompleteProductModel>)

    @Delete
    fun delete(model: IncompleteProductModel)

    @Query("SELECT * FROM incomplete_products")
    fun getLiveList(): LiveData<List<IncompleteProductModel>>

    @Query("SELECT * FROM incomplete_products WHERE id = :id")
    fun getById(id: Int): IncompleteProductModel?
}