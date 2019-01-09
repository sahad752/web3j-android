package cniao5.com.cniao5shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import cniao5.com.cniao5shop.bean.Fishes;
import cniao5.com.cniao5shop.http.OkHttpHelper;
import cniao5.com.cniao5shop.blockchain.FishAPI;
import cniao5.com.cniao5shop.widget.CNiaoToolBar;
import cniao5.com.cniao5shop.widget.TagsLayout;
import dmax.dialog.SpotsDialog;

//import co.lujun.androidtagview.TagContainerLayout;

public class FishDetailActivity_center extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.webView)
    private WebView mWebView;

    @ViewInject(R.id.toolbar)
    private CNiaoToolBar mToolBar;

    @ViewInject(R.id.webView1)
    private WebView mWebView1;

    @ViewInject(R.id.webView2)
    private WebView mWebView2;

    private TextView momFish;

    private TextView dadFish;

    private Fishes mWare;

    private WebAppInterface mAppInterfce;
    private WebAppInterface1 mAppInterfce1;
    private WebAppInterface2 mAppInterfce2;


    private SpotsDialog mDialog;

    private List<String> tags = new ArrayList<>();

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();


    private final int INIT_MSG = 1;
    private String momGene = "";
    private String dadGene = "";

    private String momGen = "";
    private String dadGen = "";


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){

                case INIT_MSG:

                    TagsLayout imageViewGroup = (TagsLayout) findViewById(R.id.image_layout);
                    ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    String[] tags={
                            "#"+mWare.getId().toString(),
                            mWare.getRare(),
                            "第"+mWare.getGeneration().toString()+"代",
                            mWare.getCooldown().toString()+"冷却",
                            FishAPI.timeStamp2Date(mWare.getBirthTime().toString())+"出生"
                    };
                    for (int i = 0; i < tags.length; i++) {
                        TextView textView = new TextView(FishDetailActivity_center.this);
                        textView.setText(tags[i]);
                        textView.setTextColor(getResources().getColor(R.color.white));
                        textView.setBackgroundResource(R.drawable.selector_textview);
                        imageViewGroup.addView(textView, lp);
                    }




                    initToolBar();
                    initWebView();


                    momFish.setText("母亲: #"+mWare.getMomId()+" 第"+momGen+"代");
                    dadFish.setText("父亲: #"+mWare.getDadId()+" 第"+dadGen+"代");

                    break;
                default:
                    break;
            }
        }
    };

    @OnClick(R.id.buy_fish)
    public void buy(View view) {
        //调用方法


        Intent intent = new Intent(FishDetailActivity_center.this, FishDetailActivity_center2.class);
        intent.putExtra(Contants.WARE, mWare);//选择繁育市场的锦鲤
        Log.v("mware", mWare.getBreed_price().toString());
//        intent.putExtra("ware2",wares);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish_detail_center);
        ViewUtils.inject(this);



        Serializable serializable = getIntent().getSerializableExtra(Contants.WARE);
        if(serializable ==null)
            this.finish();


        mDialog = new SpotsDialog(this,"loading....");
        mDialog.show();

        momFish = findViewById(R.id.momFish);
        dadFish = findViewById(R.id.dadFish);

        mWare = (Fishes) serializable;

        TextView mPrice = (TextView) findViewById(R.id.price);
        mPrice.setText(mWare.getBreed_price()+" wei");

        /*TagsLayout imageViewGroup = (TagsLayout) findViewById(R.id.image_layout);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        String[] tags={
                mWare.getRare(),
                "第"+mWare.getGeneration().toString()+"代",
                mWare.getCooldown().toString()+"冷却",
                FishAPI.timeStamp2Date(mWare.getBirthTime().toString())+"出生"
            };
        for (int i = 0; i < tags.length; i++) {
            TextView textView = new TextView(this);
            textView.setText(tags[i]);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setBackgroundResource(R.drawable.selector_textview);
            imageViewGroup.addView(textView, lp);
        }

        initToolBar();
        initWebView();*/


        getGenes();
    }

    private void getGenes(){

        BigInteger momId = mWare.getMomId();
        BigInteger dadId = mWare.getDadId();

        FishAPI fishAPI = FishAPI.getInstance();
        List<Object> temp_value = fishAPI.getDetailsOfFish(momId);
        List<Object> temp_value_2 =  fishAPI.getDetailsOfFish(dadId);
        momGene = temp_value.get(1).toString();
        dadGene = temp_value_2.get(1).toString();
        momGen = temp_value.get(5).toString();
        dadGen = temp_value_2.get(5).toString();

        Message msg = handler.obtainMessage();
        msg.what = INIT_MSG;
        handler.sendMessage(msg);

    }


    private void initWebView(){

        WebSettings settings = mWebView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        settings.setBlockNetworkImage(false);
        settings.setAppCacheEnabled(true);


        WebSettings settings1 = mWebView1.getSettings();

        settings1.setJavaScriptEnabled(true);
        settings1.setUseWideViewPort(true);
        settings1.setLoadWithOverviewMode(true);

        settings1.setBlockNetworkImage(false);
        settings1.setAppCacheEnabled(true);

        WebSettings settings2 = mWebView2.getSettings();

        settings2.setJavaScriptEnabled(true);
        settings2.setUseWideViewPort(true);
        settings2.setLoadWithOverviewMode(true);

        settings2.setBlockNetworkImage(false);
        settings2.setAppCacheEnabled(true);


        // mWebView.loadUrl(Contants.API.WARES_DETAIL);
        mWebView.loadUrl(Contants.API.FISH_IMG+mWare.getGene().toString());
        mWebView1.loadUrl(Contants.API.FISH_IMG+momGene);
        mWebView2.loadUrl(Contants.API.FISH_IMG+dadGene);

        mAppInterfce = new WebAppInterface(this);
        mAppInterfce1 = new WebAppInterface1(this);
        mAppInterfce2 = new WebAppInterface2(this);
        mWebView.addJavascriptInterface(mAppInterfce,"appInterface");
        mWebView.setWebViewClient(new WC());
        mWebView1.addJavascriptInterface(mAppInterfce1,"appInterface");
        mWebView1.setWebViewClient(new WC());
        mWebView2.addJavascriptInterface(mAppInterfce2,"appInterface");
        mWebView2.setWebViewClient(new WC());




    }



    private void initToolBar(){


        mToolBar.setNavigationOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDialog != null){
            mDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        this.finish();
    }



    class  WC extends WebViewClient{


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if(mDialog !=null && mDialog.isShowing())
                mDialog.dismiss();

            mAppInterfce.showDetail();


        }
    }



    class WebAppInterface{


        private Context mContext;
        public WebAppInterface(Context context){
            mContext = context;
        }

        @JavascriptInterface
        public  void showDetail(){


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mWebView.loadUrl("javascript:showDetail("+mWare.getGene()+")");

                }
            });
        }



    }


    class WebAppInterface1{

        private Context mContext;
        public WebAppInterface1(Context context){
            mContext = context;
        }

        @JavascriptInterface
        public  void showDetail(){


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mWebView1.loadUrl("javascript:showDetail("+mWare.getGene()+")");

                }
            });
        }


    }

    class WebAppInterface2{

        private Context mContext;
        public WebAppInterface2(Context context){
            mContext = context;
        }

        @JavascriptInterface
        public  void showDetail(){


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mWebView2.loadUrl("javascript:showDetail("+mWare.getGene()+")");

                }
            });
        }


    }
}
