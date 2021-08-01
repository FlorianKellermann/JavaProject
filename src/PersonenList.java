import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PersonenList {
    FunctionLibrary FuncLib = new FunctionLibrary();
    private HashMap<Integer, String> Personen = new HashMap<Integer, String>();
    private int NumberOfInputs = 0;


    /*
        Method: AddToList
        Parameters: String ForInteger, String ForString
        Return : none
        purpose: adding new person to class
     */
    void AddToList(String ForInteger, String ForString) {
        int ConvertedInteger = Integer.parseInt(ForInteger); // Converting out ForInteger String into actual Integer
        if(Personen.get(ConvertedInteger)==null){
            Personen.put(ConvertedInteger, ForString); // Setting the values into the HashMap
            NumberOfInputs++;
        }
    }


    /*
        Method: GetKeyByValue
        Parameters: String Input
        Return : List<Integer>
        purpose: returning personids for given personname
     */
    List<Integer> GetKeyByValue(String Input){ // getting PersonKeys for a specific name
        List<Integer> PersonKeys = new ArrayList<>();
        for(int i=1;i<=NumberOfInputs;i++){
            if(FuncLib.CheckStrings(Input, Personen.get(i))==1){ //calling checkstrings to make sure they match
                PersonKeys.add(i);
            }
        }
        return PersonKeys;
    }

    String GetElementByInteger(Integer Input){ //getting element by the key
        return Personen.get(Input);
    }
}
