import java.util.ArrayList;

public abstract class ValueType {
    double value = 0.0;
    double threshold = 0.0;
    int dataValue = -1;
    ArrayList<DataBlock> dataSet = new ArrayList<>();
    public abstract boolean test();
    public abstract void newValue(int i);
}
