package com.creatpixel.userdisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Queue;

public class MainActivity extends AppCompatActivity {
    RequestQueue myQueue;
    ImageView profilePic;
    TextView userNameText;
    Button randomButton;

    TextView phoneNumberText;
    TextView emailText;
    TextView cityText;
    TextView addressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profilePic = findViewById(R.id.profilePic);
        userNameText = findViewById(R.id.nameText);
        randomButton = findViewById(R.id.randomButton);
        phoneNumberText = findViewById(R.id.myPhoneNumberText);
        emailText = findViewById(R.id.myEmailText);
        cityText = findViewById(R.id.myCityText);
        addressText = findViewById(R.id.myAddressText);

        myQueue = Volley.newRequestQueue(this);

        String url = "https://randomuser.me/api/";
        final JsonObjectRequest jsonobjectR = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray resultsJsonArray = response.getJSONArray("results");

                            JSONObject jsonObject = resultsJsonArray.getJSONObject(0);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("picture");
                            JSONObject jsonObject2 = jsonObject.getJSONObject("name");
                            JSONObject jsonObject3 = jsonObject.getJSONObject("location");

                            //Get user full name from data
                            String titleOfName = jsonObject2.getString("title");
                            String firstName = jsonObject2.getString("first");
                            String lastName = jsonObject2.getString("last");
                            String fullName = titleOfName +"."+ firstName +" "+lastName;

                            userNameText.setText(fullName);

                            //Get user profile from data
                            String urlString = jsonObject1.getString("large");
                            Picasso.get().load(urlString).into(profilePic);

                            //Get phone number
                            phoneNumberText.setText(jsonObject.getString("phone"));

                            //Get email
                            emailText.setText(jsonObject.getString("email"));

                            //Get city
                            cityText.setText(jsonObject3.getString("city"));

                            //Get address
                            JSONObject streetObject = jsonObject3.getJSONObject("street");
                            String number = streetObject.getString("number");
                            String name = streetObject.getString("name");
                            String addressStr = number +" "+ name;
                            addressText.setText(addressStr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("akkuLog", "Error baby");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("akkuLog", "Error is " + error.getMessage());
            }
        });

        //RandomButton
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myQueue.add(jsonobjectR);
            }
        });
    }
}
