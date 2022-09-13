package spintimez.slot.com.util.network

import spintimez.slot.com.appContext
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.CompletableDeferred
import org.json.JSONObject


object Network {
    private const val BASE_URL = "https://tb-int-site.pp.ua/"

    private val volley = Volley.newRequestQueue(appContext)



    suspend fun postJsonObject(
        urlPost    : UrlPost,
        requestBody: JSONObject,
    ) = CompletableDeferred<VolleyResult>().also { continuation ->

        val jsonRequest = JsonObjectRequest(
            Request.Method.POST, BASE_URL + urlPost.url, requestBody,
            { continuation.complete(VolleyResult.VolleyJSONObject(it)) },
            { continuation.complete(VolleyResult.VolleyError(it)) }
        )

        volley.add(jsonRequest)
    }.await()



    enum class UrlPost(val url: String) {
        CHECK("api/user/check/v2/"),
        DATA( "api/user/data/v2/" ),
    }

    sealed class VolleyResult {
        data class VolleyJSONObject(val jsonObject: JSONObject): VolleyResult()
        data class VolleyError(     val error     : com.android.volley.VolleyError): VolleyResult()
    }





}