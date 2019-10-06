package com.example.vendingmachine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.List;

import adaptors.ItemListAdapter;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.listItems)
    RecyclerView listViewItems;

    private TextView textViewResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        StaggeredGridLayoutManager mStaggeredGridManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        listViewItems.setLayoutManager(mStaggeredGridManager);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( "http://tsg-vending.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        VendingMachineApi vendingMachineApi = retrofit.create(VendingMachineApi.class);

        Call<List<Item>> call = vendingMachineApi.getItems();

        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {

//                if (!response.isSuccessful()) {
//                    textViewResult.setText("Code: " + response.code());
//                    return;
//                }
                List<Item> items = response.body();
                ItemListAdapter adapter = new ItemListAdapter(items);
                listViewItems.setAdapter(adapter);
//                for (Item item : items) {
//                    String content = "";
//                    content += "ID: "  + item.getId() + "\n";
//                    content += "Name: " + item.getName() + "\n";
//                    content += "Price: " + item.getPrice() + "\n";
//                    content +=  "Quantity: " + item.getQuantity() + "\n\n";
//
//                    textViewResult.append(content);
//                 }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });


    }
}
