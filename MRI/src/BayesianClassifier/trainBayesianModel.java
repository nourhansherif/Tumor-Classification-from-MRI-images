/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BayesianClassifier;

import People.Patient;
import People.normalPatient;
import People.tumorPatient;
import java.io.*;
import static java.lang.Math.pow;
import java.util.*;
import javafx.util.Pair;
import mri.Writable;


public class trainBayesianModel implements Writable, Calculatable
{
    private final ArrayList<double[]> tumorMatrix = new ArrayList <>();
    private final ArrayList<double[]> normalMatrix = new ArrayList <>();
    private final ArrayList<Pair<double[],Patient>> featuresMatrix;
    private final double[][] trainMeanVarianceMatrix;
    private double probTumor = 0, probNormal = 0;
    
    public trainBayesianModel(ArrayList<Pair<double[],Patient>> featuresMatrix)
    {
        trainMeanVarianceMatrix = new double [2][(featuresMatrix.get(0).getKey().length) * 2];
        this.featuresMatrix = new ArrayList<> (featuresMatrix);
    }
    
    public void divideMatrix()
    {
        
        for (Pair<double[],Patient> featurevector : featuresMatrix) 
        {
            if (featurevector.getValue() instanceof normalPatient) 
            {
                normalMatrix.add(featurevector.getKey());
            } 
            else if (featurevector.getValue() instanceof tumorPatient) 
            {
                tumorMatrix.add(featurevector.getKey());
            }
        }
        
    }
    
    @Override
    public void calculate() 
    {
        CalculateNormalMean();
        CalculateNormalVariance();
        CalculateTumorMean();
        CalculateTumorVariance();
        calculateProbOfNormal();
        calculateProbOfTumor();
    }
    
    private void CalculateNormalMean()
    {
        for (int i = 0; i < normalMatrix.size(); i++)
        {
            double normalvector[] = Arrays.copyOf(normalMatrix.get(i), normalMatrix.get(i).length);
            
            for (int j = 0, k = 0; j < normalvector.length; j++, k+=2)
            {
                trainMeanVarianceMatrix[0][k] += normalvector[j];
            }
        }
                
        for(int k = 0; k < trainMeanVarianceMatrix[0].length; k+=2)
        {
            trainMeanVarianceMatrix[0][k] /= normalMatrix.size();
        }
        
    }

    private void CalculateNormalVariance()
    {
       for (int i = 0; i < normalMatrix.size(); i++)
       {
           double normalvector[] = Arrays.copyOf(normalMatrix.get(i), normalMatrix.get(i).length);
           
           for (int j = 0, k = 0; j < normalvector.length; j++, k+=2)
           {
               trainMeanVarianceMatrix[0][k+1] += pow(normalvector[j] - trainMeanVarianceMatrix[0][k],2);
           }
       }
       
       for(int k = 0; k < trainMeanVarianceMatrix[0].length; k+=2)
       {
           trainMeanVarianceMatrix[0][k+1] /= normalMatrix.size();
       }

    }

    private void CalculateTumorMean()
    {
        for (int i = 0; i < tumorMatrix.size(); i++)
        {
            double Tumorvector[] = Arrays.copyOf(tumorMatrix.get(i), tumorMatrix.get(i).length);
            
            for (int j = 0, k = 0; j < Tumorvector.length; j++, k+=2)
            {
                trainMeanVarianceMatrix[1][k] += Tumorvector[j];
            }
        }

        for(int k = 0; k < trainMeanVarianceMatrix[0].length; k+=2)
        {
            trainMeanVarianceMatrix[1][k] /= tumorMatrix.size();
        }

    }

    private void CalculateTumorVariance()
    {
       for (int i = 0; i < tumorMatrix.size(); i++)
       {
           double Tumorvector[] = Arrays.copyOf(tumorMatrix.get(i), tumorMatrix.get(i).length);
           
           for (int j = 0, k = 0; j < Tumorvector.length; j++, k+=2)
           {
               trainMeanVarianceMatrix[1][k+1] += pow(Tumorvector[j] - trainMeanVarianceMatrix[0][k],2);
           }
       }

       for(int k = 0; k < trainMeanVarianceMatrix[0].length; k+=2)
       {
           trainMeanVarianceMatrix[1][k+1] /= tumorMatrix.size();
       } 
       
    }

    private void calculateProbOfNormal()
    {
        probNormal =  normalMatrix.size() / (double)(tumorMatrix.size() + normalMatrix.size());
    }

    private void calculateProbOfTumor()
    {
        probTumor =  tumorMatrix.size()/(double)(tumorMatrix.size() + normalMatrix.size());
    }
    
    @Override
    public void writeToFile() throws IOException 
    {
        writeMeanVarience();
        writeProb();
    }
    
    @SuppressWarnings("ConvertToTryWithResources")
    private void writeMeanVarience() throws IOException
    {
        File file = new File ("MeanVariance.txt");

        if (file.createNewFile()) 
        {
            System.out.println("File created: " + file.getName());
        } 
        else 
        {
            System.out.println("File already exists.");
        }

        FileWriter myWriter = new FileWriter(file);

        for (int j = 0; j < trainMeanVarianceMatrix[0].length; j++)
        {
            myWriter.write(String.valueOf(trainMeanVarianceMatrix[0][j])+ "::");
        }

        myWriter.write("\n");

        for (int j = 0; j < trainMeanVarianceMatrix[1].length; j++)
        {
            myWriter.write(String.valueOf(trainMeanVarianceMatrix[1][j]) + "::");
        }

        myWriter.close();
    }
    
    @SuppressWarnings("ConvertToTryWithResources")
    private void writeProb()  throws FileNotFoundException, IOException
    {
        File file = new File ("prob.txt");

        if (file.createNewFile()) 
        {
            System.out.println("File created: " + file.getName());
        }
        else 
        {
            System.out.println("File already exists.");
        }

        FileWriter myWriter = new FileWriter(file);
        
        myWriter.write(String.valueOf(probNormal) + "::" + String.valueOf(probTumor));
        
        myWriter.close();
    }   
    
}

