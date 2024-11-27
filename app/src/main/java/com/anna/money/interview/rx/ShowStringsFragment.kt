package com.anna.money.interview.rx

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

@SuppressLint("CheckResult")
abstract class ShowStringsFragment : Fragment(), View.OnClickListener {

    private var currentPage = 0
    private var loadMoreTrigger = {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Observable.merge(
            Observable.fromCallable {
                fetch()
            },
            Observable.create { emitter ->
                loadMoreTrigger = {
                    emitter.onNext(fetch(currentPage++))
                }
            }
        )
            .scan(emptyList<String>()) { accumulated, new ->
                accumulated + new
            }
            .subscribe {
                updateView(it)
            }
    }

    abstract fun updateView(data: List<String>)

    private fun fetch(page: Int? = null): List<String> {
        var url = "https://example.com/api/getStrings"
        if (page != null) {
            url += "?page=$page"
        }
        val response = OkHttpClient().newCall(Request.Builder().url(url).build()).execute()
        val jsonArray = JSONArray(response.body?.string() ?: "[]")
        return List(jsonArray.length()) { i ->
            val jsonObject = jsonArray.getJSONObject(i)
            jsonObject.getString("id")
            jsonObject.getString("value")
        }
    }

    override fun onClick(v: View?) {
        loadMoreTrigger()
    }
}
