package com.example.dashtricks;
 
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dashtricks.data.Query;
 
public class ColdChainFragment extends Fragment {
 
	private static String coldResult;
	private static WebView mWebView;
	private static int vaccineId = 0;
	private static int functionId = -1;
	private JSONParser parser;
	
	public ColdChainFragment() {
		parser = new JSONParser();
	}
	
    @SuppressLint("SetJavaScriptEnabled")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		// load the parent view
		View rootView = inflater.inflate(R.layout.fragment_cold_chain, container, false);
		// get the appropriate child webview from the parent
		mWebView = (WebView) rootView.findViewById(R.id.coldWebViewMain);
		mWebView.addJavascriptInterface(this, "android");
		// enable javascript
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl("about:blank");
		
		Button showByMonth = (Button) rootView.findViewById(R.id.coldByMonth);
		showByMonth.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	functionId = 1;
		        loadDataToWebView();
		        showStockView(false, -1, null, -1);
		    }
		});
		
		Button showBySub = (Button) rootView.findViewById(R.id.coldBySub);
		showBySub.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	functionId = 0;
		        loadDataToWebView();
		        showStockView(false, -1, null, -1);
		    }
		});
		
		/* Button for reloading the web view */
		Button button = (Button) rootView.findViewById(R.id.coldLoadButton);
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	functionId=-1;
		        loadDataToWebView();
		        showDetail(false, -1, null, -1);
		        showStockView(false, -1, null, -1);
		    }
		});
		
		// initial load should always be vaccine view
		functionId=-1;
		// initial load
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
				coldResult = br.readLine();
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
				
				coldResult = districts.toJSONString();
			} catch (ParseException e) {
				Log.i("CoverageData", e.getMessage());
			}
	        // load the appropriate webpage from the assets folder
			mWebView.loadDataWithBaseURL("file:///android_asset/", loadFile("linegraph.html"), "text/html", "UTF-8", null);
		} else {
			// by vaccine
			
			// query for immunization coverage for a district
			String coverageByVaccine = q.getImmunization(distId);
			String districtFridges = q.getDistrictFridgeCapacity(distId);
			String stockLevels = q.getDistrictStockLevel(distId);

			// parse the result into a JSON string and set coverageResults
			try {
				JSONObject districtObj = (JSONObject) parser.parse(districtFridges);
				JSONArray districts = (JSONArray) districtObj.get("fridge_capacity");
				
				coldResult = districts.toJSONString();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				Log.i("CoverageData", e.getMessage());
			}
	        // load the appropriate webpage from the assets folder
			mWebView.loadUrl("about:blank");
			mWebView.loadDataWithBaseURL("file:///android_asset/", loadFile("bargraph2.html"), "text/html", "UTF-8", null);
		}
    }
    
	/** This passes our data out to the JS */
	@JavascriptInterface
	public String getData() {
		return coldResult;
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
		
		long subId = 0;
		String subName = null;
		long coverage = 0;
		try {
			JSONObject barData = (JSONObject) parser.parse(data);
			subId = (Long) barData.get("subDistrictId");
			subName = (String) barData.get("subDistrictName");
			coverage = (Long) barData.get("coverage");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Log.v("bar clicked", data);
		
		Activity context = this.getActivity();
		CharSequence text = "Sub-District Name: " + subName + " Coverage: " + coverage;
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
	    // run code on the UI thread:
	    context.runOnUiThread(new Runnable() {
	    	private long subId;
	    	private String subName;
	    	private long coverage;
	    	
	        public void run() {
	        	ColdChainFragment.this.showStockView(true, subId, subName, coverage);
	        }
	        
	        public Runnable init(long subId, String subName, long coverage) {
	        	this.subId = subId;
	        	this.subName = subName;
	        	this.coverage = coverage;
	        	return this;
	        }
	    }.init(subId, subName, coverage) );
	}
	
	@JavascriptInterface
	public void registerClick(String data) {
		//Log.d(TAG, "getData() called");
		long vId = 0;
		String vName = null;
		long coverage = 0;
		try {
			JSONObject barData = (JSONObject) parser.parse(data);
			vId = (Long) barData.get("id");
			vName = (String) barData.get("name");
			coverage = (Long) barData.get("coverage");
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
	    	private long coverage;
	    	
	        public void run() {
	        	ColdChainFragment.this.showDetail(true, vId, vName, coverage);
	        }
	        
	        public Runnable init(long vId, String vName, long coverage) {
	        	this.vId = vId;
	        	this.vName = vName;
	        	this.coverage = coverage;
	        	return this;
	        }
	    }.init(vId, vName, coverage) );
	}
	
	/***
	 * Shows or hides further options for vaccine exploration in the sidebar. 
	 * 
	 * @param show: if true, show, if false, hide
	 * @param vaccId: vaccine id for more exploring
	 * @param vaccName: name of this vaccine
	 * @param coverage: the coverage rate for the vaccine
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void showDetail(boolean show, long vaccId, String vaccName, long coverage) {
		if (show) {
			
			vaccineId = (int) vaccId;
			View rootView = this.getView();
			
			TextView instructions = (TextView) rootView.findViewById(R.id.instructions);
			instructions.setVisibility(View.GONE);
			
			TextView vaccineDetail = (TextView) rootView.findViewById(R.id.vaccineName);
			vaccineDetail.setText(vaccName + ": " + coverage + "%");
			vaccineDetail.setVisibility(View.VISIBLE);
			
			Button showBySub = (Button) rootView.findViewById(R.id.bySub);
			showBySub.setVisibility(View.VISIBLE);
			
			Button showByMonth = (Button) rootView.findViewById(R.id.byMonth);
			showByMonth.setVisibility(View.VISIBLE);

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
	
	/***
	 * Shows/Hides View showing further detail about stock wastage for a subDistrict.
	 * 
	 * @param show if true, show, otherwise, hide
	 * @param coverage coverage rate for the subDistrict
	 * @param subName name of the subDistrict
	 * @param subId id of the subDistrict
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void showStockView(boolean show, long subId, String subName, long coverage) {
		View rootView = this.getView();
		WebView stockView = (WebView) rootView.findViewById(R.id.smallWebView);
		TextView stockDesc = (TextView) rootView.findViewById(R.id.stockDesc);
		if (show) {
			stockView.setVisibility(View.VISIBLE);
			stockDesc.setVisibility(View.VISIBLE);
			long wastage = (long) Math.ceil(60 - 0.58*coverage);
			Random r = new Random();
			double weight = 2 - (1.75/100)*coverage;
			long outs = (long) Math.floor(weight * (r.nextInt(1)+ 1));
			long level = (long) Math.ceil(20 + 0.75*coverage);
			stockDesc.setText("Stock Wastage: " + wastage + "%\nStock Outs: " + 
					outs + "\nStock Level: " + level + "%");
			stockView.addJavascriptInterface(this, "android");
			// enable javascript
			stockView.getSettings().setJavaScriptEnabled(true);
			stockView.loadUrl("file:///android_asset/circlegraph.html");
		} else {
			stockView.setVisibility(View.GONE);
			stockDesc.setVisibility(View.GONE);
		}
	}
}