package kolmachikhin.alexander.epictodolist.ui.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import kolmachikhin.alexander.epictodolist.R

class CommonSpinnerAdapter(
    context: Context,
    list: ArrayList<CommonSpinnerItem>
) : ArrayAdapter<CommonSpinnerItem>(context, R.layout.common_spinner_item, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup) = initView(position, convertView, parent)

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) = initView(position, convertView, parent)

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.common_spinner_item, parent, false)!!
        }

        val content = itemView.findViewById<TextView>(R.id.content)
        val icon = itemView.findViewById<ImageView>(R.id.icon)

        val item = getItem(position)!!
        if (item.icon == R.drawable.ic_void) {
            icon.visibility = View.GONE
        } else {
            icon.setImageResource(item.icon)
        }
        content.text = item.content

        return itemView
    }
}