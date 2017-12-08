package www.wielabs.com.arc.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

import www.wielabs.com.arc.R;


public class Registration extends AppCompatActivity {

    NodeList nodelist;
    Button sign_in;

    String username, firstname, lastname, password1, password2, country_id, email;
    ProgressDialog pDialog;

    EditText username_view, firstname_view, lastname_view, password1_view, password2_view, country_id_view, email_view;

    String url = "http://148.251.140.183/billing/api/user_register.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

//        Spinner spin = (Spinner) findViewById(R.id.simpleSpinner);
//        spin.setOnItemSelectedListener(this);
//
//        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),flags,countryNames);
//        spin.setAdapter(customAdapter);


        username_view = (EditText) findViewById(R.id.reg_username);
        firstname_view = (EditText) findViewById(R.id.reg_first);
        lastname_view = (EditText) findViewById(R.id.reg_last);
        password1_view = (EditText) findViewById(R.id.reg_pass1);
        password2_view = (EditText) findViewById(R.id.reg_pass2);
        email_view = (EditText) findViewById(R.id.reg_email);
        sign_in = (Button) findViewById(R.id.btn_signup);

        //  country_id_view= (EditText) findViewById(R.id.country_code)



        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtoserver();
            }
        });


    }

    private void addtoserver() {

        username = username_view.getText().toString();
        firstname = firstname_view.getText().toString();
        lastname = lastname_view.getText().toString();
        password1 = password1_view.getText().toString();
        password2 = password2_view.getText().toString();
        email = email_view.getText().toString();
        country_id = "10";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Registration.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Registration.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", "iae853cc58");
                params.put("username", username);
                params.put("password", password1);
                params.put("password2", password2);
                params.put("first_name", firstname);
                params.put("last_name", lastname);
                params.put("country_id", country_id);
                params.put("email", email);
                params.put("device_type", "SIP");
                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }



}