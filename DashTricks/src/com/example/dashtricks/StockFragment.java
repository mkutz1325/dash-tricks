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
 
    	//this.getActivity().setContentView(R.layout.fragment_coverage);
    	// load the parent view
        View rootView = inflater.inflate(R.layout.fragment_stock, container, false);
        // get the appropriate child webview from the parent
        WebView myWebView = (WebView) rootView.findViewById(R.id.webView2);
        // enable javascript
        myWebView.getSettings().setJavaScriptEnabled(true);
        // load the appropriate webpage from the assets folder
        myWebView.loadUrl("file:///android_asset/circlegraph.html");
        
        return rootView;
    }
}