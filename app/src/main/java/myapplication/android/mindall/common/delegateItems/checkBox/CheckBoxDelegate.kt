package myapplication.android.mindall.common.delegateItems.checkBox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.common.Constants.Companion.GREEN
import myapplication.android.mindall.common.Constants.Companion.ID
import myapplication.android.mindall.common.Constants.Companion.IS_COMPLETE
import myapplication.android.mindall.common.Constants.Companion.RED
import myapplication.android.mindall.common.Constants.Companion.YELLOW
import myapplication.android.mindall.common.delegate.AdapterDelegate
import myapplication.android.mindall.common.delegate.DelegateItem
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.databinding.RecyclerViewCheckListItemBinding

class CheckBoxDelegate : AdapterDelegate {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            RecyclerViewCheckListItemBinding.inflate(
                LayoutInflater.from(parent.context),parent, false)
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind(item.content() as CheckBoxModel)
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is CheckBoxDelegateItem

    class ViewHolder(val binding: RecyclerViewCheckListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var isChecked: Boolean? = null

        fun bind(model: CheckBoxModel) {

            if (isChecked == null) {
                isChecked = model.isComplete
            }

            setEditListener(model.editListener)

            when (model.flag) {
                RED -> {
                    setVisibilities(binding.yellowFlagLayout, binding.greenFlagLayout)
                    setText(binding.redTitle, model)
                    if (model.isComplete == true) {
                        setChecked(binding.redCheckboxChecked)
                    }
                    setLayoutClickListener(binding.redFlagLayout, binding.redCheckboxChecked, model)
                }

                YELLOW -> {
                    setVisibilities(binding.redFlagLayout, binding.greenFlagLayout)
                    setText(binding.yellowTitle, model)
                    if (model.isComplete == true) {
                        setChecked(binding.yellowCheckboxChecked)
                    }
                    setLayoutClickListener(binding.yellowFlagLayout, binding.yellowCheckboxChecked, model)
                }

                GREEN -> {
                    setVisibilities(binding.redFlagLayout, binding.yellowFlagLayout)
                    setText(binding.greenTitle, model)
                    if (model.isComplete == true) {
                        setChecked(binding.greenCheckboxChecked)
                    }
                    setLayoutClickListener(binding.greenFlagLayout, binding.greenCheckboxChecked, model)
                }
            }
        }

        private fun setEditListener(editListener: ButtonClickListener) {

        }

        private fun setLayoutClickListener(
            firstLayout: ConstraintLayout,
            imageButton: ImageButton,
            model: CheckBoxModel
        ) {
            firstLayout.setOnClickListener {
                isChecked(imageButton)
                model.taskId?.let { id -> model.listener.onClick(
                    Bundle().apply {
                        putString(ID, id)
                        isChecked?.let { isComplete -> putBoolean(IS_COMPLETE, isComplete) }
                })
                }
            }
        }

        private fun setChecked(imageButton: ImageButton) {
            isChecked = true
            imageButton.visibility = VISIBLE
        }

        private fun setVisibilities(firstLayout: ConstraintLayout, secondLayout: ConstraintLayout) {
            firstLayout.visibility = GONE
            secondLayout.visibility = GONE
        }

        private fun setText(textview: TextView, model: CheckBoxModel) {
            textview.text = model.title
        }

        private fun isChecked(imageButton: ImageButton) {
            if (isChecked == true) {
                isChecked = false
                imageButton.visibility = GONE
            } else {
                isChecked = true
                imageButton.visibility = VISIBLE
            }
        }
    }
}