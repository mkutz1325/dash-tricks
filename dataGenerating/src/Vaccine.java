import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * the class define table of Vaccine
 * id/ name/ number of dose(per person per cycle)/ dose per vile/wasted percentage/ space per vile cm^3(1l = 1000cm^3)
 * @author Chelsea
 *
 */
public class Vaccine {
	
	public HashMap<Integer, Pair<String, int[]>> vaccine = new HashMap<Integer, Pair<String, int[]>>();
	
	public ArrayList<String> vaccineGener(){
		ArrayList<String> file = new ArrayList<String>();
		DataRange dr = new DataRange();
		Random dose_gener = new Random();
		Random vile_gener = new Random();
		Random space_gener = new Random();
		Random wasted_gener = new Random();
		for(int i = 1; i <= dr.VACCINE.length; i++){
			String name = dr.VACCINE[i-1];
			int no_dose = dose_gener.nextInt(dr.NUM_DOSE_MAX - dr.NUM_DOSE_MIN + 1) + dr.NUM_DOSE_MIN;
			int vile = vile_gener.nextInt(dr.DOSE_VILE_MAX - dr.DOSE_VILE_MIN + 1) + dr.DOSE_VILE_MIN;
			int space = (space_gener.nextInt(dr.SPACE_VILE_MAX - dr.SPACE_VILE_MIN + 1) + dr.SPACE_VILE_MIN) * 10;
			int waste = wasted_gener.nextInt(dr.WASTED_MAX - dr.WASTED_MIN + 1) + dr.WASTED_MIN;
			int[] temp = {no_dose, vile, waste, space};
			Pair<String, int[]> info = new Pair(name, temp);
			vaccine.put(i,  info);
			String line = i + ", '" + name + "', " + no_dose + ", " + vile + ", " + waste + ", " + space;
			file.add(line);
			if(dr.PRINT){
				System.out.println(line);
			}
			
			
		}
		return file;
		
	}
	
	
	

}
