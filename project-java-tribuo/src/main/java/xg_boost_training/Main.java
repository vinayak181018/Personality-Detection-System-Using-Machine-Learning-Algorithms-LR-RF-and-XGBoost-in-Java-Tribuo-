package xg_boost_training;

import org.tribuo.Dataset;
import org.tribuo.classification.Label;
import xg_boost_training.PersonalityDataLoader; // import data loader

/**
 * Main entry point - loads data, trains and tunes model.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        PersonalityDataLoader dataLoader = new PersonalityDataLoader();
        String csvPath = "C://Users//vinay//Downloads//personality_datasert.csv"; // dataset file path

        // Load train/test datasets with 70% training split and fixed seed
        Dataset<Label>[] datasets = dataLoader.loadData(csvPath, 0.7, 1L);
        Dataset<Label> train = datasets[0];
        Dataset<Label> test = datasets[1];

        // Create trainer and run tuning + training process
        PersonalityTrainer trainer = new PersonalityTrainer();
        trainer.tuneAndTrain(train, test);
    }
}
