package com.hasura.todo.Todo.model.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.coroutines.toFlow
import com.apollographql.apollo.exception.ApolloException
import com.hasura.todo.*
import com.hasura.todo.type.CustomType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map


class TodoRepository constructor(private val apolloClient: ApolloClient) {

    fun getTodoList(): Flow<GetAllTodoListQuery.Data?> {
        return flow {
            val getAllTodoListQuery = GetAllTodoListQuery()
            val result = apolloClient.query(getAllTodoListQuery)?.await()?.data
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getTodoList(
        limit: Int,
        page: Int
    ): Response<GetTodoListQuery.Data>? {
        val getTodoListQuery = GetTodoListQuery(Input.fromNullable(limit),Input.fromNullable(limit*page))
        return apolloClient.query(getTodoListQuery)?.await()
    }

    fun removeTodoList(id: Int): Flow<Response<RemoveTodoListMutation.Data>> {
        val mutation = RemoveTodoListMutation(id)
        return apolloClient.mutate(mutation).toFlow()
            .flowOn(Dispatchers.IO)
            .map {
                if (it.hasErrors()) {
                    throw ApolloException(it.errors?.get(0)?.message ?: "Error is Empty")
                } else {
                    return@map it
                }
            }
    }

    fun addTodoList(assignee: String, task: String): Flow<Response<AddTodoListMutation.Data>> {
        val mutation = AddTodoListMutation(assignee, task)
        return apolloClient.mutate(mutation).toFlow()
            .flowOn(Dispatchers.IO)
            .map {
                if (it.hasErrors()) {
                    throw ApolloException(it.errors?.get(0)?.message ?: "Error is Empty")
                } else {
                    return@map it
                }
            }
    }

    fun updateTodoList(id: Int, assignee: String, task: String): Flow<Response<UpdateTodoListMutation.Data>> {
        val mutation = UpdateTodoListMutation(id, assignee, task)
        return apolloClient.mutate(mutation).toFlow()
            .flowOn(Dispatchers.IO)
            .map {
                if (it.hasErrors()) {
                    throw ApolloException(it.errors?.get(0)?.message ?: "Error is Empty")
                } else {
                    return@map it
                }
            }
    }

}