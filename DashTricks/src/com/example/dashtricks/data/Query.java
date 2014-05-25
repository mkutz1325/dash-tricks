package com.example.dashtricks.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

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
	
	String allDistrict = "";
	HashMap<Integer, String> subDistrict = new HashMap<Integer, String>();
	
	HashMap<String, String> immu = new HashMap<String, String>();
	
	HashMap<Integer, String> distCapa = new HashMap<Integer, String>();
	
	HashMap<Integer, String> distStock = new HashMap<Integer, String>();

	HashMap<String, String> distCover = new HashMap<String, String>();
	
	HashMap<String, String> subCover = new HashMap<String, String>();
	
	HashMap<String, String> monCover = new HashMap<String, String>();
	
	HashMap<String, String> vaccMon = new HashMap<String, String>();
	
	HashMap<Integer, String> yearCover = new HashMap<Integer, String>();
	
	HashMap<Integer, String> yearStock = new HashMap<Integer, String>();
	
	HashMap<String, String> monStock = new HashMap<String, String>();
	
	/**
	 * get  all districts name/id/ coor
	 * @return json format string 
	 */
	public String getAllDistricts(){
		Log.v("jian", "Start");
		if(allDistrict.length() > 0){
			return allDistrict;
		}
		String res = "{\"districts\": [";
		Cursor cur = database.rawQuery("Select " + DistrictTable.NAME + ", " + DistrictTable.ID +  ", "
										+ DistrictTable.COOR + " "
										+ "from " + DistrictTable.TABLENAME,	new String[]{});
		cur.moveToFirst();
		List<String> list = new ArrayList<String>();
		while(!cur.isAfterLast()){
			String temp = "{ \"name\" : \"" + cur.getString(0) + "\", \"id\" : " + cur.getInt(1) + ", \"coor\": \"" + cur.getString(2) + "\"}";  
			list.add(temp);
			cur.moveToNext();
		}
		cur.close();
		res += buildString(list) + "]}";
		Log.v("jian", "end");
		allDistrict = res;
		return res;
	}
	/*
	 * Json format
	 * {"districts":[{"name":"district1", "id": 2, "coor": "[ ]"},{},{},{}]}
	 *
	 *
	 */

	/**
	 * get all subDistrict by given district id
	 * @param distID
	 * @return json format string {"subDistricts":[{"name":"subdistrict1", "id": 2, "coor": "[ ]"},{},{},{}]}
	 */
	
	public String getAllSubDistricts(int distID){
		Log.v("jian", "Start");
		if(subDistrict.containsKey(distID)){
			return immu.get(distID);
		}
		String res = "{\"subDistricts\": [";
		Cursor cur = database.rawQuery("Select " + SubDistrictTable.NAME + ", " + SubDistrictTable.ID +  ", "
										+ SubDistrictTable.COOR + " " + "from " + SubDistrictTable.TABLENAME + " "
										+ "where " + SubDistrictTable.DISTRICTID + "=" + distID + " ",	new String[]{});
		cur.moveToFirst();
		List<String> list = new ArrayList<String>();
		while(!cur.isAfterLast()){
			String temp = "{ \"name\" : \"" + cur.getString(0) + "\", \"id\" : " + cur.getInt(1) + ", \"coor\": \"" + cur.getString(2) + "\"}";  
			list.add(temp);
			cur.moveToNext();
		}
		cur.close();
		res += buildString(list) + "]}";
		Log.v("jian", "end");
		allDistrict = res;
		return res;
	}
	
	

	/**
	 * Immunization Immunization return(vaccine, coverage)(get coverage for each distict coverage = median)
	 * @param districtID
	 * @param month
	 * @return Json format string of vaccine/coverage
	 */
	public String getImmunization(int districtID, int month){
		Log.v("jian", "Start");
		String key = districtID + ", " + month;
		if(immu.containsKey(key)){
			return immu.get(key);
		}
		String m = YEAR + "-" + month;
		String res = "{\"immunization\" : [";

		Cursor cur = database.rawQuery("Select v." + VaccineTable.NAME + ", avg(a." + AggVaccineTable.COVERAGE + ") " + " "
								+ "from "  + SubDistrictTable.TABLENAME + " as s, " +  FacilityTable.TABLENAME + " as f, "
								+ AggVaccineTable.TABLENAME + " as a, " + VaccineTable.TABLENAME + " as v "
								+ "where s." + SubDistrictTable.ID + "= f." + FacilityTable.SUBID + 
									" and f." + FacilityTable.ID + " = a." + AggVaccineTable.FACILITY_ID + " and a." + 
								AggVaccineTable.VACCINE_ID + "= v." + VaccineTable.ID + " and a." +
								AggVaccineTable.MONTH + " = '" + m + "' and s." + SubDistrictTable.DISTRICTID + " = " + districtID
								+ " " + "group by v." + VaccineTable.NAME, new String[] {});
			
		
		List<String> list = new ArrayList<String>();
		cur.moveToFirst();
		while(!cur.isAfterLast()){
			String temp = "{ \"name\" : \"" + cur.getString(0) + "\", \"coverage\" : " + cur.getInt(1) + "}";  
			list.add(temp);			
			//Log.e("Query.java ", temp);
			cur.moveToNext();
		}
		cur.close();

		res += buildString(list) + "]}";
		Log.v("jian", "End");
		immu.put(key, res);
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
		Log.v("jian", "Start");
		if(distCapa.containsKey(districtID)){
			return distCapa.get(districtID);
		}
		String res = "{\"fridge_capacity\":[";
		Cursor cur = database.rawQuery("Select d." + DistrictTable.NAME + ", sum(a." + AggCapacityTable.REQUIRED_CAPA + ") " +
										", sum(a." + AggCapacityTable.CURR_CAPA + ") "
										+ "from " + DistrictTable.TABLENAME + " as d, " + SubDistrictTable.TABLENAME + 
										" as s, "  + FacilityTable.TABLENAME + " as f, "+ AggCapacityTable.TABLENAME  + " as a "
										+ "where d." + DistrictTable.ID + " = " + districtID + 
										" and d." +  DistrictTable.ID + "= s." + SubDistrictTable.DISTRICTID + 
										" and s." + SubDistrictTable.ID + " = f." + FacilityTable.SUBID + 
										" and f." + FacilityTable.ID + " = a." + AggCapacityTable.FACILITY_ID + 
										" " + "group by d." + DistrictTable.NAME, new String[] {});
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
		Log.v("jian", "End");
		distCapa.put(districtID, res);
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
		Log.v("Jian Query","Start");
		if(distStock.containsKey(districtID)){
			return distStock.get(districtID);
		}
		String res = "{\"vaccine_level\":[";
		Cursor cur = database.rawQuery("Select d." + DistrictTable.NAME + ", sum(a." + AggVaccineTable.STOCK_LEVEL + ") " + " "
										+ "from " + DistrictTable.TABLENAME + " as d, " + SubDistrictTable.TABLENAME + " as s, " + AggVaccineTable.TABLENAME
										+ " as a, " + FacilityTable.TABLENAME + " as f "
										+ "where d." + DistrictTable.ID + "= s." + SubDistrictTable.DISTRICTID + " and s."
										+ SubDistrictTable.ID + "= f." + FacilityTable.SUBID + " and f." + FacilityTable.ID+ " = a." + AggVaccineTable.FACILITY_ID + " and d." + 
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
		res += buildString(list) + "]}";
		Log.v("Jian Query","End");
		distStock.put(districtID, res);
		return res;
	}
	
	
	/**
	 * get minimum coverage rate for given vaccine of subdistrict in given district
	 * @param vID
	 * @param distID
	 * @return jason fomart of string 
	 * {"district_coverage" : [{"vaccine_name":"", "sub_district": "", "coverage": }{}{}{}]}
	 */
	
	public String getDistrictCoverage(int vID, int districtID){
		Log.v("jian", "Start");
		String key = vID + ", " + districtID;
		if(distCover.containsKey(key)){
			return distCover.get(key);
		}
		String res = "{\"distrcit_coverage\": [";
		Cursor cur = database.rawQuery("Select v." + VaccineTable.NAME + ", s." + SubDistrictTable.NAME + ",  min(a." + AggVaccineTable.COVERAGE + ") " + " "
				+ "from " + DistrictTable.TABLENAME + " as d, " + SubDistrictTable.TABLENAME + " as s, " + AggVaccineTable.TABLENAME
				+ " as a, " + FacilityTable.TABLENAME + " as f, " + VaccineTable.TABLENAME + " as v " 
				+ "where d." + DistrictTable.ID + "= s." + SubDistrictTable.DISTRICTID + " and s."
				+ SubDistrictTable.ID + "= f." + FacilityTable.SUBID + " and f." + FacilityTable.ID+ " = a." + AggVaccineTable.FACILITY_ID + " and d." + 
				 DistrictTable.ID + " = " + districtID + " and a." + AggVaccineTable.VACCINE_ID + " = " + vID + " and a."+ AggVaccineTable.VACCINE_ID + " = v."
				 + VaccineTable.ID
				+ " " + "group by s." + SubDistrictTable.NAME + ", v." + VaccineTable.NAME, new String[] {});
		cur.moveToFirst();
		List<String> list = new ArrayList<String>();
		while(!cur.isAfterLast()){
			String temp = "{ \"vaccine_name\" : \"" + cur.getString(0) + "\", \"subDistrict_name\" : \"" + cur.getString(1) + "\", \"min_coverage\" :" + cur.getInt(2) + "}";  
			list.add(temp);
			cur.moveToNext();
		}
		cur.close();
		res += buildString(list) + "]}";
		Log.v("jian", "end");
		distCover.put(key, res);
		return res;
	}
	
	public String getSubDistrictCoverage(int vID, int subID){
		Log.v("jian", "Start");
		String key = vID + ", " + subID;
		if(subCover.containsKey(key)){
			return subCover.get(key);
		}
		String res = "{\"subDistrcit_coverage\": [";
		Cursor cur = database.rawQuery("Select v." + VaccineTable.NAME + ", s." + FacilityTable.NAME + ", avg(a. " + AggVaccineTable.COVERAGE  + ") "
				+ "from " + SubDistrictTable.TABLENAME + " as s, " + AggVaccineTable.TABLENAME
				+ " as a, " + FacilityTable.TABLENAME + " as f, " + VaccineTable.TABLENAME + " as v " 
				+ "where s." + SubDistrictTable.ID + "= f." + FacilityTable.SUBID + " and f." + FacilityTable.ID+ " = a." + AggVaccineTable.FACILITY_ID + " and s." + 
				 SubDistrictTable.ID + " = " + subID + " and a." + AggVaccineTable.VACCINE_ID + " = " + vID + " and a."+ AggVaccineTable.VACCINE_ID + " = v."
				 + VaccineTable.ID
				+ " " + "group by f." + FacilityTable.NAME + ", v." + VaccineTable.NAME, new String[] {});
		cur.moveToFirst();
		List<String> list = new ArrayList<String>();
		while(!cur.isAfterLast()){
			String temp = "{ \"vaccine_name\" : \"" + cur.getString(0) + "\", \"facility_name\" : \"" + cur.getString(1) + "\", \"coverage\" :" + cur.getInt(2) + "}";  
			list.add(temp);
			cur.moveToNext();
		}
		cur.close();
		res += buildString(list) + "]}";
		Log.v("jian", "end");
		subCover.put(key, res);
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
		Log.v("jian", "Start");
		String key = facility_id + ", " + month;
		if(monCover.containsKey(key)){
			return monCover.get(key);
		}
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
		Log.v("jian", "end");
		monCover.put(key, res);
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
		Log.v("jian", "Start");
		String key = facility_id + ", " + month + ", " + vaccine_id;
		if(vaccMon.containsKey(key)){
			return vaccMon.get(key);
		}
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
		Log.v("jian", "end");
		vaccMon.put(key, res);
		return res;
		
	}
	
	/**
	 * get coverage rate for all vaccines in give facility in current year
	 * @param facility_id
	 * @return list of Pair<Vaccine name, Pair<month, coverage>>
	 */
	public String getYearlyCoverageRate(int facility_id){
		Log.v("jian", "Start");
		if(yearCover.containsKey(facility_id)){
			return yearCover.get(facility_id);
		}
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
		Log.v("jian", "end");
		yearCover.put(facility_id, res);
		return res;
	}
	
	/**
	 * get montly stock lever/out for given facility in given month
	 * @param fid facility id
	 * @param month
	 * @return list Pair<Vaccine name, Pair<stock level, stock out>
	 */
	
	public String getMonthlyStock(int facility_id, int month){
		Log.v("jian", "Start");
		String key = facility_id + ", " + month;
		if(monStock.containsKey(key)){
			return monStock.get(key);
		}
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
		Log.v("jian", "end");
		monStock.put(key, res);
		return res;
		
	}
	
	/**
	 * get year stock level for give facility
	 * @param facility_id
	 * @return list Pair<Vaccine name Pair<Month, stock level>>
	 */
	public String getYearlyStock(int facility_id){
		Log.v("jian", "Start");
		if(yearStock.containsKey(facility_id)){
			return yearStock.get(facility_id);
		}
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
		Log.v("jian", "end");
		yearStock.put(facility_id, res);
		return res;
	}
/*return Json format String 
 * [{"name":"Vaccine1", "level": 2, "out": 3}, {},{}]
 */
	
	private String buildString(List<String> list){//use string builder
		Iterator<String> iter = list.iterator();
		StringBuilder res = new StringBuilder();
		if(iter.hasNext()){
			res.append(iter.next());
		}
		while(iter.hasNext()){
			res.append("," + iter.next()); 
		}
		return res.toString();
	}
}
