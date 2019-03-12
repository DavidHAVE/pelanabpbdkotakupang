package com.kupang.androidbpbd.bpbdkupang.ui.autentikasi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kupang.androidbpbd.bpbdkupang.R;
import com.kupang.androidbpbd.bpbdkupang.constants.Constants;
import com.kupang.androidbpbd.bpbdkupang.helper.SharedPrefManager;
import com.kupang.androidbpbd.bpbdkupang.helper.VolleySingleton;
import com.kupang.androidbpbd.bpbdkupang.model.User;
import com.kupang.androidbpbd.bpbdkupang.ui.MainActivity;
import com.kupang.androidbpbd.bpbdkupang.ui.SplashScreenActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mUsernameEditText, mPasswordEditText;
    private Button mLoginButton, mSignupButton;

    private ProgressDialog loadingDialog;

    private String responseLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        mUsernameEditText = (EditText) findViewById(R.id.username_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.password_edit_text);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mSignupButton = (Button) findViewById(R.id.signup_button);

        mLoginButton.setOnClickListener(this);
        mSignupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mLoginButton) {
            userLogin();
        } else if (view == mSignupButton) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void userLogin() {
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setCanceledOnTouchOutside(true);
        loadingDialog.setCancelable(true);
        loadingDialog.setMessage("Please Wait...");
        loadingDialog.show();

        //first getting the values
        final String username = mUsernameEditText.getText().toString().trim();
        final String password = mPasswordEditText.getText().toString().trim();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            mUsernameEditText.setError("Please enter your username");
            mUsernameEditText.requestFocus();
            loadingDialog.dismiss();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordEditText.setError("Please enter your password");
            mPasswordEditText.requestFocus();
            loadingDialog.dismiss();
            return;
        }

        Log.e("LoginActivity", "username : " + username + ", password : " + password);
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("LoginActivity", "responseLogin : " + response);
                        responseLogin = response;

                        if (responseLogin.equals("[]") || responseLogin.isEmpty()) {
                            loadingDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Login gagal", Toast.LENGTH_SHORT).show();
                        }

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
//                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("pengguna");

                                //creating a new user object
                                User user = new User(
                                        userJson.getInt("pengguna_id"),
                                        userJson.getString("nama"),
                                        userJson.getString("nomor_telepon"),
                                        userJson.getString("email")
                                );

                                if (TextUtils.isEmpty(user.getName())) {
                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    Log.e("LoginActivity", "Kosong");
                                    viewEmpty(obj.getString("message"));
                                }


                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                Log.e("LoginActivity", "Response");


                                //starting the profile activity
                                finish();
                                startActivity(new Intent(LoginActivity.this, SplashScreenActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                viewEmpty(obj.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loadingDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", username);
                params.put("nomor_telepon", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void viewEmpty(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.e("LoginActivity", "viewEmpty");
    }
}
