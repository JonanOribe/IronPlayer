package com.example.jonan.ironplayer;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class MainActivity_Formulario_Registro extends AppCompatActivity {

    public EditText editTextUser;
    public EditText editTextPassword;

    Button botonInsertarRegristro;

    EditText textoUsuario, textoPassword, textoEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__formulario__registro);

        botonInsertarRegristro = (Button) findViewById(R.id.btnRegistrarse);
        editTextUser = (EditText) findViewById(R.id.editTextUsuario);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);


        final BBDD_Helper helper = new BBDD_Helper(this);

        botonInsertarRegristro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    if (editTextUser.getText().toString().equals("") || editTextPassword.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Introduce usuario y contraseña", Toast.LENGTH_LONG).show();
                    } else {
                        boolean rightLoginIronUser = addNewIronUser(editTextUser.getText().toString(), editTextPassword.getText().toString());
                        if (rightLoginIronUser) {
                            Intent pasar_a_MainActivity_Principal= new Intent(MainActivity_Formulario_Registro.this,YoutubeActivity.class);// Anteriormente MainActivity_Youtube
                            String user= editTextUser.getText().toString();
                            String pass= editTextPassword.getText().toString();

                            pasar_a_MainActivity_Principal.putExtra("SESSION_ID", editTextUser.getText().toString());
                            pasar_a_MainActivity_Principal.putExtra("SESSION_PASS", editTextPassword.getText().toString());

                            startActivity(pasar_a_MainActivity_Principal);

                        } else {
                            Toast.makeText(getApplicationContext(), Constants.ERROR_USER_EXISTS, Toast.LENGTH_LONG).show();
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Usuario ya registrado.", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public boolean addNewIronUser(String login, String password) {
        boolean rightLoginIronUser = true;

        String urlApiREST = Constants.URL_IRON_PLAYER_API_RESTFULL + "/login/addNewIronUser/" + login + "/" + password;
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy().Builder().per
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
