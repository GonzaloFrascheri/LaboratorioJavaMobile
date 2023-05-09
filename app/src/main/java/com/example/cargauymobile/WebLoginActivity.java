package com.example.cargauymobile;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WebLoginActivity extends AppCompatActivity {

    String callback_uri = "";

    String code = "";

    String state = "";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_auth_login);
        String resultado = getIntent().getDataString();
        Uri uresultado = getIntent().getData();
        Log.e("onCreate", "Resultado:"+resultado);
        final TextView resultadoTextView = (TextView)findViewById(R.id.Resultado);
        resultadoTextView.setText("Resultado:"+resultado);

        callback_uri = uresultado.getHost() + uresultado.getPath();
        Log.e("onCreate","callback_uri:"+callback_uri);
        final TextView redirectUri = (TextView)findViewById(R.id.RedirectUri);
        redirectUri.setText("callback_uri:"+callback_uri);
        code = uresultado.getQueryParameter("code");
        state     = uresultado.getQueryParameter("state");

        Log.e("onCreate","code:"+code);
        final TextView codeTextView = (TextView)findViewById(R.id.Code);
        codeTextView.setText("Code:"+code);

        Log.e("onCreate","state:"+state);
        final TextView stateTextView = (TextView)findViewById(R.id.State);
        stateTextView.setText("State:"+ state);

    }


}
