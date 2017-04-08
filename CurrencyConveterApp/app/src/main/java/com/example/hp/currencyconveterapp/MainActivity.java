package com.example.hp.currencyconveterapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.apache.http.protocol.HTTP.UTF_8;

public class MainActivity extends AppCompatActivity {

    //URL to get JSON Array
    private static String url1 = "http://www.floatrates.com/daily/usd.json";
    private static String url2 = "http://www.floatrates.com/daily/eur.json";
    private static String url3 = "http://www.floatrates.com/daily/jpy.json";
    private static String url4 = "http://www.floatrates.com/daily/gbp.json";
    private String TAG = MainActivity.class.getSimpleName();
    String sourcechange;
    TextView txt_usdvnd;
    TextView txt_eurvnd;
    TextView txt_jpyvnd;
    TextView txt_gbpvnd;
    Spinner currencyspin;
    EditText txt_source;
    TextView txt_destination;
    Button buttonchange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Set variable value
        txt_usdvnd = (TextView) findViewById(R.id.textView_usdvnd);
        txt_eurvnd = (TextView) findViewById(R.id.textView_eurvnd);
        txt_jpyvnd = (TextView) findViewById(R.id.textView_jpyvnd);
        txt_gbpvnd = (TextView) findViewById(R.id.textView_gbpvnd);
        currencyspin = (Spinner) findViewById(R.id.spinner_curr);
        txt_source = (EditText) findViewById(R.id.editText_source);
        txt_destination = (TextView) findViewById(R.id.textView_destination);
        buttonchange = (Button) findViewById(R.id.button_con);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute(url1, url2, url3, url4);
            }
        });
    }

    public void convert(View view){
        String source = txt_source.getText().toString();
        Integer sourceint = Integer.parseInt(source);
        sourcechange = currencyspin.getSelectedItem().toString();
        Integer rate = 0, result = 0;
        if (!source.isEmpty()){
            switch (sourcechange){
                case "USD":
                    String haha = txt_usdvnd.getText().toString();
//                    rate = Integer.parseInt(txt_usdvnd.getText().toString());
                    txt_destination.setText(haha);
                    break;
                case "EUR":
                    txt_source.setText(sourcechange);
                    break;
                case "JPY":
                    txt_source.setText(sourcechange);
                    break;
                case "GBP":
                    txt_source.setText(sourcechange);
                    break;
                default:
                    break;
            }
        }else{
            Toast.makeText(MainActivity.this, "You did not enter the source", Toast.LENGTH_LONG).show();
        }
    }

    private void proccessValue(String[] s){
        try{
            String jo0 = s[0], jo1 = s[1], jo2 = s[2], jo3 = s[3];
            JSONObject obj1 = new JSONObject(jo0);
            JSONObject vnd1 = obj1.getJSONObject("vnd");
            String rate1 = vnd1.getString("rate");
            txt_usdvnd.setText(rate1);

            JSONObject obj2 = new JSONObject(jo1);
            JSONObject vnd2 = obj2.getJSONObject("vnd");
            String rate2 = vnd2.getString("rate");
            txt_eurvnd.setText(rate2);

            JSONObject obj3 = new JSONObject(jo2);
            JSONObject vnd3 = obj3.getJSONObject("vnd");
            String rate3 = vnd3.getString("rate");
            txt_jpyvnd.setText(rate3);

            JSONObject obj4 = new JSONObject(jo3);
            JSONObject vnd4 = obj4.getJSONObject("vnd");
            String rate4 = vnd4.getString("rate");
            txt_gbpvnd.setText(rate4);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class ReadJSON extends AsyncTask<String, Integer, String[]>{

        @Override
        protected String[] doInBackground(String... param) {
            String[] chuoi = new String[100];
            chuoi[0] = getXMLfromUrl(param[0].toString());
            chuoi[1] = getXMLfromUrl(param[1].toString());
            chuoi[2] = getXMLfromUrl(param[2].toString());
            chuoi[3] = getXMLfromUrl(param[3].toString());
            return chuoi;
        }

        @Override
        protected void onPostExecute(String[] s) {
            proccessValue(s);
        }
    }

    private String getXMLfromUrl(String urlString){
        String xml = null;
        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(urlString);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity, UTF_8);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }catch (ClientProtocolException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return xml;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
