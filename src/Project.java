import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Project {
    static PersonenList Personen = new PersonenList(); //importing all classes
    static LocationList Locations = new LocationList();
    static TimingList Timings = new TimingList();
    static FunctionLibrary FuncLib = new FunctionLibrary();


    /*
        Method: main
        Parameters: String[] args
        Return : none (void)
        purpose: Reading from file, inputting data and calling task methods
     */
    public static void main(String[] args) {
        // Start reading File and sending information to the different
        try {

            File ElementFile = new File("src\\contacts2021.db"); // Importing via the Filename (in src folder)
            Scanner ElementFileScanner = new Scanner(ElementFile); // Setting the scanner to work for the file
            int SendToPersons = 0;
            int sendToLocations = 0;
            int sendToTimings = 0;
            int iterations=0;
            while (ElementFileScanner.hasNextLine()) { // running while there is still unread content in the file
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
        if(FirstSplit[0].equals("--personensuche")){ //opened when trying to find person via name
            String[] SecondSplit;
            SecondSplit = FirstSplit[1].split("\"");
            PersonSearch(SecondSplit[0]); //actual function to do the work
        }
        else if(FirstSplit[0].equals("--ortssuche")){ //user is trying to find locationdetails
            String[] SecondSplit;
            SecondSplit = FirstSplit[1].split("\"");
            PlaceSearch(SecondSplit[0]); // method that does the job
        }
        else if(FirstSplit[0].equals("--kontaktpersonen")){ // if user is trying to find all direct contacts for one person
            List<Integer> ReturnList = new ArrayList<>();
            List<String> ToStringList = new ArrayList<>();
            Integer PersonID = Integer.parseInt(FirstSplit[1]);
            ReturnList = ContactPersonSearch(PersonID); // calling method that returns all contactIds
            for(int i=0; i<ReturnList.size(); i++){
                ToStringList.add(Personen.GetElementByInteger(ReturnList.get(i))); //adding all contacts to a string list
            }
            ToStringList = FuncLib.SortAscending(ToStringList); //sorting the list for output
            String AusgabeString = new String("");
            for(int i=0; i< ToStringList.size();i++){
                AusgabeString = AusgabeString + ToStringList.get(i)+", "; //putting everything in one string
            }
            AusgabeString = AusgabeString.substring(0, AusgabeString.length()-2);
            System.out.println(AusgabeString); //outputting the string
        }
        else if(FirstSplit[0].equals("--besucher")){ // user is trying to find all persons and indirect contacts for location id and timestamp
            String[] SecondSplit;
            SecondSplit = FirstSplit[1].split(",");
            VisitorSearch(Integer.parseInt(SecondSplit[0]), SecondSplit[1]); //calling method that does the work
        }
        else{
            System.err.println("Falsche Argumentenübergabe!");
        }

    }


    /*
        Method: PersonSearch
        Parameters: String pPersonName
        Return : none (void)
        purpose: Finding all personIds for name
     */
    static void PersonSearch(String pPersonName){ // finding person by name
        List<Integer> PersonKeys = new ArrayList<>();
        List<Integer> LocationKeys = new ArrayList<>();
        PersonKeys = Personen.GetKeyByValue(pPersonName); // searching for name in List and saving the matching keyvalues
        if(PersonKeys.isEmpty()){
            System.err.println("No person with that name found");
        }
        else{
            System.out.println("Es wurde(-n) " + PersonKeys.size() + " Person(-en) zur Suche gefunden."); //printing number of found persons
            for(int i=0; i<PersonKeys.size();i++){ //printing locations, name and id for every person
                String PersonName = Personen.GetElementByInteger(PersonKeys.get(i));
                LocationKeys = Timings.GetKeyForPersonID(PersonKeys.get(i));
                System.out.println("\n"+PersonName +" ("+PersonKeys.get(i)+") war zu Besuch in");
                System.out.println("===============================");
                for(int j=0;j<LocationKeys.size();j++){
                    Integer LocationKey = Timings.GetLocationID(LocationKeys.get(j));
                    String LocationName = Locations.GetNameByKey(LocationKey);
                    System.out.println(LocationName);
                }
            }
        }
    }


    /*
        Method: PlaceSearch
        Parameters: String pLocationName
        Return : none (void)
        purpose: Finding all loactions and details for given names
     */
    static void PlaceSearch(String pLocationName){ // finding location by name
        List <Integer> FoundLocationKeys = new ArrayList<>();
        FoundLocationKeys = Locations.GetKeyFromAttribute(pLocationName); //searching for all locations with matching name
        if(FoundLocationKeys.isEmpty()){
            System.err.println("No Locations found for your request");
        }
        else{
            System.out.println("Found " + FoundLocationKeys.size() + " Locations:\n"); //printing out number of matches
            for (int i = 0; i < FoundLocationKeys.size(); i++) { //printing out what I found
                String LocationName = Locations.GetNameByKey(FoundLocationKeys.get(i));
                String LocationStatus = Locations.GetStatusByKey(FoundLocationKeys.get(i));
                System.out.println("Der Ort "+LocationName+" ("+LocationStatus+") hat die ID " +FoundLocationKeys.get(i)+".");
            }
        }
    }


    /*
        Method: ContactPersonSearch
        Parameters: Integer pPersonID
        Return : List<Integer>
        purpose: Finding all direct contacts for a person
     */
    static List<Integer> ContactPersonSearch(Integer pPersonID){ //finding all direct contacts for a person
        List<Integer> pPersonTimingKeys = new ArrayList<>();
        List<Integer> pPersonTimingKeysAfterLocation = new ArrayList<>();
        List<Integer> pPersonLocationIDs = new ArrayList<>(); //Need these informations about the person
        List<Integer> ContactPersonIDs = new ArrayList<>();
        List<Integer> TempList = new ArrayList<>();
        pPersonTimingKeys = Timings.GetKeyForPersonID(pPersonID); // Getting all timingKeys where the person is mentioned
        for (int i = 0; i < pPersonTimingKeys.size(); i++) {
            if(Locations.GetStatusByKey(Timings.GetLocationID(pPersonTimingKeys.get(i))).equals("in_door")){ // checking if visited locations are indoor
                pPersonLocationIDs.add(Timings.GetLocationID(pPersonTimingKeys.get(i)));
                pPersonTimingKeysAfterLocation.add(pPersonTimingKeys.get(i)); //saving Locations and TimingKeys for every in door location the person was at
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

    /*
        Method: VisitorSearch
        Parameters: Integer GivenLocation, String GivenTimeStamp
        Return : none (void)
        purpose: Finding all direct and indirect contacts for timestamp
     */
    static void VisitorSearch(Integer GivenLocation, String GivenTimeStamp){
        List<Integer> ContactIDs = new ArrayList<>();
        List<String> ContactNames = new ArrayList<>();
        String AusgabeString = new String("");
        ContactIDs = Timings.GetPersonsForTimeStamp(GivenLocation, GivenTimeStamp); //getting all person ids at the location at this time
        ContactIDs = FuncLib.NoDoubles(ContactIDs); //deleting all double elements
        if(ContactIDs.isEmpty()){
            System.err.println("Nobody found!");
        }
        if(Locations.GetStatusByKey(GivenLocation).equals("out_door")){ // if Location is outdoor
            for (int i = 0; i < ContactIDs.size(); i++) {
                ContactNames.add(Personen.GetElementByInteger(ContactIDs.get(i))); //adding all the names to a String List
            }
            ContactNames = FuncLib.SortAscending(ContactNames); //sorting all the names
            for (int i = 0; i < ContactNames.size(); i++) {
                AusgabeString = AusgabeString + ContactNames.get(i) +", "; //putting them in one string
            }
            AusgabeString = AusgabeString.substring(0, AusgabeString.length()-2); //cutting the end so the string is correct
            System.out.println(AusgabeString); //printing the strings
        }
        else{ //if location is indoor -> I need indirect contacts too
            List<Integer> TempList = new ArrayList<>();
            List<Integer> SecondContactID = new ArrayList<>();
            List<String> SecondContactName = new ArrayList<>();
            for(int i=0;i<ContactIDs.size(); i++){ //gotta find all contacts for each contact
                TempList = ContactPersonSearch(ContactIDs.get(i)); //using method above to find all contacts
                SecondContactID.add(ContactIDs.get(i)); //adding the direct contacts to the same list one by one -> will be sorted anyway
                for (int j = 0; j < TempList.size(); j++) {
                    SecondContactID.add(TempList.get(j)); // adding second contacts to the list too
                }
            }
            SecondContactID = FuncLib.NoDoubles(SecondContactID); //deleting all doubles
            for (int i = 0; i < SecondContactID.size(); i++) {
                SecondContactName.add(Personen.GetElementByInteger(SecondContactID.get(i))); //retrieving name information
            }
            SecondContactName = FuncLib.SortAscending(SecondContactName); //sorting the names ascending
            for (int i = 0; i < SecondContactName.size(); i++) {
                AusgabeString = AusgabeString + SecondContactName.get(i)+", "; //putting everything in one string
            }
            AusgabeString = AusgabeString.substring(0, AusgabeString.length()-2); //shortening it so it matches requests
            System.out.println(AusgabeString); //outprinting the string
        }
    }
}
