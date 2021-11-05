package fastcampus.aop.part2.recylcerviewtest.Data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.io.Serializable

//open class TestList (
//    val localee : String? = null,
//    val version_no : Int? = null,
//    val language : String? = null,
//    val res_url : String? = null,
//    val vorder : Int? = null,
//    val sts : Int? = null,
//    val lang : String? = null
//):Serializable

@RealmClass
open class TestList : RealmObject() {
    @SerializedName("locale")
    @Expose
    var locale : String? = null

    @SerializedName("version_no")
    @Expose
    var version_no : Int? = null

    @SerializedName("language")
    @Expose
    var language : String? = null

    @SerializedName("res_url")
    @Expose
    var res_url: String? = null

    @SerializedName("vorder")
    @Expose
    var vorder : Int? = null

    @SerializedName("sts")
    @Expose
    var sts : Int? = null

    @SerializedName("lang")
    @Expose
    var lang : String? = null
}