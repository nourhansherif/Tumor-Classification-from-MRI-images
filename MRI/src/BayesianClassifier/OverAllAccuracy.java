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
import java.util.*;
import javafx.util.Pair;
import mri.Readable;
import mri.Writable;


public class OverAllAccuracy implements Readable, Calculatable,Writable
{
    private int trueNormal = 0, trueTumor = 0, falseNormal = 0, falseTumor = 0;
    private testBayesianClassifier Test;
    private static double overAllAccuracy;
    
    public static double getOverAllAccuracy() 
    {
        return overAllAccuracy;
    }
    
    public ArrayList<String> setConfusionMatrix(ArrayList <Pair<double[], Patient>>FeaturesMatrix) throws IOException 
    {  
        Test = new testBayesianClassifier();
        Test.readFromFile();
        ArrayList<String> results = new ArrayList<>();
        
        for(Pair<double[], Patient> featureVector : FeaturesMatrix)
        {
            System.out.println("__________________________________\n");
            Test.setImageFeatures(featureVector.getKey());
            Test.calculate();
            results.add(Test.Prediction());
            System.out.println("__________________________________");
            
            if (featureVector.getValue() instanceof normalPatient) 
            {
                if(results.get(results.size() - 1).equals("Normal") ) 
                {
                    trueNormal++;
                }
                else 
                {
                    falseNormal++;
                }
            }
            else if (featureVector.getValue() instanceof tumorPatient) 
            {
                if(results.get(results.size() - 1).equals("Tumor") )
                {
                    trueTumor++;
                }
                else
                {
                    falseTumor++;
                }
            }
        } 
        return results;
    }
    
    @Override
    public void calculate() 
    {
        System.out.println("Accuracy analysis");
        System.out.println("True Normal: " + trueNormal + "\nTrue Tumor: " + trueTumor 
                + "\nFalse Normal: " + falseNormal + "\nFalse Tumor: " + falseTumor);
        
        overAllAccuracy = (((trueNormal + trueTumor) / 
                (double)(trueNormal + trueTumor + falseNormal + falseTumor)) * 100);
    }

    public ArrayList<File> Validate()
    {
        ArrayList<File> filesToValidate = new ArrayList<>();
        
        File file = new File (System.getProperty("user.dir") + "/DataSet/Validation/Normal/");
        File[] files = file.listFiles();
        for (File f : files)
        {   
            if (!f.isDirectory()) 
            {
                filesToValidate.add(f);
            }
        }
        
        file = new File (System.getProperty("user.dir") + "/DataSet/Validation/Tumor/");
        files = file.listFiles();
        for (File f : files)
        {
            if (!f.isDirectory()) 
            {
                filesToValidate.add(f);
            }
        }

       return filesToValidate;
    }
    
    @Override
    @SuppressWarnings({"ConvertToTryWithResources", "NestedAssignment"})
    public void readFromFile() throws FileNotFoundException, IOException 
    {
        if (Readable.check("OverAllAccuracy.txt"))  
        {  
            BufferedReader br = new BufferedReader(new FileReader("OverAllAccuracy.txt"));
            
            overAllAccuracy = Double.valueOf(br.readLine());
            
            br.close();
        }  
        
    }

    @Override
    @SuppressWarnings("ConvertToTryWithResources")
    public void writeToFile() throws IOException 
    {
        File file = new File ("OverAllAccuracy.txt");

        if (file.createNewFile()) 
        {
            System.out.println("File created: " + file.getName());
        } 
        else 
        {
            System.out.println("File already exists.");
        }
        
        FileWriter myWriter = new FileWriter(file);
        
        myWriter.write(String.valueOf(overAllAccuracy));
        
        myWriter.close();
    } 
      
}
