package mg.studio.weatherappdesign;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    static TextView t0,date,d0,d1,d2,d3,d4,d5,news;
    static ImageView w0,w1,w2,w3,w4,w5;
    static long last_update_time;
    private String ch2en(String date_in_chinese){
        switch (date_in_chinese){
            case "周一":return "MON";
            case "周二":return "TUE";
            case "周三":return "WED";
            case "周四":return "THU";
            case "周五":return "FRI";
            case "周六":return "SAT";
            case "周日":
            case "周天":return "SUN";
            default:return "NOW";
        }
    }

    private void chooseImg(ImageView iv,String weather){
        switch (weather){
            case "阴":iv.setImageResource(R.drawable.cloudy);break;
            case "晴":iv.setImageResource(R.drawable.sunny);break;
            case "多云":iv.setImageResource(R.drawable.partly_sunny);break;
            case "小雨":iv.setImageResource(R.drawable.rainy);break;
            case "大雨":iv.setImageResource(R.drawable.rainy_up);break;
            default:iv.setImageResource(R.drawable.windy);break;
        }
    }

    private  boolean update(String str){
        try {
            JSONObject json = new JSONObject(str);

            JSONObject res=json.getJSONObject("RESULT");
            JSONArray wt=res.getJSONArray("weather_next");
            JSONObject cur;
            cur=wt.getJSONObject(0);
            d1.setText(ch2en(cur.getString("fj")));
            chooseImg(w1,cur.getString("fa"));chooseImg(w0,cur.getString("fa"));
            cur=wt.getJSONObject(1);
            d2.setText(ch2en(cur.getString("fj")));
            chooseImg(w2,cur.getString("fa"));
            cur=wt.getJSONObject(2);
            d3.setText(ch2en(cur.getString("fj")));
            chooseImg(w3,cur.getString("fa"));
            cur=wt.getJSONObject(3);
            d4.setText(ch2en(cur.getString("fj")));
            chooseImg(w4,cur.getString("fa"));
            cur=wt.getJSONObject(4);
            d5.setText(ch2en(cur.getString("fj")));
            chooseImg(w5,cur.getString("fa"));

            JSONObject wn=res.getJSONObject("weather_now");
            t0.setText(wn.getString("temp"));


        } catch (org.json.JSONException e) {
            Log.e("JSON error", e.getMessage());
            return false;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        news=(TextView) findViewById(R.id.tv_news);
        t0=(TextView) findViewById(R.id.temperature_of_the_day);
        date=(TextView)findViewById(R.id.day);
        d0=(TextView)findViewById(R.id.tv_date);
        d1=(TextView) findViewById(R.id.text1);
        d2=(TextView) findViewById(R.id.text2);
        d3=(TextView) findViewById(R.id.text3);
        d4=(TextView) findViewById(R.id.text4);
        d5=(TextView) findViewById(R.id.text5);
        w0=(ImageView) findViewById(R.id.img_weather_condition);
        w1=(ImageView) findViewById(R.id.img1);
        w2=(ImageView) findViewById(R.id.img2);
        w3=(ImageView) findViewById(R.id.img3);
        w4=(ImageView) findViewById(R.id.img4);
        w5=(ImageView) findViewById(R.id.img5);
        Calendar cal=Calendar.getInstance();
        int d=cal.get(Calendar.DAY_OF_WEEK);
        switch(d){
            case 2:date.setText("MONDAY");break;
            case 3:date.setText("TUESDAY");break;
            case 4:date.setText("WEDNESDAY");break;
            case 5:date.setText("THURSDAY");break;
            case 6:date.setText("FRIDAY");break;
            case 7:date.setText("SATURDAY");break;
            case 1:date.setText("SUNDAY");break;
            default:break;
        }
        String t=""+cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE);
        d0.setText(t);
        cal.set(1940,1,1);
        last_update_time=cal.getTimeInMillis();
        /*
        String test = "{\"ERRORCODE\":\"0\",\"RESULT\":{\"weather_next\":[{\"ff\":\"微风\",\"fg\":\"<3级\",\"fi\":\"3/2\",\"fj\":\"今天\",\"fn\":\"01\",\"fa\":\"阴\",\"fb\":\"多云\",\"fc\":\"15\",\"fs\":\"02\",\"fd\":\"9\"},{\"ff\":\"微风\",\"fg\":\"<3级\",\"fi\":\"3/3\",\"fj\":\"周日\",\"fn\":\"01\",\"fa\":\"晴\",\"fb\":\"多云\",\"fc\":\"19\",\"fs\":\"00\",\"fd\":\"8\"},{\"ff\":\"微风\",\"fg\":\"<3级\",\"fi\":\"3/4\",\"fj\":\"周一\",\"fn\":\"07\",\"fa\":\"阴\",\"fb\":\"小雨\",\"fc\":\"16\",\"fs\":\"02\",\"fd\":\"10\"},{\"ff\":\"微风\",\"fg\":\"<3级\",\"fi\":\"3/5\",\"fj\":\"周二\",\"fn\":\"02\",\"fa\":\"小雨\",\"fb\":\"阴\",\"fc\":\"13\",\"fs\":\"07\",\"fd\":\"10\"},{\"ff\":\"微风\",\"fg\":\"<3级\",\"fi\":\"3/6\",\"fj\":\"周三\",\"fn\":\"07\",\"fa\":\"小雨\",\"fb\":\"小雨\",\"fc\":\"12\",\"fs\":\"07\",\"fd\":\"9\"}],\"weather_now\":{\"sd\":\"91%\",\"temp\":\"10\",\"weather\":\"多云\",\"ws\":\"2级\",\"wd\":\"西北风\"}}}";
        update(test);*/
        new DownloadUpdate().execute();
    }

    public void btnClick(View view) {

        int res=1;
        res=res+(int)((Math.random()*128));
        news.setText(getResources().getIdentifier("sent"+(res),"string",getPackageName()));
        Calendar cal=Calendar.getInstance();
        long nowtime=cal.getTimeInMillis();
        if(nowtime-last_update_time>10000){
            new DownloadUpdate().execute();
        }
        else{
            String notetime="Request too frequently.";
            Toast.makeText(getBaseContext(),notetime,Toast.LENGTH_LONG).show();
        }

    }
    public void clockClick(View view){
        Intent intent = new Intent();
        intent.setAction(AlarmClock.ACTION_SHOW_TIMERS);
        startActivity(intent);

    }

    private class DownloadUpdate extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = "http://api.shujuzhihui.cn/api/weather/dailyweather?appKey=519520c912be443ebc97b86d33afef0e&city=Chongqing";
            HttpURLConnection urlConnection = null;
            BufferedReader reader=null;
            StringBuffer sb = new StringBuffer();
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                httpCon.setRequestMethod("GET");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                httpCon.setUseCaches(false);
                httpCon.setConnectTimeout(5000);
                httpCon.setReadTimeout(5000);
                InputStream in = httpCon.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in));

                String str=null;
                while((str=reader.readLine())!=null){

                    str=str.replace("\t","");
                    sb.append(str.replace(" ",""));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try{
                    if(reader!=null) {
                        reader.close();
                    }
                }
                catch(java.io.IOException e){

                }
            }
            String result =sb.toString();
            return result;
        }
        @Override
        protected void onPostExecute(String res) {
            //when the post is done
            Log.i("json",res);
            if(update(res)) {
                Toast.makeText(getBaseContext(),"Weather is updated.",Toast.LENGTH_LONG).show();
                Calendar cal=Calendar.getInstance();
                last_update_time=cal.getTimeInMillis();
            }
            else
                Toast.makeText(getBaseContext(),"Fail to update, please contact the developer.",Toast.LENGTH_LONG).show();

        }
    }
}
