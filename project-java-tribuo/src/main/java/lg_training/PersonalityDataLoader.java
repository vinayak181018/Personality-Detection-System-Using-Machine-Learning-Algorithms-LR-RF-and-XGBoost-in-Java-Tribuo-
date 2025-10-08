package lg_training;

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

public class PersonalityDataLoader {
    private Dataset<Label> trainDataset; // Holds training data
    private Dataset<Label> testDataset;  // Holds testing data

    public void loadDataset(String csvPath) throws Exception {
        LabelFactory labelFactory = new LabelFactory(); // Factory to create Label objects

        List<FieldProcessor> processors = new ArrayList<>();
        // Define how each feature column will be processed
        processors.add(new DoubleFieldProcessor("Time_spent_Alone"));        // Numerical feature
        processors.add(new IdentityProcessor("Stage_fear"));                 // Categorical feature
        processors.add(new DoubleFieldProcessor("Social_event_attendance")); // Numerical
        processors.add(new DoubleFieldProcessor("Going_outside"));           // Numerical
        processors.add(new IdentityProcessor("Drained_after_socializing"));  // Categorical
        processors.add(new DoubleFieldProcessor("Friends_circle_size"));     // Numerical
        processors.add(new DoubleFieldProcessor("Post_frequency"));          // Numerical

        // Define which column is the response (label) for classification
        FieldResponseProcessor responseProcessor =
            new FieldResponseProcessor("Personality", "UNK", labelFactory);

        // Create a row processor that applies feature processors + response processor to each CSV line
        RowProcessor<Label> rowProcessor = new RowProcessor.Builder<Label>()
                .setFieldProcessors(processors)
                .build(responseProcessor);

        // Load the dataset from CSV using the row processor
        CSVDataSource<Label> csvSource =
            new CSVDataSource<>(Paths.get(csvPath), rowProcessor, true); // 'true' means first line is header

        // Split the CSV data into 70% training and 30% testing
        TrainTestSplitter<Label> splitter =
            new TrainTestSplitter<>(csvSource, 0.7, 1L); // 1L is seed for reproducibility

        // Store train/test datasets
        trainDataset = new MutableDataset<>(splitter.getTrain());
        testDataset  = new MutableDataset<>(splitter.getTest());

        // Print dataset statistics
        System.out.println("Training samples: " + trainDataset.size());
        System.out.println("Testing samples:  " + testDataset.size());
        System.out.println("Distinct classes: " + trainDataset.getOutputInfo().size());
    }

    public Dataset<Label> getTrainDataset() { return trainDataset; }
    public Dataset<Label> getTestDataset()  { return testDataset; }
}
