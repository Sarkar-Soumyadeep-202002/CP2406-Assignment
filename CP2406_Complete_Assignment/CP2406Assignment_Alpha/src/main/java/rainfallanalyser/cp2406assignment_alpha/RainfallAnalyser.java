package rainfallanalyser.cp2406assignment_alpha;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Objects;

import org.apache.commons.csv.*;

/**
 * CP2406 Assignment - Soumyadeep Sarkar
 * Alpha Version
 * This program will get rainfall data from a file that the user specifies
 * and return the analysed data in a format for a visualiser.
 * This version can only analyse files stored in the rainfalldata directory of the project.
 */
public class RainfallAnalyser {

    public static void main(String[] args) {

        /* Display a welcome message for the user.*/
        System.out.println("Welcome to the Rainfall Analyser");
        System.out.println("This program analyses the rainfall data given from various sources like BOM");
        System.out.println("It will then return the extracted Total Monthly Rainfall for the data set");
        System.out.println("as well as the minimum and maximum rainfall that occurred that month.");
        System.out.println("Enter zero to exit the program.");

        while (true) {
            try {
                String filename = getFilename(); // Retrieve the name of the file selected by the user.
                if (filename == null) {
                    System.out.println("Goodbye!"); //Display a message when the user quits the program.
                    break;
                }
                ArrayList<String> analysedRainfallData = analyseRainfallData(filename);
                saveRainfallData(analysedRainfallData, filename);
                System.out.println(filename + " successfully analysed!");

            } catch (Exception e) {
                System.out.println("Error: there was an issue");
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Writes the analysed rainfall data to the /rainfalldata_analysed/ directory
     * Added as a separate method at the end to avoid creating unnecessary files
     */
    private static void saveRainfallData(ArrayList<String> analysedRainfallData, String filename) {
        // Set the output file based on the file being analysed
        TextIO.writeFile(getSavePath(filename));
        TextIO.putln("year,month,total,minimum,maximum");

        for (String rainfallData: analysedRainfallData) {
            TextIO.putln(rainfallData);
        }
    }

    /**
     * Load a file to be analysed and create an ArrayList representing rainfall data for that csv file.
     * Uses the Commons CSV package from Apache (https://commons.apache.org/proper/commons-csv/)
     */
    private static ArrayList<String> analyseRainfallData(String fileName) throws Exception {

        // Check if the file is empty
        File f = new File("src/main/resources/rainfalldata/" + fileName);
        if (f.length() == 0)
            throw new Exception("That file is empty");

        // Create the FileReader and CSV reader objects.
        // See https://commons.apache.org/proper/commons-csv/ for reference.
        Reader reader = new FileReader("src/main/resources/rainfalldata/" + fileName);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(reader); // Read the csv file with the selected path and store in records.


        int year, month, day, currentYear = 0, currentMonth = 1;
        double monthlyTotal = 0.0, rainfall, minRainfall = Double.POSITIVE_INFINITY, maxRainfall = 0.0;
        ArrayList<String> rainfallData = new ArrayList<>();

        for (CSVRecord record : records) {
            // Get the data from specific columns of the Rainfall Data CSV file.
            String yearText = record.get("Year");
            String monthText = record.get("Month");
            String dayText = record.get("Day");
            String rainfallText = record.get("Rainfall amount (millimetres)");

            // Convert the data into integer.
            year = Integer.parseInt(yearText);
            month = Integer.parseInt(monthText);
            day = Integer.parseInt(dayText);

            // Check if the recorded date is valid
            if ((month < 1 || month > 12) || (day < 1 || day > 31)) {
                System.out.println("Error: Invalid format for dates");
                throw new NumberFormatException ("Dates are out of expected range.");
            }

            // Check if there is data for rainfall, otherwise assign zero to the variable rainfall.
            rainfall = Objects.equals(rainfallText, "") ? 0 : Double.parseDouble(rainfallText);

            // Check to see if it's the next month. If it is the next month, save the data and reset the total values to 0.
            if (month != currentMonth) {
                // Check if it is the first year before saving the data
                rainfallData.add(writeCurrentData(monthlyTotal, minRainfall, maxRainfall, currentMonth, currentYear == 0? year : currentYear));
                currentYear = year;
                currentMonth = month;
                monthlyTotal = 0;
                maxRainfall = 0.0;
                minRainfall = Double.POSITIVE_INFINITY; // Assign the default value of the minRainfall variable as positive infinity.
            }

            // Update the total rainfall for the month.
            monthlyTotal += rainfall; // Calculate the monthly rainfall.
            if (rainfall > maxRainfall) maxRainfall = rainfall;
            if (rainfall < minRainfall) minRainfall = rainfall;
        }
        // Catch an incomplete month when exiting the for loop
        rainfallData.add(writeCurrentData(monthlyTotal, minRainfall, maxRainfall, currentMonth, currentYear)); // Convert the data to String before adding it to rainfallData.
        return rainfallData;
    }

    /**
     * Return a String representation of the months data
     */
    private static String writeCurrentData(double monthlyTotal, double minRainfall, double maxRainfall, int month, int year) {
        return String.format("%d,%d,%1.2f,%1.2f,%1.2f", year, month, monthlyTotal, minRainfall, maxRainfall); // Arrange the data in the correct format.
    }

    /**
     * Get a list of available rainfall data sets to be analysed.
     * Allows the user to pick which data set to analyse from this list.
     */
    private static String getFilename() {
        System.out.println("\nThe files available are:");
        File f = new File("src/main/resources/rainfalldata");
        String[] pathNames = f.list(); // Create an array with the names of all the files in the given directory.

        assert pathNames != null; // Check if the array stores the names of the files.
        for (int i = 0; i < pathNames.length; i++) {
            System.out.println((i+1) + ": " + pathNames[i]); // Display the names of the files in the correct format.
        }

        System.out.println("\nWhich file would you like to analyse? Enter the corresponding number");

        int fileNumber;
        String filename;
        while (true) {
            // Check that the selected file is valid. TextIO handles a non-Int input.
            try {
                fileNumber = TextIO.getInt();
                if (fileNumber == 0) {
                    return null;
                }
                filename = pathNames[fileNumber - 1]; // Retrieve the required file name from the array on the basis of the user input.
                break;
            }
            catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("That is outside of the range of available data files to analyse.");
                System.out.println("Please choose another one");
            }
        }
        return filename;
    } // End getFilename

    /**
     * Return the path for an analysed rainfall data file
     */
    private static String getSavePath(String filename) {
        String[] filenameElements = filename.trim().split("\\."); // Extract the file path of the file which was recently analysed.
        return "src/main/resources/rainfalldata_analysed/" + filenameElements[0] + "_analysed.csv"; // Display the name of the analysed file.
    }
}

