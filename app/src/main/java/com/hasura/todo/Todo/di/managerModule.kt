package com.hasura.todo.Todo.di

import com.hasura.todo.Todo.model.manager.RepositoryManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object managerModule {

    @Singleton
    @Provides
    fun provideRepositoryManager(
        @ApiModule.GraphqlOkHttpClient graphqlOkHttpClient: OkHttpClient,
    ): RepositoryManager {
        return RepositoryManager(graphqlOkHttpClient)
    }
}
