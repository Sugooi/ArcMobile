package www.wielabs.com.arc.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import www.wielabs.com.arc.R;

import static www.wielabs.com.arc.activity.Registration.username;


public class MainActivity extends AppCompatActivity {

    ImageView mvideo, mcall, mmessage, mhelp, mapp, mrate;


    TextView textview;
    NodeList nodelist;
    ProgressDialog pDialog;
    String URL = "#";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textview = (TextView) findViewById(R.id.balance);
        // Execute DownloadXML AsyncTask
        new DownloadXML().execute(URL);

        SharedPreferences preferences = getSharedPreferences("default", MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putBoolean("isLoggedIn",true);
        editor.putString("uname",username);
        editor.commit();


        mvideo = (ImageView) findViewById(R.id.mvideo);
        mcall = (ImageView) findViewById(R.id.mcall);
        mmessage = (ImageView) findViewById(R.id.mmessage);
        mhelp = (ImageView) findViewById(R.id.mhelp);
        mapp = (ImageView) findViewById(R.id.mapp);
        mrate = (ImageView) findViewById(R.id.mrates);

        mvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        mrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Rates.class);
                startActivity(intent);
            }
        });

        mhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Help.class);
                startActivity(intent);
            }
        });

        mcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Calls.class);
                startActivity(intent);
            }
        });
        mmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Message.class);
                startActivity(intent);
            }
        });

        mapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AppToApp.class);
                startActivity(intent);
            }
        });
    }

    private class DownloadXML extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressbar
            pDialog = new ProgressDialog(MainActivity.this);
            // Set progressbar title
            pDialog.setTitle("Android Simple XML Parsing using DOM Tutorial");
            // Set progressbar message
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            // Show progressbar
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... Url) {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                // Download the XML file

                URL url_1 = new URL("http://148.251.140.183/billing/api/user_balance_get?u=admin&username=arceurop&hash=9d1d83cd9660231d0ae903efd6be8db1996e54e4");
                HttpURLConnection urlConnection = (HttpURLConnection) url_1.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    StringBuilder total = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        total.append(line).append('\n');
                    }

                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    InputSource is = new InputSource(new StringReader(total.toString()));

                    Document doc = db.parse(is);
                    doc.getDocumentElement().normalize();
                    // Locate the Tag Name
                    nodelist = doc.getElementsByTagName("page");
                } catch (Exception e) {

                }


            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void args) {
            int temp = 0;

            Node nNode = nodelist.item(temp);
            Element eElement = (Element) nNode;

            textview.setText(textview.getText()
                    + getNode("balance", eElement));
            // Close progressbar
            pDialog.dismiss();
        }
    }

    private static String getNode(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
                .getChildNodes();
        Node nValue = (Node) nlList.item(0);
        return nValue.getNodeValue();
    }
}