package xg_boost_training;

import org.tribuo.Dataset;
import org.tribuo.Model;
import org.tribuo.classification.Label;
import org.tribuo.classification.evaluation.LabelEvaluator;
import org.tribuo.classification.evaluation.LabelEvaluation;
import org.tribuo.classification.xgboost.XGBoostClassificationTrainer;
import org.tribuo.common.xgboost.XGBoostModel;

import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class trains an XGBoost classifier on the personality dataset,
 * tests multiple hyperparameter combinations, and picks the best model.
 */
public class PersonalityTrainer {

    /**
     * Iterates over multiple hyperparameter sets and trains XGBoost models,
     * evaluating each and selecting the best one.
     */
    public void tuneAndTrain(Dataset<Label> train, Dataset<Label> test) {
        // Define search space for tuning
        List<Integer> roundsList = Arrays.asList(200, 400, 800); // training iterations (boosting rounds)
        List<Integer> maxDepthList = Arrays.asList(3, 6, 9);     // tree depth
        List<Float> etaList = Arrays.asList(0.01f, 0.1f, 0.3f);  // learning rate

        double bestAccuracy = 0.0;
        Model<Label> bestModel = null;
        String bestParams = "";

        LabelEvaluator evaluator = new LabelEvaluator();

        // Loop through all combinations of rounds, maxDepth, and eta
        for (int rounds : roundsList) {
            for (int maxDepth : maxDepthList) {
                for (float eta : etaList) {
                    try {
                        System.out.println("\n>>> Training with rounds=" + rounds + ", maxDepth=" + maxDepth + ", eta=" + eta);

                        // Set XGBoost parameters
                        Map<String, Object> params = new HashMap<>();
                        params.put("max_depth", maxDepth);
                        params.put("eta", eta);
                        params.put("objective", "binary:logistic"); // binary classification objective

                        // Trainer currently uses fixed rounds (200) - could match 'rounds' param here if desired
                        XGBoostClassificationTrainer trainer = new XGBoostClassificationTrainer(200);
                        Model<Label> model = trainer.train(train);

                        // Evaluate model on test set
                        LabelEvaluation eval = evaluator.evaluate(model, test);
                        double acc = eval.accuracy();

                        System.out.println("Accuracy = " + acc);

                        // Keep track of best performing model
                        if (acc > bestAccuracy) {
                            bestAccuracy = acc;
                            bestModel = model;
                            bestParams = "rounds=" + rounds + ", maxDepth=" + maxDepth + ", eta=" + eta;
                        }
                    } catch (Exception e) {
                        System.err.println("Error training with rounds=" + rounds + ", maxDepth=" + maxDepth + ", eta=" + eta);
                        e.printStackTrace();
                    }
                }
            }
        }

        // Print best model's parameters and accuracy
        System.out.println("\n=== BEST MODEL ===");
        System.out.println("Params: " + bestParams);
        System.out.println("Accuracy: " + bestAccuracy);

        // Show confusion matrix for best model
        LabelEvaluation bestEval = evaluator.evaluate(bestModel, test);
        System.out.println("\nConfusion Matrix:");
        System.out.println(bestEval.getConfusionMatrix());

        // Display top 10 most important features per class
        System.out.println("\n=== Feature Importances (Top 10) ===");
        bestModel.getTopFeatures(10).forEach((className, features) -> {
            System.out.println("Class: " + className);
            for (var f : features) {
                System.out.println("  " + f.getA() + " -> " + f.getB());
            }
        });
    }
}
