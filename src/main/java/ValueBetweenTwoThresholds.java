import java.util.ArrayList;

public class ValueBetweenTwoThresholds extends ValueType {
    double thresholdLo = 0.0;
    double thresholdHi = 0.0;
    int dataValue = 0;
    double value = 0.0;
    ValueBetweenTwoThresholds(ArrayList<DataBlock> dataSet, int dataValue, double thresholdLo, double thresholdHi) {
        this.thresholdHi = thresholdHi;
        this.thresholdLo = thresholdLo;
        this.dataValue = dataValue;
        this.dataSet = dataSet;
    }

    @Override
    public boolean test() {
        return value < thresholdHi && value > thresholdLo;
    }

    @Override
    public void newValue(int i) {
        DataBlock tempDB = dataSet.get(i);
        value = tempDB.data[dataValue];
    }
}
