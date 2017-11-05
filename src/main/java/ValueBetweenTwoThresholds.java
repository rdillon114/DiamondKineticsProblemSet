import java.util.ArrayList;

public class ValueBetweenTwoThresholds extends ValueType {
    double thresholdHi = 0.0;
    ValueBetweenTwoThresholds(ArrayList<DataBlock> dataSet, int dataValue, double thresholdLo, double thresholdHi) {
        this.thresholdHi = thresholdHi;
        this.threshold = thresholdLo;
        this.dataValue = dataValue;
        this.dataSet = dataSet;
    }

    @Override
    public boolean test() {
        return value < thresholdHi && value > threshold;
    }

    @Override
    public void newValue(int i) {
        DataBlock tempDB = dataSet.get(i);
        value = tempDB.data[dataValue];
    }
}
