package kolmachikhin.alexander.epictodolist.ui.hero

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.products.ProductModel.Companion.isUnlocked
import kolmachikhin.alexander.epictodolist.storage.products.FeaturesStorage
import kolmachikhin.alexander.epictodolist.ui.graphs.attributes.AttributeGraphFragment
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.UIFragment
import kolmachikhin.alexander.epictodolist.ui.products.LockedProductFragment

class AttributesFragment(container: ViewGroup) : UIFragment(R.layout.attributes_fragment, container) {

    private lateinit var tvStrength: TextView
    private lateinit var tvIntellect: TextView
    private lateinit var tvCreation: TextView
    private lateinit var tvHealth: TextView
    private lateinit var attributeGraphContainer: FrameLayout

    override fun findViews() {
        tvStrength = find(R.id.strength)
        tvIntellect = find(R.id.intellect)
        tvCreation = find(R.id.creation)
        tvHealth = find(R.id.health)
        attributeGraphContainer = find(R.id.attribute_graph_container)
    }

    @SuppressLint("SetTextI18n")
    override fun start() {
        val hero = MainActivity.core!!.heroLogic.hero
        tvStrength.text = MainActivity.ui!!.getString(R.string.strength) + ": " + hero.strength
        tvIntellect.text = MainActivity.ui!!.getString(R.string.intellect) + ": " + hero.intellect
        tvCreation.text = MainActivity.ui!!.getString(R.string.creation) + ": " + hero.creation
        tvHealth.text = MainActivity.ui!!.getString(R.string.health) + ": " + hero.health
        if (MainActivity.core!!.productsLogic.isAttributeGraphUnlocked) {
            AttributeGraphFragment(attributeGraphContainer).replace()
        } else {
            LockedProductFragment.open(
                MainActivity.core!!.productsLogic.getFeature(FeaturesStorage.ATTRIBUTE_GRAPH_ID),
                attributeGraphContainer
            ) { product ->
                if (isUnlocked(product)) {
                    AttributeGraphFragment(attributeGraphContainer).replace()
                }
            }
        }
    }

    companion object {
        fun open(viewGroup: ViewGroup) {
            AttributesFragment(viewGroup).replace()
        }
    }
}