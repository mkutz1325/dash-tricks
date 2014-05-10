package com.example.dash_tricks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
		res += buildString(list) + "]}";
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
		String m = String.format("'%d-%d'", YEAR, month);
		String res = "{\"immunization\" : [";
		
		Cursor cur = database.rawQuery("Select v." + VaccineTable.NAME + ", avg(a." + AggVaccineTable.COVERAGE + ") " + ", a." + AggVaccineTable.MONTH +" "
										+ "from " + DistrictTable.TABLENAME + " as d, " + SubDistrictTable.TABLENAME + 
										" as s, " + AggVaccineTable.TABLENAME + " as a, " + VaccineTable.TABLENAME + " as v "
										+ "where d." + DistrictTable.SUBID + "= s." + SubDistrictTable.ID + " and s."
										+ SubDistrictTable.FACILITY_ID + " = a." + AggVaccineTable.FACILITY_ID + " and a." + 
										AggVaccineTable.VACCINE_ID + "= v." + VaccineTable.ID + " and a." +
										AggVaccineTable.MONTH + " = " + m + " and d." +DistrictTable.ID + " = " + districtID
										+ " " + "group by v." + VaccineTable.NAME, new String[] {});
										
		
		List<String> list = new ArrayList<String>();
		cur.moveToFirst();
		while(!cur.isAfterLast()){
			String temp = "{ \"name\" : \"" + cur.getString(0) + "\", \"coverage\" : " + cur.getInt(1) + "}";  
			list.add(temp);			
			Log.e("Query.java ", temp);
			cur.moveToNext();
		}
		cur.close();

		res += buildString(list) + "]}";
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
	public String getDistrictFridgeCapacity(int districtID){
		String res = "{\"sub_district\":[";
		Cursor cur = database.rawQuery("Select d." + DistrictTable.NAME + ", sum(a." + AggCapacityTable.REQUIRED_CAPA + ") " +
										", sum(a." + AggCapacityTable.CURR_CAPA + ") "
										+ "from " + DistrictTable.TABLENAME + " as d, " + SubDistrictTable.TABLENAME + 
										" as s, " + AggCapacityTable.TABLENAME  + " as a "
										+ "where d." + DistrictTable.SUBID + "= s." + SubDistrictTable.ID + " and s."
										+ SubDistrictTable.FACILITY_ID + " = a." + AggCapacityTable.FACILITY_ID + " and d." + 
										 DistrictTable.ID + " = " + districtID
										+ " " + "group by d." + DistrictTable.NAME, new String[] {});
		cur.moveToFirst();
		List<String> list = new ArrayList<String>();
		while(!cur.isAfterLast()){
			String temp = "{ \"name\" : \"" + cur.getString(0) + "\", \"required_capacity\" : " + cur.getInt(1) + 
								", \"current_capaity\": " + cur.getInt(2) + "}";  
			list.add(temp);
			cur.moveToNext();
		}
		cur.close();
	
		res += buildString(list) + "]}";
		return res;
	}
	
	/*
	 * Json format
	 * {"sub_district": [{"name" : "sub", "req_capacity":34, "cur_capacity": 34},{},{}]}
	 */
	
	/**
	 * get district stock level given(district id) return(vaccine, level)
	 * sum up the level
	 */
	/**
	 * get district stock level
	 * @param districtID
	 * @return  Jason format string vaccine/ level
	 */
	public String getDistrictStockLevel(int districtID){
		String res = "{\"vaccine_level\":[";
		Cursor cur = database.rawQuery("Select d." + DistrictTable.NAME + ", sum(a." + AggVaccineTable.STOCK_LEVEL + ") " + " "
										+ "from " + DistrictTable.TABLENAME + " as d, " + SubDistrictTable.TABLENAME + " as s, " + AggVaccineTable.TABLENAME
										+ " as a " + "where d." + DistrictTable.SUBID + "= s." + SubDistrictTable.ID + " and s."
										+ SubDistrictTable.FACILITY_ID + " = a." + AggVaccineTable.FACILITY_ID + " and d." + 
										 DistrictTable.ID + " = " + districtID
										+ " " + "group by d." + DistrictTable.NAME, new String[] {});
		cur.moveToFirst();
		List<String> list = new ArrayList<String>();
		while(!cur.isAfterLast()){
			String temp = "{ \"name\" : \"" + cur.getString(0) + "\", \"level\" : " + cur.getInt(1) + "}";  
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
	 * @return 
	 */
	public String getMonthlyCoverageRate(int facility_id, int month){
		String res = "[";
		String m = YEAR + "-" + month;
		Cursor cur = database.rawQuery("Select v." + VaccineTable.NAME + ", a." + AggVaccineTable.COVERAGE + " "
									+ "from " + VaccineTable.TABLENAME + " as v, " + AggVaccineTable.TABLENAME + " as a "
									+ "where v." + VaccineTable.ID + "= a." + AggVaccineTable.VACCINE_ID + " and a." + 
									AggVaccineTable.FACILITY_ID + "= " + facility_id + " and a." + 
									AggVaccineTable.MONTH + " = '" + m + "'", new String[]{});
		cur.moveToFirst();
		List<String> list = new ArrayList<String>();
		while(!cur.isAfterLast()){
			list.add("{\"facility_id\" : " + cur.getString(0) + ", \"month\" :" + cur.getInt(1) + "}");
			cur.moveToNext();
		}
		cur.close();
		res += buildString(list) + "]";
		return res;
	}
	
	/**
	 * get coverage rate for given a vaccine in given facility given month
	 * @param facility_id
	 * @param month
	 * @param vaccine_id
	 * @return
	 */
	public String getVaccineMonthlyRate(int facility_id, int month, int vaccine_id){
		String res = "[";
		String m = YEAR + "-" + month;
		Cursor cur = database.rawQuery("Select v." + VaccineTable.NAME + ", a." + AggVaccineTable.COVERAGE + " "
				+ "from " + VaccineTable.TABLENAME + " as v, " + AggVaccineTable.TABLENAME + " as a "
				+ "where v." + VaccineTable.ID + "= a." + AggVaccineTable.VACCINE_ID + " and a." + 
				AggVaccineTable.FACILITY_ID + "= " + facility_id + " and a." + 
				AggVaccineTable.MONTH + " = '" + m + "' and a." + AggVaccineTable.VACCINE_ID + " = " + vaccine_id,
				new String[]{});
		cur.moveToFirst();
		List<String> list = new ArrayList<String>();
		while(!cur.isAfterLast()){
			list.add("{\"name\" : " + cur.getString(0) + ", \"coverage\": " + cur.getInt(1) + "}");
			cur.moveToNext();
		}
		cur.close();
		res += buildString(list) + "]";
		return res;
		
	}
	
	/**
	 * get coverage rate for all vaccines in give facility in current year
	 * @param facility_id
	 * @return list of Pair<Vaccine name, Pair<month, coverage>>
	 */
	public String getYearlyCoverageRate(int facility_id){
		String res = "[";
		Cursor cur = database.rawQuery("Select v." + VaccineTable.NAME + ", a." + AggVaccineTable.COVERAGE + ", a." 
										+ AggVaccineTable.MONTH + " " + "from " + VaccineTable.TABLENAME + " as v, " 
										+ AggVaccineTable.TABLENAME + " as a " + "where v." + VaccineTable.ID + "= a." + 
										AggVaccineTable.VACCINE_ID + " and a." + AggVaccineTable.FACILITY_ID + "=" 
										+ facility_id,	new String[]{});
		List<String> list = new ArrayList<String>();
		cur.moveToFirst();
		while(!cur.isAfterLast()){
			list.add("{\"name\" : \"" + cur.getString(0) + "\", \"month\": \"" + cur.getString(2) 
							+ "\", \"coverage\" : " + cur.getInt(1) + "}");
			cur.moveToNext();
		}
		cur.close();
		res += buildString(list) + "]";
		return res;
	}
	
	/**
	 * get montly stock lever/out for given facility in given month
	 * @param fid facility id
	 * @param month
	 * @return list Pair<Vaccine name, Pair<stock level, stock out>
	 */
	
	public String getMonthlyStock(int facility_id, int month){
		String res = "[";
		String m = YEAR + "-" + month;
		Cursor cur = database.rawQuery("Select v." + VaccineTable.NAME + ", a." + AggVaccineTable.STOCK_LEVEL + ", a." 
										+ AggVaccineTable.STOCK_OUT + " " + "from " + VaccineTable.TABLENAME + " as v, " 
										+ AggVaccineTable.TABLENAME + " as a " + "where v." + VaccineTable.ID + "= a." + 
										AggVaccineTable.VACCINE_ID + " and a." + AggVaccineTable.FACILITY_ID + "=" 
										+ facility_id + " and a." + AggVaccineTable.MONTH + " = '" + m + "'",	new String[]{});
		List<String> list = new ArrayList<String>();
		cur.moveToFirst();
		while(!cur.isAfterLast()){
			list.add("{\"name\" : \"" + cur.getString(0) + "\", \"level\" : " + cur.getInt(1) + 
					", \"out\" : " + cur.getInt(2) + "}");
			cur.moveToNext();
		}
		cur.close();
		res += buildString(list) + "]";
		return res;
		
	}
	
	/**
	 * get year stock level for give facility
	 * @param facility_id
	 * @return list Pair<Vaccine name Pair<Month, stock level>>
	 */
	public String getYearlyStock(int facility_id){
		String res = "[";
		Cursor cur = database.rawQuery("Select v." + VaccineTable.NAME + ", a." + AggVaccineTable.STOCK_LEVEL +  ", a."
										+ AggVaccineTable.MONTH + " "
										+ "from " + VaccineTable.TABLENAME + " as v, " + AggVaccineTable.TABLENAME + " as a " 
										+ "where v." + VaccineTable.ID + "= a." + AggVaccineTable.VACCINE_ID + " and a." 
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
	
	private String buildString(List<String> list){
		Iterator<String> iter = list.iterator();
		String res = "";
		if(iter.hasNext()){
			res += iter.next();
		}
		while(iter.hasNext()){
			res += "," + iter.next(); 
		}
		return res;
	}
}
