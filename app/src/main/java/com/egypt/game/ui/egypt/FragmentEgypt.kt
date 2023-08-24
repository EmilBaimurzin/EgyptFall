package com.egypt.game.ui.egypt

import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.egypt.game.R
import com.egypt.game.core.library.safe
import com.egypt.game.ui.other.ViewBindingFragment
import com.egypt.game.databinding.FragmentEgyptBinding
import com.egypt.game.domain.SP
import com.egypt.game.ui.other.CallbackVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class FragmentEgypt : ViewBindingFragment<FragmentEgyptBinding>(FragmentEgyptBinding::inflate) {
    private var moveScope = CoroutineScope(Dispatchers.Default)
    private val viewModel: EgyptViewModel by viewModels()
    private val callbackViewModel: CallbackVM by activityViewModels()
    private val xy by lazy {
        val display = requireActivity().windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        Pair(size.x, size.y)
    }
    private val sp by lazy {
        SP(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtons()
        binding.root.setBackgroundResource(
            when (sp.getBg()) {
                1 -> R.drawable.background02
                2 -> R.drawable.background03
                3 -> R.drawable.background04
                4 -> R.drawable.background05
                5 -> R.drawable.background06
                else -> R.drawable.background07
            }
        )

        viewModel.endCallback = {
            lifecycleScope.launch(Dispatchers.Main) {
                viewModel.stop()
                viewModel.gameState = false
                if (viewModel.scores.value!! > sp.getBest()) {
                    sp.setBest(viewModel.scores.value!!)
                }
                findNavController().navigate(
                    FragmentEgyptDirections.actionFragmentEgyptToDialogGameOver(
                        viewModel.scores.value!!
                    )
                )
            }
        }

        callbackViewModel.callback = {
            lifecycleScope.launch(Dispatchers.Main) {
                delay(40)
                viewModel.pauseState = false
                viewModel.start(
                    dpToPx(80),
                    xy.first,
                    8,
                    xy.second,
                    binding.character.width,
                    binding.character.height,
                    10
                )
            }
        }


        binding.settings.setOnClickListener {
            viewModel.stop()
            viewModel.pauseState = true
            findNavController().navigate(FragmentEgyptDirections.actionFragmentEgyptToFragmentSettings(false))
        }

        binding.home.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.playerXY.observe(viewLifecycleOwner) {
            binding.character.x = it.x
            binding.character.y = it.y
        }

        viewModel.symbols.observe(viewLifecycleOwner) {
            binding.symbolsLayout.removeAllViews()
            it.forEach { symbol ->
                val symbolView = ImageView(requireContext())
                symbolView.layoutParams = ViewGroup.LayoutParams(dpToPx(80), dpToPx(80))
                val symbolImg = when (symbol.value) {
                    1 -> R.drawable.symbol01
                    2 -> R.drawable.symbol02
                    3 -> R.drawable.symbol03
                    4 -> R.drawable.symbol04
                    5 -> R.drawable.symbol05
                    6 -> R.drawable.symbol06
                    else -> R.drawable.symbol07
                }
                symbolView.setImageResource(symbolImg)
                symbolView.x = symbol.x
                symbolView.y = symbol.y
                binding.symbolsLayout.addView(symbolView)
            }
        }

        viewModel.symbolToCatch.observe(viewLifecycleOwner) {
            binding.collect.setImageResource(
                when (it) {
                    1 -> R.drawable.symbol01
                    2 -> R.drawable.symbol02
                    3 -> R.drawable.symbol03
                    4 -> R.drawable.symbol04
                    5 -> R.drawable.symbol05
                    6 -> R.drawable.symbol06
                    else -> R.drawable.symbol07
                }
            )
        }

        viewModel.scores.observe(viewLifecycleOwner) {
            binding.scores.text = it.toString()
        }

        lifecycleScope.launch {
            delay(20)
            if (viewModel.playerXY.value == null) {
                viewModel.initPlayer(xy.first, xy.second, binding.character.height)
            }
            if (viewModel.gameState && !viewModel.pauseState) {
                viewModel.start(
                    dpToPx(80),
                    xy.first,
                    8,
                    xy.second,
                    binding.character.width,
                    binding.character.height,
                    10
                )
            }
        }
    }

    private fun dpToPx(dp: Int): Int {
        val displayMetrics = resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setButtons() {
        binding.buttonLeft.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    moveScope.launch {
                        while (true) {
                            viewModel.playerMoveLeft(0f)
                            delay(2)
                        }
                    }
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    moveScope.cancel()
                    moveScope = CoroutineScope(Dispatchers.Default)
                    moveScope.launch {
                        while (true) {
                            viewModel.playerMoveLeft(0f)
                            delay(2)
                        }
                    }
                    true
                }

                else -> {
                    moveScope.cancel()
                    moveScope = CoroutineScope(Dispatchers.Default)
                    false
                }
            }
        }

        binding.buttonRight.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    moveScope.launch {
                        while (true) {
                            viewModel.playerMoveRight((xy.first - binding.character.width).toFloat())
                            delay(2)
                        }
                    }
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    moveScope.cancel()
                    moveScope = CoroutineScope(Dispatchers.Default)
                    moveScope.launch {
                        while (true) {
                            viewModel.playerMoveRight((xy.first - binding.character.width).toFloat())
                            delay(2)
                        }
                    }
                    true
                }

                else -> {
                    moveScope.cancel()
                    moveScope = CoroutineScope(Dispatchers.Default)
                    false
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stop()
    }
}