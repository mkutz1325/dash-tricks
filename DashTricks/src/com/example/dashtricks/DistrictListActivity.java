package com.example.dashtricks;

import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.app.LoaderManager;

/**
 * An activity representing a list of Districts. 

 * This activity also implements the required
 * {@link DistrictListFragment.Callbacks} interface to listen for item selections.
 */
public class DistrictListActivity extends FragmentActivity 
									implements DistrictListFragment.Callbacks,
									LoaderManager.LoaderCallbacks<Cursor>{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_district_list);
		getLoaderManager().initLoader(0, null, this);

	}

	/**
	 * Callback method from {@link MetricListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		Intent detailIntent = new Intent(this, MainActivity.class);
		startActivity(detailIntent);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		
	}
}