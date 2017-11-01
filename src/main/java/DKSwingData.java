
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class opens a given DK swing data file, parses it, and stores the data blocks in an ArrayList.
 * @author Richard Dillon
 */
public class DKSwingData {
    
    
    class DataBlock {
        long timestamp;
        double[] data = new double[6];
        DataBlock(long timestamp, double ax, double ay, double az, double wx, double wy, double wz) {
            this.timestamp = timestamp;
            data[AX] = ax;
            data[AY] = ay;
            data[AZ] = az;
            data[WX] = wx;
            data[WY] = wy;
            data[WZ] = wz;
        }
    }
    
    ArrayList<DataBlock> dataSet = new ArrayList<>();
    //these are the indexes in the DataBlock data array of ax, ay, az, wx, wy, and wz.
    public static final int AX = 0;
    public static final int AY = 1;
    public static final int AZ = 2;
    public static final int WX = 3;
    public static final int WY = 4;
    public static final int WZ = 5;
    
    /**
     * Initializes the dataSet array to contain all data blocks within the given file.
     * @param filename the name of the swing data file.
     */
    DKSwingData(String filename) {
        File swingDataFile = new File(filename);
        try {
            Scanner scanner = new Scanner(swingDataFile);
            while(scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                long timestamp = Long.parseLong(values[0]);
                double ax = Double.parseDouble(values[1]);
                double ay = Double.parseDouble(values[2]);
                double az = Double.parseDouble(values[3]);
                double wx = Double.parseDouble(values[4]);
                double wy = Double.parseDouble(values[5]);
                double wz = Double.parseDouble(values[6]);
                DataBlock tempDB = new DataBlock(timestamp, ax, ay, az, wx, wy, wz);
                dataSet.add(tempDB);
            }
            scanner.close();
        } catch (FileNotFoundException ex) {
            System.out.println("The specified file was not found.");
        }
    }
    
    /**
     * This method returns the start index of the first window of winLength 
     * consecutive values greater than the specified threshold.
     * @param dataValue the data field that is searched.
     * @param indexBegin the start index.
     * @param indexEnd the end index.
     * @param threshold the value threshold.
     * @param winLength the window length.
     * @return the start index of the first window found that is at least the size of winLength.
     */
    public int searchContinuityAboveValue(int dataValue, int indexBegin, int indexEnd, double threshold, int winLength) {
        int returnIndex = -1;
        if(indexEnd >= indexBegin) {
            int currentWinLength = 0;  //once the currentWinLength equals winLength, we have met the requirement, and can return the start index.
            
            for (int i = indexBegin; i <= indexEnd; i++) {
                if(i < dataSet.size() && i >= 0) {//check if i is a valid index
                    DataBlock tempDB = dataSet.get(i);
                    if(tempDB.data[dataValue] > threshold) {
                        currentWinLength++;
                        if(currentWinLength == 1) {
                            returnIndex = i;
                        }
                        if(currentWinLength == winLength) {//assuming winLength is greater than zero.
                            break;
                        }
                    }
                    else {
                        currentWinLength = 0;  //reset the currentWinLength if the threshold is not met.
                        returnIndex = -1;  //reset the returnIndex if the threshold is not met.
                    }
                }
            }
            if(currentWinLength != winLength) {
                returnIndex = -1;
            }
        }
        return returnIndex;
    }
    
    /**
     * This method returns the start index of the first window of winLength 
     * consecutive values greater than thresholdLo, and less than thresholdHi.
     * It starts at the beginning index, and searches backwards to the end index.
     * @param dataValue the data field that is searched.
     * @param indexBegin the start index.
     * @param indexEnd the end index.
     * @param thresholdLo the lower threshold.
     * @param thresholdHi the upper threshold.
     * @param winLength the window length.
     * @return the start index of the first window found that is at least the size of winLength.
     */
    public int backSearchContinuityWithinRange(int dataValue, int indexBegin, int indexEnd, double thresholdLo, double thresholdHi, int winLength) {
        int returnIndex = -1;
        int currentWinLength = 0;  //once the currentWinLength equals winLength, we have met the requirement, and can return the start index.

        for (int i = indexBegin; i >= indexEnd; i--) {
            if(i < dataSet.size() && i >= 0) {//check if i is a valid index
                DataBlock tempDB = dataSet.get(i);
                if(tempDB.data[dataValue] > thresholdLo && tempDB.data[dataValue] < thresholdHi) {
                    currentWinLength++;
                    if(currentWinLength == 1) {
                        returnIndex = i;
                    }
                    if(currentWinLength == winLength) {//assuming winLength is greater than zero.
                        break;
                    }
                }
                else {
                    currentWinLength = 0;  //reset the currentWinLength if the value is not between thresholdLo and thresholdHi.
                    returnIndex = -1;  //reset the returnIndex if the value is not between thresholdLo and thresholdHi.
                }
            }
        }
        if(currentWinLength != winLength) {
            returnIndex = -1;
        }
        return returnIndex;
    }
    
    /**
     * This method returns the start index of the first window of winLength 
     * consecutive values that have dataValue1's field greater than threshold1,
     * and dataValue2's field greater than threshold2.
     * @param dataValue1 the first data field that is being searched.
     * @param dataValue2 the second data field that is being searched.
     * @param indexBegin the start index.
     * @param indexEnd the end index.
     * @param threshold1 the threshold associated with the first data field.
     * @param threshold2 the threshold associated with the second data field.
     * @param winLength the window length.
     * @return the start index of the first window found that is at least the size of winLength.
     */
    public int searchContinuityAboveValueTwoSignals(int dataValue1, int dataValue2, int indexBegin, int indexEnd, double threshold1, double threshold2, int winLength) {
        int returnIndex = -1;
        int currentWinLength = 0;  //once the currentWinLength equals winLength, we have met the requirement and can return the start index.

        for (int i = indexBegin; i <= indexEnd; i++) {
            if(i < dataSet.size() && i >= 0) {//check if i is a valid index
                DataBlock tempDB = dataSet.get(i);
                if(tempDB.data[dataValue1] > threshold1 && tempDB.data[dataValue2] > threshold2) {
                    currentWinLength++;
                    if(currentWinLength == 1) {
                        returnIndex = i;
                    }
                    if(currentWinLength == winLength) {//assuming winLength is greater than zero.
                        break;
                    }
                }
                else {
                    currentWinLength = 0;  //reset the currentWinLength if either threshold is not met.
                    returnIndex = -1;  //reset the returnIndex if either threshold is not met.
                }
            }
        }
        if(currentWinLength != winLength) {
            returnIndex = -1;
        }
        return returnIndex;
    }
    
    /**
     * This method returns a list of start and end indexes of windows that were 
     * found to have values between thresholdLo and thresholdHi.  The windows 
     * that are found must have at least winLength consecutive values between 
     * thresholdLo and thresholdHi.
     * @param dataValue the data field that is searched.
     * @param indexBegin the start index.
     * @param indexEnd the end index.
     * @param thresholdLo the lower threshold.
     * @param thresholdHi the upper threshold.
     * @param winLength the window length.
     * @return an ArrayList of beginning and end indexes.
     */
    public ArrayList<ArrayList<Integer>> searchMultiContinuityWithinRange(int dataValue, int indexBegin, int indexEnd, double thresholdLo, double thresholdHi, int winLength) {
        ArrayList<ArrayList<Integer>> results = new ArrayList<>();
        int currentWinLength = 0;
        int startIndex = -1;
        boolean metWindow = false;

        for (int i = indexBegin; i <= indexEnd; i++) {
            if(i < dataSet.size() && i >= 0) {//check if i is a valid index
                DataBlock tempDB = dataSet.get(i);
                if(tempDB.data[dataValue] > thresholdLo && tempDB.data[dataValue] < thresholdHi) {
                    currentWinLength++;
                    if(currentWinLength == 1) {
                        startIndex = i;
                    }
                    if(currentWinLength >= winLength) {//assuming winLength is greater than zero.
                        metWindow = true;
                    }
                }
                else {
                    if(metWindow) { //if we met the given winLength with the last series of values between thresholdLo and thresholdHi
                        ArrayList<Integer> indexes = new ArrayList<>();
                        indexes.add(startIndex);
                        indexes.add(i - 1);
                        results.add(indexes);
                    }
                    currentWinLength = 0;  //reset the currentWinLength if the value is not between thresholdLo and thresholdHi.
                    startIndex = -1;  //reset the startIndex if the value is not between thresholdLo and thresholdHi.
                    metWindow = false;  //reset metWindowif the value is not between thresholdLo and thresholdHi.
                }
            }
        }
        if(metWindow) {
            ArrayList<Integer> indexes = new ArrayList<>();
            indexes.add(startIndex);
            indexes.add(dataSet.size() - 1);
            results.add(indexes);
        }
        return results;
    }
}
