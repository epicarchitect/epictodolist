package kolmachikhin.alexander.epictodolist.ui.graphs.progress

class Interval(
    val startDate: Date,
    val endDate: Date
) {

    constructor(
        startDay: Int,
        startMonth: Int,
        startYear: Int,
        endDay: Int,
        endMonth: Int,
        endYear: Int
    ) : this(
        Date(startDay, startMonth, startYear),
        Date(endDay, endMonth, endYear)
    )
}