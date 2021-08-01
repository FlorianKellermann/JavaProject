import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationList {
    FunctionLibrary FuncLib=new FunctionLibrary();
    private HashMap<Integer, String> LocationName = new HashMap<Integer, String>();
    private HashMap<Integer, String> LocationStatus = new HashMap<Integer, String>();
    private int NumberofValues=0;

    void AddLocation(String ForInteger, String ForName, String ForStatus){
        int ConvertedInteger = Integer.parseInt(ForInteger);
        if(LocationName.get(ConvertedInteger)==null){
            LocationName.put(ConvertedInteger, ForName);
            LocationStatus.put(ConvertedInteger, ForStatus);
            NumberofValues++;
        }
    }

    String GetNameByKey(Integer Input){
        return LocationName.get(Input);
    }

    String GetStatusByKey(Integer Input){
        return LocationStatus.get(Input);
    }

    List<Integer> GetKeyFromAttribute(String Attribute){
        List<Integer> LocationKeys = new ArrayList<>();
        for(int i=1;i<=NumberofValues;i++){
            if(FuncLib.CheckStrings(Attribute, LocationName.get(i))==1){
                LocationKeys.add(i);
            }
        }
        return LocationKeys;
    }
}
