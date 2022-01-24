package com.hasura.todo.Todo.model.manager

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.CustomTypeAdapter
import com.apollographql.apollo.api.CustomTypeValue
import com.hasura.todo.Todo.model.repository.TodoRepository
import com.hasura.todo.type.CustomType
import okhttp3.OkHttpClient
import java.text.ParseException
import java.text.SimpleDateFormat


class RepositoryManager(
    private val graphqlOkHttpClient: OkHttpClient
) {
    val domain = "http://35.189.161.175:8080/v1/graphql"

    val todoRepository by lazy { TodoRepository(apolloClient) }

    val dateCustomTypeAdapter = object : CustomTypeAdapter<String> {
        var ISO8601 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")

        override fun decode(value: CustomTypeValue<*>): String {
            try {
                return ISO8601.parse(value.value.toString()).toString()
            } catch (e: ParseException) {
                throw RuntimeException(e)
            }

        }

        override fun encode(value: String): CustomTypeValue<*> {
            return CustomTypeValue.GraphQLString(value)
        }
    }

    private val apolloClient by lazy {
        ApolloClient.builder()
            .serverUrl(domain)
            .okHttpClient(graphqlOkHttpClient)
            .addCustomTypeAdapter(CustomType.TIMESTAMPTZ, dateCustomTypeAdapter)
            .build()
    }
}