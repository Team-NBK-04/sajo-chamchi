package com.team4.sajochamchi.ui.dialog

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.team4.sajochamchi.databinding.DialogEditBinding
import com.team4.sajochamchi.databinding.DialogViewDetailBinding


class EditDialog(
    private val name : String,
    private val description : String,
    private val clickEventListener: ClickEventListener,
) : BottomSheetDialogFragment() {

    companion object {
        private const val TAG = "EditDialog"
        fun newInstance(
            name : String,
            description : String,
            clickEventListener: ClickEventListener
        ) = EditDialog(name,description,clickEventListener)
    }

    interface ClickEventListener {
        fun nameChanged(string: String)

        fun descriptionChanged(string: String)

        fun favoriteClearClicked()

    }

    private var _binding: DialogEditBinding? = null
    private val binding: DialogEditBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DialogEditBinding.inflate(inflater, container, false)
        //dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //layout 크기 조절
        binding.layout.layoutParams = ViewGroup.LayoutParams(
            resources.displayMetrics.widthPixels,
            resources.displayMetrics.heightPixels
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val offsetFromTop = 0
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            isFitToContents = false
            setExpandedOffset(offsetFromTop)
            state = BottomSheetBehavior.STATE_EXPANDED
        }

        initViews()
    }

    private fun initViews() = with(binding) {
        titleEditeText.setText(name)
        titleEditeText.addTextChangedListener { s: Editable? ->
            //Log.d(TAG, "titleEditeText.addTextChangedListener")
            clickEventListener.nameChanged(s.toString())
        }

        descriptionsEditeText.setText(description)
        descriptionsEditeText.addTextChangedListener { s: Editable? ->
            //Log.d(TAG, "descriptionsEditeText.addTextChangedListener")
            clickEventListener.descriptionChanged(s.toString())
        }

        clearImageView.setOnClickListener {
            clickEventListener.favoriteClearClicked()
        }

        closeImageButton.setOnClickListener {
            dismiss()
        }

        //드래그 방지
        try {
            val behavior = (dialog as BottomSheetDialog).behavior
            behavior.addBottomSheetCallback(object : BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}