package com.example.dashtricks;
 
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
 
public class ColdChainFragment extends Fragment {
	
	private WebView mWebView;
	
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
	
	public ColdChainFragment() {
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
    	View rootView = inflater.inflate(R.layout.fragment_cold_chain, container, false);
        WebView mainWebView = (WebView) rootView.findViewById(R.id.webView3a);
        mainWebView.addJavascriptInterface(this, "android");
        mainWebView.getSettings().setJavaScriptEnabled(true);
        // load the appropriate webpage from the assets folder
        mainWebView.loadUrl("file:///android_asset/linegraph.html");
        
/*		Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner1);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
		        R.array.explore_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(mSpinnerListener);*/
        
        
        return rootView;
    }
    
    private void loadDataToWebView(int pos) {
    	
    }
    
    char esc = '"';
    String data = "[{name: " + esc + "BCG" + esc + ",value: 97},{name: " + esc + "OPV-0" + esc + ",value: 50}]";
	/** This passes our data out to the JS */
	@JavascriptInterface
	public String getData() {
		//Log.d(TAG, "getData() called");
	    // String minTemps = Data.getMinTemps(districtId);
		// String maxTemps = Data.getMaxTemps(districtId);
		return data;
	}
}