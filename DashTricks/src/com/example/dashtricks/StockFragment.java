package com.example.dashtricks;
 
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.example.dashtricks.data.Query;

public class StockFragment extends Fragment {
 
	
	public StockFragment() {
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
    	//this.getActivity().setContentView(R.layout.fragment_coverage);
    	// load the parent view
        View rootView = inflater.inflate(R.layout.fragment_stock, container, false);
        // get the appropriate child webview from the parent
        WebView myWebView = (WebView) rootView.findViewById(R.id.webView2);
        // enable javascript
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.addJavascriptInterface(this, "android");
        // load the appropriate webpage from the assets folder
        myWebView.loadUrl("file:///android_asset/map.html");
        
        return rootView;
    }
    
    char esc = '"';
    String data = "[{name: " + esc + "BCG" + esc + ",value: 97},{name: " + esc + "OPV-0" + esc + ",value: 50}]";
	/** This passes our data out to the JS */
	@JavascriptInterface
	public String getData() {
		
//		Query q = new Query(this.getActivity());
//		q.open();
//		String districts = q.getAllDistricts();
//		q.close();
//		return districts;

//		File f = new File("file:///android_asset/districtmap.json");
		String string = new String();
		try {
			Activity a = this.getActivity();
			AssetManager assetManager = a.getAssets();
			//String[] files = assetManager.list("");
			InputStream input = assetManager.open("tza.json");
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			string = br.readLine();
			return string;
			//Oreturn s.hasNext() ? s.next() : "";
/*			while (s.hasNext()) {
				
				//string.concat(newLine);
			}*/
/*			s.close();
			return string;*/
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			String message = e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			String message = e.getMessage();
			e.printStackTrace();
		}
		return string;
	}
}