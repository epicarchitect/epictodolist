package kolmachikhin.alexander.epictodolist.ui.hero.maker

import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.hero.HeroModel
import kolmachikhin.alexander.epictodolist.App
import kolmachikhin.alexander.epictodolist.logic.Core.Companion.with
import kolmachikhin.alexander.epictodolist.storage.creator.learning.LearnMessageStorage
import kolmachikhin.alexander.epictodolist.storage.hero.BeardStorage
import kolmachikhin.alexander.epictodolist.storage.hero.BrowsStorage
import kolmachikhin.alexander.epictodolist.storage.hero.ColorBodyStorage
import kolmachikhin.alexander.epictodolist.storage.hero.ColorHairStorage
import kolmachikhin.alexander.epictodolist.storage.hero.EarsStorage
import kolmachikhin.alexander.epictodolist.storage.hero.ExtrasStorage
import kolmachikhin.alexander.epictodolist.storage.hero.EyesStorage
import kolmachikhin.alexander.epictodolist.storage.hero.HairStorage
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.MakerDialog
import kolmachikhin.alexander.epictodolist.ui.animations.Animations
import kolmachikhin.alexander.epictodolist.ui.creator.CreatorLearnDialog
import kolmachikhin.alexander.epictodolist.ui.hero.HeroBodyFragment
import kolmachikhin.alexander.epictodolist.ui.hero.HeroBodyFragment.Companion.needHeight
import kolmachikhin.alexander.epictodolist.ui.messages.MessageFragment.Companion.show
import kolmachikhin.alexander.epictodolist.ui.navigation.text.TextNavigationFragment
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set
import java.util.Random

class HeroMakerDialog : MakerDialog<HeroModel> {

    private lateinit var inputName: EditText
    private lateinit var gender0: ImageView
    private lateinit var gender1: ImageView
    private lateinit var buttonHeaddress: ImageView
    private lateinit var buttonRandom: ImageView
    private lateinit var heroBody: HeroBodyFragment
    private lateinit var topBarCreate: LinearLayout
    private lateinit var topBarEdit: LinearLayout
    private lateinit var rvParts: RecyclerView
    private lateinit var heroFrame: FrameLayout
    private lateinit var heroBodyContainer: FrameLayout
    private lateinit var navigationContainer: FrameLayout
    private lateinit var scrollView: ScrollView
    private lateinit var navigation: TextNavigationFragment
    private lateinit var analyticsConsentLayout: LinearLayout
    private lateinit var checkAnalyticsConsent: CheckBox
    private lateinit var tvAnalyticsConsent: TextView

    constructor() : super(R.layout.hero_maker_fragment)

    constructor(container: ViewGroup) : super(R.layout.hero_maker_fragment, container)

    override fun findViews() {
        super.findViews()
        heroBodyContainer = find(R.id.hero_body_container)
        heroFrame = find(R.id.hero_frame)
        navigationContainer = find(R.id.navigation_container)
        scrollView = find(R.id.scroll_view)
        topBarCreate = find(R.id.top_bar_create)
        topBarEdit = find(R.id.top_bar_edit)
        gender0 = find(R.id.button_gender_0)
        gender1 = find(R.id.button_gender_1)
        buttonHeaddress = find(R.id.button_headdress)
        buttonRandom = find(R.id.button_random)
        rvParts = find(R.id.rv_parts)
        if (modeCreate) {
            buttonDone = find(R.id.button_continue_create)
            inputName = find(R.id.input_name_create)
            analyticsConsentLayout = find(R.id.analytics_consent_layout)
            checkAnalyticsConsent = find(R.id.check_analytics_consent)
            tvAnalyticsConsent = find(R.id.tv_analytics_consent)
        } else {
            inputName = find(R.id.input_name)
        }
    }

    override fun start() {
        super.start()
        heroBody = HeroBodyFragment(heroBodyContainer)
        heroBody.replace()
        val params = heroFrame.layoutParams
        params.height = needHeight
        heroFrame.layoutParams = params
        set(buttonHeaddress, 100) { changeHeaddressVisibility() }
        set(buttonRandom, 100) { setByRandom() }
        set(gender0) { setGender(0) }
        set(gender1) { setGender(1) }
        rvParts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        if (modeCreate) {
            topBarEdit.visibility = View.GONE
            topBarCreate.visibility = View.VISIBLE
            tvTitle?.text = MainActivity.ui!!.getString(R.string.hero_creating)
            buttonHeaddress.visibility = View.GONE
            CreatorLearnDialog.open(LearnMessageStorage.HERO_MAKER_CREATE)
            setupAnalyticsConsent()
            set(buttonDone) {
                CreatorLearnDialog.open(LearnMessageStorage.AFTER_CREATE_HERO) {
                    buttonDone.isEnabled = false
                    buttonDone.isClickable = false
                    App.analytics.setAnalyticsEnabled(checkAnalyticsConsent.isChecked)
                    val hero = done()
                    if (onDoneListener != null) onDoneListener!!.onDone(hero)
                    closeDialog()
                }
            }
        } else {
            set(buttonDone) {
                HeroEditPaymentDialog.open(model!!) {
                    buttonDone.isEnabled = false
                    buttonDone.isClickable = false
                    val hero = done()
                    if (onDoneListener != null) onDoneListener!!.onDone(hero)
                    closeDialog()
                }
            }
            topBarEdit.visibility = View.VISIBLE
            topBarCreate.visibility = View.GONE
            CreatorLearnDialog.openIfNotLearned(LearnMessageStorage.HERO_MAKER_EDIT)
        }
        inputName.setText(model!!.name)
        setNavigation()
        navigation.select(HeroPartsRVA.HAIR)
        setGender(model!!.gender)
        if (MainActivity.core!!.settingsLogic.getSettings().showHeaddress) {
            buttonHeaddress.setImageResource(R.drawable.ic_headdress_hide)
        } else {
            buttonHeaddress.setImageResource(R.drawable.ic_headdress_show)
        }

        if (modeCreate) {
            ViewCompat.requestApplyInsets(view) // kostil

            ViewCompat.setOnApplyWindowInsetsListener(view) { v, windowInsets ->
                val statusInsets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
                val navigationInsets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
                v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = statusInsets.top
                    bottomMargin = navigationInsets.bottom
                }
                WindowInsetsCompat.CONSUMED
            }
        }
    }

    override fun openDialog() {
        add()
        Animations.showView(mainLayout, null)
        MainActivity.ui!!.hideKeyboard()
    }

    private fun setupAnalyticsConsent() {
        analyticsConsentLayout.visibility = View.VISIBLE
        buttonDone.visibility = View.VISIBLE
        checkAnalyticsConsent.isChecked = false
        val text = MainActivity.ui!!.getString(R.string.analytics_consent_text)
        tvAnalyticsConsent.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        tvAnalyticsConsent.movementMethod = LinkMovementMethod.getInstance()
        tvAnalyticsConsent.setOnClickListener {
            checkAnalyticsConsent.isChecked = !checkAnalyticsConsent.isChecked
        }
    }

    private fun setNavigation() {
        navigation = TextNavigationFragment(TextNavigationFragment.TOP, navigationContainer)
        navigation.replace()
        navigation.addButton(
            HeroPartsRVA.HAIR,
            MainActivity.ui!!.getString(R.string.hair)
        ) { setRvHair() }
        navigation.addButton(
            HeroPartsRVA.BEARD,
            MainActivity.ui!!.getString(R.string.beard)
        ) { setRvBeard() }
        navigation.addButton(
            HeroPartsRVA.EYES,
            MainActivity.ui!!.getString(R.string.eyes)
        ) { setRvEyes() }
        navigation.addButton(
            HeroPartsRVA.EARS,
            MainActivity.ui!!.getString(R.string.ears)
        ) { setRvEars() }
        navigation.addButton(
            HeroPartsRVA.BROWS,
            MainActivity.ui!!.getString(R.string.brows)
        ) { setRvBrows() }
        navigation.addButton(
            HeroPartsRVA.EXTRAS,
            MainActivity.ui!!.getString(R.string.extras)
        ) { setRvExtras() }
        navigation.addButton(
            HeroPartsRVA.COLOR_HAIR,
            MainActivity.ui!!.getString(R.string.hair_color)
        ) { setRvColorHair() }
        navigation.addButton(
            HeroPartsRVA.COLOR_BODY,
            MainActivity.ui!!.getString(R.string.body_color)
        ) { setRvColorBody() }
    }

    private fun changeHeaddressVisibility() {
        if (heroBody.isHaveHeaddress) {
            val settings = MainActivity.core!!.settingsLogic.getSettings()
            if (settings.showHeaddress) {
                settings.showHeaddress = false
                buttonHeaddress.setImageResource(R.drawable.ic_headdress_show)
                heroBody.hideHeaddress()
            } else {
                settings.showHeaddress = true
                buttonHeaddress.setImageResource(R.drawable.ic_headdress_hide)
                heroBody.showHeaddress()
            }
            MainActivity.core!!.settingsLogic.update(settings)
        } else {
            show(MainActivity.ui!!.getString(R.string.headgear_is_missing))
        }
    }

    private fun setParts(partsId: Int, listener: HeroPartsRVA.Listener) {
        val rva = HeroPartsRVA(model!!, partsId) { position ->
            listener.onClick(position)
            heroBody.setParts(model!!)
        }
        rvParts.adapter = rva
    }

    private fun setRvHair() {
        setParts(HeroPartsRVA.HAIR) { position -> model!!.hair = position }
    }

    private fun setRvBeard() {
        setParts(HeroPartsRVA.BEARD) { position -> model!!.beard = position }
    }

    private fun setRvEyes() {
        setParts(HeroPartsRVA.EYES) { position -> model!!.eyes = position }
    }

    private fun setRvEars() {
        setParts(HeroPartsRVA.EARS) { position -> model!!.ears = position }
    }

    private fun setRvBrows() {
        setParts(HeroPartsRVA.BROWS) { position: Int -> model!!.brows = position }
    }

    private fun setRvExtras() {
        setParts(HeroPartsRVA.EXTRAS) { position -> model!!.extras = position }
    }

    private fun setRvColorHair() {
        setParts(HeroPartsRVA.COLOR_HAIR) { position -> model!!.colorHair = position }
    }

    private fun setRvColorBody() {
        setParts(HeroPartsRVA.COLOR_BODY) { position -> model!!.colorBody = position }
    }

    override fun done(): HeroModel {
        model!!.name = inputName.text.toString().trim()
        if (modeCreate) {
            model!!.coins = START_COINS
            model!!.crystals = START_CRYSTALS
        }
        if (model!!.name.equals("creator", ignoreCase = true)) {
            model!!.coins = 90000
            model!!.crystals = 9000
        }
        return if (modeEdit) {
            MainActivity.core!!.heroLogic.update(model!!)
        } else {
            MainActivity.core!!.heroLogic.create(
                model!!
            )
        }
    }

    private fun setGender(gender: Int) {
        val oldGender = model!!.gender
        model!!.gender = gender
        try {
            heroBody.setParts(model!!)
        } catch (ignored: Exception) {
        }

        if (gender == 0) {
            gender0.setBackgroundResource(R.drawable.bg_active)
            gender1.setBackgroundResource(R.drawable.ic_void)
            navigation.showButton(HeroPartsRVA.BEARD)
        } else {
            gender0.setBackgroundResource(R.drawable.ic_void)
            gender1.setBackgroundResource(R.drawable.bg_active)
            navigation.hideButton(HeroPartsRVA.BEARD)
        }
        if (gender != oldGender) {
            reloadParts()
        }
    }

    private fun reloadParts() {
        navigation.select(HeroPartsRVA.HAIR)
    }

    private fun setByRandom() {
        val r = Random(System.currentTimeMillis())
        model!!.hair = r.nextInt(HairStorage.size())
        model!!.beard = r.nextInt(BeardStorage.size())
        model!!.eyes = r.nextInt(EyesStorage.size())
        model!!.ears = r.nextInt(EarsStorage.size())
        model!!.brows = r.nextInt(BrowsStorage.size())
        model!!.extras = r.nextInt(ExtrasStorage.size())
        model!!.colorHair = r.nextInt(ColorHairStorage.size())
        model!!.colorBody = r.nextInt(ColorBodyStorage.size())
        heroBody.setParts(model!!)
        reloadParts()
    }

    companion object {
        const val START_COINS = 100
        const val START_CRYSTALS = 20

        fun open(container: ViewGroup, onDoneListener: OnDoneListener<HeroModel>? = null) {
            val maker = HeroMakerDialog(container)
            if (with(container.context).heroLogic.isHeroCreated) {
                maker.setModeEdit()
                maker.model = with(container.context).heroLogic.hero
            } else {
                maker.setModeCreate()
                maker.model = HeroModel()
            }
            maker.onDoneListener = onDoneListener
            maker.openDialog()
        }
    }
}