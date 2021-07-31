import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Project {
    static PersonenList Personen = new PersonenList();
    static LocationList Locations = new LocationList();
    static TimingList Timings = new TimingList();
    static FunctionLibrary FuncLib = new FunctionLibrary();

    public static void main(String[] args) {
        // Start reading File and sending information to the different
        try {

            File ElementFile = new File("src\\contacts2021.db"); // Importing via the Filename when in the same folder
            Scanner ElementFileScanner = new Scanner(ElementFile); // Setting the scanner to work for the file
            int SendToPersons = 0;
            int sendToLocations = 0;
            int sendToTimings = 0;
            int iterations=0;
            while (ElementFileScanner.hasNextLine()) {
                String ContentFromFile = ElementFileScanner.nextLine();

                if (SendToPersons == 1) {    // Will be opened when first category has been found
                    String[] PersonsSplit = ContentFromFile.split("\""); //Splitting up the string in the parts that I need
                    if(PersonsSplit[1].equals("location_id")){}//Otherwise it will send one element too many
                    else{
                        Personen.AddToList(PersonsSplit[1], PersonsSplit[3]);
                    }
                }

                String[] FirstSplit = ContentFromFile.split(" "); // splitting initial String to find new categories
                if (FirstSplit[0].equals("New_Entity:") && FirstSplit[1].equals("\"person_id\",\"person_name\"")) {
                    SendToPersons = 1;
                }

                if(sendToLocations == 1){ // will be opened when second category is found
                    String[] LocationsSplit = ContentFromFile.split("\""); // splitting at " to find all entities
                    if(LocationsSplit[1].equals("start_date")){}
                    else{
                        Locations.AddLocation(LocationsSplit[1], LocationsSplit[3],LocationsSplit[5]);
                    }
                }

                if (FirstSplit[0].equals("New_Entity:")
                        && FirstSplit[1].equals("\"location_id\",\"location_name\",\"in_door\"")) {    // Zweite Daten-Gruppe gefunden
                    SendToPersons = 0;
                    sendToLocations = 1;
                }

                if(sendToTimings==1){ //equivalent to upper points
                    iterations++;
                    String[] TimingsSplit = ContentFromFile.split("\"");
                    Timings.AddTiming(iterations, TimingsSplit[1], TimingsSplit[3], TimingsSplit[5], TimingsSplit[7]);
                }

                if (FirstSplit[0].equals("New_Entity:") //equivalent to upper points
                        && FirstSplit[1].equals("\"start_date\",")) {
                    sendToLocations = 0;
                    sendToTimings = 1;
                }



            }
            ElementFileScanner.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("An Error occurred.");
            e.printStackTrace();
        }

        String[] FirstSplit;
        FirstSplit = args[0].split("=");
        if(FirstSplit[0].equals("--personensuche")){
            String[] SecondSplit;
            SecondSplit = FirstSplit[1].split("\"");
            PersonSearch(SecondSplit[0]);
        }
        else if(FirstSplit[0].equals("--ortssuche")){
            String[] SecondSplit;
            SecondSplit = FirstSplit[1].split("\"");
            PlaceSearch(SecondSplit[0]);
        }
        else if(FirstSplit[0].equals("--kontaktpersonen")){
            List<Integer> ReturnList = new ArrayList<>();
            List<String> ToStringList = new ArrayList<>();
            Integer PersonID = Integer.parseInt(FirstSplit[1]);
            ReturnList = ContactPersonSearch(PersonID);
            for(int i=0; i<ReturnList.size(); i++){
                ToStringList.add(Personen.GetElementByInteger(ReturnList.get(i)));
            }
            ToStringList = FuncLib.SortAscending(ToStringList);

            System.out.println("Kontakte für "+Personen.GetElementByInteger(PersonID)+"\n=======================");
            for (int i = 0; i < ToStringList.size(); i++) {
                System.out.println(ToStringList.get(i));
            }
        }
        else if(FirstSplit[0].equals("--besucher")){
            String[] SecondSplit;
            SecondSplit = FirstSplit[1].split(",");
            VisitorSearch(Integer.parseInt(SecondSplit[0]), SecondSplit[1]);
        }
        else{
            System.err.println("Falsche Argumentenübergabe!");
        }

    }

    static void PersonSearch(String pPersonName){
        List<Number> PersonKeys = new ArrayList<>();
        List<Integer> LocationKeys = new ArrayList<>();
        PersonKeys = Personen.GetKeyByValue(pPersonName); // searching for name in List and saving the key
        if(PersonKeys.isEmpty()){
            System.err.println("No person with that name found");
        }
        else{
            System.out.println("Es wurde(-n) " + PersonKeys.size() + " Person(-en) zur Suche gefunden.");
            for(int i=0; i<PersonKeys.size();i++){
                String PersonName = Personen.GetElementByInteger(PersonKeys.get(i).intValue());
                LocationKeys = Timings.GetKeyForPersonID(PersonKeys.get(i).intValue());
                System.out.println("\n"+PersonName +" war zu Besuch in");
                System.out.println("===============================");
                for(int j=0;j<LocationKeys.size();j++){
                    Integer LocationKey = Timings.GetLocationID(LocationKeys.get(j).intValue());
                    String LocationName = Locations.GetNameByKey(LocationKey);
                    System.out.println(LocationName);
                }
            }
        }
    }

    static void PlaceSearch(String pLocationName){
        List <Integer> FoundLocationKeys = new ArrayList<>();
        List <Integer> FoundLocationTimings = new ArrayList<>();
        FoundLocationKeys = Locations.GetKeyFromAttribute(pLocationName);
        if(FoundLocationKeys.isEmpty()){
            System.err.println("No Locations found for your request");
        }
        else{
            System.out.println("Found " + FoundLocationKeys.size() + " Locations matching your request.\n");
            for (int i = 0; i < FoundLocationKeys.size(); i++) {
                String LocationName = Locations.GetNameByKey(FoundLocationKeys.get(i));
                String LocationStatus = Locations.GetStatusByKey(FoundLocationKeys.get(i));
                FoundLocationTimings = Timings.GetKeyForLocationID(FoundLocationKeys.get(i));
                System.out.println("Der Ort "+LocationName+"("+LocationStatus+") hat die ID " +FoundLocationKeys.get(i)+" und wurde besucht von: \n===============================");
                for (int j = 0; j < FoundLocationTimings.size(); j++) {
                    int BesucherID = Timings.GetPersonID(FoundLocationTimings.get(j));
                    String BesucherName = Personen.GetElementByInteger(BesucherID);
                    System.out.println(BesucherName+" ("+BesucherID+")");
                }
            }
        }
    }

    static List<Integer> ContactPersonSearch(Integer pPersonID){
        List<Integer> pPersonTimingKeys = new ArrayList<>();
        List<Integer> pPersonTimingKeysAfterLocation = new ArrayList<>();
        List<Integer> pPersonLocationIDs = new ArrayList<>(); //Need these three informations about him
        List<Integer> ContactPersonIDs = new ArrayList<>();
        List<Integer> TempList = new ArrayList<>();
        pPersonTimingKeys = Timings.GetKeyForPersonID(pPersonID); // Getting all timingKeys where the person is mentioned
        for (int i = 0; i < pPersonTimingKeys.size(); i++) {
            if(Locations.GetStatusByKey(Timings.GetLocationID(pPersonTimingKeys.get(i))).equals("in_door")){ // checking if visited locations are indoor
                pPersonLocationIDs.add(Timings.GetLocationID(pPersonTimingKeys.get(i)));
                pPersonTimingKeysAfterLocation.add(pPersonTimingKeys.get(i));
            }
        }
        if(pPersonLocationIDs.isEmpty()){
            System.err.println("No In Door Locations found for this person");
        }
        for (int i = 0; i < pPersonLocationIDs.size(); i++) { //retrieving all contact elements from the timinglist
            TempList=Timings.GetPersonsForLocationID(pPersonLocationIDs.get(i), pPersonID,pPersonTimingKeysAfterLocation.get(i));
            for (int j = 0; j < TempList.size(); j++) {
                ContactPersonIDs.add(TempList.get(j));
            }
        }
        ContactPersonIDs = FuncLib.NoDoubles(ContactPersonIDs);
        return ContactPersonIDs;
    }

    static void VisitorSearch(Integer GivenLocation, String GivenTimeStamp){
        List<Integer> ContactIDs = new ArrayList<>();
        List<String> ContactNames = new ArrayList<>();
        ContactIDs = Timings.GetPersonsForTimeStamp(GivenLocation, GivenTimeStamp);
        ContactIDs = FuncLib.NoDoubles(ContactIDs); //doppelte austauschen
        for (int i = 0; i < ContactIDs.size(); i++) {
            ContactNames.add(Personen.GetElementByInteger(ContactIDs.get(i)));
        }
        ContactNames=FuncLib.SortAscending(ContactNames); //aufsteigend sortieren
        System.out.println("Zu diesem Zeitpunkt anwesende Personen");
        System.out.println("=================================================");
        if(ContactNames.isEmpty()){
            System.out.println("Nobody!");
        }
        else{
            for (int i = 0; i < ContactNames.size(); i++) { // print all elements
                System.out.println(ContactNames.get(i));
            }
        }
        if(Locations.GetStatusByKey(GivenLocation).equals("out_door")){ // if Location is outdoor
            System.out.println("Die Location ist Outdoor. Dies waren alle Kontake.");
        }
        else{
            System.out.println("\nIndirekte Kontakte\n===============");
            List<Integer> TempList = new ArrayList<>();
            List<Integer> SecondContactID = new ArrayList<>();
            List<String> SecondContactName = new ArrayList<>();
            int Same = 0;
            for(int i=0;i<ContactIDs.size(); i++){
                TempList = ContactPersonSearch(ContactIDs.get(i));
                for (int j = 0; j < TempList.size(); j++) {
                    for(int c=0; c<ContactIDs.size(); c++){
                        if(TempList.get(j)==ContactIDs.get(c)){
                            Same = 1;
                        }
                    }
                    if(Same==0){
                        SecondContactID.add(TempList.get(j));
                    }
                    else{
                        Same = 0;
                    }

                }
            }
            SecondContactID = FuncLib.NoDoubles(SecondContactID);
            for (int i = 0; i < SecondContactID.size(); i++) {
                SecondContactName.add(Personen.GetElementByInteger(SecondContactID.get(i)));
            }
            SecondContactName = FuncLib.SortAscending(SecondContactName);
            for (int i = 0; i < SecondContactName.size(); i++) {
                System.out.println(SecondContactName.get(i));
            }
        }
    }
}
