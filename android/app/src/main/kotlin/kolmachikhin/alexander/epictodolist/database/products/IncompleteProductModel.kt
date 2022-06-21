package kolmachikhin.alexander.epictodolist.database.products

import androidx.room.Entity
import kolmachikhin.alexander.epictodolist.database.Model

@Entity(tableName = "incomplete_products")
open class IncompleteProductModel : Model {

    
    var type = -1
    
    var countParts = 0

    constructor()

    constructor(id: Int, type: Int, countParts: Int) {
        this.id = id
        this.type = type
        this.countParts = countParts
    }
}