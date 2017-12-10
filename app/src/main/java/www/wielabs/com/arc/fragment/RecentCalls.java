package www.wielabs.com.arc.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import www.wielabs.com.arc.R;
import www.wielabs.com.arc.activity.CallScreenActivity;
import www.wielabs.com.arc.activity.MainActivity;
import www.wielabs.com.arc.adapter.RecentCallAdapter;
import www.wielabs.com.arc.others.RecentCallModel;

import static android.content.ContentValues.TAG;
import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;



public class RecentCalls extends Fragment {

    // Declare variables
    TextView num, tim, date;
    ListView listView;
    NodeList nodelist;
    ProgressDialog pDialog;
    // Insert image URL
    String URL = "http://arcmobile.esy.es/ARC_Android_API/callHistory.xml";
    Dialog MyDialog;
    ArrayList<RecentCallModel> recentcall_list;


    View view;
    public RecentCalls() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recent_calls, container, false);

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        new DownloadXML().execute(URL);


        return view;
    }

    // DownloadXML AsyncTask
    private class DownloadXML extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressbar
            pDialog = new ProgressDialog(getActivity());
            // Set progressbar title
            pDialog.setTitle("Recent Call History");
            // Set progressbar message
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            // Show progressbar
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... Url) {
            try {
                java.net.URL url = new URL(Url[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                // Download the XML file
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                // Locate the Tag Name
                nodelist = doc.getElementsByTagName("call");

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void args) {
           recentcall_list = new ArrayList<RecentCallModel>();

            for (int temp = 0; temp < nodelist.getLength(); temp++) {
                Node nNode = nodelist.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    recentcall_list.add(new RecentCallModel(""+getNode("dst",eElement),""+getNode("calldate2",eElement),""+getNode("nice_billsec",eElement)));
                    RecentCallAdapter itemAdapter  = new RecentCallAdapter(getActivity(),recentcall_list);
                   listView = (ListView) view.findViewById(R.id.list);
                    listView.setAdapter(itemAdapter);

                }
            }
            // Close progressbar
            pDialog.dismiss();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                   RecentCallModel recentCallModel = recentcall_list.get(i);


                    String number=recentCallModel.getMobile_number();



                    MyCustomAlertDialog(number);



                }
            });


        }
    }

    public void MyCustomAlertDialog(String detail_number){
        MyDialog = new Dialog(getActivity());
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.contact_detail);
        MyDialog.setTitle("My Custom Dialog");

        TextView number = (TextView) MyDialog.findViewById(R.id.detail_number);
        number.setText(detail_number);

        ImageView imageView = (ImageView) MyDialog.findViewById(R.id.call_detail);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callnumber(detail_number);
            }
        });

        Button close_button = (Button) MyDialog.findViewById(R.id.close);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            MyDialog.cancel();
            }
        });


        MyDialog.show();
    }

    // getNode function
    private static String getNode(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
                .getChildNodes();
        Node nValue = (Node) nlList.item(0);
        return nValue.getNodeValue();
    }


    private void callnumber(String number_detail) {


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //request permission from user if the app hasn't got the required permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                    10);

        } else {     //have got permission
            try {
                try {




                    Intent callintent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number_detail));
                    startActivity(callintent);

                } catch (Exception e) {
                }  //call activity and make phone call
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
            }
        }

    }



    }
