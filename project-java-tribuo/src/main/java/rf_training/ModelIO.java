package rf_training;

import org.tribuo.Model;

import java.io.*;

/**
 * Handles saving and loading trained Tribuo models via Java serialization.
 */
public class ModelIO {

    // Save model to file
    public static void saveModel(Model<?> model, String modelPath) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(modelPath))) {
            out.writeObject(model); // Serialize model
        }
        System.out.println("Model saved to: " + modelPath);
    }

    // Load model from file
    @SuppressWarnings("unchecked")
    public static <T extends Model<?>> T loadModel(String modelPath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(modelPath))) {
            return (T) in.readObject(); // Deserialize model
        }
    }
}
