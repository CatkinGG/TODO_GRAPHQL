package com.hasura.todo.Todo.ui.todos

import com.hasura.todo.GetTodoListQuery

class TodosFuncItem(
    val onEdit: ((GetTodoListQuery.Todo_list) -> Unit) = { _-> },
    val onDelete: ((Int) -> Unit) = { _-> }
)