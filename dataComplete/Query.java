package com.example.dash_tricks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * this class provides all functions needed for the display
 * @author jianzhao
 *
 */
public class Query {
	private final int YEAR = 2014;
	private SQLiteDatabase database;
	private DataBaseHelper dbHelper;

	
	public Query(Context context){
		dbHelper = new DataBaseHelper(context);
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}

	/**
	 * get  all districts name/id/ coor
	 * @return json format string 
	 */
	public String getAllDistricts(){
		String res = "{\"districts\": [";
		Cursor cur = database.rawQuery("Select " + DistrictTable.NAME + ", " + DistrictTable.ID +  ", "
										+ DistrictTable.COOR + " "
										+ "from " + DistrictTable.TABLENAME,	new String[]{});
		cur.moveToFirst();
		List<String> list = new ArrayList<String>();
		while(!cur.isAfterLast()){
			String coor = "[" + cur.getString(0) + "]";
			String temp = "{ \"name\" : \"" + cur.getString(0) + "\", \"id\" : " + cur.getInt(1) + ", \"coor\": \"" + coor + "\"}";  
			list.add(temp);
			cur.moveToNext();
		}
		cur.close();
		Iterator<String> iter = list.iterator();
		if(iter.hasNext()){
			res += iter.next();
		}
		while(iter.hasNext()){
			res += "," + iter.next(); 
		}
		res += "]}";
		return res;
	}
	/*
	 * Json format
	 * {"districts":[{"name":"district1", "id": 2, "coor": "[ ]"},{},{},{}]}
	 */
	
	

	/**
	 * Immunization Immunization return(vaccine, coverage)(get coverage for each distict coverage = median)
	 * @param districtID
	 * @param month
	 * @return Json format string of vaccine/coverage
	 */
	public String getImmunization(int districtID, int month){
		String m = YEAR + "-" + month;
		String res = "{[";
		Cursor cur = database.rawQuery("Select " + VaccineTable.NAME + ", avg(" + AggVaccineTable.COVERAGE + ") "
										+ "from " + DistrictTable.TABLENAME + ", " + SubDistrictTable.TABLENAME + 
										", " + AggVaccineTable.TABLENAME + ", " + VaccineTable.TABLENAME + " "
										+ "where " + DistrictTable.SUBID + "=" + SubDistrictTable.ID + " and "
										+ SubDistrictTable.FACILITY_ID + " = " + AggVaccineTable.FACILITY_ID + " and " + 
										AggVaccineTable.VACCINE_ID + "= " + VaccineTable.ID + " and " + DistrictTable.ID + " = " + districtID
										+ " " + "group by " + VaccineTable.NAME, new String[] {});
		cur.moveToFirst();
		List<String> list = new ArrayList<String>();
		while(!cur.isAfterLast()){
			String coor = "[" + cur.getString(0) + "]";
			String temp = "{ \"name\" : \"" + cur.getString(0) + "\", \"id\" : " + cur.getInt(1) + ", \"coor\": \"" + coor + "\"}";  
			list.add(temp);
			cur.moveToNext();
		}
		cur.close();
		Iterator<String> iter = list.iterator();
		if(iter.hasNext()){
			res += iter.next();
		}
		while(iter.hasNext()){
			res += "," + iter.next(); 
		}
		res += "]}";
		return res;
	}
	/*
	 * Json format
	 * {"vaccines":[{name:"", "coverage":""}, {},{}]} 
	 */
	
	/**
	 * get DistrictfridgeCapacity given(distric id) return(district name, req capacity, current capaity)
	 * sum the capacity of each fridge in district
	 */
	
	
	/*
	 * Json format
	 * {"sub_district": [{"name" : "sub", "req_capacity":34, "cur_capacity": 34},{},{}]}
	 */
	
	/**
	 * get district stock level given(district id) return(vaccine, level)
	 * sum up the level
	 */
	/*
	 * Json format
	 * {"vaccine":[{"name":"", "level":453},{},{}]}
	 */
	
	/**
	 * cold chain
	 * given(district_id, month) return(district, max_temp, min_temp)
	 * 
	 */
	
	/**
	 * get vaccine coverage rate for given facility given month
	 * @param facility_id
	 * @param month
	 * @return list of pair <Vaccine, coverage> 
	 */
	public List<Pair<String, Integer>> getMonthlyCoverageRate(int facility_id, int month){
		List<Pair<String, Integer>> list = new ArrayList<Pair<String, Integer>>();
		String m = YEAR + "-" + month;
		Cursor cur = database.rawQuery("Select " + VaccineTable.NAME + ", " + AggVaccineTable.COVERAGE + " "
									+ "from " + VaccineTable.TABLENAME + ", " + AggVaccineTable.TABLENAME + " "
									+ "where " + VaccineTable.ID + "=" + AggVaccineTable.VACCINE_ID + " and " + 
									AggVaccineTable.FACILITY_ID + "=" + facility_id + " and " + 
									AggVaccineTable.MONTH + " = '" + m + "'", new String[]{});
		cur.moveToFirst();
		while(!cur.isAfterLast()){
			list.add(new Pair<String, Integer>(cur.getString(0), cur.getInt(1)));
			cur.moveToNext();
		}
		cur.close();
		return list;
	}
	
	/**
	 * get coverage rate for given a vaccine in given facility given month
	 * @param facility_id
	 * @param month
	 * @param vaccine_id
	 * @return
	 */
	public List<Pair<String, Integer>> getVaccineMonthlyRate(int facility_id, int month, int vaccine_id){
		List<Pair<String, Integer>> list = new ArrayList<Pair<String, Integer>>();
		String m = YEAR + "-" + month;
		Cursor cur = database.rawQuery("Select " + VaccineTable.NAME + ", " + AggVaccineTable.COVERAGE + " "
				+ "from " + VaccineTable.TABLENAME + ", " + AggVaccineTable.TABLENAME + " "
				+ "where " + VaccineTable.ID + "=" + AggVaccineTable.VACCINE_ID + " and " + 
				AggVaccineTable.FACILITY_ID + "=" + facility_id + " and " + 
				AggVaccineTable.MONTH + " = '" + m + "' and " + AggVaccineTable.VACCINE_ID + " = " + vaccine_id,
				new String[]{});
		cur.moveToFirst();
		while(!cur.isAfterLast()){
			list.add(new Pair<String, Integer>(cur.getString(0), cur.getInt(1)));
			cur.moveToNext();
		}
		cur.close();
		return list;
		
	}
	
	/**
	 * get coverage rate for all vaccines in give facility in current year
	 * @param facility_id
	 * @return list of Pair<Vaccine name, Pair<month, coverage>>
	 */
	public List<Pair<String, Pair<String, Integer>>> getYearlyCoverageRate(int facility_id){
		List<Pair<String, Pair<String,Integer>>> list = new ArrayList<Pair<String, Pair<String,Integer>>>();
		Cursor cur = database.rawQuery("Select " + VaccineTable.NAME + ", " + AggVaccineTable.COVERAGE + ", " 
										+ AggVaccineTable.MONTH + " " + "from " + VaccineTable.TABLENAME + ", " 
										+ AggVaccineTable.TABLENAME + " " + "where " + VaccineTable.ID + "=" + 
										AggVaccineTable.VACCINE_ID + " and " + AggVaccineTable.FACILITY_ID + "=" 
										+ facility_id,	new String[]{});
		cur.moveToFirst();
		while(!cur.isAfterLast()){
			Pair<String, Integer> month_cover = new Pair<String, Integer>(cur.getString(2), cur.getInt(1));
			list.add(new Pair<String, Pair<String, Integer>>(cur.getString(0), month_cover));
			cur.moveToNext();
		}
		cur.close();
		return list;
	}
	
	/**
	 * get montly stock lever/out for given facility in given month
	 * @param fid facility id
	 * @param month
	 * @return list Pair<Vaccine name, Pair<stock level, stock out>
	 */
	
	public List<Pair<String, Pair<Integer, Integer>>> getMonthlyStock(int facility_id, int month){
		List<Pair<String, Pair<Integer,Integer>>> list = new ArrayList<Pair<String, Pair<Integer,Integer>>>();
		String m = YEAR + "-" + month;
		Cursor cur = database.rawQuery("Select " + VaccineTable.NAME + ", " + AggVaccineTable.STOCK_LEVEL + ", " 
										+ AggVaccineTable.STOCK_OUT + " " + "from " + VaccineTable.TABLENAME + ", " 
										+ AggVaccineTable.TABLENAME + " " + "where " + VaccineTable.ID + "=" + 
										AggVaccineTable.VACCINE_ID + " and " + AggVaccineTable.FACILITY_ID + "=" 
										+ facility_id + " and " + AggVaccineTable.MONTH + " = '" + m + "'",	new String[]{});
		cur.moveToFirst();
		while(!cur.isAfterLast()){
			Pair<Integer, Integer> stock = new Pair<Integer, Integer>(cur.getInt(1), cur.getInt(2));
			list.add(new Pair<String, Pair<Integer, Integer>>(cur.getString(0), stock));
			cur.moveToNext();
		}
		cur.close();
		return list;
		
	}
	
	/**
	 * get year stock level for give facility
	 * @param facility_id
	 * @return list Pair<Vaccine name Pair<Month, stock level>>
	 */
	public String getYearlyStock(int facility_id){
		String res = "[";
		Cursor cur = database.rawQuery("Select " + VaccineTable.NAME + ", " + AggVaccineTable.STOCK_LEVEL +  ", "
										+ AggVaccineTable.MONTH + " "
										+ "from " + VaccineTable.TABLENAME + ", " + AggVaccineTable.TABLENAME + " " 
										+ "where " + VaccineTable.ID + "=" + AggVaccineTable.VACCINE_ID + " and " 
										+ AggVaccineTable.FACILITY_ID + "=" + facility_id ,	new String[]{});
		cur.moveToFirst();
		List<String> list = new ArrayList<String>();
		while(!cur.isAfterLast()){
			String temp = "{ \"name\" : \"" + cur.getString(0) + "\", \"level\" : " + cur.getInt(1) + ", \"out\": " + cur.getInt(2) + "}";  
			list.add(temp);
			cur.moveToNext();
		}
		cur.close();
		Iterator<String> iter = list.iterator();
		if(iter.hasNext()){
			res += iter.next();
		}
		while(iter.hasNext()){
			res += "," + iter.next(); 
		}
		res += "]";
		return res;
	}
/*return Json format String 
 * [{"name":"Vaccine1", "level": 2, "out": 3}, {},{}]
 */
	
	
}
