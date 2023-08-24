package com.egypt.game.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.egypt.game.R
import com.egypt.game.databinding.FragmentSettingsBinding
import com.egypt.game.domain.SP
import com.egypt.game.ui.other.CallbackVM
import com.egypt.game.ui.other.ViewBindingFragment

class FragmentSettings :
    ViewBindingFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {
    private val viewModel: SettingsViewModel by viewModels()
    private val callbackVM: CallbackVM by activityViewModels()
    private val sp by lazy {
        SP(requireContext())
    }
    private val args: FragmentSettingsArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        binding.buttonLeft.setOnClickListener {
            viewModel.left()
        }

        binding.buttonRight.setOnClickListener {
            viewModel.right()
        }

        if (viewModel.currentBg.value!! == -1) {
            viewModel.initBg(sp.getBg())
        }

        binding.confirm.setOnClickListener {
            if (!args.isDialog) {
                findNavController().popBackStack()
                callbackVM.callback?.invoke()
            } else {
                findNavController().popBackStack(R.id.fragmentMain, false, false)
            }
            sp.setBg(viewModel.currentBg.value!!)
        }

        binding.close.setOnClickListener {
            if (!args.isDialog) {
                findNavController().popBackStack()
                callbackVM.callback?.invoke()
            } else {
                findNavController().popBackStack(R.id.fragmentMain, false, false)
            }
        }

        binding.home.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentMain, false, false)
        }

        viewModel.currentBg.observe(viewLifecycleOwner) {
            binding.centerImg.setImageResource(
                when (it) {
                    1 -> R.drawable.background02
                    2 -> R.drawable.background03
                    3 -> R.drawable.background04
                    4 -> R.drawable.background05
                    5 -> R.drawable.background06
                    else -> R.drawable.background07
                }
            )
        }
    }
}