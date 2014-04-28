package com.example.dashtricks;
 
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.example.dashtricks.R;
 
public class ColdChainFragment extends Fragment {
 
	public ColdChainFragment() {
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
    	View rootView = inflater.inflate(R.layout.fragment_cold_chain, container, false);
        WebView mainWebView = (WebView) rootView.findViewById(R.id.webView3a);
        WebView sideWebView = (WebView) rootView.findViewById(R.id.webView3b);

        mainWebView.getSettings().setJavaScriptEnabled(true);
        // load the appropriate webpage from the assets folder
        mainWebView.loadUrl("file:///android_asset/linegraph.html");
        String sidebar = "<html><body>This is the sidebar for a cold chain fragment</body></html>";
        sideWebView.loadData(sidebar, "text/html", null);
        
        return rootView;
    }
}