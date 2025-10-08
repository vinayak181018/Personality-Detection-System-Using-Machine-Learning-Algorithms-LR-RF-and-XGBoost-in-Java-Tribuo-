package rf_training;

import org.tribuo.Dataset;
import org.tribuo.classification.Label;

/**
 * Main execution class — loads data, trains a Random Forest, evaluates, and saves the model.
 */
public class Main_rf {
    public static void main(String[] args) throws Exception {
        String csvPath = "C://Users//vinay//Downloads//personality_datasert.csv";

        // Load datasets
        PersonalityDataLoader_rf loader = new PersonalityDataLoader_rf();
        loader.loadDataset(csvPath);

        Dataset<Label> train = loader.getTrainDataset();
        Dataset<Label> test = loader.getTestDataset();

        // Train and evaluate Random Forest model
        PersonalityTrainer_rf trainer = new PersonalityTrainer_rf(train);
        String modelPath = "C:\\Users\\vinay\\Documents\\PersonalityModel.model";
        trainer.trainEvaluateAndSaveModel(test, modelPath);
    }
}
