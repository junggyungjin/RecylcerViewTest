package fastcampus.aop.part2.recylcerviewtest.Network

import android.app.Application
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.realm.rx.RxObservableFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MasterApplication : Application() {
    lateinit var service: RetrofitService
    override fun onCreate() {
        super.onCreate()

        createRetrofit()
    }

    fun createRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(RetrofitService::class.java)
    }
}