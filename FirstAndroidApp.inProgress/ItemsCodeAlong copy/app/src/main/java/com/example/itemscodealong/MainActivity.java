package com.codinginflow.retrofitexample;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import java.util.List;

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

        textViewResult = findViewById(R.id.text_view_result);

        ButterKnife.bind(this);
        StaggeredGridLayoutManager mStaggeredGridManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        listViewItems.setLayoutManager(mStaggeredGridManager);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        com.codinginflow.retrofitexample.JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(com.codinginflow.retrofitexample.JsonPlaceHolderApi.class);

        Call<List<com.codinginflow.retrofitexample.Post>> call = jsonPlaceHolderApi.getPosts();



        call.enqueue(new Callback<List<com.codinginflow.retrofitexample.Post>>() {
            @Override
            public void onResponse(Call<List<com.codinginflow.retrofitexample.Post>> call, Response<List<com.codinginflow.retrofitexample.Post>> response) {

                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<com.codinginflow.retrofitexample.Post> posts = response.body();

                for (com.codinginflow.retrofitexample.Post post : posts) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "User ID: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n\n";

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<com.codinginflow.retrofitexample.Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}