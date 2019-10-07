package com.example.vendingmachine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;
    private Cash cash = new Cash();
    List<Item> items = new ArrayList<>();
    Button mButton;
    EditText mEdit;
    Change change = new Change();
    private VendingMachineApi vendingMachineApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button)findViewById(R.id.purchase);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view) {
                mEdit = (EditText)findViewById(R.id.item_id);
                String currentMoney = cash.getBalance().setScale(2).toString();
                int chosenItem = Integer.parseInt(mEdit.getText().toString());
                purchaseItem(currentMoney, chosenItem);
            }
        });
        cash.setBalance(BigDecimal.ZERO);
        textViewResult = findViewById(R.id.text_view_result);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( "http://tsg-vending.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
         vendingMachineApi = retrofit.create(VendingMachineApi.class);
        getItems();
    }
    private void purchaseItem(String money, int chosenItem) {
        Call<Change> call = vendingMachineApi.getChange(money, chosenItem);

        call.enqueue(new Callback<Change>() {
            @Override
            public void onResponse(Call<Change> call, Response<Change> response) {

                if (!response.isSuccessful()) {
                    ModelError error = ErrorUtils.parseError(response);
                   Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                Change changeBack = response.body();
                change.setQuarters(changeBack.getQuarters());
                change.setDimes(changeBack.getDimes());
                change.setPennies(changeBack.getPennies());
                change.setNickels(changeBack.getNickels());
                Toast.makeText(MainActivity.this, "Thank you!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Change> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    private void getItems() {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


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
                String changeString = change.getQuarters() + " quarters, " + change.getDimes() + " dimes, " + change.getNickels() + " nickels, and " + change.getPennies() + " pennies.";
                Toast.makeText(this, changeString, Toast.LENGTH_LONG).show();
                cash.setBalance(BigDecimal.ZERO);
                return true;
        }
        return true;
    }
}
