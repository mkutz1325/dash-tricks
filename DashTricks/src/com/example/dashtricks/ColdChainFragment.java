package com.example.dashtricks;
 
import java.io.IOException;
import java.io.InputStream;

import android.app.Fragment;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
 
public class ColdChainFragment extends Fragment {
 
	public ColdChainFragment() {
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
    	View rootView = inflater.inflate(R.layout.fragment_cold_chain, container, false);
        WebView mainWebView = (WebView) rootView.findViewById(R.id.webView3a);
        mainWebView.addJavascriptInterface(this, "android");
        //mainWebView.setInitialScale(1);
        //WebView sideWebView = (WebView) rootView.findViewById(R.id.webView3b);

        mainWebView.getSettings().setJavaScriptEnabled(true);
        // load the appropriate webpage from the assets folder
        mainWebView.loadUrl("file:///android_asset/linegraph.html");
        
    	//mainWebView.loadData(getData(),"text/html","utf-8");
        //String sidebar = "<html><body>This is the sidebar for a cold chain fragment</body></html>";
        //sideWebView.loadData(sidebar, "text/html", null);
        
        return rootView;
    }
    
    
    char esc = '"';
    String data = "[{name: " + esc + "BCG" + esc + ",value: 97},{name: " + esc + "OPV-0" + esc + ",value: 50}]";
	/** This passes our data out to the JS */
	@JavascriptInterface
	public String getData() {
		//Log.d(TAG, "getData() called");
	    // String minTemps = Data.getMinTemps(districtId);
		// String maxTemps = Data.getMaxTemps(districtId);
		return data;
	}
}