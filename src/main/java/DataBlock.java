public class DataBlock {
    long timestamp;
    double[] data = new double[6];
    DataBlock(long timestamp, double ax, double ay, double az, double wx, double wy, double wz) {
        this.timestamp = timestamp;
        data[DKSwingData.AX] = ax;
        data[DKSwingData.AY] = ay;
        data[DKSwingData.AZ] = az;
        data[DKSwingData.WX] = wx;
        data[DKSwingData.WY] = wy;
        data[DKSwingData.WZ] = wz;
    }
}
