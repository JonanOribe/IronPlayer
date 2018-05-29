package com.example.jonan.ironplayer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class MainActivityConfiguracion extends AppCompatActivity {

    private String credenciales_usuario;
    private String pass_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_configuracion);

        Button atras= (Button) findViewById(R.id.btnAtras);
        Button borrarCuenta=(Button) findViewById(R.id.btnBorrar);
        Intent intent = getIntent();
         credenciales_usuario = getIntent().getStringExtra("SESSION_ID");
         pass_usuario= getIntent().getStringExtra("SESSION_PASS");

        TextView textoUsuario=findViewById(R.id.txtUsuario);
        textoUsuario.setText(String.valueOf(credenciales_usuario));
        TextView textoPass=findViewById(R.id.txtContrasena);
        textoPass.setText((String.valueOf(pass_usuario)));


        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityConfiguracion.this,YoutubeActivity.class));
            }
        });

        borrarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        MainActivityConfiguracion.this);

                // set title
                alertDialogBuilder.setTitle("¿Seguro que quieres borrar tus datos?");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Si pulsas 'Si' estaras cerrando tu cuenta definitivamente")
                        .setCancelable(false)
                        .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                deleteIronUser(credenciales_usuario);
                                Intent cierre=new Intent(MainActivityConfiguracion.this,LoginActivity.class);
                                cierre.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                cierre.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Toast.makeText(getApplicationContext(),
                                        "Usuario "+credenciales_usuario+" borrado de IronPlayer", Toast.LENGTH_LONG)
                                        .show();
                                startActivity(cierre);
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

            }
    public boolean deleteIronUser(String login) {
        boolean rightDeleteIronUser = true;

        String urlApiREST = Constants.URL_IRON_PLAYER_API_RESTFULL + "/login/deleteIronUser/" + login;
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
                rightDeleteIronUser = true;
            } else
                rightDeleteIronUser = true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rightDeleteIronUser;
    }
    }

