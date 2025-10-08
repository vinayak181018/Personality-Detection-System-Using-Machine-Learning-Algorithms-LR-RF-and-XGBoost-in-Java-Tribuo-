//package rf_training;


//import org.tribuo.Dataset;
//import org.tribuo.Example;
//import org.tribuo.Model;
//import org.tribuo.MutableDataset;
//import org.tribuo.classification.Label;
//import org.tribuo.classification.LabelFactory;
//import org.tribuo.classification.dtree.CARTClassificationTrainer;
//import org.tribuo.classification.ensemble.FullyWeightedVotingCombiner;
//import org.tribuo.classification.evaluation.LabelEvaluator;
//import org.tribuo.classification.evaluation.LabelEvaluation;
//import org.tribuo.common.tree.RandomForestTrainer;
//import org.tribuo.datasource.ListDataSource;
//import org.tribuo.provenance.DataSourceProvenance;
//
//import com.oracle.labs.mlrg.olcut.provenance.Provenance;
//import com.oracle.labs.mlrg.olcut.util.Pair;
//
//import java.util.*;
//
//public class PersonalityTrainer_rf {
//
//    // Minimal DataSourceProvenance implementation
//    public static class MinimalDataSourceProvenance implements DataSourceProvenance {
//        private final String description = "MinimalDataSourceProvenance";
//
//        @Override
//        public String toString() { return description; }
//        @Override
//        public int hashCode() { return Objects.hash(description); }
//        @Override
//        public boolean equals(Object obj) {
//            if (!(obj instanceof MinimalDataSourceProvenance)) return false;
//            MinimalDataSourceProvenance other = (MinimalDataSourceProvenance)obj;
//            return Objects.equals(description, other.description);
//        }
//        @Override
//        public String getClassName() { return description; }
//        @Override
//        public Iterator<Pair<String, Provenance>> iterator() { return Collections.emptyIterator(); }
//    }
//
//    private final RandomForestTrainer<Label> trainer;
//    private final Dataset<Label> oversampledTrain;
//
//    public PersonalityTrainer_rf(Dataset<Label> train) {
//        LabelFactory labelFactory = new LabelFactory();
//
//        System.out.println("Original train provenance: " + train.getProvenance());
//
//        // Oversample minority classes for balanced classes
//        Map<String, List<Example<Label>>> examplesByClass = new HashMap<>();
//        for (Example<Label> ex : train) {
//            examplesByClass.computeIfAbsent(ex.getOutput().getLabel(), k -> new ArrayList<>()).add(ex);
//        }
//
//        int maxCount = examplesByClass.values().stream().mapToInt(List::size).max().orElse(0);
//
//        List<Example<Label>> oversampledExamples = new ArrayList<>();
//        Random rnd = new Random(1L);
//        for (Map.Entry<String, List<Example<Label>>> entry : examplesByClass.entrySet()) {
//            List<Example<Label>> classEx = entry.getValue();
//            int currentSize = classEx.size();
//            oversampledExamples.addAll(classEx);
//            // Add random oversampling to match max class
//            while (oversampledExamples.size() < (maxCount * examplesByClass.size())) {
//                Example<Label> ex = classEx.get(rnd.nextInt(currentSize));
//                oversampledExamples.add(ex);
//            }
//        }
//
//        DataSourceProvenance minimalProv = new MinimalDataSourceProvenance();
//
//        ListDataSource<Label> dataSource = new ListDataSource<>(
//            oversampledExamples,
//            labelFactory,
//            minimalProv
//        );
//
//        this.oversampledTrain = new MutableDataset<>(dataSource);
//        System.out.println("Oversampled dataset size: " + oversampledTrain.size());
//
//        CARTClassificationTrainer treeTrainer = new CARTClassificationTrainer(
//            10,    // maxDepth
//            0.7f,  // fractionFeaturesInSplit
//            false, // useRandomSplitPoints
//            1L     // seed
//        );
//
//        FullyWeightedVotingCombiner combiner = new FullyWeightedVotingCombiner();
//
//        this.trainer = new RandomForestTrainer<>(
//            treeTrainer,
//            combiner,
//            100,   // number of trees
//            1L     // seed
//        );
//    }
//
//    public void trainEvaluateAndPrint(Dataset<Label> test) {
//        Model<Label> model = trainer.train(oversampledTrain);
//
//        LabelEvaluator evaluator = new LabelEvaluator();
//        LabelEvaluation evaluation = evaluator.evaluate(model, test);
//
//        System.out.println("\n=== Evaluation Results ===");
//        System.out.println(evaluation);
//
//        System.out.println("\n=== Confusion Matrix ===");
//        System.out.println(evaluation.getConfusionMatrix());
//    }
//}
package rf_training;

import org.tribuo.Dataset;
import org.tribuo.Model;
import org.tribuo.classification.Label;
import org.tribuo.classification.dtree.CARTClassificationTrainer; // Decision tree trainer
import org.tribuo.classification.ensemble.FullyWeightedVotingCombiner; // Voting strategy
import org.tribuo.classification.evaluation.LabelEvaluator;
import org.tribuo.classification.evaluation.LabelEvaluation;
import org.tribuo.common.tree.RandomForestTrainer; // Random Forest trainer

import java.io.IOException;

/**
 * Trains a Random Forest model and evaluates it.
 */
public class PersonalityTrainer_rf {
    private final RandomForestTrainer<Label> trainer; // Random forest trainer
    private final Dataset<Label> trainData;           // Training data

    public PersonalityTrainer_rf(Dataset<Label> train) {
        this.trainData = train;

        // Build CART (Decision Tree) trainer with parameters
        CARTClassificationTrainer treeTrainer = new CARTClassificationTrainer(
            15,  // Maximum depth of trees
            0.7f, // Fraction of features per split
            false, // No pruning
            1L     // Random seed
        );

        FullyWeightedVotingCombiner combiner = new FullyWeightedVotingCombiner(); // Combines tree votes weighted equally

        // Configure Random Forest with 200 trees
        this.trainer = new RandomForestTrainer<>(
            treeTrainer, combiner, 200, 1L
        );
    }

    /**
     * Trains model, evaluates it, prints results, and saves model file.
     */
    public void trainEvaluateAndSaveModel(Dataset<Label> test, String modelPath) {
        // Train on training data
        Model<Label> model = trainer.train(trainData);

        // Evaluate model
        LabelEvaluator evaluator = new LabelEvaluator();
        LabelEvaluation evaluation = evaluator.evaluate(model, test);

        // Show evaluation metrics
        System.out.println("\n=== Evaluation Results ===");
        System.out.println(evaluation);

        // Show confusion matrix
        System.out.println("\n=== Confusion Matrix ===");
        System.out.println(evaluation.getConfusionMatrix());

        // Save model to disk
        try {
            ModelIO.saveModel(model, modelPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\nBest model has been saved to " + modelPath);
    }
}

