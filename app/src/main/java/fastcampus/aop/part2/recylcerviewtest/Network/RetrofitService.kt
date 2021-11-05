package fastcampus.aop.part2.recylcerviewtest.Network

import fastcampus.aop.part2.recylcerviewtest.Data.TestList
import io.reactivex.rxjava3.core.Observable
import io.realm.OrderedRealmCollection
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitService {
    @GET("getMultilingual.json")
//    fun getTestList():Call<OrderedRealmCollection<TestList>>
    fun getTestList():Call<OrderedRealmCollection<TestList>>
}