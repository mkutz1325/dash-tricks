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
import android.widget.Button;
import android.widget.Spinner;

import com.example.dashtricks.data.Query;
 
public class CoverageFragment extends Fragment {
 
	private static String coverageResult;
	private static WebView mWebView;
	private static int vaccineId = 0;
	private static int functionId = 0;
	
	public CoverageFragment() {
	}
	
	private OnItemSelectedListener mVaccinesListener = new OnItemSelectedListener(0);
	private OnItemSelectedListener mFunctionsListener = new OnItemSelectedListener(1);
			
	private class OnItemSelectedListener implements AdapterView.OnItemSelectedListener {
		
		private int type;

		public OnItemSelectedListener(int type) {
			this.type = type;
		}
		
	    @Override
		public void onItemSelected(AdapterView<?> parent, View view, 
	            int pos, long id) {
	        // An item was selected. You can retrieve the selected item using
	    	if (type == 0) {
	    		vaccineId = pos;
	    	} else {
	    		functionId = pos;
	    	}
	    }

	    @Override
		public void onNothingSelected(AdapterView<?> parent) {
	        if (type == 0)
	        	vaccineId = 0;
	        else
	        	functionId = 0;
	    }
	};
	
    @SuppressLint("SetJavaScriptEnabled")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		// load the parent view
		View rootView = inflater.inflate(R.layout.fragment_coverage, container, false);
		// get the appropriate child webview from the parent
		mWebView = (WebView) rootView.findViewById(R.id.webView1);
		mWebView.addJavascriptInterface(this, "android");
		// enable javascript
		mWebView.getSettings().setJavaScriptEnabled(true);
		
		/* Spinner for choosing the vaccine to focus in on */
		Spinner vaccineDropDown = (Spinner) rootView.findViewById(R.id.spinner1);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
		        R.array.vaccine_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		vaccineDropDown.setAdapter(adapter);
		vaccineDropDown.setOnItemSelectedListener(mVaccinesListener);
		
		/* Spinner for choosing byLocation or byMonth */
		Spinner functions = (Spinner) rootView.findViewById(R.id.spinner2);
		// Create an ArrayAdapter with the different function options
		ArrayAdapter<CharSequence> funcAdapter = ArrayAdapter.createFromResource(this.getActivity(),
		        R.array.explore_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		funcAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		functions.setAdapter(funcAdapter);
		functions.setOnItemSelectedListener(mFunctionsListener);
		
		/* Button for reloading the web view */
		Button button = (Button) rootView.findViewById(R.id.button_load);
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		        loadDataToWebView();
		    }
		});
		
		// initial load
		loadDataToWebView();
        return rootView;
    }
    
    public void loadDataToWebView() {
    	// data stuff
		GlobalState state = (GlobalState) this.getActivity().getApplicationContext();
		Query q = state.getQuery();
		DistrictActivityDr d = (DistrictActivityDr) this.getActivity();
		String districtId = d.getDistrictId();
		Integer distId = Integer.parseInt(districtId);
		
		if (functionId == 0 || vaccineId==0) {
			// by vaccine
			// TODO have this query be yearly not for a specific month
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
		} else if (functionId == 1){
		    // by sub-district
			// TODO replace 1 with result of first spinner
			String coverageBySubDistrict = q.getDistrictCoverage(vaccineId, distId);
			
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
		} else {
			// monthly
			//TODO this doesn't work, but not worth the effort until new query received
			String coverageByMonth = q.getMonthlySubDistrictCoverage(vaccineId, distId);
			
			JSONParser parser = new JSONParser();
			try {
				JSONObject districtObj = (JSONObject) parser.parse(coverageByMonth);
				JSONArray districts = (JSONArray) districtObj.get("monthly_subDistrict_coverage");
				
				coverageResult = districts.toJSONString();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				Log.i("CoverageData", e.getMessage());
			}
	        // load the appropriate webpage from the assets folder
	        mWebView.loadUrl("file:///android_asset/bargraph3.html");
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