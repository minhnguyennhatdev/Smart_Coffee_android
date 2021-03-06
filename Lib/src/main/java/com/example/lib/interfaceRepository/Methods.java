package com.example.lib.interfaceRepository;

import com.example.lib.Model.Customer;
import com.example.lib.Model.CustomerResponse;
import com.example.lib.Model.DrinkDetail;
import com.example.lib.Model.DrinkList;
import com.example.lib.Model.GHNapi.District;
import com.example.lib.Model.GHNapi.Province;
import com.example.lib.Model.GHNapi.Ward;
import com.example.lib.Model.Order.Address;
import com.example.lib.Model.Order.Order;
import com.example.lib.Model.Order.OrderDetail;
import com.example.lib.Model.Order.ShippingFee;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Methods {

    //product
    @GET("api/drink")
    Call<DrinkList> getProducts();
    @GET("api/drink/detail/{id}")
    Call<DrinkDetail> getDetail(@Path("id") String id);
    @POST("api/order/orderonline")
    Call<Order> insertOrder(@Body Order order);
    @POST("api/Details")
    Call<OrderDetail> insertOrderDetail(@Body OrderDetail orderDetail);
    @POST("api/customer/login")
    Call<CustomerResponse> loginCustomer(@Body Customer user);

    //ghnApi
    @GET("https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province")
    Call<Province> getProvince(@Header("token") String token);

    @GET("https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district")
    Call<District> getDistrict(@Header("token") String token);

    @GET("https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward")
    Call<Ward> getWard(@Header("token") String token, @Query("district_id") int districtID);

    @GET("http://api.positionstack.com/v1/forward")
    Call<Address> getAddressLocation(
            @Query("access_key") String access_key,
            @Query("query") String query);

    @GET("https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee")
    Call<ShippingFee> getShippingFee(
            @Header("token") String token,
            @Query("service_id") int service_id,
            @Query("from_province_id") int from_province_id,
            @Query("from_district_id") int from_district_id,
            @Query("to_province_id") int to_province_id,
            @Query("to_district_id") int to_district_id,
            @Query("height") int height,
            @Query("length") int length,
            @Query("weight") int weight,
            @Query("width") int width);
}
