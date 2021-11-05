package fastcampus.aop.part2.recylcerviewtest.View

import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import fastcampus.aop.part2.recylcerviewtest.Data.TestList
import fastcampus.aop.part2.recylcerviewtest.Network.MasterApplication
import fastcampus.aop.part2.recylcerviewtest.R
import fastcampus.aop.part2.recylcerviewtest.databinding.ActivityRecyclerViewBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.toObservable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmQuery
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.item_view_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class RecyclerViewActivity : AppCompatActivity() {

    lateinit var binding: ActivityRecyclerViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //서버 통신
        (application as MasterApplication).service.getTestList().enqueue(
            object : Callback<OrderedRealmCollection<TestList>> {
                override fun onResponse(
                    call: Call<OrderedRealmCollection<TestList>>,
                    response: Response<OrderedRealmCollection<TestList>>
                ) {
                    if (response.isSuccessful) {
                        var testList: OrderedRealmCollection<TestList>? = response.body()

                        // Realm 생성
                        Realm.init(this@RecyclerViewActivity)
                        val config: RealmConfiguration =
                            RealmConfiguration.Builder().allowWritesOnUiThread(true)
                                .deleteRealmIfMigrationNeeded()
                                .build()
                        Realm.setDefaultConfiguration(config)

                        val realm = Realm.getDefaultInstance()

                        realm.executeTransaction {
                            //데이터 저장
                            with(it.createObject(TestList::class.java)) {
                                this.locale = locale
                                this.version_no = version_no
                                this.language = language
                                this.res_url = res_url
                                this.vorder = vorder
                                this.sts = sts
                                this.lang = lang
                            }
                            // 데이터 업데이트
                            it.copyToRealmOrUpdate(testList)

                        }
                        // 데이터 가져오기
                        testList = realm.where(TestList::class.java).findAll()
                        Log.d("dataa", "data : $testList")

                        // 리사이클러뷰 초기화
                        initRecyclerView(testList)

                        // 1초 간격으로 데이터 화면에 넘기면서 보여주기
                        makeInterval()

                    }
                }

                override fun onFailure(call: Call<OrderedRealmCollection<TestList>>, t: Throwable) {
                    Log.d("dataa", "response fail")
                }
            }
        )

    }

    fun initRecyclerView(testList: OrderedRealmCollection<TestList>) {
        val adapter = RecyclerAdapter(
            testList, //Realm의 데이터를 넣어줘보자
            LayoutInflater.from(this@RecyclerViewActivity)
        )
        binding.testRecycler.adapter = adapter
        binding.testRecycler.layoutManager =
            LinearLayoutManager(
                this@RecyclerViewActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
    }

    fun makeInterval() {
        Observable.interval(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.testRecycler.scrollToPosition(it.toInt())
            }
    }

}

class RecyclerAdapter(
    var list: OrderedRealmCollection<TestList>?,
    val inflater: LayoutInflater
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val locale: TextView
        val version_no: TextView?
        val language: TextView
        val res_url: TextView
        val vorder: TextView
        val sts: TextView
        val lang: TextView

        init {
            locale = itemView.findViewById(R.id.locale)
            version_no = itemView.findViewById(R.id.version_no)
            language = itemView.findViewById(R.id.language)
            res_url = itemView.findViewById(R.id.res_url)
            vorder = itemView.findViewById(R.id.vorder)
            sts = itemView.findViewById(R.id.sts)
            lang = itemView.findViewById(R.id.lang)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_view_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.locale?.setText(list?.get(position)?.locale)
        holder.version_no?.setText(list?.get(position)?.version_no.toString())
        holder.language?.setText(list?.get(position)?.language)
        holder.res_url?.setText(list?.get(position)?.res_url)
        holder.vorder?.setText(list?.get(position)?.vorder.toString())
        holder.sts?.setText(list?.get(position)?.sts.toString())
        holder.lang?.setText(list?.get(position)?.lang)

    }

    override fun getItemCount(): Int {
        return list!!.size
    }

}