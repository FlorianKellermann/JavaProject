import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionLibrary {
    public Integer CheckStrings(String FirstWord, String SecondWord){ //Checking if the words are equal or if FirstWord is contained in SecondWord
        int NumberOfMatches=0;
        FirstWord = FirstWord.toUpperCase(); //converting both to Upper case so I can compare them correctly
        SecondWord = SecondWord.toUpperCase();
        if(FirstWord.equals(SecondWord)){ // easiest case (if the equal eachother), then Iam return 1 which means the words match eachother
            return 1;
        }

        char[] FirstWordArray = FirstWord.toCharArray(); //converting words to character arrays so I can use the letters individually
        char[] SecondWordArray = SecondWord.toCharArray();

        for(int i=0;i<= ( SecondWord.length()-FirstWord.length() );i++){
            for(int j=0;j<FirstWord.length();j++){ // with these two loops I can see if word one is somehow contained in word 2 (->only one direction)
                if(FirstWordArray[j]==SecondWordArray[i+j]){
                    NumberOfMatches++;
                    if(NumberOfMatches==FirstWord.length()){ //if I found all letters of word one in the exact order in word two then Iam returning 1 (=equals)
                        return 1;
                    }
                }
                else{ //if not then Iam resetting the number of matching letters and break the inner loop
                    NumberOfMatches=0;
                    break;
                }
            }
        }
        return 0;
    }

    public List<Integer> NoDoubles(List<Integer> pList){
        List<Integer> eList = pList.stream()
                .distinct()
                .collect(Collectors.toList());

        return eList;
    }



    public Integer CheckTimeSimilarity(String StartTime1, String EndTime1, String StartTime2, String EndTime2){ //checking if they have been at the same place at the same time
        String[] ST1Split = StartTime1.split("T"); //Splitting up into date and time
        String[] ET1Split = EndTime1.split("T");
        String[] ST2Split = StartTime2.split("T");
        String[] ET2Split = EndTime2.split("T");
        String[] ST1Time = ST1Split[1].split(":");
        String[] ET1Time = ET1Split[1].split(":");
        String[] ST2Time = ST2Split[1].split(":");
        String[] ET2Time = ET2Split[1].split(":");
        int FirstArriving = 4; // 0-> ST1 ; 1->ST2 ; 2->same time
        if(ST1Split[0].equals(ST2Split[0]) && ET1Split[0].equals(ET2Split[0])){ //checking if it is the same date
            if(ST1Time[0].equals(ST2Time[0])){ //Finding who arrived first at the location
                if(ST1Time[1].equals(ST2Time[1])){
                    if(ST1Time[2].equals(ST2Time[2])){
                        FirstArriving = 2;
                    }
                    else if(Integer.parseInt(ST1Time[2]) < Integer.parseInt(ST2Time[2])){
                        FirstArriving = 0;
                    }
                    else{
                        FirstArriving = 1;
                    }
                }
                else if(Integer.parseInt(ST1Time[1]) < Integer.parseInt(ST2Time[1])){
                    FirstArriving = 0;
                }
                else{
                    FirstArriving = 1;
                }

            }
            else if(Integer.parseInt(ST1Time[0]) < Integer.parseInt(ST2Time[0])){
                FirstArriving = 0;
            }
            else{
                FirstArriving =1;
            }
        }
        else{
            System.err.println("Unterschiedliche Datumsangaben!");
        }

        switch (FirstArriving) {
            case 0:
                if(Integer.parseInt(ET1Time[0])<Integer.parseInt(ST2Time[0])){
                    return 0;
                }
                else if(ET1Time[0].equals(ST2Time[0])){
                    if(Integer.parseInt(ET1Time[1])<Integer.parseInt(ST2Time[1])){
                        return 0;
                    }
                    else if(ET1Time[1].equals(ST2Time[1])){
                        if(Integer.parseInt(ET1Time[2])<Integer.parseInt(ST2Time[2])){
                            return 0;
                        }
                        else{
                            return 1;
                        }
                    }
                    else{
                        return 1;
                    }
                }
                else{
                    return 1;
                }
            case 1:
                if(Integer.parseInt(ET1Time[0])<Integer.parseInt(ST2Time[0])){
                    return 0;
                }
                else if(ET2Time[0].equals(ST1Time[0])){
                    if(Integer.parseInt(ET2Time[1])<Integer.parseInt(ST1Time[1])){
                        return 0;
                    }
                    else if(ET2Time[1].equals(ST1Time[1])){
                        if(Integer.parseInt(ET2Time[2])<Integer.parseInt(ST1Time[2])){
                            return 0;
                        }
                        else{
                            return 1;
                        }
                    }
                    else{
                        return 1;
                    }
                }
                else{
                    return 1;
                }
            case 2:
                return 1;
            default:
                System.err.println("Problem with if statement whie checking similarity");
                break;
        }

        return 0;
    }

    public List<String> SortAscending(List<String> ToBeSorted){
        ToBeSorted.sort(Comparator.comparing(String::toString));
        return ToBeSorted;
    }
}
