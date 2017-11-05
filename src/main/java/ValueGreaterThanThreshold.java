import java.util.ArrayList;

public class ValueGreaterThanThreshold extends ValueType {
    ValueGreaterThanThreshold(ArrayList<DataBlock> dataSet, int dataValue, double threshold) {
        this.dataValue = dataValue;
        this.threshold = threshold;
        this.dataSet = dataSet;
    }

    @Override
    public boolean test() {
        return value > threshold;
    }

    @Override
    public void newValue(int i) {
        DataBlock tempDB = dataSet.get(i);
        value = tempDB.data[dataValue];
    }
}
