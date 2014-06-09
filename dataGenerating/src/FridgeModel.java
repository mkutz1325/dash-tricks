import java.util.ArrayList;
import java.util.HashMap;

/**
 * this class defines the table of Fridge model
 * id/modelname/freezer capacity/fridge capacity
 * @author Chelsea
 *
 */
public class FridgeModel {
	//pair with first = freezer capacity, and second = fridge capacity
	public HashMap<Integer,Pair<String, Pair<Integer, Integer>>> fridge_model = new HashMap<Integer,Pair<String, Pair<Integer, Integer>>>();
	
	
	public ArrayList<String> modelGener(){
		ArrayList<String> file = new ArrayList<String>();
		DataRange dr = new DataRange();
		for(int i = 1; i <= dr.MODEL; i++){
			String model = "Model" + i;
			fridge_model.put(i, new Pair(model, new Pair(dr.FREEZER_CAPA, dr.FRIDGE_CAPA)));
			String line = i + ", '" +  model + "', " + dr.FREEZER_CAPA + ", " + dr.FRIDGE_CAPA;
			file.add(line);
			if(dr.PRINT){
				System.out.println(line);
			}
		}
		return file;
	}
	
	
}
