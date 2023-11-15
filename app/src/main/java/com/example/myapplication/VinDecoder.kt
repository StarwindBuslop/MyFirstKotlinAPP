package com.example.myapplication
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {
    // Define your API endpoints using Retrofit annotations
    // For example:
    // @GET("endpoint")
    // suspend fun getSomeData(): Response<YourDataClass>
    @GET("vehicles/DecodeVinValues/{vin}?format=json")
    suspend fun decodeVin(@Path("vin") vin: String): Response<ApiResponse>
}
data class ApiResponse(val Results: List<VehicleInfo>)
data class VehicleInfo(val vin:String)


interface VinDecoderService {
    @GET("vehicles/DecodeVinValues/{vin}?format=json")
    suspend fun decodeVin(@Path("vin") vin: String): Response<ApiResponse>
}
class VinDecoderRepository(private val vinDecoderService: VinDecoderService) {
    suspend fun decodeVin(vin: String): ApiResponse? {
        val response = vinDecoderService.decodeVin(vin)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}
class VinDecoderViewModel(private val repository: VinDecoderRepository) : ViewModel() {
    private val _response = MutableLiveData<ApiResponse?>()
    val response: LiveData<ApiResponse?> get() = _response

    fun decodeVin(vin: String) {
        viewModelScope.launch {
            _response.value = repository.decodeVin(vin)
        }
    }
}