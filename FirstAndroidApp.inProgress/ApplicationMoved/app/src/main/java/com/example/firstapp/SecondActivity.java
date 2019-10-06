package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.AjaxChaosServiceGenerator;
import com.example.Animal;
import com.example.AnimalService;
import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor;

import java.io.IOException;
import java.util.Random;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondActivity extends AppCompatActivity {
    private static final String TOTAL_COUNT = "total_count";
    AnimalService service
            = AjaxChaosServiceGenerator.createService(AnimalService.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        showRandomNumber();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new OkHttpProfilerInterceptor());
        }
        OkHttpClient client = builder.build();

    }
    private void showRandomNumber () {
        TextView randomView = (TextView) findViewById(R.id.textview_random);
        TextView randomHeader = (TextView) findViewById(R.id.textview_label);
        int count = getIntent().getIntExtra(TOTAL_COUNT, 0);

        Random random = new Random();
        int randomInt = 0;
        if (count > 0) {
            randomInt = random.nextInt(count);
        }
        randomView.setText(Integer.toString(randomInt));
//        try {
//            Response<Animal> response = CallSync.execute();
//           Animal randoAnimal  = response.body();
//            System.out.println(user);
//        } catch (IOException ex) {
//
//        }//        callAsync.enqueue(new Callback<Animal>() {
////            @Override
//            public void onResponse(Call<Animal> call, Response<Animal> response) {
//                 animal = response.body();
//            }
//
//            @Override
//            public void onFailure(Call<Animal> call, Throwable throwable) {
//                System.out.println(throwable);
//            }
//        });

        //randomHeader.setText(getString(R.string.random_heading, count));
        Animal randoAnimal = getAnimal();
        randomHeader.setText(randoAnimal.getName());

    }
    public Animal getAnimal() {
        Call<Animal> call = service.getAnimal();
        Response<Animal> response;
        try {
            response = call.execute();
        } catch (IOException e) {
            return null; // Handle network communication errors here
        }

        if (!response.isSuccessful()) {
            return null;
            // Handle REST service errors here
        }
        return response.body();
    }


}
