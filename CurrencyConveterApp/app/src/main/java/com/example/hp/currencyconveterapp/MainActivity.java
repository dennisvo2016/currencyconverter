package com.example.hp.currencyconveterapp;

import android.content.SharedPreferences;
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
import java.math.RoundingMode;
import java.text.DecimalFormat;

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
    String prefname = "my_data";

    public boolean isConnected() throws InterruptedException, IOException
    {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec (command).waitFor() == 0);
    }

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
        try {
            if (isConnected()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new ReadJSON().execute(url1, url2, url3, url4);
                    }
                });
                onPause();
            } else {
                onResume();
//                Toast.makeText(MainActivity.this, "Please connect to your internet first!", Toast.LENGTH_LONG).show();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    protected void onPause(){
        super.onPause();
        savingref();
    }

    protected void onResume(){
        super.onResume();
        restoringref();
    }

    public void savingref(){
        SharedPreferences pre = getSharedPreferences(prefname, MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        String usd = txt_usdvnd.getText().toString(), eur = txt_eurvnd.getText().toString(),
                jpy = txt_jpyvnd.getText().toString(), gbp = txt_gbpvnd.getText().toString();
        editor.clear();
        editor.putString("usd", usd);
        editor.putString("eur", eur);
        editor.putString("jpy", jpy);
        editor.putString("gbp", gbp);
        editor.commit();
    }

    public void restoringref(){
        SharedPreferences pre = getSharedPreferences(prefname, MODE_PRIVATE);
        String usd = pre.getString("usd","0000"), eur = pre.getString("eur","0000"),
                jpy = pre.getString("jpy","0000"), gbp = pre.getString("gbp","0000");
        txt_usdvnd.setText(usd);
        txt_eurvnd.setText(eur);
        txt_jpyvnd.setText(jpy);
        txt_gbpvnd.setText(gbp);
    }

    public void convert(View view){
        String source = txt_source.getText().toString();
        sourcechange = currencyspin.getSelectedItem().toString();
        Double rate = 0.0, result = 0.0, sourcenum = 0.0;
        if (!source.isEmpty()) {
            try {
                sourcenum = Double.parseDouble(source);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            switch (sourcechange) {
                case "USD":
                    try {
                        rate = Double.parseDouble(txt_usdvnd.getText().toString());
                        result = (rate * sourcenum);
                        DecimalFormat df = new DecimalFormat("#.####");
                        df.setRoundingMode(RoundingMode.CEILING);
                        txt_destination.setText(df.format(result));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    break;
                case "EUR":
                    try {
                        rate = Double.parseDouble(txt_eurvnd.getText().toString());
                        result = (rate * sourcenum);
                        DecimalFormat df = new DecimalFormat("#.####");
                        df.setRoundingMode(RoundingMode.CEILING);
                        txt_destination.setText(df.format(result));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    break;
                case "JPY":
                    try {
                        rate = Double.parseDouble(txt_jpyvnd.getText().toString());
                        result = (rate * sourcenum);
                        DecimalFormat df = new DecimalFormat("#.####");
                        df.setRoundingMode(RoundingMode.CEILING);
                        txt_destination.setText(df.format(result));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    break;
                case "GBP":
                    try {
                        rate = Double.parseDouble(txt_gbpvnd.getText().toString());
                        result = (rate * sourcenum);
                        DecimalFormat df = new DecimalFormat("#.####");
                        df.setRoundingMode(RoundingMode.CEILING);
                        txt_destination.setText(df.format(result));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            Toast.makeText(MainActivity.this, "DONE", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(MainActivity.this, "You have to fill in source number first!", Toast.LENGTH_LONG).show();
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
