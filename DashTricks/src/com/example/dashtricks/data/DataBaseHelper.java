package com.example.dashtricks.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*import com.example.dash_tricks.FridgeCapacityContract.FridgeCapacity;
import com.example.dash_tricks.FridgeTemperatureContract.FridgeTemperature;
import com.example.dash_tricks.VaccCoverContract.VaccCoverage;
import com.example.dash_tricks.VaccSupplyContract.VaccSupply;*/

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DataBaseHelper extends SQLiteOpenHelper, it creates tables and insert values into corresponding table
 * @author jianzhao
 *
 */
public class DataBaseHelper extends SQLiteOpenHelper {
	
	private static final String TEXT_TYPE_TEXT = "TEXT";
	private static final String TEXT_TYPE_INT = "INTEGER";
	//private static final String TEXT_TYPE_REAL = "REAL";
	
	//create tables
	//District table
	private static final String DISTRICT = "CREATE TABLE " + DistrictTable.TABLENAME + "(" + DistrictTable.ID + " "
    + TEXT_TYPE_INT + " PRIMARY KEY, " + DistrictTable.NAME + " " + TEXT_TYPE_TEXT + ", " + DistrictTable.REGID + " " +
    TEXT_TYPE_INT + ", " + DistrictTable.MANAGER + " " +
    TEXT_TYPE_TEXT + ", " + DistrictTable.COOR + " " + TEXT_TYPE_TEXT + ")";
	
	//sub district table
	private static final String SUBDISTRICT = "CREATE TABLE " + SubDistrictTable.TABLENAME + "(" + SubDistrictTable.ID + " "
    + TEXT_TYPE_INT + " PRIMARY KEY, " + SubDistrictTable.NAME + " " + TEXT_TYPE_TEXT + ", " + SubDistrictTable.DISTRICTID + " "
    + TEXT_TYPE_INT + ", " + SubDistrictTable.COOR + " " + TEXT_TYPE_TEXT+ ")";
	
	//Facility table
	private static final String FACILITY = "CREATE TABLE " + FacilityTable.TABLENAME + "(" + FacilityTable.ID + " "
    + TEXT_TYPE_INT + " PRIMARY KEY, " + FacilityTable.NAME + " " + TEXT_TYPE_TEXT + ", " + FacilityTable.POPULATION + " " +
    TEXT_TYPE_INT + ", "  + FacilityTable.SUBID + " " + TEXT_TYPE_INT + ", "+ FacilityTable.COOR + " " + TEXT_TYPE_TEXT + ")";
	//fridge table
	private static final String FRIDGE = "CREATE TABLE " + FridgeTable.TABLENAME + "(" + FridgeTable.ID + " "
    + TEXT_TYPE_INT + " PRIMARY KEY, " + FridgeTable.FACILITY_ID + " " + TEXT_TYPE_INT + ", " + FridgeTable.MODEL + " " +
    TEXT_TYPE_TEXT + ", " + FridgeTable.REQUIRED_CAPACITY + "  " + TEXT_TYPE_INT + ", " + FridgeTable.DATE + " " + TEXT_TYPE_TEXT + ")";
	//fridge model table
	private static final String FRIDGEMODEL = "CREATE TABLE " + FridgeModelTable.TABLENAME + "(" + FridgeModelTable.ID + " "
    + TEXT_TYPE_INT + " PRIMARY KEY, " + FridgeModelTable.MODEL + " " + TEXT_TYPE_TEXT + ", " + FridgeModelTable.FREEZER_CAPA + " " +
    TEXT_TYPE_INT + ", " + FridgeModelTable.FRIDGE_CAPA + "  " + TEXT_TYPE_INT + ")";
    
	//Vaccine model table
	private static final String VACCINE = "CREATE TABLE " + VaccineTable.TABLENAME + "(" + VaccineTable.ID + " "
    + TEXT_TYPE_INT + " PRIMARY KEY, " + VaccineTable.NAME + " " + TEXT_TYPE_TEXT + ", " + VaccineTable.DOSE_NO+ " " +
    TEXT_TYPE_INT + ", " + VaccineTable.DOSE_VILE + " " + TEXT_TYPE_INT +", " +VaccineTable.WASTED + " " + TEXT_TYPE_INT
    + ", " + VaccineTable.SPACE + " " + TEXT_TYPE_INT + ")";
	
	//Aggregate capacity data model
	private static final String AGGFRIDGE= "CREATE TABLE " + AggCapacityTable.TABLENAME + "(" + AggCapacityTable.FRIDGE_ID + " "
    + TEXT_TYPE_INT + ", " + AggCapacityTable.FACILITY_ID + " " + TEXT_TYPE_INT + ", " + AggCapacityTable.MONTH+ " " +
    TEXT_TYPE_TEXT + ", " + AggCapacityTable.CURR_CAPA + "  " + TEXT_TYPE_INT + ", " + AggCapacityTable.REQUIRED_CAPA + " " + TEXT_TYPE_INT +
    ", PRIMARY KEY (" + AggCapacityTable.FRIDGE_ID + ", " + AggCapacityTable.FACILITY_ID + ", " + AggCapacityTable.MONTH + "))";
    
	//Aggregate vaccine data model
	private static final String AGGVACC= "CREATE TABLE " + AggVaccineTable.TABLENAME + "(" + AggVaccineTable.VACCINE_ID + " "
    + TEXT_TYPE_INT + ", " + AggVaccineTable.FACILITY_ID + " " + TEXT_TYPE_INT + ", " + AggCapacityTable.MONTH+ " " +
    TEXT_TYPE_TEXT + ", " + AggVaccineTable.STOCK_LEVEL + "  " + TEXT_TYPE_INT + ", " +  AggVaccineTable.STOCK_OUT + " " + TEXT_TYPE_INT + ", "
    + AggVaccineTable.COVERAGE + " " + TEXT_TYPE_INT + ", PRIMARY KEY (" + AggVaccineTable.VACCINE_ID + ", " + AggVaccineTable.FACILITY_ID
    + ", " + AggVaccineTable.MONTH + "))";
	

	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ";
	
	public static final int DATABASE_VERSION = 27;//increment the database version when change the database schema
	//public static final String DATABASE_NAME = "VaccCover.db";
	public static final String DATABASE_NAME = "DashTrick";
	private Context context;
	public DataBaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}
	
	/**
	 * create tables and insert data
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String column = "";

		//load district
		db.execSQL(DISTRICT);
		column = DistrictTable.ID + ", " + DistrictTable.NAME + ", " + DistrictTable.REGID + ", " +
        DistrictTable.MANAGER + ", " + DistrictTable.COOR;
		loadData("district.csv", column, db, DistrictTable.TABLENAME);
        
		//load sub_district
		db.execSQL(SUBDISTRICT);
		column = SubDistrictTable.ID + ", " + SubDistrictTable.NAME + ", " + SubDistrictTable.DISTRICTID + ", " + SubDistrictTable.COOR ;
		loadData("subDistrict.csv", column, db, SubDistrictTable.TABLENAME);
        
		//load facility
		db.execSQL(FACILITY);
		column = FacilityTable.ID + ", " + FacilityTable.NAME + ", " + FacilityTable.POPULATION + ", " + FacilityTable.SUBID + ", " + FacilityTable.COOR;
		loadData("facility.csv", column, db, FacilityTable.TABLENAME);
		
		//load fridge
		db.execSQL(FRIDGE);
		column = FridgeTable.ID + ", " + FridgeTable.FACILITY_ID + ", " + FridgeTable.MODEL + ", " + FridgeTable.REQUIRED_CAPACITY
        + ", " + FridgeTable.DATE;
		loadData("fridge.csv", column, db, FridgeTable.TABLENAME);
		
		//load fridge model
		db.execSQL(FRIDGEMODEL);
		column = FridgeModelTable.ID + ", " + FridgeModelTable.MODEL + ", " + FridgeModelTable.FREEZER_CAPA + ", " +
        FridgeModelTable.FRIDGE_CAPA;
		loadData("fridgeModel.csv", column, db, FridgeModelTable.TABLENAME);
		
		//load vaccine model
		db.execSQL(VACCINE);
		column = VaccineTable.ID + ", " + VaccineTable.NAME + ", " + VaccineTable.DOSE_NO + ", " + VaccineTable.DOSE_VILE +
        ", " + VaccineTable.WASTED + ", " + VaccineTable.SPACE;
		loadData("vaccine.csv", column, db, VaccineTable.TABLENAME);
		
		//load aggregate fridge data model
		db.execSQL(AGGFRIDGE);
		column = AggCapacityTable.FRIDGE_ID + ", " + AggCapacityTable.FACILITY_ID + ", " + AggCapacityTable.MONTH + ", " +
        AggCapacityTable.CURR_CAPA + ", " + AggCapacityTable.REQUIRED_CAPA;
		loadData("aggregated_Facility_Capacity_Data.csv", column, db, AggCapacityTable.TABLENAME);
		
		//load aggregated vaccine data model
		db.execSQL(AGGVACC);
		column = AggVaccineTable.VACCINE_ID + ", " + AggVaccineTable.FACILITY_ID + ", " + AggVaccineTable.MONTH + ", " +
        AggVaccineTable.STOCK_LEVEL + ", " + AggVaccineTable.STOCK_OUT + ", " + AggVaccineTable.COVERAGE;
		loadData("aggregated_Facility_Vaccine_Data.csv", column, db, AggVaccineTable.TABLENAME);
        
   
	}
	
	private void loadData(String file, String column,SQLiteDatabase db, String tablename){
		db.beginTransaction();
		BufferedReader buffer = null;
		try {
			AssetManager am = context.getAssets();
            
			buffer = new BufferedReader(new InputStreamReader(am.open(file)));
		} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
		}
		String str1 = "INSERT INTO " + tablename + " (" + column + ") values(";
		String str2 = ");";
		String line = "";
		try {
			while ((line = buffer.readLine()) != null) {
			    StringBuilder sb = new StringBuilder(str1);
			    sb.append(line);
			    sb.append(str2);
			    db.execSQL(sb.toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
        
	}
	
	
	
	/**
	 * upgrade the tables
	 */
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL(SQL_DELETE_ENTRIES + DistrictTable.TABLENAME);
		db.execSQL(SQL_DELETE_ENTRIES + FacilityTable.TABLENAME);
		db.execSQL(SQL_DELETE_ENTRIES + SubDistrictTable.TABLENAME);
		db.execSQL(SQL_DELETE_ENTRIES + FridgeTable.TABLENAME);
		db.execSQL(SQL_DELETE_ENTRIES + FridgeModelTable.TABLENAME);
		db.execSQL(SQL_DELETE_ENTRIES + VaccineTable.TABLENAME);
		db.execSQL(SQL_DELETE_ENTRIES + AggCapacityTable.TABLENAME);
		db.execSQL(SQL_DELETE_ENTRIES + AggVaccineTable.TABLENAME); 
		
		onCreate(db);
		
	}
	
	
	public void downUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * open database
	 * @throws SQLException
	 */
	public void open() throws SQLException{
		this.getWritableDatabase();
	}
	
}







