
import static org.junit.Assert.assertEquals;

import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

/**
 * This class will contain test cases for DKSwingData
 * @author Richard Dillon
 */
public class DKSwingDataTest {

    DKSwingData dataObject;
    
    @Before
    public void initialize() throws FileNotFoundException {
        dataObject = new DKSwingData("latestSwing.csv");
    }
    
    @Test
    public void testSearchContinuityAboveValueAXOne() {
        int index = dataObject.searchContinuityAboveValue(DKSwingData.AX, 10, 1000, 1, 35);
        assertEquals(index, 37);
    }
    
    @Test
    public void testSearchContinuityAboveValueAXTwo() {
        int index = dataObject.searchContinuityAboveValue(DKSwingData.AX, 10, 1000, 1, 50);
        assertEquals(index, -1);
    }
    
    @Test
    public void testSearchContinuityAboveValueAXThree() {
        int index = dataObject.searchContinuityAboveValue(DKSwingData.AX, 0, 1275, 0, 700);
        assertEquals(index, 20);
    }
    
    @Test
    public void testSearchContinuityAboveValueAXFour() {
        int index = dataObject.searchContinuityAboveValue(DKSwingData.AX, 0, 1275, 0, 800);
        assertEquals(index, -1);
    }
    
    @Test
    public void testBackSearchContinuityWithinRangeAXOne() {
        int index = dataObject.backSearchContinuityWithinRange(DKSwingData.AX, 88, 0, 0.5, 0.7, 6);
        assertEquals(index, 31);
    }

    @Test
    public void testBackSearchContinuityWithinRangeAXTwo() {
        int index = dataObject.backSearchContinuityWithinRange(DKSwingData.AX, 88, 0, 0.5, 0.7, 5);
        assertEquals(index, 88);
    }

    @Test
    public void testSearchContinuityAboveValueTwoSignalsAXAYOne() {
        int index = dataObject.searchContinuityAboveValueTwoSignals(DKSwingData.AX, DKSwingData.AY, 0, 100, 1, 1, 7);
        assertEquals(index, 37);
    }

    @Test
    public void testSearchContinuityAboveValueTwoSignalsAXAYTwo() {
        int index = dataObject.searchContinuityAboveValueTwoSignals(DKSwingData.AX, DKSwingData.AY, 0, 100, 1, 1, 8);
        assertEquals(index, -1);
    }

    @Test
    public void testSearchMultiContinuityWithinRangeAXOne() {
        Collection<Pair<Integer, Integer>> indexes = dataObject.searchMultiContinuityWithinRange(DKSwingData.AX, 0, 100, 1, 1.5, 5);
        assertEquals(indexes.size(), 1);
        Iterator<Pair<Integer, Integer>> it = indexes.iterator();
        Pair p = it.next();
        assertEquals(p.getKey(), 37);
        assertEquals(p.getValue(), 72);
    }

    @Test
    public void testSearchMultiContinuityWithinRangeAXTwo() {
        Collection<Pair<Integer, Integer>> indexes = dataObject.searchMultiContinuityWithinRange(DKSwingData.AX, 0, 100, 0.6, 1.5, 1);
        assertEquals(indexes.size(), 3);
        Iterator<Pair<Integer, Integer>> it = indexes.iterator();
        Pair p = it.next();
        assertEquals(p.getKey(), 29);
        assertEquals(p.getValue(), 90);
    }
}
