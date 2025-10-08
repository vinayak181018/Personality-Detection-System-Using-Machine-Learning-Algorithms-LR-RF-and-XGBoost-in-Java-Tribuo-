package rf_training;

import org.tribuo.Feature;
import org.tribuo.Example;
import org.tribuo.Prediction;
import org.tribuo.classification.Label;
import org.tribuo.impl.ArrayExample;
import org.tribuo.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Loads a saved Random Forest model, accepts user input for feature values,
 * builds an Example, and predicts personality classification.
 */
public class PersonalityDetection_rf {

    public static void main(String[] args) {
        try {
            String modelPath = "C:\\Users\\vinay\\Documents\\PersonalityModel.model";
            
            // Load saved model
            Model<Label> model = ModelIO.loadModel(modelPath);
            System.out.println("Model loaded successfully!\n");

            // Show available features used by the model
            System.out.println("=== Model Feature Names ===");
            System.out.println(model.getFeatureIDMap().keySet());
            System.out.println();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter personality features (matching your CSV format):");

            // Collect numerical & categorical inputs
            System.out.print("Time_spent_Alone: ");
            double timeSpentAlone = Double.parseDouble(scanner.nextLine());

            System.out.print("Stage_fear (Yes/No): ");
            String stageFear = scanner.nextLine().trim();

            System.out.print("Social_event_attendance: ");
            double socialEventAttendance = Double.parseDouble(scanner.nextLine());

            System.out.print("Going_outside: ");
            double goingOutside = Double.parseDouble(scanner.nextLine());

            System.out.print("Drained_after_socializing (Yes/No): ");
            String drainedAfterSocializing = scanner.nextLine().trim();

            System.out.print("Friends_circle_size: ");
            double friendsCircleSize = Double.parseDouble(scanner.nextLine());

            System.out.print("Post_frequency: ");
            double postFrequency = Double.parseDouble(scanner.nextLine());

            // Create features list (names must match model feature naming convention)
            List<Feature> features = new ArrayList<>();

            // Numeric features use "featureName@value"
            features.add(new Feature("Time_spent_Alone@value", timeSpentAlone));
            features.add(new Feature("Social_event_attendance@value", socialEventAttendance));
            features.add(new Feature("Going_outside@value", goingOutside));
            features.add(new Feature("Friends_circle_size@value", friendsCircleSize));
            features.add(new Feature("Post_frequency@value", postFrequency));

            // Categorical features use "featureName@CategoryValue"
            features.add(new Feature("Stage_fear@" + stageFear, 1.0));
            features.add(new Feature("Drained_after_socializing@" + drainedAfterSocializing, 1.0));

            // Display constructed features
            System.out.println("\nFeatures for prediction:");
            for (Feature f : features) {
                System.out.println(f.getName() + " -> " + f.getValue());
            }

            // Dummy label for example creation (Tribuo requires an output placeholder)
            Label dummy = new Label("UNK");
            Example<Label> example = new ArrayExample<>(dummy, features);

            // Predict personality
            Prediction<Label> prediction = model.predict(example);

            // Output prediction scores and label
            System.out.println("\nClass Probabilities: " + prediction.getOutputScores());
            System.out.println("Predicted Personality: " + prediction.getOutput().getLabel());

            scanner.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
