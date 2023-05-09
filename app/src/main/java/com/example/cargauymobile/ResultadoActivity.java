package com.example.cargauymobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);


        Intent intent = getIntent();
        String access_token = intent.getStringExtra(MainActivity.ACCESS_TOKEN_MESSAGE);
        String refresh_token = intent.getStringExtra(MainActivity.REFRESH_TOKEN_MESSAGE);
        String id_token = intent.getStringExtra(MainActivity.ID_TOKEN_MESSAGE);



        final TextView accessTokenTV  = (TextView)findViewById(R.id.AccessTokenTextView);
        accessTokenTV.setText("access_token:"+access_token);

        final TextView refreshTokenTV = (TextView)findViewById(R.id.RefreshTokenTextView);
        refreshTokenTV.setText("refresh_token:"+refresh_token);
        final TextView idTokenTV = (TextView)findViewById(R.id.idTokenTextView);
        idTokenTV.setText("id_token:"+id_token);

    }

}
