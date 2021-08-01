import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationList {
    FunctionLibrary FuncLib=new FunctionLibrary();
    private HashMap<Integer, String> LocationName = new HashMap<Integer, String>();
    private HashMap<Integer, String> LocationStatus = new HashMap<Integer, String>();
    private int NumberofValues=0;


    /*
        Method: AddLocation
        Parameters: String ForInteger, String ForName, String ForStatus
        Return : none
        purpose: adding location to class
     */
    void AddLocation(String ForInteger, String ForName, String ForStatus){ //adding locationdetails to the hashmaps
        int ConvertedInteger = Integer.parseInt(ForInteger); // converting the string key to integer
        if(LocationName.get(ConvertedInteger)==null){
            LocationName.put(ConvertedInteger, ForName);
            LocationStatus.put(ConvertedInteger, ForStatus);
            NumberofValues++; // one up the number of elements
        }
    }

    String GetNameByKey(Integer Input){ //simply get the LocationName from the ID (variable is private so i need it)
        return LocationName.get(Input);
    }

    String GetStatusByKey(Integer Input){ // same for status
        return LocationStatus.get(Input);
    }


    /*
        Method: GetKeyFromAttribute
        Parameters: String Attribute
        Return : List<Integer>
        purpose: Finding all LocationIds matching the name attribute
     */
    List<Integer> GetKeyFromAttribute(String Attribute){ // getting key for all matching locationnames
        List<Integer> LocationKeys = new ArrayList<>();
        for(int i=1;i<=NumberofValues;i++){
            if(FuncLib.CheckStrings(Attribute, LocationName.get(i))==1){ // used to ignore case and word length
                LocationKeys.add(i);
            }
        }
        return LocationKeys;
    }
}
