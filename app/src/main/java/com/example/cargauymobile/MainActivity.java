package com.example.cargauymobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;



import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    /*
    TextView textView;
    public String location;

    String client_id = "";
    String client_secret = "";
    String redirect_uri = "https://openidconnect.net/callback";
    Uri uredirect_uri = Uri.parse("https://openidconnect.net/callback");
    String last_state = "";
    CookieManager manager;
    int RC_AUTH = 0;
    public static final String ACCESS_TOKEN_MESSAGE = "";
    public static final String REFRESH_TOKEN_MESSAGE = "";
    public static final String ID_TOKEN_MESSAGE = "";

    /*
    //Definiendo los valores de gub.uy
    String AUTHORIZATION_SERVER_URL = "https://auth-testing.iduruguay.gub.uy/oidc/v1";
    String ACCESS_TOKEN_ENDPOINT_URL = "https://auth-testing.iduruguay.gub.uy/oidc/v1/token";
    String AUTHORIZE_ENDPOINT_URL = "https://auth-testing.iduruguay.gub.uy/oidc/v1/authorize";
    String END_SESSION_ENDPOINT = "https://auth-testing.iduruguay.gub.uy/oidc/v1/logout";
    String REDIRECT_URI = "https://openidconnect.net/callback";
    */

    TextView textView;
    public String location;
/******************************************************************/
    //INGRESAR SU CLIENT_SECRET Y CLIENT ID AQUI
// /******************************************************************/
    String client_id = "";
    String client_secret ="";
    String redirect_uri ="https://openidconnect.net/callback";
    Uri uredirect_uri =Uri.parse("https://openidconnect.net/callback");
    String last_state ="" ;
    CookieManager manager;
    int RC_AUTH = 0;
    public static final String ACCESS_TOKEN_MESSAGE = "com.example.cargauymobile.ACCESS_TOKEN_MESSAGE";
    public static final String REFRESH_TOKEN_MESSAGE = "com.example.cargauymobile.REFRESH_TOKEN_MESSAGE";
    public static final String ID_TOKEN_MESSAGE = "com.example.cargauymobile.ID_TOKEN_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        textView = (TextView) findViewById(R.id.text);
        handleSSLHandshake();
        manager = new CookieManager();
        CookieHandler.setDefault( manager  );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Login (View v) {

        // Instantiate the RequestQueue.
        //RequestQueue queue = Volley.newRequestQueue(this);
        RequestQueue queue = Volley.newRequestQueue(this, new HurlStack() {
            @Override
            protected HttpURLConnection createConnection(URL url) throws IOException {
                HttpURLConnection connection = super.createConnection(url);
                connection.setInstanceFollowRedirects(false);

                return connection;
            }
        });


        String url = armarAuthenticationURL();
        Log.e("Login", "LoginURL:" + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Login", "onResponseLogin:" + response);
                        textView.setText("Response is: " + response.substring(0, 500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Login", "onErrorResponse" + "lalala");
                textView.setText("That didn't work!");
            }
        }) {
            @Override
            public void deliverError(final VolleyError error) {
                final int status = error.networkResponse.statusCode;

                if (HttpURLConnection.HTTP_MOVED_PERM == status || status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_SEE_OTHER) {
                    location = error.networkResponse.headers.get("Location");
                    Log.e("Login.deliverError", "Location: " + location);
                    int ps = location.indexOf("process_state");
                    String process_state = location.substring(ps + 14);
                    Log.e("Login.deliverError", "process_state: " + process_state);

                    enviarCredencialesCI(process_state);
                }
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private String armarAuthenticationURL(){
        int random = ThreadLocalRandom.current().nextInt(0, 999999);
        String state = "asdf" +random;
        last_state = state;
        String url ="https://auth-testing.iduruguay.gub.uy/oidc/v1/authorize?";
        url = url + "response_type=code";
        url = url +"&scope=openid%20personal%20email";
        url = url + "&client_id=" + client_id;
        url = url + "&state="+state;
        url = url + "&redirect_uri=https%3A%2F%2Fopenidconnect.net%2Fcallback";

        return url;
    }

    /**
     * Enables https connections
     */

    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake(){
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        }catch (Exception ignored){
        }
    }

    public void enviarCredencialesCI(String process_state){
        //Obtenemos user y pass
        final EditText userEditText = (EditText)findViewById(R.id.editTextTextPersonName);
        String usuario = userEditText.getText().toString();

        String url ="https://auth-testing.iduruguay.gub.uy/auth/login";
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "https://auth-testing.iduruguay.gub.uy/auth/login";
            final JSONObject jsonBody = new JSONObject();
            final String requestBody = "{\"challenge\":\"username\",\"data_challenge\":{\"username\":\"" +usuario+"\",\"pais_emisor\":null,\"tipo_documento\":null},\"extra\":{\"agnt\":null,\"process_state\":\""+process_state+ "\"}}";
            Log.e ("enviarCredencialesCI", "requestBody:"+requestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String token = "";
                    Log.e("VOLLEY", response);
                    try {
                        JSONObject jo = new JSONObject(response);
                        token = jo.getString("token");
                        Log.e("token", token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    enviarCredencialesPass(token);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEYerror", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError{
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        uee.printStackTrace();
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

            };

            requestQueue.add(stringRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void enviarCredencialesPass (String token){
        final EditText passEditText = (EditText)findViewById(R.id.editTextTextPassword);
        String pass = passEditText.getText().toString();

        //RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://auth-testing.iduruguay.gub.uy/auth/login";
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "https://auth-testing.iduruguay.gub.uy/auth/login";
            JSONObject jsonBody = new JSONObject();
            final String requestBody = "{\"challenge\":\"urn:iduruguay:am:password\",\"data_challenge\":{\"password\":\""+pass+"\"},\"extra\":null,\"token\":\""+token+"\"}";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("onResponse","enviarCredencialesPass.onResponse:"+ response);
                    String URLFin="";
                    try {
                        JSONObject jo = new JSONObject(response);
                        URLFin= jo.getString("location");
                        Log.e("enviarCredencialesPass", URLFin);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    pedidoFinal2(URLFin);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("enviarCredencialesPass", "VOLLEYerror:"+error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pedidoFinal (String URLFin) {
        //RequestQueue queue = Volley.newRequestQueue(this);

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, URLFin, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String REDIRFIN ="";
                    Log.e("PedidoFinalOnResponse", response);
                   /* try {
                        JSONObject jo = new JSONObject(response);
                        REDIRFIN= jo.getString(REDIRFIN);
                        Log.e("REDIRFIN", REDIRFIN);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEYerror", error.toString());
                }
            }) {

            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void pedidoFinal2 (String url) {
        // Instantiate the RequestQueue.
        //RequestQueue queue = Volley.newRequestQueue(this);
        RequestQueue queue = Volley.newRequestQueue(this, new HurlStack() {
            @Override
            protected HttpURLConnection createConnection(URL url) throws IOException {
                HttpURLConnection connection = super.createConnection(url);
                connection.setInstanceFollowRedirects(false);

                return connection;
            }
        });

        // RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.e("onResponseFinal2",response);
                        textView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("onErrorResponseFinal2", "That didn't work!");
                textView.setText("That didn't work!");

            }
        }){

            @Override
            public void deliverError(final VolleyError error) {
                //log.d("deliverError", "deliverError");

                final int status = error.networkResponse.statusCode;
                // Handle 30x
                if(HttpURLConnection.HTTP_MOVED_PERM == status || status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_SEE_OTHER) {
                    location = error.networkResponse.headers.get("Location");
                    Log.e("deliverErrorFinal2", "Location: " + location);
                    int  codeInicio = location.indexOf("code=");
                    int  stateInicio = location.indexOf("state=");
                    //Log.e("codeInicio", "CodeInicio: " +codeInicio);
                    //Log.e("stateInicio", "stateInicio: " + stateInicio);
                    String codeString = location.substring(codeInicio+5, stateInicio-1);
                    String stateString = location.substring(stateInicio+6);
                    //Log.e("deliverErrorFinal2", "codeString: " + codeString);
                    //Log.e("deliverErrorFinal2", "stateString: " + stateString);
                    getToken2(codeString,stateString);
                }
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    /***Pedido como json**/
    public void getToken (String code, String state) {
        //RequestQueue queue = Volley.newRequestQueue(this);
        Log.e("getToken","Este pedido lo haría el server (backend)");
        String url ="https://auth-testing.iduruguay.gub.uy/oidc/v1/token";
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "https://auth-testing.iduruguay.gub.uy/oidc/v1/token";
            final JSONObject jsonBody = new JSONObject();
            final String requestBody = "";


            //final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String token ="";
                    Log.e("getToken.onResponse", response);
                    try {
                        JSONObject jo = new JSONObject(response);
                        token= jo.getString("token");
                        Log.e("token", token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    enviarCredencialesPass(token);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("getToken.onErrorRespons", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        uee.printStackTrace();
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*** Pedido con params***/
    public void getToken2 (String code, String state) {
        //RequestQueue queue = Volley.newRequestQueue(this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url ="https://auth-testing.iduruguay.gub.uy/oidc/v1/token";
        final String codeS = code;
        //Log.e("getToken2", "inicio");
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("getToken2.Response", response);
                        String access_token="";
                        String refresh_token ="";
                        String id_token ="";
                        try {
                            JSONObject jo = new JSONObject(response);
                            access_token= jo.getString("access_token");
                            refresh_token= jo.getString("refresh_token");
                            id_token= jo.getString("id_token");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        iniciarResultado(access_token, refresh_token, id_token);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.e("getToken2.onErrorRespon", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("grant_type", "authorization_code");
                params.put("code", codeS);
                params.put("redirect_uri",redirect_uri);

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                //OJO, esto es solo ejemplo, no client_secret en el android, esto va en el server
                Log.e("getToken2","Ojo, esto va en el servidor, el secret no debe de estar en el apk" );
                String creds = String.format("%s:%s",client_id,client_secret);
                //String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                String auth = "Basic " + "ODkwMTkyOjQ1N2Q1MmYxODFiZjExODA0YTMzNjViNDlhZTRkMjlhMmUwM2JiYWJlNzQ5OTdhMmY1MTBiMTc5";
                params.put("Authorization", auth);
                //Content-Type: application/x-www-form-urlencoded
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(postRequest);
    }

    public void iniciarResultado(String access_token, String refresh_token, String id_token) {
        Intent intent = new Intent(this, ResultadoActivity.class);
        intent.putExtra(ACCESS_TOKEN_MESSAGE, access_token);
        intent.putExtra(REFRESH_TOKEN_MESSAGE, refresh_token);
        intent.putExtra(ID_TOKEN_MESSAGE, id_token);
        startActivity(intent);
    }



      //Empieza el LoginWeb - Ejemplo profesor Federico, clase Android 2022

    public void LoginWeb (View v){
        String url = "https://auth-testing.iduruguay.gub.uy/oidc/v1/authorize?response_type=code&scope=openid%20personal%20email&client_id=890192&state=asdf934087&redirect_uri=https%3A%2F%2Fopenidconnect.net%2Fcallback";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }


}

