# Personality-Detection-System-Using-Machine-Learning-Algorithms-LR with SGD-RF-and-XGBoost-in-Java-Tribuo
A Java-based Personality Detection System using the Tribuo ML framework. Trains and compares Logistic Regression, Random Forest, and XGBoost models to predict personality traits from behavioral data, deploying the best-performing model for accurate predictions.

📦 Resources Used
------------------

Java: Version 17 was used to develop and run the project, leveraging modern Java features and long-term support stability.

IDE / Platform: The project is developed using Eclipse IDE, a popular Java development environment that integrates well with Maven and provides powerful debugging and editing features.

Tribuo: Oracle’s open-source machine learning library for Java, compatible with Java 8+, used for loading datasets, model building, evaluation, and serialization.

📊 Dataset
-----------

Source: [Extrovert vs. Introvert Behavior Data (Kaggle)](https://www.kaggle.com/datasets/rakeshkapilavai/extrovert-vs-introvert-behavior-data)

Description:
The dataset contains behavioral and social variables, such as:

Time_spent_Alone (numeric)

Stage_fear (categorical: Yes/No)

Social_event_attendance (numeric)

Going_outside (numeric)

Drained_after_socializing (categorical: Yes/No)

Friends_circle_size (numeric)

Post_frequency (numeric)

Class variable: Personality with values INTROVERT or EXTROVERT (target for classification)

🏋️‍♂️ Training Algorithms
-----------------------

Logistic Regression:
Binary classification using a linear model trained via stochastic gradient descent.

Random Forest:
An ensemble of decision trees trained with bagging and fully weighted voting, offering robust, high-accuracy predictions.

XGBoost:
Gradient boosted trees optimized through hyperparameter tuning (rounds, max depth, learning rate).

🚀 Results and Application
---------------------------

The Random Forest model was selected as the best model with highest accuracy (~92.5%).

This trained Random Forest model was saved, loaded, and used to build a real-time personality detection system that predicts personality from new input feature values.

🖥️ Sample Training Output
--------------------------

Logistic Regression trained with Stochastic Gradient Descent (SGD)
------------------------------------------------------------------
https://github.com/vinayak181018/Personality-Detection-System-Using-Machine-Learning-Algorithms-LR-RF-and-XGBoost-in-Java-Tribuo-/blob/8d3866130980b46486bc7f6f101c7627ce17f840/PD_trained_model_images/LR_with_SGD_trained_output.png

Random Forest (Ensemble Training)
---------------------------------
https://github.com/vinayak181018/Personality-Detection-System-Using-Machine-Learning-Algorithms-LR-RF-and-XGBoost-in-Java-Tribuo-/blob/a667df0f9f7da44fb85736916111a7d55d561a70/PD_trained_model_images/RF_trained_output.png

XGBoost (Hyperparameter Search)
-------------------------------
https://github.com/vinayak181018/Personality-Detection-System-Using-Machine-Learning-Algorithms-LR-RF-and-XGBoost-in-Java-Tribuo-/blob/5bb84f35bdfdc9bb107b6bd49a5e356c4e11be86/PD_trained_model_images/RF_trained_output.png

🛠️ Build and Dependencies
--------------------------

This project uses Maven as the build and dependency management tool.

Java version: 17 (configured via <maven.compiler.release>17</maven.compiler.release>)

Build tool: Apache Maven (Maven plugins for compilation, testing, packaging, and deployment are configured)

Important dependencies included:
Tribuo Core and Data Libraries:

tribuo-core (4.3.2) — The foundation of the Tribuo ML framework

tribuo-data (4.3.2) — For loading and processing CSV datasets

Classification Implementations:

tribuo-classification-core (4.3.2) — Core classification interfaces

tribuo-classification-sgd (4.3.2) — Stochastic Gradient Descent trainer for Logistic Regression

tribuo-classification-tree (4.3.2) — Decision trees support

tribuo-classification-xgboost & tribuo-common-xgboost (4.3.2) — XGBoost gradient boosting models

tribuo-classification-experiments (4.3.2) — Additional classification algorithms, experiments

tribuo-common-tree (4.3.2) — Common tree utilities for ensembles

Mathematics and JSON Support:

tribuo-math (4.3.2) — Math utilities used internally by Tribuo

tribuo-json (4.3.2) — Optional JSON serialization of models and metadata

Testing Libraries:

JUnit Jupiter API and parameterized tests for unit testing support.

Maven plugins configured for build lifecycle:
Clean, Compile, Test, Package, Install, Deploy and Site lifecycle plugins with locked versions for stability.
