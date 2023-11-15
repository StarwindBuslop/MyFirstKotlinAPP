package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class VinDecoderActivity : AppCompatActivity() {

    private lateinit var viewModel: VinDecoderViewModel
    private lateinit var repository: VinDecoderRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val vinDecoderService = RetrofitClient.getVinDecoderService()
        repository = VinDecoderRepository(vinDecoderService)
        viewModel = VinDecoderViewModel(repository)
        var picked =""
        // Set click listener for the button to invoke API call
        findViewById<Button>(R.id.decodeButton).setOnClickListener {
            val enteredVin = findViewById<EditText>(R.id.vinEditText).text.toString()
            if (enteredVin.length == 17) {
                viewModel.decodeVin(enteredVin)
                picked=enteredVin
            } else {
                // Handle invalid VIN length
            }
        }

        // Observe ViewModel response
        viewModel.response.observe(this) { response ->
            findViewById<TextView>(R.id.textview_second).setText(picked)
        }
    }
}
object RetrofitClient {
    private const val BASE_URL = "https://vpic.nhtsa.dot.gov/api/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getVinDecoderService(): VinDecoderService {
        return retrofit.create(VinDecoderService::class.java)
    }
}
