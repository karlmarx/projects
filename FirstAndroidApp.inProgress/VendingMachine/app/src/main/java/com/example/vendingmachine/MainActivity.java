package com.example.vendingmachine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;
    public Cash cash = new Cash();
    List<Item> items = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cash.setBalance(BigDecimal.ZERO);
        textViewResult = findViewById(R.id.text_view_result);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( "http://tsg-vending.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        VendingMachineApi vendingMachineApi = retrofit.create(VendingMachineApi.class);

        Call<List<Item>> call = vendingMachineApi.getItems();

        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                items = response.body();
                for (Item item : items) {
                    String content = "";
                    content += "ID: "  + item.getId() + "\n";
                    content += "Name: " + item.getName() + "\n";
                    content += "Price: " + item.getPrice() + "\n";
                    content +=  "Quantity: " + item.getQuantity() + "\n\n";

                    textViewResult.append(content);
                 }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });

    }
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.purchase_item:
                Toast.makeText(this, "purchase what", Toast.LENGTH_SHORT).show();
                //dostuff
                return true;
            case R.id.show_balance:
                Toast.makeText(this, "Balance: $" + cash.getBalance().setScale(2), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.dollar:
                cash.setBalance(cash.getBalance().add(BigDecimal.ONE));
                Toast.makeText(this, "Thank you! Balance is now $" + cash.getBalance().setScale(2), Toast.LENGTH_LONG).show();
                return true;
            case R.id.quarter:
                cash.setBalance(cash.getBalance().add(new BigDecimal(0.25)));
                Toast.makeText(this, "Thank you! Balance is now $" + cash.getBalance().setScale(2), Toast.LENGTH_LONG).show();
                return true;
            case R.id.dime:
                cash.setBalance(cash.getBalance().add(new BigDecimal(0.10)));
                Toast.makeText(this, "Thank you! Balance is now $" + cash.getBalance().setScale(2), Toast.LENGTH_LONG).show();
                return true;
            case R.id.nickel:
                cash.setBalance(cash.getBalance().add(new BigDecimal(0.05)));
                Toast.makeText(this, "Thank you! Balance is now $" + cash.getBalance().setScale(2), Toast.LENGTH_LONG).show();
                return true;
            case R.id.return_change:
        }
        return true;
    }
}
