package com.example.jonan.ironplayer;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ironplayer.android.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RecoverPasswordActivity extends AppCompatActivity {

    EditText user;
    Button recoverButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);
        user = (EditText) findViewById(R.id.editTextRP);
        recoverButton = (Button) findViewById(R.id.buttonRP);
        recoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Introduce usuario", Toast.LENGTH_LONG).show();
                } else {

                    boolean rightLoginIronUser = recoverPassword(user.getText().toString());
                    if (rightLoginIronUser) {
                        Toast.makeText(getApplicationContext(), "Password enviado a tu dirección de correo", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RecoverPasswordActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Usuario no existente", Toast.LENGTH_LONG).show();
                    }


                }
            }
        });
    }
    public boolean recoverPassword(String login) {
        boolean rightLoginIronUser = false;

        String urlApiREST = Constants.URL_IRON_PLAYER_API_RESTFULL+"/login/recoverPassword/" + login ;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(urlApiREST);
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            String json = new String();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            json = response.toString();

            JSONObject jsonObject = new JSONObject(json);


            Log.d("JSON  code", jsonObject.get("code").toString());
            Log.d("JSON  description", jsonObject.get("description").toString());

            if (jsonObject.get("code").toString().equals(Constants.OK)) {
                rightLoginIronUser = true;
            } else
                rightLoginIronUser = false;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rightLoginIronUser;
    }




}
