import java.util.ArrayList;

public abstract class ValueType {
    ArrayList<DataBlock> dataSet = new ArrayList<>();
    public abstract boolean test();
    public abstract void newValue(int i);
}
