package com.example.dashtricks;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;

import com.example.dashtricks.data.Query;
 
public class CoverageFragment extends Fragment {
 
	private static String coverageResult;
	private static boolean byVaccine = true;
	private static WebView mWebView;
	
	public CoverageFragment() {
	}
	
	// Create an anonymous implementation of OnClickListener
	private OnClickListener mOnOffListener = new OnClickListener() {
	    public void onClick(View v) {
	    	byVaccine = !byVaccine;
	    	loadDataToWebView();
	    }
	};
	
    @SuppressLint("SetJavaScriptEnabled")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
    	//this.getActivity().setContentView(R.layout.fragment_coverage);
    	// load the parent view
        View rootView = inflater.inflate(R.layout.fragment_coverage, container, false);
        // get the appropriate child webview from the parent
        mWebView = (WebView) rootView.findViewById(R.id.webView1);
        mWebView.addJavascriptInterface(this, "android");
        // enable javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        loadDataToWebView();
        // Capture button from layout
        Button button = (Button) rootView.findViewById(R.id.toggleButton1);
        // Register the onClick listener with the implementation above
        button.setOnClickListener(mOnOffListener);
        
        return rootView;
    }
    
    public void loadDataToWebView() {
    	// data stuff
		GlobalState state = (GlobalState) this.getActivity().getApplicationContext();
		Query q = state.getQuery();
		DistrictActivity d = (DistrictActivity) this.getActivity();
		String districtId = d.getDistrictId();
		Integer distId = Integer.parseInt(districtId);
		
		if (byVaccine) {
			String coverageByVaccine = q.getImmunization(distId, 1);
			
			JSONParser parser = new JSONParser();
			try {
				JSONObject districtObj = (JSONObject) parser.parse(coverageByVaccine);
				JSONArray districts = (JSONArray) districtObj.get("immunization");
				
				coverageResult = districts.toJSONString();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				Log.i("CoverageData", e.getMessage());
			}
	        // load the appropriate webpage from the assets folder
	        mWebView.loadUrl("file:///android_asset/bargraph.html");
		} else {
		    char esc = '"';
		    String data = "[{name: " + esc + "BCG" + esc + ",value: 97},{name: " + esc + "OPV-0" + esc + ",value: 50}]";
		    coverageResult = data;
	        // load the appropriate webpage from the assets folder
	        mWebView.loadUrl("file:///android_asset/linegraph.html");
		}
    }
    
	/** This passes our data out to the JS */
	@JavascriptInterface
	public String getData() {
		//Log.d(TAG, "getData() called");
	    // String data = Data.getImmunization(districtId, monthId)

		return coverageResult;
	}
}