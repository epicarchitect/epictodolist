package kolmachikhin.alexander.epictodolist.ui.graphs.progress

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kolmachikhin.alexander.epictodolist.R
import kolmachikhin.alexander.epictodolist.ui.animations.Animations
import kolmachikhin.alexander.epictodolist.ui.spinner.CommonSpinnerAdapter
import kolmachikhin.alexander.epictodolist.ui.spinner.CommonSpinnerItem
import kolmachikhin.alexander.epictodolist.ui.MainActivity
import kolmachikhin.alexander.epictodolist.ui.UIFragment
import kolmachikhin.alexander.epictodolist.util.time.TimeMaster
import kolmachikhin.alexander.epictodolist.util.time.TimeMaster.Companion.calculateDaysInMonth
import kolmachikhin.alexander.epictodolist.util.ui.Clicks.set

class ProgressGraphFragment(container: ViewGroup) : UIFragment(R.layout.progress_graph_fragment, container) {

    private lateinit var spinnerChartMode: Spinner
    private lateinit var spinnerDay1: Spinner
    private lateinit var spinnerMonth1: Spinner
    private lateinit var spinnerYear1: Spinner
    private lateinit var spinnerDay2: Spinner
    private lateinit var spinnerMonth2: Spinner
    private lateinit var spinnerYear2: Spinner
    private lateinit var customIntervalLayout: LinearLayout
    private lateinit var buttonShow: Button
    private lateinit var chart: LineChart
    private var listener: OnSelectModeListener? = null
    private var customIntervalLayoutHeight = 0
    private var isCustomShowed = false
    private var selectedDay1 = 0
    private var selectedDay2 = 0
    private val years = intArrayOf(2019, 2020, 2021, 2022, 2023, 2024, 2025)
    private val intervalBySelectedValues: Interval
        get() = Interval(
            spinnerDay1.selectedItemPosition + 1,
            spinnerMonth1.selectedItemPosition,
            years[spinnerYear1.selectedItemPosition],
            spinnerDay2.selectedItemPosition + 1,
            spinnerMonth2.selectedItemPosition,
            years[spinnerYear2.selectedItemPosition]
        )

    override fun findViews() {
        chart = find(R.id.chart)
        spinnerDay1 = find(R.id.spinner_day_1)
        spinnerMonth1 = find(R.id.spinner_month_1)
        spinnerYear1 = find(R.id.spinner_year_1)
        spinnerDay2 = find(R.id.spinner_day_2)
        spinnerMonth2 = find(R.id.spinner_month_2)
        spinnerYear2 = find(R.id.spinner_year_2)
        buttonShow = find(R.id.button_show)
        spinnerChartMode = find(R.id.spinner_chart_mode)
        customIntervalLayout = find(R.id.custom_interval_layout)
    }

    private fun setFirstValues() {
        val timeMaster = TimeMaster()
        timeMaster.timeInMillis = System.currentTimeMillis()
        val currentMonth = timeMaster.month
        val currentYear = timeMaster.year
        val todayOfWeek = timeMaster.todayOfWeek
        val today = timeMaster.day
        val firstDayOfWeek = today - todayOfWeek
        val lastDayOfWeek = today + (6 - todayOfWeek)
        val interval = Interval(firstDayOfWeek, currentMonth, currentYear, lastDayOfWeek, currentMonth, currentYear)
        for (i in years.indices) {
            if (currentYear == years[i]) {
                spinnerYear1.setSelection(i)
                spinnerYear2.setSelection(i)
                break
            }
        }
        spinnerMonth1.setSelection(currentMonth)
        spinnerMonth2.setSelection(currentMonth)
        spinnerDay1.setSelection(0)
        spinnerDay2.setSelection(0)
        showProgressChart(interval)
    }

    override fun start() {
        set(buttonShow) { showProgressChart(intervalBySelectedValues) }
        initSpinnerChartMode()
        initSpinnerYear1()
        initSpinnerYear2()
        initSpinnerMonth1()
        initSpinnerMonth2()
        initSpinnerDay1(spinnerMonth1.selectedItemPosition, years[spinnerYear1.selectedItemPosition])
        initSpinnerDay2(spinnerMonth2.selectedItemPosition, years[spinnerYear2.selectedItemPosition])
        setFirstValues()
        customIntervalLayoutHeight = MainActivity.ui!!.dp(210)
    }

    fun showProgressChart(interval: Interval?) {
        val list = ArrayList<Entry>()
        val progressList = MainActivity.core!!.completedTasksLogic.getEarnedProgressOnInterval(interval!!)
        for (i in progressList.indices) {
            list.add(Entry((i + 1).toFloat(), progressList[i].toFloat()))
        }
        val dataSet = LineDataSet(list, MainActivity.ui!!.getString(R.string.daily_progress))
        dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        dataSet.setCircleColor(MainActivity.ui!!.getAttrColor(R.attr.colorPrimaryDark))
        dataSet.color = MainActivity.ui!!.getAttrColor(R.attr.colorPrimaryDark)
        dataSet.circleHoleColor = MainActivity.ui!!.getAttrColor(R.attr.colorAccent)
        val iLineDataSets = ArrayList<ILineDataSet>()
        iLineDataSets.add(dataSet)
        val lineData = LineData(iLineDataSets)
        chart.data = lineData
        val desc = Description()
        desc.text = " "
        chart.description = desc
        chart.setDrawMarkers(false)
        chart.animateY(1500)
        chart.setBorderColor(MainActivity.ui!!.getAttrColor(R.attr.colorPrimaryDark))
    }

    fun initSpinnerDay1(month: Int, year: Int) {
        val list = ArrayList<CommonSpinnerItem>()
        for (i in 1..calculateDaysInMonth(month, year)) {
            list.add(CommonSpinnerItem(i.toString() + ""))
        }
        spinnerDay1.adapter = CommonSpinnerAdapter(context, list)
        spinnerDay1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                selectedDay1 = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        if (selectedDay1 > calculateDaysInMonth(month, year)) {
            selectedDay1 = calculateDaysInMonth(month, year) - 1
        }
        spinnerDay2.setSelection(selectedDay1)
    }

    fun initSpinnerDay2(month: Int, year: Int) {
        val list = ArrayList<CommonSpinnerItem>()
        for (i in 1..calculateDaysInMonth(month, year)) {
            list.add(CommonSpinnerItem(i.toString() + ""))
        }
        spinnerDay2.adapter = CommonSpinnerAdapter(context, list)
        spinnerDay2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                selectedDay2 = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        if (selectedDay2 > calculateDaysInMonth(month, year)) {
            selectedDay2 = calculateDaysInMonth(month, year) - 1
        }
        spinnerDay2.setSelection(selectedDay2)
    }

    private fun initSpinnerMonth1() {
        val list = ArrayList<CommonSpinnerItem>()
        for (month in MainActivity.ui!!.getStringArray(R.array.months)) {
            list.add(CommonSpinnerItem(month))
        }
        spinnerMonth1.adapter = CommonSpinnerAdapter(context, list)
        spinnerMonth1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                initSpinnerDay1(position, years[spinnerYear1.selectedItemPosition])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initSpinnerMonth2() {
        val list = ArrayList<CommonSpinnerItem>()
        for (month in MainActivity.ui!!.getStringArray(R.array.months)) {
            list.add(CommonSpinnerItem(month))
        }
        spinnerMonth2.adapter = CommonSpinnerAdapter(context, list)
        spinnerMonth2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                initSpinnerDay2(position, years[spinnerYear2.selectedItemPosition])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initSpinnerYear1() {
        val list = ArrayList<CommonSpinnerItem>()
        for (i in years.indices) {
            list.add(CommonSpinnerItem(years[i].toString() + ""))
        }
        spinnerYear1.adapter = CommonSpinnerAdapter(context, list)
        spinnerYear1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                initSpinnerDay1(spinnerMonth1.selectedItemPosition, years[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initSpinnerYear2() {
        val list = ArrayList<CommonSpinnerItem>()
        for (i in years.indices) {
            list.add(CommonSpinnerItem(years[i].toString() + ""))
        }
        spinnerYear2.adapter = CommonSpinnerAdapter(context, list)
        spinnerYear2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                initSpinnerDay2(spinnerMonth2.selectedItemPosition, years[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initSpinnerChartMode() {
        val list = ArrayList<CommonSpinnerItem>()
        list.add(CommonSpinnerItem(MainActivity.ui!!.getString(R.string.this_week)))
        list.add(CommonSpinnerItem(MainActivity.ui!!.getString(R.string.this_month)))
        list.add(CommonSpinnerItem(MainActivity.ui!!.getString(R.string.adjust_interval)))
        spinnerChartMode.adapter = CommonSpinnerAdapter(context, list)
        spinnerChartMode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                if (position == 2) {
                    if (!isCustomShowed) {
                        Animations.slideView(customIntervalLayout, 0, customIntervalLayoutHeight) { listener!!.onSelectMode() }
                        isCustomShowed = true
                    }
                } else {
                    if (isCustomShowed) {
                        Animations.slideView(customIntervalLayout, customIntervalLayoutHeight, 0)
                        isCustomShowed = false
                    }
                    val timeMaster = TimeMaster()
                    timeMaster.timeInMillis = System.currentTimeMillis()
                    val currentMonth = timeMaster.month
                    val currentYear = timeMaster.year
                    val todayOfWeek = timeMaster.todayOfWeek
                    val today = timeMaster.day
                    val firstDayOfWeek = today - todayOfWeek
                    val lastDayOfWeek = today + (6 - todayOfWeek)
                    val interval = if (position == 1) {
                        Interval(1, currentMonth, currentYear, timeMaster.daysInMonth, currentMonth, currentYear)
                    } else {
                        Interval(firstDayOfWeek, currentMonth, currentYear, lastDayOfWeek, currentMonth, currentYear)
                    }
                    showProgressChart(interval)
                    if (listener != null) listener!!.onSelectMode()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    fun interface OnSelectModeListener {
        fun onSelectMode()
    }

    companion object {
        fun open(viewGroup: ViewGroup, listener: OnSelectModeListener) {
            val f = ProgressGraphFragment(viewGroup)
            f.listener = listener
            f.replace()
        }
    }
}