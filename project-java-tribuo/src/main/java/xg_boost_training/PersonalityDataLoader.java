package xg_boost_training;

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

/**
 * This class loads personality CSV dataset into Tribuo's Dataset objects,
 * sets up features and target labels, and splits data into training & testing sets.
 */
public class PersonalityDataLoader {

    private final LabelFactory labelFactory; // Factory to create classification labels

    public PersonalityDataLoader() {
        this.labelFactory = new LabelFactory();
    }

    /**
     * Loads the dataset from CSV, processes feature columns, sets target label, and splits train/test.
     * @param csvFilePath Path to the CSV dataset file.
     * @param trainSplitRatio Fraction of data for training (e.g. 0.7 means 70% train).
     * @param seed Random seed for reproducible splitting.
     * @return Array containing [trainDataset, testDataset].
     */
    public Dataset<Label>[] loadData(String csvFilePath, double trainSplitRatio, long seed) throws Exception {
        List<FieldProcessor> processors = new ArrayList<>();
        // Numeric features use DoubleFieldProcessor, categorical features use IdentityProcessor.
        processors.add(new DoubleFieldProcessor("Time_spent_Alone"));
        processors.add(new IdentityProcessor("Stage_fear"));
        processors.add(new DoubleFieldProcessor("Social_event_attendance"));
        processors.add(new DoubleFieldProcessor("Going_outside"));
        processors.add(new IdentityProcessor("Drained_after_socializing"));
        processors.add(new DoubleFieldProcessor("Friends_circle_size"));
        processors.add(new DoubleFieldProcessor("Post_frequency"));

        // Tell Tribuo which column is the response label (target variable)
        FieldResponseProcessor response = new FieldResponseProcessor("Personality", "UNK", labelFactory);

        // Create processor to handle each CSV row's features and label
        RowProcessor<Label> rowProcessor = new RowProcessor.Builder<Label>()
                .setFieldProcessors(processors)
                .build(response);

        // Load dataset from CSV file
        CSVDataSource<Label> source = new CSVDataSource<>(Paths.get(csvFilePath), rowProcessor, true); // true = header row

        // Split the dataset into train and test portions
        org.tribuo.evaluation.TrainTestSplitter<Label> splitter = new org.tribuo.evaluation.TrainTestSplitter<>(source, trainSplitRatio, seed);

        // Create mutable datasets for training and testing
        Dataset<Label> train = new MutableDataset<>(splitter.getTrain());
        Dataset<Label> test = new MutableDataset<>(splitter.getTest());

        // Print dataset summary
        System.out.println("Training samples: " + train.size());
        System.out.println("Testing samples: " + test.size());
        System.out.println("Classes: " + train.getOutputInfo().size());

        // Return datasets in an array
        return new Dataset[]{train, test};
    }
}
