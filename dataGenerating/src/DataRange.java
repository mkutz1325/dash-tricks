
/**
 * this class provide ranges for random generating each data 
 * @author jianzhao
 *
 */
public class DataRange {
	
	
	public static final int FACILITY_MIN = 1;
	public static final int FACILITY_MAX = 2;
	
	public static final int FRIDGE_MIN = 1;
	public static final int FRIDGE_MAX = 2;
	
	public static final int REQ_CAPACITY_MIN = 40;
	public static final int REQ_CAPACITY_MAC = 120;
	public static final int CURR_CAPACITY = 50;
	//required capacity is current_capacity +/- 50
	
	public static final int COVERAGE_MIN = 50;
	public static final int COVERAGE_MAX = 120;
	
	public static final int STOCK_OUT_MIN = 0;
	public static final int STOCK_OUT_MAX = 5;
	
	public static final int STOCK_LEVEL_MIN = 10;
	public static final int STOCK_LEVEL_MAX = 50;
	
	//public static final int VACC_COVERAGE_MIN = 50;
	//public static final int VACC_COVERAGE_MAX = 120;
	
	public static final int[] VACC_COVERAGE_MIN = {20, 50, 40, 60, 70, 30, 80, 50};
	public static final int[] VACC_COVERAGE_MAX = {90, 120, 140, 100, 110, 120, 100, 110} ;
	
	public static final int DISTRICT = 60;
	
	public static final int SUB_DISTRICT_MIN = 2;
	public static final int SUB_DISTRICT_MAX = 4;
	
	public static final int FACILITY_POPULATION_MIN = 8000;
	public static final int FACILITY_POPULATION_MAX = 12000;
	
	//public static final int VACCINE = 10;
	public static final String[] VACCINE = {"BCG", "OPV", "Penta", "Pneum", "Rota", "Measles", "YF", "DT"};
			
	public static final int NUM_DOSE_MIN = 3;
	public static final int NUM_DOSE_MAX = 8;
	public static final int DOSE_VILE_MIN = 2;
	public static final int DOSE_VILE_MAX = 5;
	public static final int SPACE_VILE_MIN = 1;//10cm^3
	public static final int SPACE_VILE_MAX = 5;
	public static final int WASTED_MIN = 15;
	public static final int WASTED_MAX = 40;
	
	
	
	public static final boolean PRINT = false;
	
	public static final int MODEL = 10;
	public static final int FREEZER_CAPA = 150;
	public static final int FRIDGE_CAPA = 150;
	
	public static final int YEAR = 2014;
	public static final int MONTH = 12;
	
	public static final int YEAR_MAX = 2014;
	public static final int YEAR_MIN = 2000;
	
	public static final int RECEIVED_MAX = 50;

	
	
	
	
	
}
