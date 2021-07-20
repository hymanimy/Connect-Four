import java.io.File;                   // Import the File class
import java.io.FileWriter;             // Import the FileWriter class
import java.io.IOException;            // Import the IOException class to handle errors
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner;              // Import the Scanner class to read text files

public class FileHandling {

    public static void createFile(String filename){

        // Creates a file called "<filename>.txt" and places it in the savedBoards folder

        try {
            // Use a file seperator since path notation is inconsistent across platforms
            File f = new File("savedBoards" + File.separator + filename + ".txt");

            if (f.createNewFile()) {
                System.out.println("File created: " + f.getName());
                System.out.println("Absolute path: " + f.getAbsolutePath());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            // This occurs if the file could not be created for some reason
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void writeToFile(String filename, String[] input){
        // writes to a file "<filename>.txt" within the savedBoards folder, with a String array as input
        try {

            FileWriter myWriter = new FileWriter("savedBoards" + File.separator + filename + ".txt");

            // Write each line and then take a new line
            for(String line : input){
                myWriter.write(line);
                myWriter.write("\n");

            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static String[] readFile(String filename, int numberOfLines){
        String[] output = new String[numberOfLines];
        try {
            File myObj = new File("savedBoards" + File.separator + filename + ".txt");
            Scanner myReader = new Scanner(myObj);

            int i = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine(); // read the line
                output[i] = data; // save the data
                i++; // increment line counter
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return output;
    }

    public static int numberOfLinesInFile(String filename){
        // naive way to find the number of lines in a file
        try {
            File myObj = new File("savedBoards" + File.separator + filename + ".txt");
            Scanner myReader = new Scanner(myObj);

            int numberOfLines = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine(); // read the line
                numberOfLines++; // increment line counter
            }

            myReader.close();
            return numberOfLines;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean validFilename(String filename){
        String[] filenames = readFile("validFilenames", numberOfLinesInFile("validFilenames"));
        for(String name : filenames){
            if(name.equals(filename)){
                return true;
            }
        }
        return false;

    }
}
