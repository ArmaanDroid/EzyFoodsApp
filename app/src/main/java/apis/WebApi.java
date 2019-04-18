package apis;

import model.ApiResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Armaan on 01-11-2017.
 */

public interface WebApi {

    @POST("{path}")
    Call<ApiResponse> makeRequest(
            @Body WebRequestBody payload, @Path(value = "path",
            encoded = true) String urlEndPoint);

//    @FormUrlEncoded
//    @POST("{path}")
//    Call<ApiResponse> makePaymentRequest(
//            @Header("Authorization") String authorization,
//            @Body WebRequestBody payload,
//            @Field("token") Token token, @Path(value = "path",
//            encoded = true) String urlEndPoint);

    @POST("{path}")
    Call<ApiResponse> makeRequestAuthentication(
            @Header("Authorization") String authorization,
            @Body WebRequestBody payload, @Path(value = "path",
            encoded = true) String urlEndPoint);

    @GET()
    Call<ApiResponse> makeGetRequest(
            @Header("Authorization") String authorization,
            @Url String urlEndPoint);

    @GET("{path}")
    Call<ApiResponse> makeGetRequestWithoutEncode(@Path(value = "path",
            encoded = true) String urlEndPoint);

    @GET("user/transport")
    Call<ApiResponse> getRoutes(@Query("source") String source,
                                @Query("destination") String destination);

    @PUT("{path}")
    Call<ApiResponse> updateData(
            @Header("authorization") String authorization,
            @Path(value = "path", encoded = true) String urlEndPoint,
            @Body WebRequestBody payload
    );

    @Multipart
    @PUT("{path}")
    Call<ApiResponse> updateProfile(
            @Header("authorization") String authorization,
            @Path(value = "path", encoded = true) String urlEndPoint,
            @Part MultipartBody.Part file,
            @Part("data") RequestBody description);

    @Multipart
    @POST("{path}")
    Call<ApiResponse> updateProfileStudent(
            @Header("authorization") String authorization,
            @Path(value = "path", encoded = true) String urlEndPoint,
            @Part MultipartBody.Part file,
            @Part("name") RequestBody name,
            @Part("countryCode") RequestBody countryCode,
            @Part("phoneNo") RequestBody phoneNo,
            @Part("university") RequestBody university,
            @Part("country") RequestBody country
    );

    @Multipart
    @POST("{path}")
    Call<ApiResponse> requestWithData(
            @Header("Authorization") String authorization,
            @Path(value = "path", encoded = true) String urlEndPoint,
            @Part MultipartBody.Part file,
            @Part("data") RequestBody description);

    @Multipart
    @POST("{path}")
    Call<ApiResponse> requestWithMultipleData(
            @Header("Authorization") String authorization,
            @Path(value = "path", encoded = true) String urlEndPoint,
            @Part MultipartBody.Part[] file,
            @Part("data") RequestBody description);

}
