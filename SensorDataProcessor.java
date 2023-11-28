import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The SensorDataProcessor class processes raw sensor data based on specified
 * conditions
 * and writes the processed results to a file.
 */
public class SensorDataProcessor {

    // Constants Instead of magic numbers.
    private static final int MIN_VALUE = 10; // Minimum threshold for sensor data processing
    private static final int MAX_VALUE = 50; // Maximum threshold for sensor data processing
    private static final double POWER_CONSTANT = 2.0; // Exponential factor for sensor data calculations

    // Sensor data and limits.
    public double[][][] data; // 3D array representing raw sensor data
    public double[][] limit; // 2D array representing sensor limits

    /**
     * Constructor for the SensorDataProcessor class.
     *
     * @param data  3D array representing raw sensor data.
     * @param limit 2D array representing sensor limits.
     */
    public SensorDataProcessor(double[][][] data, double[][] limit) {
        this.data = data;
        this.limit = limit;
    }

    /**
     * Calculates the average of an array of doubles.
     *
     * @param array The array for which to calculate the average.
     * @return The average value of the array.
     */
    private double average(double[] array) {
        int i = 0;
        double sum = 0;

        // Calculate sum of array elements
        for (i = 0; i < array.length; i++) {
            sum += array[i];
        }

        // Calculate and return the average
        return sum / array.length;
    }

    /**
     * Processes raw sensor data based on specified conditions and writes the
     * results to a file.
     */
    public void calculateProcessorData() {
        double[][][] newProcessorData = new double[data.length][data[0].length][data[0][0].length];
        BufferedWriter out;

        // Write processed data into a file
        try {
            out = new BufferedWriter(new FileWriter("ProcessedSensorData.txt"));
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length; j++) {
                    for (int k = 0; k < data[0][0].length; k++) {
                        // Process sensor data based on given conditions
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

            // Write processed data to the output file
            for (int i = 0; i < newProcessorData.length; i++) {
                for (int j = 0; j < newProcessorData[0].length; j++) {
                    for (int k = 0; k < newProcessorData[0][0].length; k++) {
                        out.write(newProcessorData[i][j][k] + "\t");
                    }
                }
            }

            // Close the output stream
            out.close();
        } catch (IOException ioException) {
            System.err.println("Error occurred while writing to the file: " + ioException.getMessage());
        } catch (Exception exception) {
            System.err.println("An unexpected error occurred: " + exception.getMessage());
        }
    }
}
