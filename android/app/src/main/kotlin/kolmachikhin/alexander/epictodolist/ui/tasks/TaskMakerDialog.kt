package kolmachikhin.alexander.epictodolist.ui.tasks

import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.database.skills.SkillModel
import kolmachikhin.alexander.epictodolist.database.tasks.TaskModel
import kolmachikhin.alexander.epictodolist.ui.spinner.CommonSpinnerAdapter
import kolmachikhin.alexander.epictodolist.ui.spinner.CommonSpinnerItem
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.MakerDialog
import kolmachikhin.alexander.epictodolist.ui.drag.ModelOrderManager
import kolmachikhin.alexander.epictodolist.util.icons.IconHelper.getFrame
import kolmachikhin.alexander.epictodolist.util.icons.IconHelper.getIcon
import kolmachikhin.alexander.epictodolist.util.icons.IconHelper.getIconAttribute
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

open class TaskMakerDialog<M : TaskModel>(layoutRes: Int) : MakerDialog<M>(layoutRes) {

    protected lateinit var icon: ImageView
    protected lateinit var inputContent: EditText
    protected lateinit var spinnerSkill: Spinner
    protected lateinit var spinnerDifficulty: Spinner

    private val skillsOrderManager = ModelOrderManager<SkillModel>(context, "SkillsFragment")
    protected var listSkills = ArrayList<SkillModel>()
    protected var voidSkill: SkillModel = SkillModel(
        -1,
        MainActivity.ui!!.getString(R.string.skill_none),
        -1,
        -1,
        0
    )

    override fun findViews() {
        super.findViews()
        icon = find(R.id.icon)
        inputContent = find(R.id.input_content)
        spinnerSkill = find(R.id.spinner_skill)
        spinnerDifficulty = find(R.id.spinner_difficulty)
    }

    override fun start() {
        super.start()
        spinnerSkill.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                setSkill(spinnerSkill.selectedItemPosition)
                MainActivity.ui!!.hideKeyboard()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
        spinnerDifficulty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                MainActivity.ui!!.hideKeyboard()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
        set(icon) {
            spinnerSkill.performClick()
            MainActivity.ui!!.hideKeyboard()
        }
        initSkills()
        initSkillsSpinner()
        initDifficultyList()
        inputContent.setText(model!!.content)
        spinnerDifficulty.setSelection(model!!.difficulty)
    }

    override fun done(): M {
        model!!.content = inputContent.text.toString()
        model!!.difficulty = spinnerDifficulty.selectedItemPosition
        model!!.skillId = listSkills[spinnerSkill.selectedItemPosition].id
        return model!!
    }

    private fun initSkills() {
        listSkills = skillsOrderManager.sorted(MainActivity.core!!.skillsLogic.getSkills())
        listSkills.add(0, voidSkill)
    }

    private fun initSkillsSpinner() {
        val list = ArrayList<CommonSpinnerItem>()
        list.add(CommonSpinnerItem(voidSkill.title!!, getIconAttribute(voidSkill.attribute)))
        for (i in 1 until listSkills.size) {
            val skill = listSkills[i]
            list.add(CommonSpinnerItem(skill.title!!, getIconAttribute(skill.attribute)))
        }
        spinnerSkill.adapter = CommonSpinnerAdapter(context, list)
        for (i in listSkills.indices) {
            val skill = listSkills[i]
            if (model!!.skillId == skill.id) {
                spinnerSkill.setSelection(i)
                setSkill(i)
                break
            }
        }
    }

    private fun initDifficultyList() {
        val list = ArrayList<CommonSpinnerItem>()
        list.add(CommonSpinnerItem(MainActivity.ui!!.getString(R.string.easy)))
        list.add(CommonSpinnerItem(MainActivity.ui!!.getString(R.string.middle)))
        list.add(CommonSpinnerItem(MainActivity.ui!!.getString(R.string.hard)))
        list.add(CommonSpinnerItem(MainActivity.ui!!.getString(R.string.legendary)))
        spinnerDifficulty.adapter = CommonSpinnerAdapter(context, list)
    }

    fun setSkill(selectedItem: Int) {
        val skill = listSkills[selectedItem]
        icon.setImageResource(getIcon(skill.iconId))
        icon.setBackgroundResource(getFrame(skill.attribute))
    }
}