package com.hasura.todo.Todo.ui.todos.edit

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.hasura.todo.GetTodoListQuery
import com.hasura.todo.Todo.R
import com.hasura.todo.Todo.ui.todos.TodosFragment
import kotlinx.android.synthetic.main.fragment_edit_todos.*


class EditTodosDialogFragment(
    val item: GetTodoListQuery.Todo_list? = null
) : DialogFragment(R.layout.fragment_edit_todos) {

    companion object {
        fun newInstance(item: GetTodoListQuery.Todo_list?) = EditTodosDialogFragment(item)
    }

    var dialogFuncListener: GeneralDialogFuncListener? = null

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        val widthPixels = (resources.displayMetrics.widthPixels * 0.8).toInt()
        window?.setLayout(widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupListener()
    }

    fun setupUI() {
        item?.task()?.also { et_task.setText(it) }
        item?.assignee()?.also { et_assignee.setText(it) }
    }

    fun setupListener() {
        bt_confirm.setOnClickListener {
            dismiss()
            if(item!=null)
                dialogFuncListener?.onEditConfirmClick?.invoke(item.id(), et_assignee.text.toString(), et_task.text.toString())
            else
                dialogFuncListener?.onInsertConfirmClick?.invoke(et_assignee.text.toString(), et_task.text.toString())
        }

        bt_cancel.setOnClickListener {
            dismiss()
            dialogFuncListener?.onCancelClick?.invoke()
        }
    }

}