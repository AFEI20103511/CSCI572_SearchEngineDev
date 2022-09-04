import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class CSVreader {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("/Users/zhengzhang/Desktop/CSCI572/HW/HW2/crawlStorageFolder/urls_NYT.csv");
        Scanner scanner = new Scanner(file);
        HashSet<String> within = new HashSet<>();
        HashSet<String> outside = new HashSet<>();
        scanner.nextLine();
        while(scanner.hasNextLine()) {
            String row = scanner.nextLine();
            String url = row.split(",")[0];
            String indicator = row.split(",")[1];

            String finalURL = url.split("//")[1];


            if(indicator.equals("OK")) {
                within.add(finalURL);
            } else {
                outside.add(finalURL);
            }
        }
        int total = within.size() + outside.size();
        System.out.println("The total number of unique urls: " + total);
        System.out.println("The total number of unique urls within: " + within.size());
        System.out.println("The total number of unique urls outside: " + outside.size());

    }
}
