import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimingList {
    FunctionLibrary FuncLib = new FunctionLibrary();
    HashMap<Integer, String> StartTime = new HashMap<Integer, String>();
    HashMap<Integer, String> EndTime = new HashMap<Integer, String>();
    HashMap<Integer, Integer> PersonID = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> LocationID = new HashMap<Integer, Integer>();
    int HighestKeyInteger=0;
    int NumberofValues = 0;


    void AddTiming(Integer KeyInteger, String StartTimeInput, String EndTimeInput, String ForPersonID, String ForLocationID){
        int PersonIDInput = Integer.parseInt(ForPersonID);
        int LocationIDInput = Integer.parseInt(ForLocationID);
        HighestKeyInteger = KeyInteger;

        StartTime.put(KeyInteger, StartTimeInput);
        EndTime.put(KeyInteger, EndTimeInput);
        PersonID.put(KeyInteger, PersonIDInput);
        LocationID.put(KeyInteger, LocationIDInput);
        NumberofValues++;
    }

    void PrintWholeList(){
        for(int i=1; i<=HighestKeyInteger; i++){
            System.out.println(i + " " + StartTime.get(i) + " " + EndTime.get(i) + " " + PersonID.get(i) + " " + LocationID.get(i));
        }
    }

    String GetStartTime(Integer IntegerKey){
        return StartTime.get(IntegerKey);
    }

    String GetEndTime(Integer IntegerKey){
        return EndTime.get(IntegerKey);
    }

    Integer GetPersonID(Integer IntegerKey){
        return PersonID.get(IntegerKey);
    }

    Integer GetLocationID(Integer IntegerKey){
        return LocationID.get(IntegerKey);
    }

    List<Integer> GetKeyForPersonID(Integer pFindPerson){
        List<Integer> KeyList = new ArrayList<>(); // generating list for returning all keys
        for(int i=1;i<NumberofValues;i++){
            if(PersonID.get(i).equals(pFindPerson)){
                KeyList.add(i);
            }
        }
        return KeyList;
    }

    List<Integer> GetKeyForLocationID(Integer pFindPerson){
        List<Integer> KeyList = new ArrayList<>(); // generating list for returning all keys
        for(int i=1;i<NumberofValues;i++){
            if(LocationID.get(i).equals(pFindPerson)){
                KeyList.add(i);
            }
        }
        return KeyList;
    }

    List<Integer> GetPersonsForLocationID(Integer pLocationID, Integer VisitorID, Integer pRefKey){
        List<Integer> ForReturnIDs = new ArrayList<>();
        for (int i = 1; i < NumberofValues; i++) {
            if(LocationID.get(i)==pLocationID && PersonID.get(i)!=VisitorID){ // Hier muss ich noch checken ob sie auch gleichzeitig da waren
                if(FuncLib.CheckTimeSimilarity(StartTime.get(pRefKey), EndTime.get(pRefKey), StartTime.get(i), EndTime.get(i)) == 1){
                    ForReturnIDs.add(PersonID.get(i));
                }

            }
        }
        return ForReturnIDs;
    }

    List<Integer> GetPersonsForTimeStamp(Integer pLocationID, String pTimeStamp){
        List<Integer> Contacts = new ArrayList<>();
        for (int i = 1; i < NumberofValues; i++) {
            if(LocationID.get(i) == pLocationID && FuncLib.CheckTimeSimilarity(pTimeStamp,pTimeStamp,StartTime.get(i), EndTime.get(i)) == 1){
                Contacts.add(PersonID.get(i));
            }
        }
        return Contacts;
    }
}
