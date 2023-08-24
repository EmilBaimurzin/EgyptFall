package com.egypt.game.ui.before

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.egypt.game.ui.other.ViewBindingFragment
import com.egypt.game.R
import com.egypt.game.databinding.FragmentBeforeGameBinding
import com.egypt.game.domain.SP
import com.egypt.game.ui.other.CallbackVM
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentBeforeGame : ViewBindingFragment<FragmentBeforeGameBinding>(FragmentBeforeGameBinding::inflate) {
    private val sp by lazy {
        SP(requireContext())
    }
    private val callbackViewModel: CallbackVM by activityViewModels()
    override fun onViewCreated(view:View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callbackViewModel.callback = null
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

        binding.start.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentEgypt)
        }

        binding.settings.setOnClickListener {
            findNavController().navigate(FragmentBeforeGameDirections.actionFragmentMainToFragmentSettings(false))
        }

        binding.back.setOnClickListener {
            requireActivity().finish()
        }

        binding.privacyText.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    ACTION_VIEW,
                    Uri.parse("https://www.google.com")
                )
            )
        }
    }
}