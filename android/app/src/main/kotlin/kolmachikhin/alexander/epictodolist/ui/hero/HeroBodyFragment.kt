package kolmachikhin.alexander.epictodolist.ui.hero

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.devs.vectorchildfinder.VectorChildFinder
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.hero.HeroModel
import kolmachikhin.alexander.epictodolist.storage.hero.*
import kolmachikhin.alexander.epictodolist.storage.products.CostumesStorage
import kolmachikhin.alexander.epictodolist.storage.products.LocationsStorage
import kolmachikhin.alexander.epictodolist.storage.products.WeaponStorage
import kolmachikhin.alexander.epictodolist.ui.animations.HeroAnimations
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.UIFragment

class HeroBodyFragment(container: ViewGroup) : UIFragment(R.layout.hero_body_fragment, container) {

    private lateinit var location: ImageView
    private lateinit var backWeapon: ImageView
    private lateinit var backHair: ImageView
    private lateinit var backHeaddress: ImageView
    private lateinit var ears: ImageView
    private lateinit var body: ImageView
    private lateinit var extras: ImageView
    private lateinit var beard: ImageView
    private lateinit var eyes: ImageView
    private lateinit var brows: ImageView
    private lateinit var hair: ImageView
    private lateinit var headdress: ImageView
    private lateinit var animatedFrame: FrameLayout

    private var mode = HERO
    var isHaveHeaddress = false
        private set

    override fun start() {
        animate()
        if (mode == CREATOR) {
            setPartsForCreator()
        } else {
            setParts(MainActivity.core!!.heroLogic.hero)
        }
    }

    override fun findViews() {
        animatedFrame = find(R.id.animated_frame)
        location = find(R.id.location)
        backWeapon = find(R.id.back_weapon)
        backHair = find(R.id.back_hair)
        backHeaddress = find(R.id.back_headdress)
        ears = find(R.id.ears)
        body = find(R.id.body)
        extras = find(R.id.extras)
        beard = find(R.id.beard)
        eyes = find(R.id.eyes)
        brows = find(R.id.brows)
        hair = find(R.id.hair)
        headdress = find(R.id.headdress)
    }

    private fun animate() {
        animatedFrame.startAnimation(HeroAnimations.breath)
    }

    fun hideHeaddress() {
        headdress.visibility = View.GONE
        backHeaddress.visibility = View.GONE
        hair.visibility = View.VISIBLE
        backHair.visibility = View.VISIBLE
        ears.visibility = View.VISIBLE
    }

    fun showHeaddress() {
        if (isHaveHeaddress) {
            hair.visibility = View.GONE
            backHair.visibility = View.GONE
            ears.visibility = View.GONE
            headdress.visibility = View.VISIBLE
            backHeaddress.visibility = View.VISIBLE
        }
    }

    fun hideLocation() {
        location.visibility = View.GONE
    }

    private fun setPartsForCreator() {
        setParts(
            HeroModel(
                MainActivity.ui!!.getString(R.string.creator),
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                LocationsStorage.HOME_OF_CREATOR,
                WeaponStorage.MAGICIAN_STAFF,
                0,
                CostumesStorage.HERMIT,
                0,
                4,
                8,
                11,
                3,
                5,
                3
            )
        )
        hideHeaddress()
    }

    fun setParts(hero: HeroModel) {
        location.setImageResource(LocationsStorage[hero.location].iconRes)
        backWeapon.setImageResource(BackWeaponStorage[hero.backWeapon])
        backHair.setImageResource(BackHairStorage[hero.gender, hero.hair])
        backHeaddress.setImageResource(BackHeaddressStorage[hero.gender, hero.body])
        ears.setImageResource(EarsStorage[hero.gender, hero.ears])
        body.setImageResource(BodyStorage[hero.gender, hero.body])
        extras.setImageResource(ExtrasStorage[hero.gender, hero.extras])
        beard.setImageResource(BeardStorage[hero.gender, hero.beard])
        eyes.setImageResource(EyesStorage[hero.gender, hero.eyes])
        brows.setImageResource(BrowsStorage[hero.gender, hero.brows])
        hair.setImageResource(HairStorage[hero.gender, hero.hair])
        val headdressRes = HeaddressStorage[hero.gender, hero.body]
        headdress.setImageResource(headdressRes)
        if (headdressRes != R.drawable.ic_void) {
            isHaveHeaddress = true
            if (MainActivity.core!!.settingsLogic.getSettings().showHeaddress && mode != CREATOR) {
                showHeaddress()
            } else {
                hideHeaddress()
            }
        } else {
            isHaveHeaddress = false
            hideHeaddress()
        }
        val colorBody = MainActivity.ui!!.getColor(ColorBodyStorage[hero.colorBody])
        val colorHair = MainActivity.ui!!.getColor(ColorHairStorage[hero.colorHair])
        try {
            setColorEars(EarsStorage[hero.gender, hero.ears], colorBody)
        } catch (ignored: Exception) {
        }
        try {
            setColorBody(BodyStorage[hero.gender, hero.body], colorBody)
        } catch (ignored: Exception) {
        }
        try {
            setColorBrows(BrowsStorage[hero.gender, hero.brows], colorHair)
        } catch (ignored: Exception) {
        }
        if (hero.beard != BeardStorage.WITHOUT_BEARD) {
            try {
                setColorBeard(BeardStorage[hero.gender, hero.beard], colorHair)
            } catch (ignored: Exception) {
            }
        }
        if (hero.hair != HairStorage.WITHOUT_HAIR) {
            try {
                setColorHair(HairStorage[hero.gender, hero.hair], colorHair)
            } catch (ignored: Exception) {
            }
            try {
                setColorBackHair(BackHairStorage[hero.gender, hero.hair], colorHair)
            } catch (ignored: Exception) {
            }
        }
    }

    private fun setColorBody(res: Int, color: Int) {
        val vector = VectorChildFinder(context, res, body)
        val pBody = vector.findPathByName("body")
        pBody.fillColor = color
        val pLeftHand = vector.findPathByName("left_hand")
        pLeftHand.fillColor = color
        val pRightHand = vector.findPathByName("right_hand")
        pRightHand.fillColor = color
        val pHead = vector.findPathByName("head")
        pHead.fillColor = color
        body.invalidate()
    }

    private fun setColorHair(res: Int, color: Int) {
        val vector = VectorChildFinder(context, res, hair)
        val pHair = vector.findPathByName("hair")
        pHair.fillColor = color
        try {
            val pHair2 = vector.findPathByName("hair2")
            pHair2.fillColor = color
        } catch (ignored: Exception) {
        }
        hair.invalidate()
    }

    private fun setColorBrows(res: Int, color: Int) {
        val vector = VectorChildFinder(context, res, brows)
        val pBrow1 = vector.findPathByName("brow1")
        pBrow1.fillColor = color
        val pBrow2 = vector.findPathByName("brow2")
        pBrow2.fillColor = color
        brows.invalidate()
    }

    private fun setColorBeard(res: Int, color: Int) {
        val vector = VectorChildFinder(context, res, beard)
        val pBeard = vector.findPathByName("beard")
        pBeard.fillColor = color
        try {
            val pMustache = vector.findPathByName("mustache")
            pMustache.fillColor = color
        } catch (ignored: Exception) {
        }
        beard.invalidate()
    }

    private fun setColorBackHair(res: Int, color: Int) {
        val vector = VectorChildFinder(context, res, backHair)
        val pBackHair = vector.findPathByName("back_hair")
        pBackHair.fillColor = color
        backHair.invalidate()
    }

    private fun setColorEars(res: Int, color: Int) {
        val vector = VectorChildFinder(context, res, ears)
        val pEar1 = vector.findPathByName("ear1")
        pEar1.fillColor = color
        val pEar2 = vector.findPathByName("ear2")
        pEar2.fillColor = color
        ears.invalidate()
    }

    fun setMode(mode: Int) {
        this.mode = mode
    }

    companion object {
        const val HERO = 0
        const val CREATOR = 1

        // 43 is hero_frame margins
        
        val needHeight: Int
            get() = MainActivity.ui!!.displayMaster.screenWidth - MainActivity.ui!!.dp(43) - MainActivity.ui!!.dp(43) // 43 is hero_frame margins
    }
}