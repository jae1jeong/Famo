package com.makeus.jfive.famo.src.data.di

import android.content.Context
import android.content.SharedPreferences
import com.makeus.jfive.famo.src.data.api.MainApiInterface
import com.makeus.jfive.famo.src.data.api.MonthlyApiInterface
import com.makeus.jfive.famo.src.data.api.TodayApiInterface
import com.makeus.jfive.famo.src.data.repository.MainRepositoryImpl
import com.makeus.jfive.famo.src.data.repository.MonthlyRepositoryImpl
import com.makeus.jfive.famo.src.data.repository.TodayRepositoryImpl
import com.makeus.jfive.famo.src.domain.repository.MainRepository
import com.makeus.jfive.famo.src.domain.repository.MonthlyRepository
import com.makeus.jfive.famo.src.domain.repository.TodayRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("SOFTSQUARED_TEMPLATE_APP", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideMonthlyRepository(api:MonthlyApiInterface):MonthlyRepository{
        return MonthlyRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideMonthlyApi(retrofit: Retrofit):MonthlyApiInterface{
        return retrofit.create(MonthlyApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideTodayApi(retrofit: Retrofit):TodayApiInterface{
        return retrofit.create(TodayApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideTodayRepository(api:TodayApiInterface):TodayRepository{
        return TodayRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideMainApi(retrofit: Retrofit):MainApiInterface = retrofit.create(MainApiInterface::class.java)

    @Provides
    @Singleton
    fun provideMainRepository(api:MainApiInterface):MainRepository = MainRepositoryImpl(api)

}