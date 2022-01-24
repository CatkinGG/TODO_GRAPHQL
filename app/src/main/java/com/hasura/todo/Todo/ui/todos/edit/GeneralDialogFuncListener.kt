package com.hasura.todo.Todo.ui.todos.edit

class GeneralDialogFuncListener(
    val onEditConfirmClick: (Int?, String, String) -> Unit = { _, _, _ -> },
    val onInsertConfirmClick: (String, String) -> Unit = { _, _ -> },
    val onCancelClick: () -> Unit = { },
)