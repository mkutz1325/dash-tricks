package com.example.dashtricks;

import com.example.dashtricks.data.Query;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

public class QueryService extends IntentService {

	public QueryService() {
		super("QueryService");
		// TODO Auto-generated constructor stub
	}

	private String districtId;
	private String districtName;

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();

		if (extras != null) {
		    districtId = extras.getString("Id");
		    districtName = extras.getString("Name");
		}
		Integer distId = Integer.parseInt(districtId);
		
		GlobalState state = (GlobalState) this.getApplicationContext();
		Query q = state.getQuery();
		q.getImmunization(distId);
/*		for (int vaccId=0; vaccId<=7; vaccId++) {
			q.getMonthlyVaccCover(distId, vaccId);
		}*/
	}

}
