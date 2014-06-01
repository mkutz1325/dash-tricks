package com.example.dashtricks;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

public class DistrictFragment extends Fragment {

	   @Override
	   public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	 
	    	View rootView = inflater.inflate(R.layout.fragment_district, container, false);
	        TextView baseTextView = (TextView) rootView.findViewById(R.id.districtText);
	        
			DistrictActivityDr d = (DistrictActivityDr) this.getActivity();
			String districtId = d.getDistrictId();
			Integer distId = Integer.parseInt(districtId);
			
	        baseTextView.setText("District " + distId);
	        
	        return rootView;
	    }
}
