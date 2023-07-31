package com.example.finance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Retrofit1 {
    @POST("api/users/register")
    fun registerUser(@Body map: HashMap<String, String>): Call<RegisterUserResponse>

    @POST("api/users/login")
    fun loginUser(@Body map: HashMap<String, String>): Call<LoginUserResponse>

    @GET("api/users/byEmail/{email}")
    fun getUserByName(@Path("email") email: String): Call<GetUserByNameResponse>


    /*@POST("/currentuser")
    fun getCurrentUser(): Call<CurrentUserResponse>*/
    @GET("api/transaction/owed-and-lent")
    fun getOwedAndLentData(@Query("userEmail") userEmail: String?): Call<OwedAndLentResponse>


    @GET("api/transaction")
    suspend fun getTransactions(): List<Transactions>

    @GET("api/splits")
    fun getAllSplits(): Call<List<Split>>


    @POST("api/splits")
    suspend fun createGroup(@Body groupData: GroupData): Response<CreateGroupResponse>

    @POST("api/splits/{splitId}/settle")
    fun settleSplit(@Path("splitId") splitId: String): Call<SplitSettleResponse>

    @POST("api/transaction/{id}/settle")
    fun settleTransaction(@Path("id") transactionId: String): Call<SettleResponse>
}



