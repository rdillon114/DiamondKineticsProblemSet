
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * This class opens a given DK swing data file, parses it, and stores the data blocks in an ArrayList.
 * @author Richard Dillon
 */
public class DKSwingData {
    
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
    DKSwingData(String filename) throws FileNotFoundException {
        File swingDataFile = new File(filename);
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
     * @throws IndexOutOfBoundsException if the beginning or end indexes are out of range.
     */
    public int searchContinuityAboveValue(int dataValue, int indexBegin, int indexEnd, double threshold, int winLength) throws IndexOutOfBoundsException {
        ValueType typeObject = new ValueGreaterThanThreshold(dataSet, dataValue, threshold);
        return indexSearchHelperMethod(true, typeObject, indexBegin, indexEnd, winLength);
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
     * @throws IndexOutOfBoundsException if the beginning or end indexes are out of range.
     */
    public int backSearchContinuityWithinRange(int dataValue, int indexBegin, int indexEnd, double thresholdLo, double thresholdHi, int winLength) throws IndexOutOfBoundsException {
        ValueType typeObject = new ValueBetweenTwoThresholds(dataSet, dataValue, thresholdLo, thresholdHi);
        return indexSearchHelperMethod(false, typeObject, indexBegin, indexEnd, winLength);
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
     * @throws IndexOutOfBoundsException if the beginning or end indexes are out of range.
     */
    public int searchContinuityAboveValueTwoSignals(int dataValue1, int dataValue2, int indexBegin, int indexEnd, double threshold1, double threshold2, int winLength) throws IndexOutOfBoundsException {
        ValueType typeObject = new TwoValuesGreaterTwoThresholds(dataSet, dataValue1, dataValue2, threshold1, threshold2);
        return indexSearchHelperMethod(true, typeObject, indexBegin, indexEnd, winLength);
    }

    /**
     * This is a helper method for searchContinuityAboveValueTwoSignals, backSearchContinuityWithinRange, and searchContinuityAboveValue.
     * @param direction the direction in which we loop.  If true, loop forwards.  If false, loop backwards.
     * @param typeObject the ValueType object that defines the test conditions we need for this method.
     * @param indexBegin the start index.
     * @param indexEnd the end index.
     * @param winLength the window length.
     * @return the start index of the first window found that is at least the size of winLength.
     * @throws IndexOutOfBoundsException if the beginning or end indexes are out of range.
     */
    public int indexSearchHelperMethod(boolean direction, ValueType typeObject, int indexBegin, int indexEnd, int winLength) throws IndexOutOfBoundsException {
        int returnIndex = -1;
        int currentWinLength = 0;

        for(int i = indexBegin; direction ? i <= indexEnd : i >= indexEnd; i = direction ? i+1 : i-1) {
            typeObject.newValue(i);
            if(typeObject.test()) {
                currentWinLength++;
                if(currentWinLength == 1) {
                    returnIndex = i;
                }
                if(currentWinLength == winLength) {
                    break;
                }
            }
            else {
                currentWinLength = 0;
                returnIndex = -1;
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
     * @throws IndexOutOfBoundsException if the beginning or end indexes are out of range.
     */
    public Collection<Pair<Integer, Integer>> searchMultiContinuityWithinRange(int dataValue, int indexBegin, int indexEnd, double thresholdLo, double thresholdHi, int winLength) throws IndexOutOfBoundsException {
        Collection<Pair<Integer, Integer>> results = new ArrayList<>();
        int currentWinLength = 0;
        int startIndex = -1;
        boolean metWindow = false;

        for (int i = indexBegin; i <= indexEnd; i++) {
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
                    Pair<Integer, Integer> p = new Pair<>(startIndex, i - 1);
                    results.add(p);
                }
                currentWinLength = 0;  //reset the currentWinLength if the value is not between thresholdLo and thresholdHi.
                startIndex = -1;  //reset the startIndex if the value is not between thresholdLo and thresholdHi.
                metWindow = false;  //reset metWindowif the value is not between thresholdLo and thresholdHi.
            }
        }
        if(metWindow) {
            Pair<Integer, Integer> p = new Pair<>(startIndex, dataSet.size() - 1);
            results.add(p);
        }
        return results;
    }
}
