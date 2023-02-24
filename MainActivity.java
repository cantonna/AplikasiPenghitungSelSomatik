package com.example.somaticcellcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // One Button
    Button BSelectImage;
    Button BCalc;
    Button BSelectHisto;
    // Preview Image IVPreviewImage buat dari galeri, satunya dari proses api
    ImageView IVPreviewImage, IVPreviewImageRes;
    // nampung banyaknya bulatan misal 20, 1 ,3
    private TextView DescTV;
    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;
    String SELECTED_IMAGE;
    //ketika android dijalankan maka oncreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // register the UI widgets with their appropriate IDs sesuai xml (activity_main.xl)
        BSelectImage = findViewById(R.id.BSelectImage);
        BCalc = findViewById(R.id.BCalc);
        BSelectHisto = findViewById(R.id.BSelectHisto);

        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        IVPreviewImageRes = findViewById(R.id.IVPreviewImageRes);
        DescTV = findViewById(R.id.idTV);
        // handle the Choose Image button to trigger
        // the image chooser function
        BSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        BCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });

        // adding click listener for our button
        BSelectHisto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent class will help to go to next activity using
                // it's object named intent.
                // SecondActivty is the name of new created EmptyActivity.
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }

    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    IVPreviewImage.setImageURI(selectedImageUri);
                    //kalau udah dapet gambarnya di convert ke base 64
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    byte[] byteArray = outputStream.toByteArray();

                    //Use your Base64 String as you wish
                    String encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    // hasil base64 kirim ke fungsi getDetails
                    SELECTED_IMAGE = encodedString;
                }
            }
        }
    }

    private void sendData(){
        // on below line we are checking if the response is null or not.
        if (SELECTED_IMAGE== null) {
            // displaying a toast message if we get error
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
        } else {
            getDetails(SELECTED_IMAGE);

        }
    }
    // buat kirim dan dapat dari server
    private void getDetails(String Data) {

        // url to post our data
        String url = "http://192.168.43.85:5000/";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // on below line passing our response to json object.
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are checking if the response is null or not.
                    if (jsonObject.getString("many_blob") == null) {
                        // displaying a toast message if we get error
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    } else {
                        // disini dapetin gambar dan hasil banyak titik
                        // if we get the data then we are setting it in our text views and img in below line.
                        String res_img = jsonObject.getString("res_img");
                        String many_blob = jsonObject.getString("many_blob");
                        DescTV.setText(many_blob);

                        byte[] byteArray = Base64.decode(res_img, Base64.DEFAULT);
                        Bitmap decodedImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                        IVPreviewImageRes.setImageBitmap(decodedImage);
                    }
                    // on below line we are displaying
                    // a success toast message.
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(MainActivity.this, "Fail to get data" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                // as we are passing data in the form of url encoded
                // so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                // below line we are creating a map for storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key and value pair to our parameters.
                params.put("Data", Data);

                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}