package com.example.dashtricks;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.example.dashtricks.data.DistrictList;
import com.example.dashtricks.data.Query;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * An activity representing a list of Districts. 

 * This activity also implements the required
 * {@link DistrictListFragment.Callbacks} interface to listen for item selections.
 */
public class DistrictListActivity extends FragmentActivity implements DistrictListFragment.Callbacks {
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
//	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Query q = new Query(this);
		q.open();
		String districts = q.getAllDistricts();
		DistrictList.setJText(districts);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_district_list);



/*		if (findViewById(R.id.metric_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((MetricListFragment) getSupportFragmentManager().findFragmentById(
					R.id.metric_list)).setActivateOnItemClick(true);
		}*/

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link MetricListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
//		if (mTwoPane) {
//			// In two-pane mode, show the detail view in this activity by
//			// adding or replacing the detail fragment using a
//			// fragment transaction.
//			Bundle arguments = new Bundle();
//			arguments.putString(MetricDetailFragment.ARG_ITEM_ID, id);
//			MetricDetailFragment fragment = new MetricDetailFragment();
//			fragment.setArguments(arguments);
//			getSupportFragmentManager().beginTransaction()
//					.replace(R.id.metric_detail_container, fragment).commit();
//
//		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, MainActivity.class);
			//detailIntent.putExtra(MetDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		//}
	}
}