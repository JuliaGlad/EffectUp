package myapplication.android.mindall.common.recyclerView.checkBox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.common.Constants
import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.databinding.RecyclerViewCheckListItemBinding

class CheckBoxAdapter : ListAdapter<CheckBoxModel, RecyclerView.ViewHolder>(CheckBoxItemCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
     = ViewHolder(RecyclerViewCheckListItemBinding.inflate(
          LayoutInflater.from(parent.context),parent, false))


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
    }

    class ViewHolder(val binding: RecyclerViewCheckListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var isChecked: Boolean? = null

        fun bind(model: CheckBoxModel) {

            if (isChecked == null) {
                isChecked = model.isComplete
            }

            setEditListener(model.editListener)

            when (model.flag) {
                Constants.RED -> {
                    setVisibilities(binding.yellowFlagLayout, binding.greenFlagLayout)
                    setText(binding.redTitle, model)
                    if (model.isComplete == true) {
                        setChecked(binding.redCheckboxChecked)
                    }
                    setLayoutClickListener(binding.redFlagLayout, binding.redCheckboxChecked, model)
                }

                Constants.YELLOW -> {
                    setVisibilities(binding.redFlagLayout, binding.greenFlagLayout)
                    setText(binding.yellowTitle, model)
                    if (model.isComplete == true) {
                        setChecked(binding.yellowCheckboxChecked)
                    }
                    setLayoutClickListener(
                        binding.yellowFlagLayout,
                        binding.yellowCheckboxChecked,
                        model
                    )
                }

                Constants.GREEN -> {
                    setVisibilities(binding.redFlagLayout, binding.yellowFlagLayout)
                    setText(binding.greenTitle, model)
                    if (model.isComplete == true) {
                        setChecked(binding.greenCheckboxChecked)
                    }
                    setLayoutClickListener(
                        binding.greenFlagLayout,
                        binding.greenCheckboxChecked,
                        model
                    )
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
                model.taskId?.let { id ->
                    model.listener.onClick(
                        Bundle().apply {
                            putString(Constants.ID, id)
                            isChecked?.let { isComplete ->
                                putBoolean(
                                    Constants.IS_COMPLETE,
                                    isComplete
                                )
                            }
                        })
                }
            }
        }

        private fun setChecked(imageButton: ImageButton) {
            isChecked = true
            imageButton.visibility = View.VISIBLE
        }

        private fun setVisibilities(firstLayout: ConstraintLayout, secondLayout: ConstraintLayout) {
            firstLayout.visibility = View.GONE
            secondLayout.visibility = View.GONE
        }

        private fun setText(textview: TextView, model: CheckBoxModel) {
            textview.text = model.title
        }

        private fun isChecked(imageButton: ImageButton) {
            if (isChecked == true) {
                isChecked = false
                imageButton.visibility = View.GONE
            } else {
                isChecked = true
                imageButton.visibility = View.VISIBLE
            }
        }
    }

}