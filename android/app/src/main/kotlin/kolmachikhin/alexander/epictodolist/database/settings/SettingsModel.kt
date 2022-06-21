package kolmachikhin.alexander.epictodolist.database.settings

import kolmachikhin.alexander.epictodolist.database.Model

class SettingsModel : Model {

    
    var showTitleNav = true
    
    var showNewTaskNotification = true
    
    var lang = "-1"
    
    var showHeaddress = true

    constructor(
        showTitleNav: Boolean,
        showNewTaskNotification: Boolean,
        lang: String,
        showHeaddress: Boolean
    ) : super(SETTINGS_ID) {
        this.showTitleNav = showTitleNav
        this.showNewTaskNotification = showNewTaskNotification
        this.lang = lang
        this.showHeaddress = showHeaddress
    }

    constructor(s: SettingsModel) : this(s.showTitleNav, s.showNewTaskNotification, s.lang, s.showHeaddress)

    constructor() : super(SETTINGS_ID)

    companion object {
        const val SETTINGS_ID = 0
    }
}