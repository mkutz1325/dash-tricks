package com.example.dashtricks;

import android.app.Application;

import com.example.dashtricks.data.Query;

public class GlobalState extends Application {

	public Query query;
	
	public Query getQuery() {
		return query;
	}
	
	public void setQuery(Query q) {
		query = q;
	}
}
