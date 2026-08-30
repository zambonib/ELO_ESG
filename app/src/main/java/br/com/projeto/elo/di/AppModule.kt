package br.com.projeto.elo.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import br.com.projeto.elo.data.local.AppDatabase
import br.com.projeto.elo.data.local.MIGRATION_1_2
<<<<<<< HEAD
import br.com.projeto.elo.data.local.MIGRATION_2_3
import br.com.projeto.elo.data.local.OrcamentoDao
=======
>>>>>>> 3df5725dd0882ba487761f3c383b1179530a7e89
import br.com.projeto.elo.data.local.TransacaoDao
import br.com.projeto.elo.data.remote.GeminiApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "elo_banco_de_dados")
<<<<<<< HEAD
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
=======
            .addMigrations(MIGRATION_1_2)
>>>>>>> 3df5725dd0882ba487761f3c383b1179530a7e89
            .build()

    @Provides
    fun provideTransacaoDao(db: AppDatabase): TransacaoDao = db.transacaoDao()

    @Provides
<<<<<<< HEAD
    fun provideOrcamentoDao(db: AppDatabase): OrcamentoDao = db.orcamentoDao()

    @Provides
=======
>>>>>>> 3df5725dd0882ba487761f3c383b1179530a7e89
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

    @Provides
    @Singleton
    fun provideGeminiApi(okHttpClient: OkHttpClient): GeminiApi =
        Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiApi::class.java)

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("elo_prefs", Context.MODE_PRIVATE)
<<<<<<< HEAD
=======

    @Provides
    @Singleton
    fun provideViaCepApi(okHttpClient: OkHttpClient): br.com.projeto.elo.data.remote.ViaCepApi =
        Retrofit.Builder()
            .baseUrl("https://viacep.com.br/ws/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(br.com.projeto.elo.data.remote.ViaCepApi::class.java)
>>>>>>> 3df5725dd0882ba487761f3c383b1179530a7e89
}