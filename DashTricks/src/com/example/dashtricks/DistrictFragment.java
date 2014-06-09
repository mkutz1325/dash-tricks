package com.example.dashtricks;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.example.dashtricks.data.Query;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.TextView;

public class DistrictFragment extends Fragment {
	
	private static WebView mWebView;

	   @SuppressLint("SetJavaScriptEnabled")
	@Override
	   public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	 
	    	View rootView = inflater.inflate(R.layout.fragment_district, container, false);
	        
	        
			DistrictActivityDr d = (DistrictActivityDr) this.getActivity();
			String districtId = d.getDistrictId();
			Integer distId = Integer.parseInt(districtId);
			
			// set district name
			TextView districtName = (TextView) rootView.findViewById(R.id.districtName);
			districtName.setText(d.getDistrictName());
			
			// set population
			TextView population = (TextView) rootView.findViewById(R.id.population);
			long basePop = 80000;
			double popMul = 1.0 + (((distId%10)*.25 + ((distId/10)%10)*.25)/2.0);
			long pop = (long) Math.ceil((basePop*popMul));
			population.setText("Population: " + pop);
			
			// set monthly births
			TextView monthlyBirths = (TextView) rootView.findViewById(R.id.monthly_births);
			long births = (long) Math.ceil(.12*pop);
			monthlyBirths.setText("Monthly Births: " + births);
			
			// set surviving infants per month
			TextView survivingInfantsMonth = (TextView) rootView.findViewById(R.id.surviving_infants_month);
			long survive = (long) Math.ceil(.90*births - popMul*3);
			survivingInfantsMonth.setText("Surviving Infants/Month: " + survive);		
			
			// set number of sub districts
			TextView numSubDistricts = (TextView) rootView.findViewById(R.id.num_sub_districts);
			long subDistricts = (long) Math.ceil(12 * popMul);
			numSubDistricts.setText("No. Sub-districts: " + subDistricts);
			
			// set number of health facilities
			TextView numHealthFacilities = (TextView) rootView.findViewById(R.id.num_health_facilities);
			long facilities = (long) Math.ceil(popMul * 2 * subDistricts);
			numHealthFacilities.setText("No. health facilities: " + facilities);
			
			/** Load the map **/
			
			mWebView = (WebView) rootView.findViewById(R.id.districtMap);
			mWebView.addJavascriptInterface(this, "android");
			// enable javascript
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.loadDataWithBaseURL("file:///android_asset/", loadFile("map.html"), "text/html", "UTF-8", null);
	        
	        return rootView;
	    }
	   
	   /** This passes our data out to the JS */
		@JavascriptInterface
		public String getData() {
			String mapData = "";
			try {
				Activity a = this.getActivity();
				AssetManager assetManager = a.getAssets();
				InputStream input = assetManager.open("tza.json");
				BufferedReader br = new BufferedReader(new InputStreamReader(input));
				mapData = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return mapData;
		}
		
		@JavascriptInterface
		public int getDistrict() {
			DistrictActivityDr d = (DistrictActivityDr) this.getActivity();
			String districtId = d.getDistrictId();
			Integer distId = Integer.parseInt(districtId);
			
			return distId;
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
}
