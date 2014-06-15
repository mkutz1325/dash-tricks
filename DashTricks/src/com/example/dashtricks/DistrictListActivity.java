package com.example.dashtricks;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.dashtricks.data.Query;

/**
 * An activity representing a list of Districts. 

 * This activity also implements the required
 * {@link DistrictListFragment.Callbacks} interface to listen for item selections.
 */
public class DistrictListActivity extends FragmentActivity 
									implements DistrictListFragment.Callbacks,
									LoaderManager.LoaderCallbacks<Cursor>{

	public Query q;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		q = new Query(this);
		q.open();
		GlobalState state = (GlobalState) getApplicationContext();
		state.setQuery(q);
		
		setContentView(R.layout.activity_district_list);
	}

	/**
	 * Callback method from {@link DistrictListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id, String name) {
		Intent detailIntent = new Intent(this, DistrictActivity.class);
		detailIntent.putExtra("Id", id);
		detailIntent.putExtra("Name", name);
		startActivity(detailIntent);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}
}