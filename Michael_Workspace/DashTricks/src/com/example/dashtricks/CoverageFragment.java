package com.example.dashtricks;
 
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.example.dashtricks.R;
 
public class CoverageFragment extends Fragment {
 
	public CoverageFragment() {
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
    	// load the parent view
        View rootView = inflater.inflate(R.layout.fragment_coverage, container, false);
        // get the appropriate child webview from the parent
        WebView myWebView = (WebView) rootView.findViewById(R.id.webView1);
        // enable javascript
        myWebView.getSettings().setJavaScriptEnabled(true);
        // load the appropriate webpage from the assets folder
        myWebView.loadUrl("file:///android_asset/bargraph.html");
        
        return rootView;
    }
    
}