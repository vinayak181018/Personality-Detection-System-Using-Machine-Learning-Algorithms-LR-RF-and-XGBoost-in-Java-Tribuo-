package lg_training;

import org.tribuo.Dataset;
import org.tribuo.classification.Label;
import org.tribuo.classification.evaluation.LabelEvaluator;
import org.tribuo.classification.evaluation.LabelEvaluation;
import org.tribuo.classification.sgd.linear.LogisticRegressionTrainer;

public class PersonalityTrainer {
    public static void main(String[] args) throws Exception {
        // Path to the CSV file
        String csvPath = "C://Users//vinay//Downloads//personality_datasert.csv";

        // Load data
        PersonalityDataLoader loader = new PersonalityDataLoader();
        loader.loadDataset(csvPath);

        // Get training and testing data
        Dataset<Label> train = loader.getTrainDataset();
        Dataset<Label> test  = loader.getTestDataset();

        // Create logistic regression trainer (SGD-based)
        LogisticRegressionTrainer trainer = new LogisticRegressionTrainer();

        // Train the model on the training dataset
        var model = trainer.train(train);

        // Create evaluator to test the model
        LabelEvaluator evaluator = new LabelEvaluator();

        // Evaluate the model using test data
        LabelEvaluation evaluation = evaluator.evaluate(model, test);

        // Print overall evaluation metrics
        System.out.println("\nEvaluation Results:");
        System.out.println(evaluation);

        // Print confusion matrix for prediction accuracy across classes
        System.out.println("\nConfusion Matrix:");
        System.out.println(evaluation.getConfusionMatrix());
    }
}
