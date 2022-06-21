package kolmachikhin.alexander.epictodolist.logic.products

import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.products.ProductModel
import kolmachikhin.alexander.epictodolist.database.products.ProductModel.Companion.isUnlocked
import kolmachikhin.alexander.epictodolist.database.products.ProductsRepository
import kolmachikhin.alexander.epictodolist.logic.Core
import kolmachikhin.alexander.epictodolist.logic.Logic
import kolmachikhin.alexander.epictodolist.logic.Observer
import kolmachikhin.alexander.epictodolist.storage.creator.learning.LearnMessageStorage
import kolmachikhin.alexander.epictodolist.storage.products.CostumesStorage
import kolmachikhin.alexander.epictodolist.storage.products.FeaturesStorage
import kolmachikhin.alexander.epictodolist.storage.products.LocationsStorage
import kolmachikhin.alexander.epictodolist.storage.products.ThemeStorage
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.creator.CreatorLearnDialog
import kolmachikhin.alexander.epictodolist.ui.messages.MessageFragment

class ProductsLogic(core: Core) : Logic(core) {

    private val repository by lazy { ProductsRepository.getInstance(context) }
    private var products = ArrayList<ProductModel>()
    val observer = Observer<ProductModel>()

    val countUnlockedProducts: Int
        get() {
            var count = 0
            for (p in products) if (isUnlocked(p)) count++
            return count
        }

    val countProducts: Int
        get() = products.size

    val isNotificationsUnlocked: Boolean
        get() = isUnlocked(getFeature(FeaturesStorage.NOTIFICATIONS_ID))

    val isAttributeGraphUnlocked: Boolean
        get() = isUnlocked(getFeature(FeaturesStorage.ATTRIBUTE_GRAPH_ID))

    val isTaskListWidgetUnlocked: Boolean
        get() = isUnlocked(getFeature(FeaturesStorage.TASK_LIST_WIDGET_ID))

    val isRepeatableTasksUnlocked: Boolean
        get() = isUnlocked(getFeature(FeaturesStorage.REPEATABLE_TASKS_ID))

    val isChallengesUnlocked: Boolean
        get() = isUnlocked(getFeature(FeaturesStorage.CHALLENGES_ID))

    override fun postInit() {
        observer.addListener(UNLOCK) { product ->
            if (product.type == ProductType.FEATURE) {
                when (product.id) {
                    FeaturesStorage.NOTIFICATIONS_ID -> {
                        CreatorLearnDialog.openIfNotLearned(LearnMessageStorage.UNLOCK_NOTIFICATION_FEATURE)
                    }
                    FeaturesStorage.TASK_LIST_WIDGET_ID -> {
                        CreatorLearnDialog.openIfNotLearned(LearnMessageStorage.UNLOCK_TASK_LIST_WIDGET_FEATURE)
                    }
                    FeaturesStorage.REPEATABLE_TASKS_ID -> {
                        CreatorLearnDialog.openIfNotLearned(LearnMessageStorage.UNLOCK_REPEATABLE_TASKS_FEATURE)
                    }
                    FeaturesStorage.ATTRIBUTE_GRAPH_ID -> {
                        CreatorLearnDialog.openIfNotLearned(LearnMessageStorage.UNLOCK_ATTRIBUTE_GRAPH_FEATURE)
                    }
                }
            }
        }
    }

    override fun attachRef() {
        repository.liveList.observeForever { list ->
            setProducts(list)
        }
    }

    fun getCountUnlockedProducts(type: Int): Int {
        var count = 0
        for (p in products) if (type == p.type && isUnlocked(p)) count++
        return count
    }

    fun update(m: ProductModel) {
        for (i in products.indices) {
            if (m.id == products[i].id && products[i].type == m.type) {
                products[i] = m
                break
            }
        }
        repository.save(m)
    }

    fun unlockPart(product: ProductModel): Boolean {
        if (!isUnlocked(product)) {
            val hero = core.heroLogic.hero
            return when {
                    hero.coins >= product.price.coins -> {
                        if (hero.crystals >= product.price.crystals) {
                            hero.coins -= product.price.coins
                            hero.crystals -= product.price.crystals
                            core.heroLogic.update(hero)
                            product.countParts++
                            update(product)
                            if (isUnlocked(product)) {
                                MessageFragment.show(getString(R.string.unlocked))
                                observer.notify(UNLOCK, product)
                            } else {
                                MessageFragment.show(getString(R.string.part_unlocked))
                                observer.notify(UNLOCK_PART, product)
                            }
                            true
                        } else {
                            MessageFragment.show(getString(R.string.not_enough_crystals))
                            false
                        }
                    }
                    else -> {
                        MessageFragment.show(getString(R.string.not_enough_coins))
                        false
                    }
                }
        }

        MessageFragment.show(getString(R.string.already_unlocked))
        return true
    }

    fun activate(p: ProductModel) {
        when (p.type) {
            ProductType.COSTUME -> {
                val hero = core.heroLogic.hero
                hero.body = p.id
                core.heroLogic.update(hero)
            }
            ProductType.WEAPON -> {
                val hero = core.heroLogic.hero
                hero.backWeapon = p.id
                core.heroLogic.update(hero)
            }
            ProductType.THEME -> {
                MainActivity.ui!!.themeMaster.setThemeAndRestart(p.id)
            }
            ProductType.LOCATION -> {
                val hero = core.heroLogic.hero
                hero.location = p.id
                core.heroLogic.update(hero)
            }
        }
    }

    fun deactivate(p: ProductModel) {
        when (p.type) {
            ProductType.COSTUME -> {
                val hero = core.heroLogic.hero
                hero.body = CostumesStorage.DEFAULT_ID
                core.heroLogic.update(hero)
            }
            ProductType.WEAPON -> {
                val hero = core.heroLogic.hero
                hero.backWeapon = CostumesStorage.DEFAULT_ID
                core.heroLogic.update(hero)
            }
            ProductType.THEME -> {
                MainActivity.ui!!.themeMaster.setThemeAndRestart(ThemeStorage.DEFAULT_THEME)
            }
            ProductType.LOCATION -> {
                val hero = core.heroLogic.hero
                hero.location = LocationsStorage.DEFAULT_ID
                core.heroLogic.update(hero)
            }
        }
    }

    private fun setProducts(list: ArrayList<ProductModel>) {
        products = list
        ready()
    }

    fun getFeature(id: Int) = findProduct(id, ProductType.FEATURE)

    fun getTheme(id: Int) = findProduct(id, ProductType.THEME)

    fun getCostume(id: Int) = findProduct(id, ProductType.COSTUME)

    fun getWeapon(id: Int) = findProduct(id, ProductType.WEAPON)

    fun getLocation(id: Int) = findProduct(id, ProductType.LOCATION)

    private fun findProduct(id: Int, type: Int): ProductModel {
        var product = ProductModel()
        for (p in products) {
            if (id == p.id && p.type == type) {
                product = p
                break
            }
        }
        return product
    }

    fun getProducts(type: Int): ArrayList<ProductModel> {
        val list = ArrayList<ProductModel>()
        for (p in products) {
            if (p.type == type) {
                list.add(p)
            }
        }
        sort(list, false)
        return list
    }

    companion object {
        const val UNLOCK_PART = 0
        const val UNLOCK = 1
    }
}