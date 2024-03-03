package com.example.twitterintegration;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private EditText editTextStatus;
    private Button buttonTweet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Twitter.initialize(this);

        editTextStatus = findViewById(R.id.editTextStatus);
        buttonTweet = findViewById(R.id.buttonTweet);

        buttonTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus(editTextStatus.getText().toString());
            }
        });
    }

    private void updateStatus(String status) {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (session != null) {
            StatusesService statusesService = TwitterCore.getInstance().getApiClient(session).getStatusesService();
            Call<Tweet> call = statusesService.update(status, null, false, null, null, null, false, null);
            call.enqueue(new Callback<Tweet>() {
                @Override
                public void onResponse(Call<Tweet> call, retrofit2.Response<Tweet> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Статус успешно обновлен", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Ошибка при обновлении статуса", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Tweet> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Ошибка при обновлении статуса: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "Пользователь не аутентифицирован", Toast.LENGTH_SHORT).show();
        }
    }
}