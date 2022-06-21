package kolmachikhin.alexander.epictodolist.util.icons

import kolmachikhin.alexander.epictodolist.R

object IconHelper {

    private const val IC_VOID = R.drawable.ic_void
    private const val ICON_DEFAULT = R.drawable.icon_default
    private const val FRAME_DEFAULT = R.drawable.frame_default

    val ICONS = intArrayOf(
        R.drawable.icon_0, R.drawable.icon_1, R.drawable.icon_2, R.drawable.icon_3,
        R.drawable.icon_4, R.drawable.icon_5, R.drawable.icon_6, R.drawable.icon_7,
        R.drawable.icon_8, R.drawable.icon_9, R.drawable.icon_10, R.drawable.icon_11,
        R.drawable.icon_12, R.drawable.icon_13, R.drawable.icon_14, R.drawable.icon_15,
        R.drawable.icon_16, R.drawable.icon_17, R.drawable.icon_18, R.drawable.icon_19,
        R.drawable.icon_20, R.drawable.icon_21, R.drawable.icon_22, R.drawable.icon_23,
        R.drawable.icon_24, R.drawable.icon_25, R.drawable.icon_26, R.drawable.icon_27,
        R.drawable.icon_28, R.drawable.icon_29, R.drawable.icon_30, R.drawable.icon_31,
        R.drawable.icon_32, R.drawable.icon_33, R.drawable.icon_34, R.drawable.icon_35,
        R.drawable.icon_36, R.drawable.icon_37, R.drawable.icon_38, R.drawable.icon_39,
        R.drawable.icon_40, R.drawable.icon_41, R.drawable.icon_42, R.drawable.icon_43,
        R.drawable.icon_44, R.drawable.icon_45, R.drawable.icon_46, R.drawable.icon_47,
        R.drawable.icon_48, R.drawable.icon_49, R.drawable.icon_50, R.drawable.icon_51,
        R.drawable.icon_52, R.drawable.icon_53, R.drawable.icon_54
    )

    private val FRAMES = intArrayOf(
        R.drawable.frame_strength,
        R.drawable.frame_intellect,
        R.drawable.frame_creation,
        R.drawable.frame_health
    )

    private val ICON_ATTRIBUTES = intArrayOf(
        R.drawable.ic_strength,
        R.drawable.ic_intellect,
        R.drawable.ic_creation,
        R.drawable.ic_health
    )

    fun getFrame(i: Int) =
        if (i < 0) FRAME_DEFAULT
        else FRAMES[i]

    fun getIconAttribute(i: Int) =
        if (i < 0) IC_VOID
        else ICON_ATTRIBUTES[i]

    fun getIcon(i: Int) =
        if (i < 0) ICON_DEFAULT
        else ICONS[i]

}