# Tumor-Classification-from-MRI-images
- A complete recognition system is done for classifying whether the input image represents a normal brain or the presence of a tumour.
- The dataset includes MRI images divided into normal brain & brain with tumour, the images are in .jpg format.

- Login Form to determine authentication whether you are a doctor, patient or admin.
- First, the MRI image is normalized.
- The results after the normalization step are shown.
- Features are extracted from all images using Statistical Features such as Mean, Variance, Smoothness, Third Moment (Skewness), Fourth Moment (Kurtosis), Uniformity, and Entropy.
- Extracted features are saved in a matrix. Each row represents a sample (MRI image) and each column represents a specific feature.
- Train a Bayesian classifier on the output matrix.
- Find the performance using the Overall Accuracy (OA) and the Overall Accuracy
- Use this classifier to classify testing images as either normal or tumour.

- The Project is written in Java Language.
- OOP Concepts are implemented.
- Extracted features and Matrices are saved in files to allow using the system at different times without losing the data.
