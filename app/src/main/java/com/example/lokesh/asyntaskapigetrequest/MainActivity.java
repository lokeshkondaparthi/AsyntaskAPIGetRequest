package com.example.lokesh.asyntaskapigetrequest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GetProductPage().execute();

    }

    class GetProductPage extends AsyncTask<Void, Void, Void> {
        StringBuffer stringBuffer = new StringBuffer("");
        BufferedReader bufferedReader = null;

        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet();
            try {
                URI uri = new URI(StringConstants.url+"products?items_per_page=10&page=1&product&q&status=A");
                httpGet.setURI(uri);
                httpGet.addHeader(BasicScheme.authenticate(
                        new UsernamePasswordCredentials("your username", "password"),
                        HTTP.UTF_8, false));
                HttpResponse httpResponse;
                httpResponse = httpClient.execute(httpGet);
                InputStream inputStream = httpResponse.getEntity().getContent();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String readLine = bufferedReader.readLine();
                while (readLine != null) {
                    stringBuffer.append(readLine);
                    stringBuffer.append("\n");
                    readLine = bufferedReader.readLine();
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {

                    }
                }
            }
            JSONObject jobj = null;
            try {
                jobj = new JSONObject(String.valueOf(stringBuffer));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // JSONObject jobj1=new JSONObject(String.valueOf(prod));
            Log.e("Objects", String.valueOf(jobj));

            return null;
        }

    }


}
