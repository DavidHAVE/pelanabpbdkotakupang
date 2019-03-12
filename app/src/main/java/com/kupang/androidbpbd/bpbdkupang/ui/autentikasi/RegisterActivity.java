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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kupang.androidbpbd.bpbdkupang.R;
import com.kupang.androidbpbd.bpbdkupang.constants.Constants;
import com.kupang.androidbpbd.bpbdkupang.helper.VolleySingleton;
import com.kupang.androidbpbd.bpbdkupang.ui.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mNameEditText, mEmailEditText, mTelephoneNumberEditText;
    private Button mRegisterAccountButton, mLoginButton;

    private ProgressDialog loadingDialog;

    private String name, telephoneNumber, email;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNameEditText = (EditText) findViewById(R.id.name_edit_text);
        mEmailEditText = (EditText) findViewById(R.id.email_edit_text);
        mTelephoneNumberEditText = (EditText) findViewById(R.id.telephone_number_edit_text);
        mRegisterAccountButton = (Button) findViewById(R.id.register_account_button);
        mLoginButton = (Button) findViewById(R.id.login_button);

        mRegisterAccountButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mRegisterAccountButton) {
            loadingDialog = new ProgressDialog(this);
            loadingDialog.setCanceledOnTouchOutside(true);
            loadingDialog.setCancelable(true);
            loadingDialog.setMessage("Please Wait...");
            loadingDialog.show();

            register();
        } else if (view == mLoginButton) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestQueue = Volley.newRequestQueue(this);
    }


    private void register() {

        name = mNameEditText.getText().toString().trim();
        telephoneNumber = mTelephoneNumberEditText.getText().toString().trim();
        email = mEmailEditText.getText().toString().trim();

        //validating inputs
        if (TextUtils.isEmpty(name)) {
            mNameEditText.setError("Please enter your name");
            mNameEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(telephoneNumber)) {
            mTelephoneNumberEditText.setError("Please enter your telephone number");
            mTelephoneNumberEditText.requestFocus();
            return;
        }

        if (!isValidPhoneNumber(telephoneNumber)){
            mTelephoneNumberEditText.setError("Telephone number not identified");
            mTelephoneNumberEditText.requestFocus();
            return;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER_PENGGUNA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressBar.setVisibility(View.GONE);
                        Log.e("RegisterActivity", "responseRegister : " + response);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                loadingDialog.dismiss();
                                return;
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("nama", name);
                params.put("nomor_telepon", telephoneNumber);
                params.put("email", email);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private static boolean isValidPhoneNumber(String phoneNumber) {
        return !TextUtils.isEmpty(phoneNumber) && android.util.Patterns.PHONE.matcher(phoneNumber).matches();
    }
}
