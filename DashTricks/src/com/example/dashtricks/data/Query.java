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
import android.database.sqlite.SQLiteQueryBuilder;
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
	
	HashMap<Integer, String> immuByVacc = new HashMap<Integer, String>();
	
	HashMap<Integer, String> distCapa = new HashMap<Integer, String>();
	
	HashMap<Integer, String> distStock = new HashMap<Integer, String>();

	HashMap<String, String> distCover = new HashMap<String, String>();
	
	HashMap<String, String> subCover = new HashMap<String, String>();
	
	HashMap<String, String> monCover = new HashMap<String, String>();
	
	HashMap<String, String> vaccMon = new HashMap<String, String>();
	
	HashMap<Integer, String> yearCover = new HashMap<Integer, String>();
	
	HashMap<Integer, String> yearStock = new HashMap<Integer, String>();
	
	HashMap<String, String> monStock = new HashMap<String, String>();
	
	HashMap<String, String > subCoverMonth = new HashMap<String, String>();
	
	HashMap<String, String> getVaccCoverDist = new HashMap<String, String>();
	SQLiteQueryBuilder query = new SQLiteQueryBuilder();
	
	
	/*public String getAcount(){
		 Cursor cur = database.rawQuery("Select count(*)" + " "
		+ "from " + AggVaccineTable.TABLENAME,	new String[]{});

	String res = "";
	
	cur.moveToFirst();
	List<String> list = new ArrayList<String>();
	while(!cur.isAfterLast()){
	String temp = "{" + cur.getInt(0) + "}";  
	list.add(temp);
	cur.moveToNext();
}
cur.close();
res += buildString(list) + "]}";
Log.v("jian", "end");
allDistrict = res;
return res;
		
	}*/
	
	/**
	 * get  all districts name/id/ coor
	 * @return json format string 
	 */
	public String getAllDistricts(){
		Log.v("jian", "Start");
		if(allDistrict.length() > 0){
			Log.v("jian", "End");
			return allDistrict;
		}
		String res = "{\"districts\": [";
		 /*Cursor cur = database.rawQuery("Select " + DistrictTable.NAME + ", " + DistrictTable.ID +  ", "
										+ DistrictTable.COOR + " "
										+ "from " + DistrictTable.TABLENAME,	new String[]{});
		*/
		query.setTables(DistrictTable.TABLENAME);
		String[] column = {DistrictTable.TABLENAME + "." + DistrictTable.NAME, DistrictTable.TABLENAME + "." + DistrictTable.ID, 
				DistrictTable.TABLENAME + "." + DistrictTable.COOR};
		//Cursor cur = database.query(DistrictTable.TABLENAME,column, null, null, null, null, null);
		Cursor cur = query.query(database, column, null, null, null, null, null);
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
	 */
	
	/**
	 * get all subDistrict by given district id
	 * @param distID
	 * @return json format string {"subDistricts":[{"name":"subdistrict1", "id": 2, "coor": "[ ]"},{},{},{}]}
	 */
	public String getAllSubDistricts(int distID){
		Log.v("jian", "Start");
		if(subDistrict.containsKey(distID)){
			Log.v("jian", "End");
			return subDistrict.get(distID);
		}
		String res = "{\"subDistricts\": [";
		/*Cursor cur = database.rawQuery("Select " + SubDistrictTable.NAME + ", " + SubDistrictTable.ID +  ", "
										+ SubDistrictTable.COOR + " " + "from " + SubDistrictTable.TABLENAME + " "
										+ "where " + SubDistrictTable.DISTRICTID + "=" + distID + " ",	new String[]{});
			*/							
		query.setTables(SubDistrictTable.TABLENAME);
		String[] column = {SubDistrictTable.TABLENAME + "." + SubDistrictTable.NAME, SubDistrictTable.TABLENAME + "." + SubDistrictTable.ID, 
					SubDistrictTable.TABLENAME + "." + SubDistrictTable.COOR};

		Cursor cur = query.query(database, column, null, null, null, null, null);

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
	 * 
	 * Json format
	 * {"vaccines":[{"id": , "name:"", "coverage":""}, {},{}]} 
	 *
	 */
	public String getImmunization(int districtID){
		Log.v("jian", "Start");
		
		if(immuByVacc.containsKey(districtID)){
			Log.v("jian", "End");
			return immuByVacc.get(districtID);
		}
		
		String res = "{\"immunization\" : [";

		/*Cursor cur = database.rawQuery("Select v." + VaccineTable.NAME + ", avg(a." + AggVaccineTable.COVERAGE + ") " + " "
								+ "from "  + SubDistrictTable.TABLENAME + " as s, " +  FacilityTable.TABLENAME + " as f, "
								+ AggVaccineTable.TABLENAME + " as a, " + VaccineTable.TABLENAME + " as v "
								+ "where s." + SubDistrictTable.ID + "= f." + FacilityTable.SUBID + 
									" and f." + FacilityTable.ID + " = a." + AggVaccineTable.FACILITY_ID + " and a." + 
								AggVaccineTable.VACCINE_ID + "= v." + VaccineTable.ID + " and a." +
								AggVaccineTable.MONTH + " = '" + m + "' and s." + SubDistrictTable.DISTRICTID + " = " + districtID
								+ " " + "group by v." + VaccineTable.NAME, new String[] {});
			*/
		query.setTables(SubDistrictTable.TABLENAME + " as s, " +  FacilityTable.TABLENAME + " as f, "
				+ AggVaccineTable.TABLENAME + " as a, " + VaccineTable.TABLENAME + " as v ");
		String[] column = { "v." + VaccineTable.ID, "v." + VaccineTable.NAME, "avg(a." + AggVaccineTable.COVERAGE + ") "};
		String where = "s." + SubDistrictTable.ID + "= f." + FacilityTable.SUBID + 
					" and f." + FacilityTable.ID + " = a." + AggVaccineTable.FACILITY_ID + " and a." + 
					AggVaccineTable.VACCINE_ID + "= v." + VaccineTable.ID + " and s." + SubDistrictTable.DISTRICTID + " = " + districtID;
		String groupby = "v." + VaccineTable.NAME + ", v." + VaccineTable.ID;

		Cursor cur = query.query(database, column, where, null, groupby, null, null);
		
		List<String> list = new ArrayList<String>();
		cur.moveToFirst();
		while(!cur.isAfterLast()){
			String temp = "{ \"id\":" + cur.getInt(0) + ", \"name\" : \"" + cur.getString(1) + "\", \"coverage\" : " + cur.getInt(2) + "}";  
			list.add(temp);			
			//Log.e("Query.java ", temp);
			cur.moveToNext();
		}
		cur.close();

		res += buildString(list) + "]}";
		Log.v("jian", "End");
		immuByVacc.put(districtID, res);
		return res;
	}
	
	
	/**
	 * get monthly coverage rate for given vaccine in given districtID
	 * @param districtID
	 * @param vaccID
	 * @return json format string {"districtName":"", "districID":  ,  "vaccineName": "", "vaccID": , "districtCoverage":[{"month":"", "coerage":}{}{}]}
	 */
	
	public String getMonthlyVaccCover(int districtID, int vaccID){
		Log.v("jian", "Start");
		
		String key = districtID + ", " + vaccID;
		if(getVaccCoverDist.containsKey(key)){
			Log.v("jian", "End");
			return getVaccCoverDist.get(key);
		}
		/*Cursor cur = database.rawQuery("Select d." + DistrictTable.NAME + ", v." + VaccineTable.NAME + ", a." + AggVaccineTable.MONTH + 
				", avg(a." + AggVaccineTable.COVERAGE + ") " + " "
				+ "from "  + DistrictTable.TABLENAME + " as d," + SubDistrictTable.TABLENAME + " as s, " +  FacilityTable.TABLENAME + " as f, "
				+ AggVaccineTable.TABLENAME + " as a, " + VaccineTable.TABLENAME + " as v "
				+ "where d." + DistrictTable.ID + " = " + districtID  + " and d." + DistrictTable.ID + " = s." + SubDistrictTable.DISTRICTID + 
				" and s." + SubDistrictTable.ID + "= f." + FacilityTable.SUBID + 
					" and f." + FacilityTable.ID + " = a." + AggVaccineTable.FACILITY_ID + " and s." + SubDistrictTable.DISTRICTID + " = " + districtID
				+ " " + "group by v." + VaccineTable.NAME + ", d." + DistrictTable.NAME + ", a." + AggVaccineTable.MONTH,  new String[] {});
				*/
		
		query.setTables(DistrictTable.TABLENAME + " as d," + SubDistrictTable.TABLENAME + " as s, " +  FacilityTable.TABLENAME + " as f, "
				+ AggVaccineTable.TABLENAME + " as a, " + VaccineTable.TABLENAME + " as v ");
		String[] column = { "d." + DistrictTable.NAME, "v." + VaccineTable.NAME, "a." + AggVaccineTable.MONTH, "avg(a." + AggVaccineTable.COVERAGE + ") "};
		String where = "d." + DistrictTable.ID + " = " + districtID  + " and d." + DistrictTable.ID + " = s." + SubDistrictTable.DISTRICTID + 
					" and s." + SubDistrictTable.ID + "= f." + FacilityTable.SUBID + 
					" and f." + FacilityTable.ID + " = a." + AggVaccineTable.FACILITY_ID + " and a." + 
					AggVaccineTable.VACCINE_ID + "= v." + VaccineTable.ID + " and s." + SubDistrictTable.DISTRICTID + " = " + districtID + " " + 
					"and a." + AggVaccineTable.VACCINE_ID + " = " + vaccID;
		String groupby = "v." + VaccineTable.NAME + ", d." + DistrictTable.NAME + ", a." + AggVaccineTable.MONTH;

		Cursor cur = query.query(database, column, where, null, groupby, null, null);
		
		List<String> list = new ArrayList<String>();
		cur.moveToFirst();
		String distName = "";
		String vaccName = "";
		while(!cur.isAfterLast()){
			distName = cur.getString(0);
			vaccName = cur.getString(1);
			String temp = "{ \"month\" : \"" + cur.getString(2) + "\", \"coverage\" : " + cur.getInt(3) + "}";  
			list.add(temp);			
			//Log.e("Query.java ", temp);
			cur.moveToNext();
		}
		cur.close();
		String res = "{\"districtName\": \"" + distName + "\", \"districtID\": " + districtID + ", \"vaccineName\": \"" + 
						vaccName + "\", \"vaccID\": " + vaccID + ", \"districtCoverage\": [";
		res += buildString(list) + "]}";
		Log.v("jian", "End");
		getVaccCoverDist.put(key, res);
		return res;

	}
	
	
	/**
	 * get DistrictfridgeCapacity given(distric id) return(district name, req capacity, current capaity)
	 * sum the capacity of each fridge in district
	 *
	 * Json format
	 * {"fridge_capacity": [{"id" : , "name" : "", "req_capacity":34, "cur_capacity": 34},{},{}]}
	 */
	public String getDistrictFridgeCapacity(int districtID){
		Log.v("jian", "Start");
		if(distCapa.containsKey(districtID)){
			Log.v("jian", "End");
			return distCapa.get(districtID);
		}
		String res = "{\"fridge_capacity\":[";
		/*Cursor cur = database.rawQuery("Select d." + DistrictTable.NAME + ", sum(a." + AggCapacityTable.REQUIRED_CAPA + ") " +
										", sum(a." + AggCapacityTable.CURR_CAPA + ") "
										+ "from " + DistrictTable.TABLENAME + " as d, " + SubDistrictTable.TABLENAME + 
										" as s, "  + FacilityTable.TABLENAME + " as f, "+ AggCapacityTable.TABLENAME  + " as a "
										+ "where d." + DistrictTable.ID + " = " + districtID + 
										" and d." +  DistrictTable.ID + "= s." + SubDistrictTable.DISTRICTID + 
										" and s." + SubDistrictTable.ID + " = f." + FacilityTable.SUBID + 
										" and f." + FacilityTable.ID + " = a." + AggCapacityTable.FACILITY_ID + 
										" " + "group by d." + DistrictTable.NAME, new String[] {});*/
		
		query.setTables(DistrictTable.TABLENAME + " as d, " + SubDistrictTable.TABLENAME + 
				" as s, "  + FacilityTable.TABLENAME + " as f, "+ AggCapacityTable.TABLENAME  + " as a ");
		String[] column = {"d." + DistrictTable.NAME, "sum(a." + AggCapacityTable.REQUIRED_CAPA + ") ",
													"sum(a." + AggCapacityTable.CURR_CAPA + ") "};
		String where = "d." + DistrictTable.ID + " = " + districtID + 
						" and d." +  DistrictTable.ID + "= s." + SubDistrictTable.DISTRICTID + 
						" and s." + SubDistrictTable.ID + " = f." + FacilityTable.SUBID + 
						" and f." + FacilityTable.ID + " = a." + AggCapacityTable.FACILITY_ID;
		String groupby = "d." + DistrictTable.NAME;

		Cursor cur = query.query(database, column, where, null, groupby, null, null);
		
		
		
		cur.moveToFirst();
		List<String> list = new ArrayList<String>();
		while(!cur.isAfterLast()){
			String temp = "{ \"id\": " + districtID + ", \"name\" : \"" + cur.getString(0) + "\", \"required_capacity\" : " + cur.getInt(1) + 
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
			Log.v("jian", "End");
			return distStock.get(districtID);
		}
		String res = "{\"vaccine_Stock_level\":[";
		/*Cursor cur = database.rawQuery("Select d." + DistrictTable.NAME + ", sum(a." + AggVaccineTable.STOCK_LEVEL + ") " + " "
										+ "from " + DistrictTable.TABLENAME + " as d, " + SubDistrictTable.TABLENAME + " as s, " + AggVaccineTable.TABLENAME
										+ " as a, " + FacilityTable.TABLENAME + " as f "
										+ "where d." + DistrictTable.ID + "= s." + SubDistrictTable.DISTRICTID + " and s."
										+ SubDistrictTable.ID + "= f." + FacilityTable.SUBID + " and f." + FacilityTable.ID+ " = a." + AggVaccineTable.FACILITY_ID + " and d." + 
										 DistrictTable.ID + " = " + districtID
										+ " " + "group by d." + DistrictTable.NAME, new String[] {});
		*/
		query.setTables(DistrictTable.TABLENAME + " as d, " + SubDistrictTable.TABLENAME + " as s, " + AggVaccineTable.TABLENAME
							+ " as a, " + FacilityTable.TABLENAME + " as f ");
		String[] column = { "d." + DistrictTable.NAME, "d." + DistrictTable.ID, "sum(a." + AggVaccineTable.STOCK_LEVEL + ") "};
		String where = "d." + DistrictTable.ID + "= s." + SubDistrictTable.DISTRICTID + " and s."
										+ SubDistrictTable.ID + "= f." + FacilityTable.SUBID + " and f." + FacilityTable.ID+ " = a." + AggVaccineTable.FACILITY_ID + " and d." + 
										 DistrictTable.ID + " = " + districtID;
		String groupby = "d." + DistrictTable.NAME + ", d." + DistrictTable.ID;

		Cursor cur = query.query(database, column, where, null, groupby, null, null);
		cur.moveToFirst();
		List<String> list = new ArrayList<String>();
		while(!cur.isAfterLast()){
			String temp = "{ \"name\" : \"" + cur.getString(0) + "\", \"districtID\": " + cur.getInt(1) + ", \"level\" : " + cur.getInt(2) + "}";  
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
	 * {"district_coverage" : [{"vaccine_name":"", "vaccine_id": , "subDistrict_name": "", "subDistrict_id": , "coverage": }{}{}{}]}
	 */
	
	public String getDistrictCoverage(int vID, int districtID){
		Log.v("jian", "Start");
		String key = vID + ", " + districtID;
		if(distCover.containsKey(key)){
			Log.v("jian", "End");
			return distCover.get(key);
		}
		String res = "{\"district_coverage\": [";
		/*Cursor cur = database.rawQuery("Select v." + VaccineTable.NAME + ", s." + SubDistrictTable.NAME + ",  min(a." + AggVaccineTable.COVERAGE + ") " + " "
				+ "from " + DistrictTable.TABLENAME + " as d, " + SubDistrictTable.TABLENAME + " as s, " + AggVaccineTable.TABLENAME
				+ " as a, " + FacilityTable.TABLENAME + " as f, " + VaccineTable.TABLENAME + " as v " 
				+ "where d." + DistrictTable.ID + "= s." + SubDistrictTable.DISTRICTID + " and s."
				+ SubDistrictTable.ID + "= f." + FacilityTable.SUBID + " and f." + FacilityTable.ID+ " = a." + AggVaccineTable.FACILITY_ID + " and d." + 
				 DistrictTable.ID + " = " + districtID + " and a." + AggVaccineTable.VACCINE_ID + " = " + vID + " and a."+ AggVaccineTable.VACCINE_ID + " = v."
				 + VaccineTable.ID
				+ " " + "group by s." + SubDistrictTable.NAME + ", v." + VaccineTable.NAME, new String[] {});
		*/
		query.setTables(DistrictTable.TABLENAME + " as d, " + SubDistrictTable.TABLENAME + " as s, " + AggVaccineTable.TABLENAME
					+ " as a, " + FacilityTable.TABLENAME + " as f, " + VaccineTable.TABLENAME + " as v ");
		String[] column = { "v." + VaccineTable.NAME, "s." + SubDistrictTable.NAME, "min(a." + AggVaccineTable.COVERAGE + ") ", "s." + SubDistrictTable.ID};
		String where = "d." + DistrictTable.ID + "= s." + SubDistrictTable.DISTRICTID + " and s."
				+ SubDistrictTable.ID + "= f." + FacilityTable.SUBID + " and f." + FacilityTable.ID+ " = a." + AggVaccineTable.FACILITY_ID + " and d." + 
				 DistrictTable.ID + " = " + districtID + " and a." + AggVaccineTable.VACCINE_ID + " = " + vID + " and a."+ AggVaccineTable.VACCINE_ID + " = v."
				 + VaccineTable.ID;
		String groupby = "s." + SubDistrictTable.NAME + ", v." + VaccineTable.NAME;
		Cursor cur = query.query(database, column, where, null, groupby, null, null);
		
		cur.moveToFirst();
		List<String> list = new ArrayList<String>();
		while(!cur.isAfterLast()){
			String temp = "{ \"vaccine_name\" : \"" + cur.getString(0) + "\", \"vaccine_id\": " + vID + ", \"subDistrict_name\" : \"" 
							+ cur.getString(1) + "\", \"subDistrict_id\": " + cur.getInt(3) + ", \"min_coverage\" :" + cur.getInt(2) + "}";  
			list.add(temp);
			cur.moveToNext();
		}
		cur.close();
		res += buildString(list) + "]}";
		Log.v("jian", "end");
		distCover.put(key, res);
		return res;
	}
	
	/**
	 * get yearly average coverage rate of each facility for give vaccine and sub district
	 * @param vID
	 * @param subID
	 * @return  {"subDistrcit_coverage": ["{ "vaccine_name" : "", "vaccine_id":,"facility_name" :"", "facility_id": "", "coverage" :} {}{}]} 
	 */
	public String getSubDistrictCoverage(int vID, int subID){
		Log.v("jian", "Start");
		String key = vID + ", " + subID;
		if(subCover.containsKey(key)){
			Log.v("jian", "End");
			return subCover.get(key);
		}
		String res = "{\"subDistrcit_coverage\": [";
		/*Cursor cur = database.rawQuery("Select v." + VaccineTable.NAME + ", f." + FacilityTable.NAME + ", avg(a. " + AggVaccineTable.COVERAGE  + ") "
				+ "from " + SubDistrictTable.TABLENAME + " as s, " + AggVaccineTable.TABLENAME
				+ " as a, " + FacilityTable.TABLENAME + " as f, " + VaccineTable.TABLENAME + " as v " 
				+ "where s." + SubDistrictTable.ID + "= f." + FacilityTable.SUBID + " and f." + FacilityTable.ID+ " = a." + AggVaccineTable.FACILITY_ID + " and s." + 
				 SubDistrictTable.ID + " = " + subID + " and a." + AggVaccineTable.VACCINE_ID + " = " + vID + " and a."+ AggVaccineTable.VACCINE_ID + " = v."
				 + VaccineTable.ID
				+ " " + "group by f." + FacilityTable.NAME + ", v." + VaccineTable.NAME, new String[] {});
		*/
		query.setTables(SubDistrictTable.TABLENAME + " as s, " + AggVaccineTable.TABLENAME
						+ " as a, " + FacilityTable.TABLENAME + " as f, " + VaccineTable.TABLENAME + " as v");
	String[] column = { "v." + VaccineTable.NAME,"f." + FacilityTable.NAME, "avg(a. " + AggVaccineTable.COVERAGE  + ") ", "f." + FacilityTable.ID};
	String where = "s." + SubDistrictTable.ID + "= f." + FacilityTable.SUBID + " and f." + FacilityTable.ID+ " = a." + AggVaccineTable.FACILITY_ID + " and s." + 
				 SubDistrictTable.ID + " = " + subID + " and a." + AggVaccineTable.VACCINE_ID + " = " + vID + " and a."+ AggVaccineTable.VACCINE_ID + " = v."
				 + VaccineTable.ID;
	String groupby = "f." + FacilityTable.NAME + ", v." + VaccineTable.NAME + ", f." + FacilityTable.ID;
	Cursor cur = query.query(database, column, where, null, groupby, null, null);
	
		cur.moveToFirst();
		List<String> list = new ArrayList<String>();
		while(!cur.isAfterLast()){
			String temp = "{ \"vaccine_name\" : \"" + cur.getString(0) + "\", \"vaccine_id\": " + vID + ", \"facility_name\" : \"" + cur.getString(1) +
						"\", \"facility_id\": " + cur.getInt(3) + ", \"coverage\" :" + cur.getInt(2) + "}";  
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
	 * find monthly average coverage rate for given vaccine and sub district
	 * @param vacID
	 * @param subID
	 * @return Joson format string {"monthly_subDistrict_coverage":[{"vacName":"name", "subDistrictName":"name", "month": "", "averageRate":  },{},{}]
	 */
	public String getMonthlySubDistrictCoverage(int vacID, int subID){
		Log.v("jian", "Start");
		String key = vacID + ", " + subID;
		if(subCoverMonth.containsKey(key)){
			Log.v("jian", "End");
			return subCover.get(key);
		}
		String res = "{\"monthly_subDistrcit_coverage\": [";
		/*Cursor cur = database.rawQuery("Select v." + VaccineTable.NAME + ", s." + SubDistrictTable.NAME + ", a." + AggVaccineTable.MONTH + ", avg(a. " + AggVaccineTable.COVERAGE  + ") "
				+ "from " + SubDistrictTable.TABLENAME + " as s, " + AggVaccineTable.TABLENAME
				+ " as a, " + FacilityTable.TABLENAME + " as f, " + VaccineTable.TABLENAME + " as v " 
				+ "where s." + SubDistrictTable.ID + "= f." + FacilityTable.SUBID + " and f." + FacilityTable.ID+ " = a." + AggVaccineTable.FACILITY_ID + " and s." + 
				 SubDistrictTable.ID + " = " + subID + " and a." + AggVaccineTable.VACCINE_ID + " = " + vacID + " and a."+ AggVaccineTable.VACCINE_ID + " = v."
				 + VaccineTable.ID
				+ " " + "group by f." + FacilityTable.NAME + ", v." + VaccineTable.NAME + ", a." + AggVaccineTable.MONTH, new String[] {});
		*/
		query.setTables(SubDistrictTable.TABLENAME + " as s, " + AggVaccineTable.TABLENAME
				+ " as a, " + FacilityTable.TABLENAME + " as f, " + VaccineTable.TABLENAME + " as v " );
		String[] column = { "v." + VaccineTable.NAME, "s." + SubDistrictTable.NAME, "a." + AggVaccineTable.MONTH,
								"avg(a. " + AggVaccineTable.COVERAGE  + ") "};
		String where = "s." + SubDistrictTable.ID + "= f." + FacilityTable.SUBID + " and f." + FacilityTable.ID+ " = a." + AggVaccineTable.FACILITY_ID + " and s." + 
				 SubDistrictTable.ID + " = " + subID + " and a." + AggVaccineTable.VACCINE_ID + " = " + vacID + " and a."+ AggVaccineTable.VACCINE_ID + " = v."
				 + VaccineTable.ID;
		String groupby = "f." + FacilityTable.NAME + ", v." + VaccineTable.NAME + ", a." + AggVaccineTable.MONTH;
		
		Cursor cur = query.query(database, column, where, null, groupby, null, null);
		cur.moveToFirst();
		List<String> list = new ArrayList<String>();
		while(!cur.isAfterLast()){
			String temp = "{ \"vaccine_name\" : \"" + cur.getString(0) + "\", \"subDistrict_name\" : \"" + cur.getString(1) + "\", \"" + 
							cur.getString(2) + "\", \"coverage\" :" + cur.getInt(3) + "}";  
			list.add(temp);
			cur.moveToNext();
		}
		cur.close();
		res += buildString(list) + "]}";
		Log.v("jian", "end");
		subCoverMonth.put(key, res);
		return res;
	}
	
	
	
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
			Log.v("jian", "End");
			return monCover.get(key);
		}
		String res = "[";
		String m = YEAR + "-" + month;
		/*Cursor cur = database.rawQuery("Select v." + VaccineTable.NAME + ", a." + AggVaccineTable.COVERAGE + " "
									+ "from " + VaccineTable.TABLENAME + " as v, " + AggVaccineTable.TABLENAME + " as a "
									+ "where v." + VaccineTable.ID + "= a." + AggVaccineTable.VACCINE_ID + " and a." + 
									AggVaccineTable.FACILITY_ID + "= " + facility_id + " and a." + 
									AggVaccineTable.MONTH + " = '" + m + "'", new String[]{});
		*/
		query.setTables(VaccineTable.TABLENAME + " as v, " + AggVaccineTable.TABLENAME + " as a ");
		String[] column = { "v." + VaccineTable.NAME, "a." + AggVaccineTable.COVERAGE};
		String where = "v." + VaccineTable.ID + "= a." + AggVaccineTable.VACCINE_ID + " and a." + 
									AggVaccineTable.FACILITY_ID + "= " + facility_id + " and a." + 
									AggVaccineTable.MONTH + " = '" + m + "'";
		
		
		Cursor cur = query.query(database, column, where, null, null, null, null);
		cur.moveToFirst();
		List<String> list = new ArrayList<String>();
		while(!cur.isAfterLast()){
			list.add("{\"facility_id\" : \"" + cur.getString(0) + "\", \"month\" :" + cur.getInt(1) + "}");
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
			Log.v("jian", "End");
			return vaccMon.get(key);
		}
		String res = "[";
		String m = YEAR + "-" + month;
		/*Cursor cur = database.rawQuery("Select v." + VaccineTable.NAME + ", a." + AggVaccineTable.COVERAGE + " "
				+ "from " + VaccineTable.TABLENAME + " as v, " + AggVaccineTable.TABLENAME + " as a "
				+ "where v." + VaccineTable.ID + "= a." + AggVaccineTable.VACCINE_ID + " and a." + 
				AggVaccineTable.FACILITY_ID + "= " + facility_id + " and a." + 
				AggVaccineTable.MONTH + " = '" + m + "' and a." + AggVaccineTable.VACCINE_ID + " = " + vaccine_id,
				new String[]{});
		*/
		query.setTables(VaccineTable.TABLENAME + " as v, " + AggVaccineTable.TABLENAME + " as a ");
		String[] column = { "v." + VaccineTable.NAME, " a." + AggVaccineTable.COVERAGE};
		String where = "v." + VaccineTable.ID + "= a." + AggVaccineTable.VACCINE_ID + " and a." + 
				AggVaccineTable.FACILITY_ID + "= " + facility_id + " and a." + 
				AggVaccineTable.MONTH + " = '" + m + "' and a." + AggVaccineTable.VACCINE_ID + " = " + vaccine_id;
		
		
		Cursor cur = query.query(database, column, where, null, null, null, null);
		cur.moveToFirst();
		List<String> list = new ArrayList<String>();
		while(!cur.isAfterLast()){
			list.add("{\"name\" : \"" + cur.getString(0) + "\", \"coverage\": " + cur.getInt(1) + "}");
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
			Log.v("jian", "End");
			return yearCover.get(facility_id);
		}
		String res = "[";
		/*Cursor cur = database.rawQuery("Select v." + VaccineTable.NAME + ", a." + AggVaccineTable.COVERAGE + ", a." 
										+ AggVaccineTable.MONTH + " " + "from " + VaccineTable.TABLENAME + " as v, " 
										+ AggVaccineTable.TABLENAME + " as a " + "where v." + VaccineTable.ID + "= a." + 
										AggVaccineTable.VACCINE_ID + " and a." + AggVaccineTable.FACILITY_ID + "=" 
										+ facility_id,	new String[]{});*/
		query.setTables(VaccineTable.TABLENAME + " as v, " 
				+ AggVaccineTable.TABLENAME + " as a ");
		String[] column = { "v." + VaccineTable.NAME, "a." + AggVaccineTable.COVERAGE, "a." + AggVaccineTable.MONTH};
		String where = "v." + VaccineTable.ID + "= a." + 
										AggVaccineTable.VACCINE_ID + " and a." + AggVaccineTable.FACILITY_ID + "=" 
										+ facility_id;
		
		
		Cursor cur = query.query(database, column, where, null, null, null, null);
		
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
			Log.v("jian", "End");
			return monStock.get(key);
		}
		String res = "[";
		String m = YEAR + "-" + month;
		/*Cursor cur = database.rawQuery("Select v." + VaccineTable.NAME + ", a." + AggVaccineTable.STOCK_LEVEL + ", a." 
										+ AggVaccineTable.STOCK_OUT + " " + "from " + VaccineTable.TABLENAME + " as v, " 
										+ AggVaccineTable.TABLENAME + " as a " + "where v." + VaccineTable.ID + "= a." + 
										AggVaccineTable.VACCINE_ID + " and a." + AggVaccineTable.FACILITY_ID + "=" 
										+ facility_id + " and a." + AggVaccineTable.MONTH + " = '" + m + "'",	new String[]{});
		*/
		query.setTables(VaccineTable.TABLENAME + " as v, " + AggVaccineTable.TABLENAME + " as a ");
		String[] column = { "v." + VaccineTable.NAME, "a." + AggVaccineTable.STOCK_LEVEL, "a." + AggVaccineTable.STOCK_OUT};
		String where = "v." + VaccineTable.ID + "= a." + 
										AggVaccineTable.VACCINE_ID + " and a." + AggVaccineTable.FACILITY_ID + "=" 
										+ facility_id + " and a." + AggVaccineTable.MONTH + " = '" + m + "'";
		
		
		Cursor cur = query.query(database, column, where, null, null, null, null);
		
		
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
			Log.v("jian", "End");
			return yearStock.get(facility_id);
		}
		String res = "[";
		/*Cursor cur = database.rawQuery("Select v." + VaccineTable.NAME + ", a." + AggVaccineTable.STOCK_LEVEL +  ", a."
										+ AggVaccineTable.MONTH + " "
										+ "from " + VaccineTable.TABLENAME + " as v, " + AggVaccineTable.TABLENAME + " as a " 
										+ "where v." + VaccineTable.ID + "= a." + AggVaccineTable.VACCINE_ID + " and a." 
										+ AggVaccineTable.FACILITY_ID + "=" + facility_id ,	new String[]{});*/
		
		query.setTables(VaccineTable.TABLENAME + " as v, " + AggVaccineTable.TABLENAME + " as a ");
		String[] column = { "v." + VaccineTable.NAME, "a." + AggVaccineTable.STOCK_LEVEL, "a." + AggVaccineTable.MONTH};
		String where = "v." + VaccineTable.ID + "= a." + AggVaccineTable.VACCINE_ID + " and a." 
										+ AggVaccineTable.FACILITY_ID + "=" + facility_id;
		
		
		Cursor cur = query.query(database, column, where, null, null, null, null);
		
		
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
