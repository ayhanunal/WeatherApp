package com.ayhanunal.havadurumuapi;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    TextView tarihText;
    TextView durumText;
    TextView dereceText;
    TextView dusukText;
    TextView yuksekText;

    ImageView havaDurumuImage;

    ArrayList<TextView> textOgeler = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tarihText = findViewById(R.id.tarihText);
        durumText = findViewById(R.id.durumText);
        dereceText = findViewById(R.id.dereceText);
        dusukText = findViewById(R.id.dusukText);
        yuksekText = findViewById(R.id.yuksekText);

        havaDurumuImage = findViewById(R.id.havaDurumuImage);

        textOgeler.add(tarihText);
        textOgeler.add(durumText);
        textOgeler.add(dereceText);
        textOgeler.add(dusukText);
        textOgeler.add(yuksekText);

        gizleGoster(textOgeler,"gizle",havaDurumuImage);




    }

    public void getAnkara(View view) throws InterruptedException {

        Goruntule("https://api.collectapi.com/weather/getWeather?data.lang=tr&data.city=ankara");

        Thread.sleep(2000);
        gizleGoster(textOgeler,"goster",havaDurumuImage);

    }

    public void getKirikkale(View view) throws InterruptedException {

        Goruntule("https://api.collectapi.com/weather/getWeather?data.lang=tr&data.city=kirikkale");

        Thread.sleep(2000);
        gizleGoster(textOgeler,"goster",havaDurumuImage);


    }

    public void Goruntule(String gelenUrl){
        DownloadData downloadData = new DownloadData();

        try {

            downloadData.execute(gelenUrl);




        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void gizleGoster(ArrayList<TextView> arrayList,String durum,ImageView imageView){
        if (durum.matches("gizle")){
            for (TextView textView :arrayList){
                textView.setVisibility(View.INVISIBLE);
            }
            imageView.setVisibility(View.INVISIBLE);
        }else if (durum.matches("goster")){
            for (TextView textView :arrayList){
                textView.setVisibility(View.VISIBLE);
            }
            imageView.setVisibility(View.VISIBLE);
        }
    }


    private class DownloadData extends AsyncTask<String,Void,String>{



        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;
            HttpsURLConnection httpsURLConnection;



            try {
                url = new URL(strings[0]);
                httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestProperty("authorization","apikey 4dTSmqeJN4YOXENSRjl5ia:4lysTxPe3MM0dfDQElov1n");
                httpsURLConnection.setRequestProperty("content-type", "application/json");
                /*httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setUseCaches(false);
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.setDoOutput(true);

                 */




                InputStream inputStream = httpsURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data > 0){
                    //0dan buyuk oldugu surece hala data var demek.

                    //veriyi karakter karakter olarak alicaz.
                    char character = (char) data;
                    result += character;

                    data = inputStreamReader.read(); //bir sonraki karaktere gec.

                }

                return result;

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }




        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONObject jsonObject = new JSONObject(s);
                //JSONArray jsonArray = new JSONArray(s);
                String result = jsonObject.getString("result");
                JSONArray jsonArray = new JSONArray(result);

                JSONObject resultObject = jsonArray.getJSONObject(0);

                tarihText.setText(resultObject.getString("date"));
                durumText.setText("Hava Durumu :"+resultObject.getString("description"));
                dereceText.setText("Hava Derece :"+resultObject.getString("degree"));
                dusukText.setText("En Düşük :"+resultObject.getString("min"));
                yuksekText.setText("En Yüksek :"+resultObject.getString("max"));

                //System.out.println(resultObject.getString("icon"));

                //Picasso.get().load(resultObject.getString("icon")).resize(300,300).centerCrop().into(havaDurumuImage);

                /*
                fun ImageView.loadSvg(url: String?) {
                    GlideToVectorYou
                            .init()
                            .with(this.context)
                            .setPlaceHolder(R.drawable.loading, R.drawable.actual)
                            .load(Uri.parse(url), this)
                }

                 */

                Uri uri = Uri.parse(resultObject.getString("icon"));

                GlideToVectorYou.justLoadImage(MainActivity.this,uri,havaDurumuImage);





            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}