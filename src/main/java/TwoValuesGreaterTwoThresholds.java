import java.util.ArrayList;

public class TwoValuesGreaterTwoThresholds extends ValueType {
    int dataValue2 = 0;
    double threshold2 = 0.0;
    double value2 = 0.0;

    TwoValuesGreaterTwoThresholds(ArrayList<DataBlock> dataSet, int dataValue1, int dataValue2, double threshold1, double threshold2) {
        this.dataSet = dataSet;
        this.dataValue = dataValue1;
        this.dataValue2 = dataValue2;
        this.threshold = threshold1;
        this.threshold2 = threshold2;
    }

    @Override
    public boolean test() {
        return value > threshold && value2 > threshold2;
    }

    @Override
    public void newValue(int i) {
        DataBlock tempDB = dataSet.get(i);
        value = tempDB.data[dataValue];
        value2 = tempDB.data[dataValue2];
    }
}
