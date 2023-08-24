package com.egypt.game.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.egypt.game.R
import com.egypt.game.core.library.ViewBindingDialog
import com.egypt.game.core.library.safe
import com.egypt.game.databinding.DialogGameOverBinding
import com.egypt.game.domain.SP
import com.egypt.game.ui.other.CallbackVM
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DialogGameOver: ViewBindingDialog<DialogGameOverBinding>(DialogGameOverBinding::inflate) {
    private val args: DialogGameOverArgs by navArgs()
    private val sp by lazy {
        SP(requireContext())
    }
    private val callbackViewModel: CallbackVM by activityViewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.Dialog_No_Border)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.setCancelable(false)
        dialog!!.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                findNavController().popBackStack(R.id.fragmentMain, false, false)
                true
            } else {
                false
            }
        }
        callbackViewModel.callback = null
        binding.scores.text = args.scores.toString()
        binding.bestScores.text = sp.getBest().toString()

        binding.home.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentMain, false, false)
        }
        binding.restart.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentMain, false, false)
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentEgypt)
        }

        binding.settings.setOnClickListener {
            findNavController().navigate(DialogGameOverDirections.actionDialogGameOverToFragmentSettings(true))
        }
    }
}