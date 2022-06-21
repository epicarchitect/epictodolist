package kolmachikhin.alexander.epictodolist.database.hero

import kolmachikhin.alexander.epictodolist.storage.products.CostumesStorage
import kolmachikhin.alexander.epictodolist.storage.products.LocationsStorage
import kolmachikhin.alexander.epictodolist.storage.products.WeaponStorage
import kolmachikhin.alexander.epictodolist.util.progress.ImprovableModel

class HeroModel : ImprovableModel {


    var name = ""

    var gender = 0
    @JvmField
    var coins = 0
    @JvmField
    var crystals = 0
    @JvmField
    var strength = 0
    @JvmField
    var intellect = 0
    @JvmField
    var creation = 0
    @JvmField
    var health = 0
    @JvmField
    var location = LocationsStorage.DEFAULT_ID
    @JvmField
    var backWeapon = WeaponStorage.DEFAULT_ID
    @JvmField
    var ears = 0
    @JvmField
    var body = CostumesStorage.DEFAULT_ID
    @JvmField
    var extras = 0
    @JvmField
    var beard = 0
    @JvmField
    var eyes = 0
    @JvmField
    var brows = 0
    @JvmField
    var hair = 0
    @JvmField
    var colorBody = 0
    @JvmField
    var colorHair = 0

    constructor(
        name: String,
        progress: Int,
        gender: Int,
        coins: Int,
        crystals: Int,
        strength: Int,
        intellect: Int,
        creation: Int,
        health: Int,
        location: Int,
        backWeapon: Int,
        ears: Int,
        body: Int,
        extras: Int,
        beard: Int,
        eyes: Int,
        brows: Int,
        hair: Int,
        colorBody: Int,
        colorHair: Int
    ) : super(HERO_ID, progress) {
        this.name = name
        this.gender = gender
        this.coins = coins
        this.crystals = crystals
        this.strength = strength
        this.intellect = intellect
        this.creation = creation
        this.health = health
        this.location = location
        this.backWeapon = backWeapon
        this.ears = ears
        this.body = body
        this.extras = extras
        this.beard = beard
        this.eyes = eyes
        this.brows = brows
        this.hair = hair
        this.colorBody = colorBody
        this.colorHair = colorHair
    }

    constructor(h: HeroModel) : this(
        h.name,
        h.progress,
        h.gender,
        h.coins,
        h.crystals,
        h.strength,
        h.intellect,
        h.creation,
        h.health,
        h.location,
        h.backWeapon,
        h.ears,
        h.body,
        h.extras,
        h.beard,
        h.eyes,
        h.brows,
        h.hair,
        h.colorBody,
        h.colorHair
    )

    constructor() : super(HERO_NOT_CREATED_ID, 0)

    companion object {
        const val HERO_ID = 0
        const val HERO_NOT_CREATED_ID = -2

        fun isCreated(h: HeroModel): Boolean {
            return h.id == HERO_ID
        }
    }
}