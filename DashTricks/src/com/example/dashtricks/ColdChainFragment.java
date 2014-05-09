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
import android.webkit.WebView;
 
public class ColdChainFragment extends Fragment {
 
	public ColdChainFragment() {
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
    	View rootView = inflater.inflate(R.layout.fragment_cold_chain, container, false);
        WebView mainWebView = (WebView) rootView.findViewById(R.id.webView3a);
        //mainWebView.setInitialScale(1);
        WebView sideWebView = (WebView) rootView.findViewById(R.id.webView3b);

        mainWebView.getSettings().setJavaScriptEnabled(true);
        // load the appropriate webpage from the assets folder
        mainWebView.loadUrl("file:///android_asset/linegraph.html");
        
    	//mainWebView.loadData(getData(),"text/html","utf-8");
        String sidebar = "<html><body>This is the sidebar for a cold chain fragment</body></html>";
        sideWebView.loadData(sidebar, "text/html", null);
        
        return rootView;
    }
    
/*    private String getData() {
    	String prompt = "";
    	AssetManager assetManager = getActivity().getApplicationContext().getResources().getAssets();
    	try {
    	    InputStream inputStream = assetManager.open("linegraph.html");
    	    byte[] b = new byte[inputStream.available()];
    	    inputStream.read(b);
    	    prompt = String.format(new String(b));
    	    inputStream.close();
    	} catch (IOException e) {
    	    //Log.e(LOGTAG, "Couldn't open upgrade-alert.html", e);
    	}

    	return prompt;
    }*/
}