package rf_training;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.tribuo.Dataset;
import org.tribuo.MutableDataset;
import org.tribuo.classification.Label;
import org.tribuo.classification.LabelFactory;
import org.tribuo.data.csv.CSVDataSource;
import org.tribuo.data.columnar.FieldProcessor;
import org.tribuo.data.columnar.RowProcessor;
import org.tribuo.data.columnar.processors.field.DoubleFieldProcessor;
import org.tribuo.data.columnar.processors.field.IdentityProcessor;
import org.tribuo.data.columnar.processors.response.FieldResponseProcessor;
import org.tribuo.evaluation.TrainTestSplitter;

/**
 * Loads the Personality dataset from a CSV file,
 * processes features, and splits into train and test sets.
 */
public class PersonalityDataLoader_rf {
    private Dataset<Label> trainDataset; // Training dataset
    private Dataset<Label> testDataset;  // Testing dataset

    public void loadDataset(String csvPath) throws Exception {
        LabelFactory labelFactory = new LabelFactory(); // Creates label objects for classification

        List<FieldProcessor> processors = new ArrayList<>();
        // Numeric features use DoubleFieldProcessor, categorical using IdentityProcessor
        processors.add(new DoubleFieldProcessor("Time_spent_Alone"));
        processors.add(new IdentityProcessor("Stage_fear"));
        processors.add(new DoubleFieldProcessor("Social_event_attendance"));
        processors.add(new DoubleFieldProcessor("Going_outside"));
        processors.add(new IdentityProcessor("Drained_after_socializing"));
        processors.add(new DoubleFieldProcessor("Friends_circle_size"));
        processors.add(new DoubleFieldProcessor("Post_frequency"));

        // Define target column (Personality) for classification
        FieldResponseProcessor responseProcessor = new FieldResponseProcessor("Personality", "UNK", labelFactory);

        // Create RowProcessor to handle CSV row -> feature vector mapping
        RowProcessor<Label> rowProcessor = new RowProcessor.Builder<Label>()
                .setFieldProcessors(processors)
                .build(responseProcessor);

        // Load data from CSV
        CSVDataSource<Label> csvSource = new CSVDataSource<>(Paths.get(csvPath), rowProcessor, true);

        // Split dataset (70% train, 30% test)
        TrainTestSplitter<Label> splitter = new TrainTestSplitter<>(csvSource, 0.7, 1L);

        trainDataset = new MutableDataset<>(splitter.getTrain());
        testDataset = new MutableDataset<>(splitter.getTest());

        // Print data stats
        System.out.println("Training samples: " + trainDataset.size());
        System.out.println("Testing samples: " + testDataset.size());
        System.out.println("Distinct classes: " + trainDataset.getOutputInfo().size());
    }

    public Dataset<Label> getTrainDataset() { return trainDataset; }
    public Dataset<Label> getTestDataset() { return testDataset; }
}
