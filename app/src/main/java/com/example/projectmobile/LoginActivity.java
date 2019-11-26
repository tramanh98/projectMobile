package com.example.projectmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectmobile.database.DatabaseManagerUser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    CardView btnLogin;
    EditText edtUsername;
    EditText edtPassword;
    String password;
    String username;
    private Cursor comprobar;
    Intent calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_uit_activity);
        btnLogin = (CardView) findViewById(R.id.login);
        edtUsername = (EditText)findViewById(R.id.username);
        edtPassword = (EditText)findViewById(R.id.password);


        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }
    private  void handleLogin()   /// xác thực MSSV và password ở đây
    {
        String endpoint = "login/token.php";
        username = edtUsername.getText().toString();
        password = edtPassword.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Login...");
        progressDialog.show();


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        String endpoint = "login/token.php";

                        final RequestParams requestParams = new RequestParams();
                        requestParams.add("username",username);
                        requestParams.add("password",password);
                        requestParams.add("service","moodle_mobile_app");

                        HttpUtils.get(endpoint, requestParams, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                                Log.d("response login",responseBody.toString());
                                try {
                                    if(responseBody.has("token")){
                                        edtUsername.getText().clear();
                                        edtPassword.getText().clear();
                                        String token = (String) responseBody.get("token");
                                        getSharedPreferences("AUTH_TOKEN",0).edit().putString("TOKEN", token).commit();
                                        Intent intent =new Intent(getApplicationContext(), Home_navigation.class);
                                        intent.putExtra("IDENT",username);
                                        startActivity(intent);
                                        progressDialog.dismiss();
                                    }
                                    else{
                                        String error = (String)responseBody.get("error");
                                        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                                        alertDialog.setTitle("Error");
                                        alertDialog.setMessage(error);
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                        progressDialog.dismiss();

                                    }
                                }
                                catch (Exception e){
                                    progressDialog.dismiss();
                                    e.printStackTrace();

                                }
                            }

                        });

                    }
                }, 3000);



    }

    private void iniciar() {

        password = edtPassword.getText().toString();
        username = edtUsername.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Iniciando...");
        progressDialog.show();

        final DatabaseManagerUser databaseManager = new DatabaseManagerUser(getApplicationContext());

        edtUsername.getText().clear();
        edtPassword.getText().clear();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        if (databaseManager.comprobarRegistro(username)){
                            comprobar = databaseManager.getDb().rawQuery("SELECT correo, password FROM demo" + " WHERE correo='"+"' AND password='"+password+"'",null);
                            if(comprobar.moveToFirst()){
                                Intent intent =new Intent(getApplicationContext(), Home_navigation.class);
                                intent.putExtra("IDENT",username);
                                startActivity(intent);
                                finish();
                                progressDialog.dismiss();
                            }else{
                                edtUsername.setText(username);
                                progressDialog.dismiss();
                                String mesg = String.format("Password incorrecto", null);
                                Toast.makeText(getApplicationContext(),mesg, Toast.LENGTH_LONG).show();
                            }
                        }else{
                            progressDialog.dismiss();
                            String mesg = String.format("El E-mail que has introducido no coinciden con ninguna cuenta", null);
                            Toast.makeText(getApplicationContext(),mesg, Toast.LENGTH_LONG).show();
                        }
                    }
                }, 3000);

    }

    private boolean validar() {
        boolean valid = true;

        String user = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();

        if (user.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
            edtUsername.setError("Introduzca una dirección de correo electrónico válida");
            valid = false;
        } else {
            edtUsername.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            edtPassword.setError("Entre 4 y 10 caracteres alfanuméricos");
            valid = false;
        } else {
            edtPassword.setError(null);
        }

        return valid;
    }
}
