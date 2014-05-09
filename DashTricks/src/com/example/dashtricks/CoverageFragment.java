package com.example.dashtricks;
 
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.example.dashtricks.R;
 
public class CoverageFragment extends Fragment {
 
	public CoverageFragment() {
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
    	//this.getActivity().setContentView(R.layout.fragment_coverage);
    	// load the parent view
        View rootView = inflater.inflate(R.layout.fragment_coverage, container, false);
        // get the appropriate child webview from the parent
        WebView myWebView = (WebView) rootView.findViewById(R.id.webView1);
        myWebView.addJavascriptInterface(this, "android");
        // enable javascript
        myWebView.getSettings().setJavaScriptEnabled(true);
        // load the appropriate webpage from the assets folder
        myWebView.loadUrl("file:///android_asset/bargraph.html");
        
        return rootView;
    }
    
    
    // pull the below data from a method
    

    //String data1 = "[{name: BCG, value:97}, {name: OPV-0, value: 50}]";
    String data = "[{\"name\": \"BCG\",\"value\": 97},{\""
    		+ "name\": \"OPV-0\",\"value\": 50}]";
    /*{name: \"OPV-1\",value: 92},{name: "
    		+ "\"Penta-1\",value: 82},{name: \"Rota-2\",value: 23},{name: \"Measles-1\",value: 83}]";*/
    
    
	/** This passes our data out to the JS */
	@JavascriptInterface
	public String getData() {
		//Log.d(TAG, "getData() called");
	    // String data = Data.getImmunization(districtId, monthId)
		return data;
	}
	
	private String a1dToJson(double[] data) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int i = 0; i < data.length; i++) {
			double d = data[i];
			if (i > 0)
				sb.append(",");
			sb.append(d);
		}
		sb.append("]");
		return sb.toString();
	}
    
}