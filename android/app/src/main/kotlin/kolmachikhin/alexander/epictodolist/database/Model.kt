package kolmachikhin.alexander.epictodolist.database

import androidx.room.PrimaryKey

open class Model {


    @PrimaryKey
    var id = -1

    constructor(id: Int) {
        this.id = id
    }

    constructor()

    companion object {
        
        fun isVoid(m: Model): Boolean {
            return m.id == -1
        }
    }
}