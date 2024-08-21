package myapplication.android.mindall.common.recyclerView.buttonWithIcon

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.R
import myapplication.android.mindall.databinding.ButtonWithIconLayoutBinding

class ButtonWithIconAdapter : ListAdapter<ButtonWithIconModel, RecyclerView.ViewHolder>(
    ButtonWithIconCallBack()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ButtonWithIconLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false).root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(
            getItem(position)
        )
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding : ButtonWithIconLayoutBinding = ButtonWithIconLayoutBinding.bind(itemView)

        fun bind(model : ButtonWithIconModel) {
            binding.title.text = model.title
            binding.icon.setImageDrawable(getResourcesCompatDrawable(model.icon))

            if (model.isAttention) {
                binding.button.setBackgroundColor(getResourcesCompatColor(R.color.colorErrorTonal))
                binding.icon.background = getResourcesCompatDrawable(R.drawable.icon_error_button_background)
                binding.title.setTextColor(getResourcesCompatColor(R.color.colorOnErrorContainerDialog))
            }
            binding.button.setOnClickListener{
                model.listener.onClick()
            }
        }

        private fun getResourcesCompatDrawable(drawable: Int) : Drawable?{
            return ResourcesCompat.getDrawable(itemView.resources, drawable, itemView.context.theme)
        }

        private fun getResourcesCompatColor(color: Int) : Int{
            return ResourcesCompat.getColor(itemView.resources, color, itemView.context.theme)
        }
    }
}