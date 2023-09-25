package com.lisners.counsellor.Activity.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lisners.counsellor.ApiModal.PageModel;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;

public class WebScreen extends AppCompatActivity implements View.OnClickListener{
    private ImageButton back_btn;
    private TextView header_title ;
    private HtmlTextView text;
    private ArrayList<PageModel> pageModels;
    /*WebView mywebview ;*/
    ProgressBar pv_bar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_screen);
        header_title = findViewById(R.id.tvHeader);
        back_btn =findViewById(R.id.btn_header_left);
        text = findViewById(R.id.text);
        back_btn.setImageResource(R.drawable.ic_svg_arrow_right);
        back_btn.setOnClickListener(this);
       /* mywebview = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mywebview.getSettings();
        webSettings.setJavaScriptEnabled(true);*/
        pv_bar = findViewById(R.id.pv_bar);
        pageModels = new ArrayList<>();
        getWebScreen();
    }

    public void showWevView(String pageContent,String title){
        header_title.setText(title+"");
        /*Log.e("url",url);*/
       /* mywebview.loadUrl(url+"");
        mywebview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if(progress == 100)
                    pv_bar.setVisibility(View.INVISIBLE);
                else
                    pv_bar.setVisibility(View.VISIBLE);

            }
        });*/


        String des = pageContent;

        des = des.replaceAll("data:image\\/jpeg;base64,", "");
        des = des.replaceAll("data:image/png;base64", "");

        text.setHtml(des, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String s) {

                byte[] data;
                data = Base64.decode(s, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Drawable d = new BitmapDrawable(getResources(), bitmap);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                return d;
            }
        });
    }

    private void getWebScreen(){
        String slug = getIntent().getStringExtra("SLUG");
        pv_bar.setVisibility(View.VISIBLE);
        GetApiHandler apiHandler = new GetApiHandler(this, URLs.GET_PAGES+"/"+slug, new GetApiHandler.OnClickListener() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                pv_bar.setVisibility(View.INVISIBLE);
                if(jsonObject.has("status"))
                {
                    /*JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        PageModel pageModel = new Gson().fromJson(jsonArray.getString(i),PageModel.class);
                        if(pageModel.getSlug().equals(slug)){
                            showWevView(pageModel.getPage_content(),pageModel.getTitle());
                        }
                    }*/
                    JSONObject jsonArray = jsonObject.getJSONObject("data");
                    PageModel pageModel = new Gson().fromJson(jsonArray.toString(),PageModel.class);
                    showWevView(pageModel.getPage_content(),pageModel.getTitle());

                }
            }

            @Override
            public void onError() {
                pv_bar.setVisibility(View.INVISIBLE);
            }
        });
        apiHandler.execute();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_header_left:
                finish();
                break;
        }
    }

}