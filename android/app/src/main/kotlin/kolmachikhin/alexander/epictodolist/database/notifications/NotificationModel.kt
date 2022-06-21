package kolmachikhin.alexander.epictodolist.database.notifications

import androidx.room.Entity
import kolmachikhin.alexander.epictodolist.database.Model

@Entity(tableName = "notifications")
class NotificationModel : Model {

    var title: String? = ""
    var content: String? = ""
    var time: Long = -1

    constructor(id: Int, title: String, content: String, time: Long) : super(id) {
        this.title = title
        this.content = content
        this.time = time
    }

    constructor()

}