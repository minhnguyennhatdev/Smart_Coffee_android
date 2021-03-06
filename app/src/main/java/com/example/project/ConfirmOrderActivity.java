package com.example.project;

import static com.example.lib.RetrofitClient.getRetrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lib.Model.CartListModel;
import com.example.lib.Model.DrinkOrder;
import com.example.lib.Model.Order.Order;
import com.example.lib.Model.Order.OrderDetail;
import com.example.lib.interfaceRepository.Methods;
import com.example.project.Adapter.ConfirmAdapter;
import com.example.project.PopupDialog.SuccessDialog;
import com.example.project.SendEmail.JavaMailAPI;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmOrderActivity extends AppCompatActivity {

    List<CartListModel> cartlist = new ArrayList<>();
    String address, name, email, phone, OrderID;
    int shippingFee;
    TextView confirmName, confirmEmail, confirmPhone, confirmAddress, confirmPrice, confirmFee, confirmTotalPrice;
    ListView lvConfirm;
    ConfirmAdapter confirmAdapter;
    Double price = 0.0;
    Double totalPrice = 0.0;
    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        confirmName = findViewById(R.id.confirmName);
        confirmPhone = findViewById(R.id.confirmPhone);
        confirmAddress = findViewById(R.id.confirmAddress);
        confirmPrice = findViewById(R.id.confirmPrice);
        confirmFee = findViewById(R.id.confirmFee);
        confirmTotalPrice = findViewById(R.id.confirmTotalPrice);

        lvConfirm = findViewById(R.id.confirmCartlist);
        confirmAdapter = new ConfirmAdapter(ConfirmOrderActivity.this, R.layout.confirmcard);

        Intent intent = getIntent();
        cartlist = (List<CartListModel>) intent.getSerializableExtra("cartlist");
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        address = intent.getStringExtra("address");

        confirmName.setText(name);
        confirmPhone.setText(phone);
        confirmAddress.setText(address);

        getCartList();

        lvConfirm.setAdapter(confirmAdapter);

        confirmPrice.setText(formatPrice(price));
        confirmTotalPrice.setText(formatPrice(price));

    }


    public void confirm(View view) {
        OrderID = getAlphaNumericString(10);
        Date d = new Date();

        ArrayList<DrinkOrder> drinks = new ArrayList<>();

        for(CartListModel item : cartlist){
            DrinkOrder drinkOrder = new DrinkOrder(item.getProduct().getId(), item.getProduct().getDrinkName(), Double.valueOf(item.getProduct().getDefaultPrice().get(0)), "S", item.getAmount());
            drinks.add(drinkOrder);
        }

        order = new Order(name, phone, drinks, totalPrice, "DELIVERY", address);

        insertOrder(order);

        cartlist.clear();

        showDialog();
    }



    public void getCartList(){
        for(CartListModel item : cartlist){
            price = price + item.getProduct().getDefaultPrice().get(0) * item.getAmount();
            confirmAdapter.add(new CartListModel(item.getProduct(), item.getAmount()));
        }
    }

    public String formatPrice (Double price){
        DecimalFormat format = new DecimalFormat("0.#");
        return format.format(price) + " VND";
    }

    public void insertOrder(Order order){
        Methods methods = getRetrofit().create(Methods.class);
        Call<Order> call = methods.insertOrder(order);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                Log.v("log:" , response.toString());
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.v("log:", t.getMessage());
            }
        });
    }

    public void insertOrderDetail(OrderDetail orderDetail){
        Methods methods = getRetrofit().create(Methods.class);
        Call<OrderDetail> call = methods.insertOrderDetail(orderDetail);
        call.enqueue(new Callback<OrderDetail>() {
            @Override
            public void onResponse(Call<OrderDetail> call, Response<OrderDetail> response) {
                Log.v("log:" , response.toString());
            }

            @Override
            public void onFailure(Call<OrderDetail> call, Throwable t) {
                Log.v("log:", t.getMessage());
            }
        });
    }

    public String getAlphaNumericString(int n)
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    public void showDialog(){
        SuccessDialog exampleDialog = new SuccessDialog();
        exampleDialog.show(getSupportFragmentManager(), "success dialog");
    }

}