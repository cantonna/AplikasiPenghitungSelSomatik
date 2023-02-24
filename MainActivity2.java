package com.example.somaticcellcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    TextView idTV0, idTV1, idTV2, idTV3, idTV4;
    ImageView IVPreviewImageRes0, IVPreviewImageRes1, IVPreviewImageRes2, IVPreviewImageRes3, IVPreviewImageRes4;
    TextView timeTV0, timeTV1, timeTV2, timeTV3, timeTV4;
    TextView statusTV0, statusTV1, statusTV2, statusTV3, statusTV4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        idTV0 = findViewById(R.id.idTV0);
        idTV1 = findViewById(R.id.idTV1);
        idTV2 = findViewById(R.id.idTV2);
        idTV3 = findViewById(R.id.idTV3);
        idTV4 = findViewById(R.id.idTV4);

        IVPreviewImageRes0 = findViewById(R.id.IVPreviewImageRes0);
        IVPreviewImageRes1 = findViewById(R.id.IVPreviewImageRes1);
        IVPreviewImageRes2 = findViewById(R.id.IVPreviewImageRes2);
        IVPreviewImageRes3 = findViewById(R.id.IVPreviewImageRes3);
        IVPreviewImageRes4 = findViewById(R.id.IVPreviewImageRes4);

        timeTV0 = findViewById(R.id.timeTV0);
        timeTV1 = findViewById(R.id.timeTV1);
        timeTV2 = findViewById(R.id.timeTV2);
        timeTV3 = findViewById(R.id.timeTV3);
        timeTV4 = findViewById(R.id.timeTV4);

        statusTV0 = findViewById(R.id.statusTV0);
        statusTV1 = findViewById(R.id.statusTV1);
        statusTV2 = findViewById(R.id.statusTV2);
        statusTV3 = findViewById(R.id.statusTV3);
        statusTV4 = findViewById(R.id.statusTV4);
        getDetails("asd");
    }

    // buat kirim dan dapat dari server
    private void getDetails(String Data) {

        // url to post our data
        String url = "http://192.168.43.85:5000/view";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);

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
                    if (jsonObject.getString("temp_id_0") == null) {
                        // displaying a toast message if we get error
                        Toast.makeText(MainActivity2.this, "Error", Toast.LENGTH_SHORT).show();
                    } else {
                        // on below line we are get data history
                        // if we get the data then we are setting it in our text views and img in below line.
                        String temp_id_0 = jsonObject.getString("temp_id_0");
                        idTV0.setText(temp_id_0);
                        String temp_id_1 = jsonObject.getString("temp_id_1");
                        idTV1.setText(temp_id_1);
                        String temp_id_2 = jsonObject.getString("temp_id_2");
                        idTV2.setText(temp_id_2);
                        String temp_id_3 = jsonObject.getString("temp_id_3");
                        idTV3.setText(temp_id_3);
                        String temp_id_4 = jsonObject.getString("temp_id_4");
                        idTV4.setText(temp_id_4);

                        String temp_time_0 = jsonObject.getString("temp_time_0");
                        timeTV0.setText(temp_time_0);
                        String temp_time_1 = jsonObject.getString("temp_time_1");
                        timeTV1.setText(temp_time_1);
                        String temp_time_2 = jsonObject.getString("temp_time_2");
                        timeTV2.setText(temp_time_2);
                        String temp_time_3 = jsonObject.getString("temp_time_3");
                        timeTV3.setText(temp_time_3);
                        String temp_time_4 = jsonObject.getString("temp_time_4");
                        timeTV4.setText(temp_time_4);

                        String temp_data_0 = jsonObject.getString("temp_data_0");
                        byte[] byteArray0 = Base64.decode(temp_data_0, Base64.DEFAULT);
                        Bitmap decodedImage0 = BitmapFactory.decodeByteArray(byteArray0, 0, byteArray0.length);
                        IVPreviewImageRes0.setImageBitmap(decodedImage0);

                        String temp_data_1 = jsonObject.getString("temp_data_1");
                        byte[] byteArray1 = Base64.decode(temp_data_1, Base64.DEFAULT);
                        Bitmap decodedImage1 = BitmapFactory.decodeByteArray(byteArray1, 0, byteArray1.length);
                        IVPreviewImageRes1.setImageBitmap(decodedImage1);

                        String temp_data_2 = jsonObject.getString("temp_data_2");
                        byte[] byteArray2 = Base64.decode(temp_data_2, Base64.DEFAULT);
                        Bitmap decodedImage2 = BitmapFactory.decodeByteArray(byteArray2, 0, byteArray2.length);
                        IVPreviewImageRes2.setImageBitmap(decodedImage2);

                        String temp_data_3 = jsonObject.getString("temp_data_3");
                        byte[] byteArray3 = Base64.decode(temp_data_3, Base64.DEFAULT);
                        Bitmap decodedImage3 = BitmapFactory.decodeByteArray(byteArray3, 0, byteArray3.length);
                        IVPreviewImageRes3.setImageBitmap(decodedImage3);

                        String temp_data_4 = jsonObject.getString("temp_data_4");
                        byte[] byteArray4 = Base64.decode(temp_data_4, Base64.DEFAULT);
                        Bitmap decodedImage4 = BitmapFactory.decodeByteArray(byteArray4, 0, byteArray4.length);
                        IVPreviewImageRes4.setImageBitmap(decodedImage4);

                        String temp_status_0 = jsonObject.getString("temp_status_0");
                        statusTV0.setText(temp_status_0);
                        String temp_status_1 = jsonObject.getString("temp_status_1");
                        statusTV1.setText(temp_status_1);
                        String temp_status_2 = jsonObject.getString("temp_status_2");
                        statusTV2.setText(temp_status_2);
                        String temp_status_3 = jsonObject.getString("temp_status_3");
                        statusTV3.setText(temp_status_3);
                        String temp_status_4 = jsonObject.getString("temp_status_4");
                        statusTV4.setText(temp_status_4);
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
                Toast.makeText(MainActivity2.this, "Fail to get data" + error, Toast.LENGTH_SHORT).show();
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