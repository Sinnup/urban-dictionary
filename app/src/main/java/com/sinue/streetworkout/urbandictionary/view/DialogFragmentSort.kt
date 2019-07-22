package com.sinue.streetworkout.urbandictionary.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.sinue.streetworkout.urbandictionary.R

@SuppressLint("ValidFragment")
class DialogFragmentSort: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fragment_sort, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val radioGrp = view.findViewById<RadioGroup>(R.id.radioGrp_sort)
        val btnDialogYes = view.findViewById<Button>(R.id.btnAccept)
        btnDialogYes.setOnClickListener {
            val dialogListener = activity as DialogListener?
            dialogListener!!.onSelectDoneDialog(getSelectedRadio(radioGrp), this.tag.toString())
            dismiss()
        }
        val btnDialogNo = view.findViewById<Button>(R.id.btnCancel)
        btnDialogNo.setOnClickListener {
            val dialogListener = activity as DialogListener?
            dialogListener!!.onCancelDialog(cancelDialog())
            dismiss()
        }
    }

    private fun getSelectedRadio(radioGrp: RadioGroup): String {

        when (radioGrp.checkedRadioButtonId) {
            R.id.radioButton_thumbsUp -> return "thumbsUp"
            R.id.radioButton_thumbsDown -> return "thumbsDown"
            else -> return "none"
        }

    }

    private fun cancelDialog() {
        //If action needed
        //Toast.makeText(context, "Inside Cancelled Dialog", Toast.LENGTH_SHORT).show()
    }

    interface DialogListener {
        fun onSelectDoneDialog(sortBy: String, inputTag: String)
        fun onCancelDialog(cancelDialog: Unit)
    }
}