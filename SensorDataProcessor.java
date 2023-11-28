import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SensorDataProcessor {

    public static void main(String[] args) {
        // Sensor data
        double[][][] data = { { { 1, 2, 3 }, { 4, 5, 6 } }, { { 7, 8, 9 }, { 10, 11, 12 } } };
        // Sensor limits
        double[][] limit = { { 1, 2 }, { 3, 4 } };
        SensorDataProcessor processor = new SensorDataProcessor(data, limit);
        processor.calculateProcessorData();
    }


    // Constants Instead of magic numbers.
    private static final int MIN_VALUE = 10; // Minimum number for sensor data processing
    private static final int MAX_VALUE = 50; // Maximum number for sensor data processing
    private static final double POWER_CONSTANT = 2.0; // Power constant for sensor data calculations

    // Sensor data and limits.
    public double[][][] data;
    public double[][] limit;

    // Constructor
    public SensorDataProcessor(double[][][] data, double[][] limit) {
        this.data = data;
        this.limit = limit;
    }

    // Calculates average of sensor data
    private double average(double[] array) {
        int i = 0;
        double val = 0;
        for (i = 0; i < array.length; i++) {
            val += array[i];
        }
        return val / array.length;
    }

    // Calculate data
    public void calculateProcessorData() {
        
        double[][][] newProcessorData = new double[data.length][data[0].length][data[0][0].length];
        BufferedWriter out;

        // Write racing stats data into a file
        try {
            out = new BufferedWriter(new FileWriter("RacingStatsData.txt"));
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length; j++) {
                    for (int k = 0; k < data[0][0].length; k++) {
                        newProcessorData[i][j][k] = data[i][j][k] / Math.pow(limit[i][j], POWER_CONSTANT);
                        if (average(newProcessorData[i][j]) > MIN_VALUE && average(newProcessorData[i][j]) < MAX_VALUE)
                            break;
                        else if (Math.max(data[i][j][k], newProcessorData[i][j][k]) > data[i][j][k])
                            break;
                        else if (Math.pow(Math.abs(data[i][j][k]), 3) < Math.pow(Math.abs(newProcessorData[i][j][k]), 3)
                                && average(data[i][j]) < newProcessorData[i][j][k] && (i + 1) * (j + 1) > 0)
                            newProcessorData[i][j][k] *= 2;
                        else
                            continue;
                    }
                }
            }
            for (int i = 0; i < newProcessorData.length; i++) {
                for (int j = 0; j < newProcessorData[0].length; j++) {
                    for (int k = 0; k < newProcessorData[0][0].length; k++) {
                        out.write(newProcessorData[i][j][k] + "\t");
                    }
                }
            }
            out.close();
        } catch (Exception e) {
            System.out.println("Error= " + e);
        }
    }
}
