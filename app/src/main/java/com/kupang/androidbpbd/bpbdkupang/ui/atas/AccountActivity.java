package com.kupang.androidbpbd.bpbdkupang.ui.atas;

import android.app.ProgressDialog;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kupang.androidbpbd.bpbdkupang.R;
import com.kupang.androidbpbd.bpbdkupang.constants.Constants;
import com.kupang.androidbpbd.bpbdkupang.helper.SharedPrefManager;
import com.kupang.androidbpbd.bpbdkupang.helper.VolleySingleton;
import com.kupang.androidbpbd.bpbdkupang.model.User;
import com.kupang.androidbpbd.bpbdkupang.model.Warning;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mNameEditText, mEmailEditText;
    private TextView mTelephoneTextView;
    private Button mChangeAccountButton, mSignoutButton;

    private ProgressDialog loadingDialog;

    private String name, telephoneNumber, email;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mNameEditText = (EditText) findViewById(R.id.name_edit_text);
        mEmailEditText = (EditText) findViewById(R.id.email_edit_text);
        mTelephoneTextView = (TextView) findViewById(R.id.telephone_text_view);
        mChangeAccountButton = (Button) findViewById(R.id.change_account_button);
        mSignoutButton = (Button) findViewById(R.id.signout_button);

        mChangeAccountButton.setOnClickListener(this);
        mSignoutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mChangeAccountButton) {
            loadingDialog = new ProgressDialog(this);
            loadingDialog.setCanceledOnTouchOutside(true);
            loadingDialog.setCancelable(true);
            loadingDialog.setMessage("Please Wait...");
            loadingDialog.show();

            updateAccount();
        } else if (view == mSignoutButton) {
            SharedPrefManager.getInstance(this).logout();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setCanceledOnTouchOutside(true);
        loadingDialog.setCancelable(true);
        loadingDialog.setMessage("Please Wait...");
        loadingDialog.show();
        userRead();

        mNameEditText.setText(name);
        mTelephoneTextView.setText(telephoneNumber);
        mEmailEditText.setText(email);
    }


    private void userRead() {

        userId = SharedPrefManager.getInstance(this).getUser().getUserId();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_READ_PENGGUNA + userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("OrderDetailActivity", "responseReadUser : " + response);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
//                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject jsonUser = obj.getJSONObject("pengguna");

                                name = jsonUser.getString(Constants.TAG_USER_NAME);
                                telephoneNumber = jsonUser.getString(Constants.TAG_USER_TELEPHONE_NUMBER);
                                email = jsonUser.getString(Constants.TAG_USER_EMAIL);

                                mNameEditText.setText(name);
                                mTelephoneTextView.setText(telephoneNumber);
                                mEmailEditText.setText(email);

                                User user = new User(name, telephoneNumber, email);
                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).updateUser(user);

                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                        loadingDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    private void updateAccount() {


        final int userId = SharedPrefManager.getInstance(this).getUser().getUserId();

        name = mNameEditText.getText().toString().trim();
        email = mEmailEditText.getText().toString().trim();

        //validating inputs
        if (TextUtils.isEmpty(name)) {
            mNameEditText.setError("Please enter your name");
            mNameEditText.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_UPDATE_PENGGUNA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("AccountActivity", "responseUpdate : " + response);

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
                        loadingDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("pengguna_id", String.valueOf(userId));
                params.put("nama", name);
                params.put("email", email);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
