package in.chardhamtour.chardhamyatra.controller.api;

import in.chardhamtour.chardhamyatra.controller.ApiCall.BannerCall;
import in.chardhamtour.chardhamyatra.controller.ApiCall.ChardhamDataCall;
import in.chardhamtour.chardhamyatra.controller.ApiCall.CommonCall;
import in.chardhamtour.chardhamyatra.controller.ApiCall.PackageCall;
import in.chardhamtour.chardhamyatra.controller.ApiCall.ReviewCall;
import in.chardhamtour.chardhamyatra.controller.ApiCall.SubPackDetailCall;
import in.chardhamtour.chardhamyatra.controller.ApiCall.SubPackageCall;
import in.chardhamtour.chardhamyatra.controller.ApiCall.UserCall;
import in.chardhamtour.chardhamyatra.controller.ApiCall.WishListCall;
import in.chardhamtour.chardhamyatra.model.QueryCall;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("sub_packages.php?trending=1")
    Call<SubPackageCall> getTrending();

    @GET("sub_packages.php")
    Call<SubPackageCall> getAssociatedSubPackages(@Query("package_id") long package_id, @Query("related") int bool);

    @GET("search.php")
    Call<PackageCall> getSearch(@Query("search") String item);

    @FormUrlEncoded
    @POST("wish_list.php")
    Call<CommonCall> resetFav(@Field("user_id") long user_id, @Field("sub_package_id") long spi);

    @FormUrlEncoded
    @POST("forgot_password.php")
    Call<CommonCall> resetPassword(@Field("email") String email, @Field("data") String otpOrPassword, @Field("action") int action);

    @GET("wish_list.php")
    Call<CommonCall> isFav(@Query("user_id") long user_id, @Query("sub_package_id") long spi);

    @GET("single_user_query.php")
    Call<QueryCall> getQueries(@Query("user_id") long user_id);

    @GET("getuserid_booking.php")
    Call<QueryCall> getBookings(@Query("user_id") long user_id);

    @GET("single_user_wish_list.php")
    Call<WishListCall> getWishList(@Query("user_id") long user_id);

    @GET("sub_packages.php")
    Call<SubPackDetailCall> getSubPackageDetail(@Query("id") long sub_package_id);

    @GET("review.php")
    Call<ReviewCall> getReviews(@Query("sub_package_id") long sub_package_id);

    @FormUrlEncoded
    @POST("review.php")
    Call<CommonCall> addReview(@Field("data") String data);

    @GET("main_packages.php")
    Call<PackageCall> getPackages();

    @POST("main_packages.php")
    Call<PackageCall> getHomePackages();

    @GET("notify.php")
    Call<PackageCall> getNotification();

    @GET("chardham_detail.php")
    Call<ChardhamDataCall> getChardhamData();

    @GET("banner.php")
    Call<BannerCall> getBanner();

    @FormUrlEncoded
    @POST("user_login.php")
    Call<UserCall> loginUser(@Field("name") String name,@Field("client") String client, @Field("email") String email,
                             @Field("password") String password, @Field("client_ id") String client_id);

    @FormUrlEncoded
    @POST("booking.php")
    Call<CommonCall> startBooking(@Field("data") String data);

    @FormUrlEncoded
    @POST("user_register.php")
    Call<CommonCall> registerUser(@Field("name") String name, @Field("email") String email,
                                  @Field("password") String password, @Field("client") String client,
                                  @Field("client_id") String client_id);


    @FormUrlEncoded
    @POST("user_query.php")
    Call<CommonCall> requestQuery(@Field("departure_name") String departure_name,
                                  @Field("user_id") long user_id,@Field("phone") String phone,
                                  @Field("email") String email,
                                  @Field("travel_date") String travel_date, @Field("sub_package_id") long pack_id);

}
