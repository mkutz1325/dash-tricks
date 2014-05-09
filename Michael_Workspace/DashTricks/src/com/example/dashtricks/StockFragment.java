package com.example.dashtricks;
 
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.dashtricks.R;
 
public class StockFragment extends Fragment {
 
	
	public StockFragment() {
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
    	View rootView = inflater.inflate(R.layout.fragment_stock, container, false);
        WebView myWebView = (WebView) rootView.findViewById(R.id.webView2);

        //myWebView.loadUrl("http://www.macrodigest.com");
        
/*        String summary = "<html><body>You scored <b>192</b> points.</body></html>";
        myWebView.loadData(summary, "text/html", null);
        return rootView;*/
    	


        myWebView.getSettings().setJavaScriptEnabled(true); // enable javascript

        final Activity activity = super.getActivity();

        myWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });

        myWebView.loadUrl("http://www.google.com");
        //setContentView(mWebview );
        
        return rootView;
    }
}