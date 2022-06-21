package kolmachikhin.alexander.epictodolist.ui.hero.maker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.hero.HeroModel
import kolmachikhin.alexander.epictodolist.storage.hero.*
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class HeroPartsRVA(
    hero: HeroModel,
    type: Int,
    var listener: Listener?
) : RecyclerView.Adapter<HeroPartsRVA.ViewHolder>() {

    private var list = ArrayList<Int>()
    private var backPartList = ArrayList<Int>()
    private var selectedMainLayout: FrameLayout? = null
    private val bodyRes = BodyStorage[hero.gender, hero.body]
    private var isBack = false
    private var withBackPart = false
    private var selectedPosition = 0

    init {
        when (type) {
            HAIR -> {
                list = HairStorage.getList(hero.gender)
                backPartList = BackHairStorage.getList(hero.gender)
                selectedPosition = hero.hair
                withBackPart = true
            }
            BEARD -> {
                list = BeardStorage.getList(hero.gender)
                selectedPosition = hero.beard
            }
            EYES -> {
                list = EyesStorage.getList(hero.gender)
                selectedPosition = hero.eyes
            }
            EARS -> {
                list = EarsStorage.getList(hero.gender)
                selectedPosition = hero.ears
                isBack = true
            }
            BROWS -> {
                list = BrowsStorage.getList(hero.gender)
                selectedPosition = hero.brows
            }
            EXTRAS -> {
                list = ExtrasStorage.getList(hero.gender)
                selectedPosition = hero.extras
            }
            COLOR_HAIR -> {
                list = ColorHairStorage.list
                selectedPosition = hero.colorHair
            }
            COLOR_BODY -> {
                list = ColorBodyStorage.list
                selectedPosition = hero.colorBody
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int) = ViewHolder(
        LayoutInflater.from(viewGroup.context).inflate(R.layout.hero_part_item, viewGroup, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val position = holder.adapterPosition
        if (selectedPosition == position) {
            selectedMainLayout = holder.mainLayout
            holder.mainLayout.setBackgroundResource(R.drawable.selected_square)
        } else {
            holder.mainLayout.setBackgroundResource(R.drawable.square)
        }

        if (isBack) {
            holder.body.setImageResource(list[position])
            holder.part.setImageResource(bodyRes)
        } else {
            holder.body.setImageResource(bodyRes)
            holder.part.setImageResource(list[position])
        }

        if (withBackPart) {
            holder.backPart.setImageResource(backPartList[position])
        } else {
            holder.backPart.setImageResource(R.drawable.ic_void)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun interface Listener {
        fun onClick(position: Int)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        var part: ImageView = v.findViewById(R.id.part)
        var body: ImageView = v.findViewById(R.id.body)
        var backPart: ImageView = v.findViewById(R.id.back_part)
        var mainLayout: FrameLayout = v.findViewById(R.id.main_layout)

        init {
            set(mainLayout, this)
        }

        override fun onClick(v: View) {
            listener?.onClick(adapterPosition)
            selectedMainLayout?.setBackgroundResource(R.drawable.square)
            selectedMainLayout = mainLayout
            selectedPosition = adapterPosition
            selectedMainLayout!!.setBackgroundResource(R.drawable.selected_square)
        }
    }

    companion object {
        const val HAIR = 0
        const val BEARD = 1
        const val EYES = 2
        const val EARS = 3
        const val BROWS = 4
        const val EXTRAS = 5
        const val COLOR_HAIR = 6
        const val COLOR_BODY = 7
    }
}