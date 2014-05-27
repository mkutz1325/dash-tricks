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
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.dashtricks.data.Query;
 
public class CoverageFragment extends Fragment {
 
	private static String coverageResult;
	private static WebView mWebView;
	
	public CoverageFragment() {
	}
	
	private OnItemSelectedListener mSpinnerListener = new OnItemSelectedListener() {
	    @Override
		public void onItemSelected(AdapterView<?> parent, View view, 
	            int pos, long id) {
	        // An item was selected. You can retrieve the selected item using
	        loadDataToWebView(pos);
	    }

	    @Override
		public void onNothingSelected(AdapterView<?> parent) {
	        loadDataToWebView(0);
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
		loadDataToWebView(0);
		// Capture button from layout
		/*Button button = (Button) rootView.findViewById(R.id.toggleButton1);
		// Register the onClick listener with the implementation above
		button.setOnClickListener(mOnOffListener);*/
		
		Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner1);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
		        R.array.explore_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(mSpinnerListener);
        
        return rootView;
    }
    
    public void loadDataToWebView(int pos) {
    	// data stuff
		GlobalState state = (GlobalState) this.getActivity().getApplicationContext();
		Query q = state.getQuery();
		DistrictActivityDr d = (DistrictActivityDr) this.getActivity();
		String districtId = d.getDistrictId();
		Integer distId = Integer.parseInt(districtId);
		
		if (pos == 0) {
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
		    // TODO real vaccine id
			String coverageBySubDistrict = q.getDistrictCoverage(1, distId);
			
			JSONParser parser = new JSONParser();
			try {
				JSONObject districtObj = (JSONObject) parser.parse(coverageBySubDistrict);
				JSONArray districts = (JSONArray) districtObj.get("district_coverage");
				
				coverageResult = districts.toJSONString();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				Log.i("CoverageData", e.getMessage());
			}
	        // load the appropriate webpage from the assets folder
	        mWebView.loadUrl("file:///android_asset/bargraph2.html");
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