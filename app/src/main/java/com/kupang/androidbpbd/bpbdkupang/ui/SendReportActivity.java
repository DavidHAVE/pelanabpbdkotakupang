package com.kupang.androidbpbd.bpbdkupang.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.kupang.androidbpbd.bpbdkupang.helper.RetroClient;
import com.kupang.androidbpbd.bpbdkupang.constants.Constants;
import com.kupang.androidbpbd.bpbdkupang.helper.SharedPrefManager;
import com.kupang.androidbpbd.bpbdkupang.helper.VolleySingleton;
import com.kupang.androidbpbd.bpbdkupang.model.FileApi;
import com.kupang.androidbpbd.bpbdkupang.model.MyResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class SendReportActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int RC_PHOTO_PICKER = 100;
    public static Uri selectedImage;
    private TextView mAddressTextView;
    private Spinner mDisasterTypeSpinner;
    private EditText mInformationEditText;
    private ImageView mDisasterImageView;
    private Button mAddPhotoButton, mSendReportButton;
    private ProgressDialog loadingDialog;
    private String disaster, information;
    private String latitude, longitude, address;
    private int userId;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_report);

        mAddressTextView = (TextView) findViewById(R.id.address_text_view);
        mDisasterTypeSpinner = (Spinner) findViewById(R.id.disaster_type_spinner);
        mInformationEditText = (EditText) findViewById(R.id.information_edit_text);
        mAddPhotoButton = (Button) findViewById(R.id.add_photo_button);
        mDisasterImageView = (ImageView) findViewById(R.id.disaster_image_view);
        mSendReportButton = (Button) findViewById(R.id.send_report_button);

        ArrayAdapter activitySpinnerAdapter = ArrayAdapter.createFromResource(getBaseContext(),
                R.array.array_category_disaster, R.layout.support_simple_spinner_dropdown_item);
        activitySpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mDisasterTypeSpinner.setAdapter(activitySpinnerAdapter);
        mDisasterTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selection = (String) adapterView.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("Angin Kencang")) {
                        disaster = "Angin Kencang";
                    } else if (selection.equals("Banjir")) {
                        disaster = "Banjir";
                    } else if (selection.equals("Kebakaran")) {
                        disaster = "Kebakaran";
                    } else if (selection.equals("Tanah Longsor")) {
                        disaster = "Tanah Longsor";
                    } else if (selection.equals("Gempa Bumi")) {
                        disaster = "Gempa Bumi";
                    } else if (selection.equals("Tsunami")) {
                        disaster = "Tsunami";
                    } else if (selection.equals("Kekeringan")) {
                        disaster = "Kekeringan";
                    } else if (selection.equals("Gelombang Pasang / Abrasi")) {
                        disaster = "Gelombang Pasang / Abrasi";
                    } else if (selection.equals("Laka Kerja")) {
                        disaster = "Laka Kerja";
                    } else if (selection.equals("Laka Laut")) {
                        disaster = "Laka Laut";
                    } else if (selection.equals("Penemuan Mayat")) {
                        disaster = "Penemuan Mayat";
                    } else if (selection.equals("Pohon Tumbang")) {
                        disaster = "Pohon Tumbang";
                    } else {
                        // activity = 5;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                disaster = "-";
            }
        });

        mAddPhotoButton.setOnClickListener(this);
        mSendReportButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mAddPhotoButton) {
            selectImage();
        } else if (view == mSendReportButton) {

            latitude = SharedPrefManager.getInstance(this).getReport().getLatitude();
            longitude = SharedPrefManager.getInstance(this).getReport().getLongitude();
            address = SharedPrefManager.getInstance(this).getReport().getAddress();

            if (!latitude.isEmpty() && !longitude.isEmpty() && !address.isEmpty()) {
                loadingDialog = new ProgressDialog(this);
                loadingDialog.setCanceledOnTouchOutside(true);
                loadingDialog.setCancelable(true);
                loadingDialog.setMessage("Please Wait...");
                loadingDialog.show();

                if (selectedImage != null) {
                    uploadFile(selectedImage);
                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(this, "Foto belum tercantum", Toast.LENGTH_SHORT).show();
                }

                Log.e("SendReportActivity", "Finish");
            } else {
                Toast.makeText(this, "Ulangi lagi dalam penentuan lokasi", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAddressTextView.setText(MapReportActivity.ADDRESS);

        requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            //the image URI
            selectedImage = data.getData();

            Log.e("SendReportActivity", "photoData2 = " + selectedImage);

            try {
                //getting bitmap object from uri
                Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);


                if (photo != null) {
                    //displaying selected image to imageview
                    mDisasterImageView.setImageBitmap(photo);
                    mDisasterImageView.setVisibility(View.VISIBLE);
                    mAddPhotoButton.setText("UBAH GAMBAR");
                }

                //calling the method uploadBitmap to upload image
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            selectedImage = getImageUri(getApplicationContext(), photo);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(selectedImage));

            Log.e("SendReportActivity", "photoData = " + selectedImage);

            if (photo != null) {
                mDisasterImageView.setImageBitmap(Bitmap.createScaledBitmap(photo, 700, 700, false));
                mDisasterImageView.setVisibility(View.VISIBLE);
                mAddPhotoButton.setText("UBAH GAMBAR");
            }

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void selectImage() {


        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(SendReportActivity.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {


                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);


                } else if (options[item].equals("Choose from Gallery"))

                {

                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RC_PHOTO_PICKER);

                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    private void sendReportWithPic(final int laporanId) {

        Log.e("SendReportActivity", "SendReport");

        userId = SharedPrefManager.getInstance(this).getUser().getUserId();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone tz = TimeZone.getTimeZone("Asia/Jakarta");
        sdf.setTimeZone(tz);
        final String currentDate = sdf.format(new Date());

        information = mInformationEditText.getText().toString().trim();

        Log.e("SendReportActivity", "tanggal =" + currentDate + ", lat =" + MapReportActivity.LATITUDE + ", long =" + MapReportActivity.LONGITUDE + ", address =" + MapReportActivity.ADDRESS +
                ", disaster =" + disaster + ", information =" + information + ", userId =" + userId);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER_LAPORAN_WITH_PIC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("SendReportActivity", "responseReport : " + response);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
//                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                Toast.makeText(SendReportActivity.this, "Berhasil melapor", Toast.LENGTH_SHORT).show();

                                String laporanIdString = obj.getString("laporan_id");

                                loadingDialog.dismiss();

                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        loadingDialog.dismiss();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingDialog.dismiss();
                        Toast.makeText(SendReportActivity.this, "Gagal melapor", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("laporan_id", String.valueOf(laporanId));
                params.put("tanggal", currentDate);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("alamat", address);
                params.put("jenis_kejadian", disaster);
                params.put("informasi", information);
                params.put("pengguna_id", String.valueOf(userId));

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void uploadFile(Uri uri) {

        FileApi service = RetroClient.getApiService();

        File file = new File(getRealPathFromURI(uri));

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

        Call<MyResponse> resultCall = service.uploadImage(body);

        resultCall.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, retrofit2.Response<MyResponse> response) {
                if (!response.body().error) {
                    Log.e("SendReportActivity", "responseMessage :" + response.body().message + ", laporan_id :" + response.body().laporan_id);
                    sendReportWithPic(response.body().laporan_id);
                } else {
                    loadingDialog.dismiss();
                    Log.e("SendReportActivity", "errorResponse =" + response.body().message);
                    Toast.makeText(SendReportActivity.this, "Gagal melapor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                loadingDialog.dismiss();
                Toast.makeText(SendReportActivity.this, "Gagal melapor", Toast.LENGTH_SHORT).show();
                Log.e("SendReportActivity", "errorResponse =" + t.getMessage());
            }
        });
    }

    /*
     * This method is fetching the absolute path of the image file
     * if you want to upload other kind of files like .pdf, .docx
     * you need to make changes on this method only
     * Rest part will be the same
     * */
    private String getRealPathFromURI(Uri contentUri) {

        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}
