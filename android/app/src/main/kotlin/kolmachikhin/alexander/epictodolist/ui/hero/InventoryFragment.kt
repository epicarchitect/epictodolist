package kolmachikhin.alexander.epictodolist.ui.hero

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.Model.Companion.isVoid
import kolmachikhin.alexander.epictodolist.database.hero.HeroModel
import kolmachikhin.alexander.epictodolist.database.products.ProductModel
import kolmachikhin.alexander.epictodolist.logic.products.ProductType
import kolmachikhin.alexander.epictodolist.storage.creator.learning.LearnMessageStorage
import kolmachikhin.alexander.epictodolist.storage.products.CostumesStorage
import kolmachikhin.alexander.epictodolist.storage.products.LocationsStorage
import kolmachikhin.alexander.epictodolist.storage.products.WeaponStorage
import kolmachikhin.alexander.epictodolist.ui.animations.Animations
import kolmachikhin.alexander.epictodolist.ui.creator.CreatorLearnDialog
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.UIFragment
import kolmachikhin.alexander.epictodolist.ui.messages.MessageFragment
import kolmachikhin.alexander.epictodolist.ui.navigation.text.TextNavigationFragment
import kolmachikhin.alexander.epictodolist.ui.products.ProductDialog
import kolmachikhin.alexander.epictodolist.ui.products.ProductDialog.Companion.open
import kolmachikhin.alexander.epictodolist.ui.products.ProductsRVFragment
import kolmachikhin.alexander.epictodolist.ui.products.ProductsRVFragment.ActivateListener
import kolmachikhin.alexander.epictodolist.ui.products.ProductsRVFragment.TryOnListener
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class InventoryFragment(container: ViewGroup) : UIFragment(R.layout.inventory_fragment, container) {

    private lateinit var containerWeapon: ImageView
    private lateinit var containerTheme: ImageView
    private lateinit var containerCostume: ImageView
    private lateinit var containerLocation: ImageView
    private lateinit var tvName: TextView
    private lateinit var hero: HeroModel
    private lateinit var weapon: ProductModel
    private lateinit var theme: ProductModel
    private lateinit var costume: ProductModel
    private lateinit var location: ProductModel
    private lateinit var heroFrame: FrameLayout
    private lateinit var navigationContainer: FrameLayout
    private lateinit var navigation: TextNavigationFragment
    private lateinit var scrollView: ScrollView
    private lateinit var heroBodyContainer: FrameLayout
    private lateinit var heroBody: HeroBodyFragment
    private lateinit var rvContainer: FrameLayout

    override fun start() {
        CreatorLearnDialog.openIfNotLearned(LearnMessageStorage.INVENTORY_NAV)
        heroBody = HeroBodyFragment(heroBodyContainer)
        heroBody.add()
        set(containerWeapon) {
            if (isVoid(weapon)) {
                MessageFragment.show(MainActivity.ui!!.getString(R.string.empty))
            } else {
                open(weapon, ProductDialog.FROM_ACTIVE) { _, _ ->
                    weapon = MainActivity.core!!.productsLogic.getWeapon(WeaponStorage.DEFAULT_ID)
                    hero.backWeapon = weapon.id
                    containerWeapon.setImageResource(weapon.iconRes)
                    heroBody.setParts(hero)
                    scrollView.smoothScrollTo(0, 0)
                }
            }
        }
        set(containerCostume) {
            if (isVoid(costume)) {
                MessageFragment.show(MainActivity.ui!!.getString(R.string.empty))
            } else {
                open(costume, ProductDialog.FROM_ACTIVE) { _, _ ->
                    costume = MainActivity.core!!.productsLogic.getCostume(CostumesStorage.DEFAULT_ID)
                    hero.body = costume.id
                    containerCostume.setImageResource(costume.iconRes)
                    heroBody.setParts(hero)
                    scrollView.smoothScrollTo(0, 0)
                }
            }
        }
        set(containerLocation) {
            if (isVoid(location)) {
                MessageFragment.show(MainActivity.ui!!.getString(R.string.empty))
            } else {
                open(location, ProductDialog.FROM_ACTIVE) { _, _ ->
                    location = MainActivity.core!!.productsLogic.getLocation(LocationsStorage.DEFAULT_ID)
                    containerLocation.setImageResource(location.iconRes)
                    hero.location = location.id
                    heroBody.setParts(hero)
                    scrollView.smoothScrollTo(0, 0)
                }
            }
        }
        set(containerTheme) { open(theme, ProductDialog.FROM_ACTIVE) }
        val params = heroFrame.layoutParams
        params.height = HeroBodyFragment.needHeight
        heroFrame.layoutParams = params
        showHero()
        setContainerPet()
        setContainerTheme()
        setContainerCostume()
        setContainerLocations()
        setRvCostumes()
        setRvWeapons()
        setRvThemes()
        setRvLocations()
        setRvFeatures()
        setNavigation()
        navigation.select(ProductType.FEATURE)
    }

    private fun setNavigation() {
        navigation = TextNavigationFragment(TextNavigationFragment.TOP, navigationContainer)
        navigation.replace()
        navigation.addButton(ProductType.FEATURE, MainActivity.ui!!.getString(R.string.features)) { setRvFeatures() }
        navigation.addButton(ProductType.THEME, MainActivity.ui!!.getString(R.string.themes)) { setRvThemes() }
        navigation.addButton(ProductType.COSTUME, MainActivity.ui!!.getString(R.string.costumes)) { setRvCostumes() }
        navigation.addButton(ProductType.WEAPON, MainActivity.ui!!.getString(R.string.weapons)) { setRvWeapons() }
        navigation.addButton(ProductType.LOCATION, MainActivity.ui!!.getString(R.string.locations)) { setRvLocations() }
    }

    override fun findViews() {
        scrollView = find(R.id.scroll_view)
        navigationContainer = find(R.id.navigation_container)
        tvName = find(R.id.name)
        heroFrame = find(R.id.hero_frame)
        heroBodyContainer = find(R.id.hero_body_container)
        containerWeapon = find(R.id.container_weapon)
        containerTheme = find(R.id.container_theme)
        containerCostume = find(R.id.container_costume)
        containerLocation = find(R.id.container_location)
        rvContainer = find(R.id.rv_container)
    }

    @SuppressLint("SetTextI18n")
    fun showHero() {
        hero = MainActivity.core!!.heroLogic.hero
        tvName.text = hero.name
    }

    private fun setContainerPet() {
        weapon = MainActivity.core!!.productsLogic.getWeapon(hero.backWeapon)
        containerWeapon.setImageResource(weapon.iconRes)
    }

    private fun setContainerTheme() {
        theme = MainActivity.core!!.productsLogic.getTheme(MainActivity.ui!!.themeMaster.theme)
        containerTheme.setImageResource(theme.iconRes)
    }

    private fun setContainerLocations() {
        location = MainActivity.core!!.productsLogic.getLocation(hero.location)
        containerLocation.setImageResource(location.iconRes)
    }

    private fun setContainerCostume() {
        costume = MainActivity.core!!.productsLogic.getCostume(hero.body)
        containerCostume.setImageResource(costume.iconRes)
    }

    private fun setRvThemes() {
        val rv = ProductsRVFragment(rvContainer)
        rv.list = MainActivity.core!!.productsLogic.getProducts(ProductType.THEME)
        rv.tryOnListener = TryOnListener { product -> MainActivity.ui!!.themeMaster.tryTheme(product!!.id) }
        Animations.hideView(rvContainer) {
            rv.replace()
            Animations.showView(rvContainer, null)
        }
        scrollView.smoothScrollTo(0, 2000)
    }

    private fun setRvFeatures() {
        val rv = ProductsRVFragment(rvContainer)
        rv.list = MainActivity.core!!.productsLogic.getProducts(ProductType.FEATURE)
        Animations.hideView(rvContainer) {
            rv.replace()
            Animations.showView(rvContainer, null)
        }
        scrollView.smoothScrollTo(0, 2000)
    }

    private fun setRvWeapons() {
        val rv = ProductsRVFragment(rvContainer)
        rv.list = MainActivity.core!!.productsLogic.getProducts(ProductType.WEAPON)
        rv.activateListener = ActivateListener { product: ProductModel? ->
            hero.backWeapon = product!!.id
            weapon = product
            containerWeapon.setImageResource(weapon.iconRes)
            heroBody.setParts(hero)
            scrollView.smoothScrollTo(0, 0)
        }
        rv.tryOnListener = TryOnListener { product: ProductModel? ->
            hero.backWeapon = product!!.id
            heroBody.setParts(hero)
            scrollView.smoothScrollTo(0, 0)
        }
        Animations.hideView(rvContainer) {
            rv.replace()
            Animations.showView(rvContainer, null)
        }
        scrollView.smoothScrollTo(0, 2000)
    }

    private fun setRvCostumes() {
        val rv = ProductsRVFragment(rvContainer)
        rv.list = MainActivity.core!!.productsLogic.getProducts(ProductType.COSTUME)
        rv.activateListener = ActivateListener { product: ProductModel? ->
            hero.body = product!!.id
            costume = product
            containerCostume.setImageResource(costume.iconRes)
            heroBody.setParts(hero)
            scrollView.smoothScrollTo(0, 0)
        }
        rv.tryOnListener = TryOnListener { product: ProductModel? ->
            hero.body = product!!.id
            heroBody.setParts(hero)
            scrollView.smoothScrollTo(0, 0)
        }
        Animations.hideView(rvContainer) {
            rv.replace()
            Animations.showView(rvContainer, null)
        }
        scrollView.smoothScrollTo(0, 2000)
    }

    private fun setRvLocations() {
        val rv = ProductsRVFragment(rvContainer)
        rv.list = MainActivity.core!!.productsLogic.getProducts(ProductType.LOCATION)
        rv.activateListener = ActivateListener { product: ProductModel? ->
            hero.location = product!!.id
            location = product
            containerLocation.setImageResource(location.iconRes)
            heroBody.setParts(hero)
            scrollView.smoothScrollTo(0, 0)
        }
        rv.tryOnListener = TryOnListener { product: ProductModel? ->
            hero.location = product!!.id
            heroBody.setParts(hero)
            scrollView.smoothScrollTo(0, 0)
        }
        Animations.hideView(rvContainer) {
            rv.replace()
            Animations.showView(rvContainer, null)
        }
        scrollView.smoothScrollTo(0, 2000)
    }

    override fun remove() {
        Animations.hideView(container) { super.remove() }
    }

    override fun replace() {
        Animations.hideView(container) { super.replace() }
    }

    override fun add() {
        super.add()
        Animations.showView(container, null)
    }
}