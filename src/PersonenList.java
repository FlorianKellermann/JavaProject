import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PersonenList {
    FunctionLibrary FuncLib = new FunctionLibrary();
    private HashMap<Integer, String> Personen = new HashMap<Integer, String>();
    private int NumberOfInputs = 0;

    void AddToList(String ForInteger, String ForString) {
        int ConvertedInteger = Integer.parseInt(ForInteger); // Converting out ForInteger String into actual Integer
        if(Personen.get(ConvertedInteger)==null){
            Personen.put(ConvertedInteger, ForString); // Setting the values into the HashMap
            NumberOfInputs++;
        }
    }

    List<Integer> GetKeyByValue(String Input){
        List<Integer> PersonKeys = new ArrayList<>();
        for(int i=1;i<=NumberOfInputs;i++){
            if(FuncLib.CheckStrings(Input, Personen.get(i))==1){
                PersonKeys.add(i);
            }
        }
        return PersonKeys;
    }

    String GetElementByInteger(Integer Input){
        return Personen.get(Input);
    }
}
