package www.wielabs.com.arc.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import www.wielabs.com.arc.R;
import www.wielabs.com.arc.activity.CallScreenActivity;
import www.wielabs.com.arc.activity.MainActivity;

import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;


public class Dial extends Fragment {

    String no;

    EditText number;
    Button call;

//    ViewHolder viewHolder;

    public Dial() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dial, container, false);

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        call = (Button) view.findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            callnumber();

            }
        });

        return view;
    }

    private void callnumber() {


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //request permission from user if the app hasn't got the required permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                    10);
            return;
        } else {     //have got permission
            try {
                try {

                    number = (EditText) getActivity().findViewById(R.id.etmobile);

                    no = number.getText().toString();


                    Intent callintent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + no));
                    startActivity(callintent);

                } catch (Exception e) {
                }  //call activity and make phone call
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
            }
        }

    }



}
