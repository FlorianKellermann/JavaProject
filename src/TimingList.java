import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimingList {
    FunctionLibrary FuncLib = new FunctionLibrary();
    private HashMap<Integer, String> StartTime = new HashMap<Integer, String>();
    private HashMap<Integer, String> EndTime = new HashMap<Integer, String>();
    private HashMap<Integer, Integer> PersonID = new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> LocationID = new HashMap<Integer, Integer>();
    private int HighestKeyInteger=0;
    private int NumberofValues = 0;

    /*
        Method: AddTiming
        Parameters: Integer KeyInteger, String StartTimeInput, String EndTimeInput, String ForPersonID, String ForLocationID
        Return : none
        purpose: adding new timingelement to the class
     */
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

    Integer GetLocationID(Integer IntegerKey){ // returning locationid from key
        return LocationID.get(IntegerKey);
    }


    /*
        Method: GetKeyForPersonID
        Parameters: Integer pFindPerson
        Return : List<Integer>
        purpose: retrieving all timingkeys where a specific person appears
     */
    List<Integer> GetKeyForPersonID(Integer pFindPerson){
        List<Integer> KeyList = new ArrayList<>(); // generating list for returning all keys
        for(int i=1;i<NumberofValues;i++){
            if(PersonID.get(i).equals(pFindPerson)){
                KeyList.add(i);
            }
        }
        return KeyList;
    }


    /*
        Method: GetPersonsForLocationID
        Parameters: Integer pLocationID, Integer VisitorID, Integer pRefKey
        Return : List<Integer>
        purpose: Getting all persons at a specific location ( besides the given visitor )
     */
    List<Integer> GetPersonsForLocationID(Integer pLocationID, Integer VisitorID, Integer pRefKey){
        List<Integer> ForReturnIDs = new ArrayList<>();
        for (int i = 1; i <= NumberofValues; i++) {
            if(LocationID.get(i)==pLocationID && PersonID.get(i)!=VisitorID){ // location id has to match and not be the same person
                if(FuncLib.CheckTimeSimilarity(StartTime.get(pRefKey), EndTime.get(pRefKey), StartTime.get(i), EndTime.get(i)) == 1){
                    ForReturnIDs.add(PersonID.get(i));
                }
            }
        }
        return ForReturnIDs;
    }


    /*
        Method: GetPersonsForTimeStamp
        Parameters: Integer pLocationID, String pTimeStamp
        Return : List<Integer>
        purpose: Getting all persons at a specific location at a specific time
     */
    List<Integer> GetPersonsForTimeStamp(Integer pLocationID, String pTimeStamp){
        List<Integer> Contacts = new ArrayList<>();
        for (int i = 1; i < NumberofValues; i++) {
            //finding the persons so location and time have to be matching
            if(LocationID.get(i) == pLocationID && FuncLib.CheckTimeSimilarity(pTimeStamp,pTimeStamp,StartTime.get(i), EndTime.get(i)) == 1){
                Contacts.add(PersonID.get(i));
            }
        }
        return Contacts;
    }
}
