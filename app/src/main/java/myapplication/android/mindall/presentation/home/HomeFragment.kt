package myapplication.android.mindall.presentation.home

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import myapplication.android.mindall.R
import myapplication.android.mindall.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGradient()
    }

    private fun initGradient() {
        initTextGradient(binding.plansTitle)
        initTextGradient(binding.pomodoroTitle)
        initTextGradient(binding.trackerTitle)
    }

    private fun initTextGradient(title: TextView) {
        val paint = title.paint
        val width = paint.measureText(title.text.toString())
        val textShader: Shader = LinearGradient(0f, 0f, width, binding.plansTitle.textSize, intArrayOf(
            Color.parseColor("#D08C25"),
            Color.parseColor("#FFBE5D")
        ), null, Shader.TileMode.REPEAT)

        binding.plansTitle.paint.setShader(textShader)
    }
}