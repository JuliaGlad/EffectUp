package myapplication.android.mindall.common.recyclerView.buttonWithIconAndText

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eftimoff.androidplayer.Player
import com.eftimoff.androidplayer.actions.property.PropertyAction
import myapplication.android.mindall.databinding.RecyclerViewButtonWithIconAndTextBinding
import myapplication.android.mindall.databinding.RecyclerViewButtonWithTextLayoutBinding

class ButtonWithIconAndTextAdapter : ListAdapter<ButtonWithIconAndTextModel, RecyclerView.ViewHolder>(ButtonWithIconAndTextCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(RecyclerViewButtonWithIconAndTextBinding.inflate(LayoutInflater.from(parent.context), parent, false).root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val binding = RecyclerViewButtonWithIconAndTextBinding.bind(itemView)

        fun bind(model: ButtonWithIconAndTextModel){
            binding.title.text = model.title
            binding.subtext.text = model.subtitle
            binding.icon.setImageDrawable(ResourcesCompat.getDrawable(itemView.resources, model.icon, itemView.context.theme))
            binding.item.setOnClickListener {
                model.listener.onClick()
            }
        }

    }
}