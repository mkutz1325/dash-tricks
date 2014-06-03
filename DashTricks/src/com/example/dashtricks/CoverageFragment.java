package com.example.dashtricks;
 
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dashtricks.data.Query;
 
public class CoverageFragment extends Fragment {
 
	private static String coverageResult;
	private static WebView mWebView;
	private static int vaccineId = 0;
	private static int functionId = -1;
	private JSONParser parser;
	
	public CoverageFragment() {
		parser = new JSONParser();
	}
	

	private OnItemSelectedListener mFunctionsListener = new OnItemSelectedListener();
			
	private class OnItemSelectedListener implements AdapterView.OnItemSelectedListener {
		
		public OnItemSelectedListener() {
		}
		
	    @Override
		public void onItemSelected(AdapterView<?> parent, View view, 
	            int pos, long id) {
	        // An item was selected. You can retrieve the selected item using
	    	//showDetail(false, -1, null);
	    	functionId=pos;
	    	loadDataToWebView();
	    }

	    @Override
		public void onNothingSelected(AdapterView<?> parent) {
	        functionId=-1;
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
		mWebView.loadUrl("about:blank");
		
		Button showByMonth = (Button) rootView.findViewById(R.id.byMonth);
		showByMonth.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	functionId = 1;
		        loadDataToWebView();
		    }
		});
		
		Button showBySub = (Button) rootView.findViewById(R.id.bySub);
		showBySub.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	functionId = 0;
		        loadDataToWebView();
		    }
		});
		
		/* Button for reloading the web view */
		Button button = (Button) rootView.findViewById(R.id.button_load);
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	functionId=-1;
		        loadDataToWebView();
		        showDetail(false, -1, null);
		    }
		});
		
		// initial load
		loadDataToWebView();
		loadDataToWebView();
        return rootView;
    }
    
    public void loadDataToWebView() {
    	// data stuff
    	mWebView.loadUrl("about:blank");
    	mWebView.loadDataWithBaseURL("file:///android_asset/", loadFile("spinner.html"), "text/html", "UTF-8", null);
    	
		GlobalState state = (GlobalState) this.getActivity().getApplicationContext();
		Query q = state.getQuery();
		DistrictActivityDr d = (DistrictActivityDr) this.getActivity();
		String districtId = d.getDistrictId();
		Integer distId = Integer.parseInt(districtId);
		
		if (functionId == 0){
		    // by sub-district

			// load the map of this district into coverageResult; then map.html will call other methods for datas
			try {			
				Activity a = this.getActivity();
				AssetManager assetManager = a.getAssets();
				InputStream input = assetManager.open("tza.json");
				BufferedReader br = new BufferedReader(new InputStreamReader(input));
				coverageResult = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        // load map.html
	        mWebView.loadDataWithBaseURL("file:///android_asset/", loadFile("mapCoverage.html"), "text/html", "UTF-8", null);
		} else if (functionId == 1) {

			// monthly
			// query for monthly coverage data for this district and for this vaccine
			String coverageByMonth = q.getMonthlyVaccCover(distId, vaccineId);
			// parse the result into a JSON string
			try {
				JSONObject districtObj = (JSONObject) parser.parse(coverageByMonth);
				JSONArray districts = (JSONArray) districtObj.get("districtCoverage");
				
				coverageResult = districts.toJSONString();
			} catch (ParseException e) {
				Log.i("CoverageData", e.getMessage());
			}
	        // load the appropriate webpage from the assets folder
			mWebView.loadDataWithBaseURL("file:///android_asset/", loadFile("linegraph.html"), "text/html", "UTF-8", null);
		} else {
			// by vaccine
			
			// query for immunization coverage for a district
			String coverageByVaccine = q.getImmunization(distId);

			// parse the result into a JSON string and set coverageResults
			try {
				JSONObject districtObj = (JSONObject) parser.parse(coverageByVaccine);
				JSONArray districts = (JSONArray) districtObj.get("immunization");
				
				coverageResult = districts.toJSONString();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				Log.i("CoverageData", e.getMessage());
			}
	        // load the appropriate webpage from the assets folder
			mWebView.loadUrl("about:blank");
			mWebView.loadDataWithBaseURL("file:///android_asset/", loadFile("bargraph.html"), "text/html", "UTF-8", null);
		}
    }
    
	/** This passes our data out to the JS */
	@JavascriptInterface
	public String getData() {
		return coverageResult;
	}
	
	// for the by-subdistrict map function
	@JavascriptInterface
	public int getDistrict() {
		DistrictActivityDr d = (DistrictActivityDr) this.getActivity();
		String districtId = d.getDistrictId();
		Integer distId = Integer.parseInt(districtId);
		
		return distId;
	}
	
	// for the by-subdistrict map function
	@JavascriptInterface
	public String getCoverage() {
		GlobalState state = (GlobalState) this.getActivity().getApplicationContext();
		Query q = state.getQuery();
		int distId = getDistrict();
		// query for coverage rates by subdistrict
		String coverage = q.getDistrictCoverage(vaccineId,distId);
		
		return coverage;
	}

	public String loadFile(String name) {
		String string = new String();
		try {
			Activity a = this.getActivity();
			AssetManager assetManager = a.getAssets();
			//String[] files = assetManager.list("");
			InputStream input = assetManager.open(name);
			
		    StringBuilder fileContents = new StringBuilder();
		    Scanner scanner = new Scanner(input);
		    String lineSeparator = System.getProperty("line.separator");

		    try {
		        while(scanner.hasNextLine()) {        
		            fileContents.append(scanner.nextLine() + lineSeparator);
		        }
		        string = fileContents.toString();
		        return string;
		    } finally {
		        scanner.close();
		    }
			
		} catch (FileNotFoundException e) {
			String message = e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			String message = e.getMessage();
			e.printStackTrace();
		}
		return string;
	}
	
	@JavascriptInterface
	public void registerMapClick(String data) {
		Log.v("map click", data);
	}
	
	@JavascriptInterface
	public void registerClick(String data) {
		//Log.d(TAG, "getData() called");
		long vId = 0;
		String vName = null;
		try {
			JSONObject barData = (JSONObject) parser.parse(data);
			vId = (Long) barData.get("id");
			vName = (String) barData.get("name");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Log.v("bar clicked", data);
		
		Activity context = this.getActivity();
		CharSequence text = "Vaccine ID: " + vId + " Name: " + vName;
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
	    // run code on the UI thread:
	    context.runOnUiThread(new Runnable() {
	    	private long vId;
	    	private String vName;
	    	
	        public void run() {
	        	CoverageFragment.this.showDetail(true, vId, vName);
	        }
	        
	        public Runnable init(long vId, String vName) {
	        	this.vId = vId;
	        	this.vName = vName;
	        	return this;
	        }
	    }.init(vId, vName) );
	}
	
	// get the stock level data
	@JavascriptInterface
	public String getStockData() {
		return "{\"name\":\"BCG\",\"value\":\"46\"}";
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void showDetail(boolean show, long vaccId, String vaccName) {
		if (show) {
			
			vaccineId = (int) vaccId;
			View rootView = this.getView();
			
			TextView instructions = (TextView) rootView.findViewById(R.id.instructions);
			instructions.setVisibility(View.GONE);
			
			TextView vaccineDetail = (TextView) rootView.findViewById(R.id.vaccineName);
			vaccineDetail.setText(vaccName);
			vaccineDetail.setVisibility(View.VISIBLE);
			
			Button showBySub = (Button) rootView.findViewById(R.id.bySub);
			showBySub.setVisibility(View.VISIBLE);
			
			Button showByMonth = (Button) rootView.findViewById(R.id.byMonth);
			showByMonth.setVisibility(View.VISIBLE);

			
/*			WebView stockView = (WebView) rootView.findViewById(R.id.smallWebView);
			stockView.addJavascriptInterface(this, "android");
			// enable javascript
			stockView.getSettings().setJavaScriptEnabled(true);
			stockView.loadUrl("file:///android_asset/circlegraph.html");*/
		} else {
			View rootView = this.getView();
			TextView instructions = (TextView) rootView.findViewById(R.id.instructions);
			TextView vaccineDetail = (TextView) rootView.findViewById(R.id.vaccineName);
			Button showBySub = (Button) rootView.findViewById(R.id.bySub);
			Button showByMonth = (Button) rootView.findViewById(R.id.byMonth);
			
			instructions.setVisibility(View.VISIBLE);
			vaccineDetail.setVisibility(View.GONE);
			showBySub.setVisibility(View.GONE);
			showByMonth.setVisibility(View.GONE);
		}
	}
}