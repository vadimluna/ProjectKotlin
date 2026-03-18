package com.example.projectkotlin.di

import com.example.projectkotlin.damain.model.PokemonRepository
import com.example.projectkotlin.data.remote.PokeApi
import com.example.projectkotlin.data.repository.PokemonRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePokeApi(retrofit: Retrofit): PokeApi {
        return retrofit.create(PokeApi::class.java)
    }

    @Provides
    @Singleton
    fun providePokemonRepository(api: PokeApi): PokemonRepository {
        return PokemonRepositoryImpl(api)
    }
}
