/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BayesianClassifier;

import java.io.*;
import static java.lang.Math.PI;
import static java.lang.Math.log;
import mri.Readable;


public class testBayesianClassifier implements Readable, Calculatable
{
    private double[][] MeanVarianceMatrix = new double[2][10];
    private double probTestImageTumor = 0, probTestImageNormal = 0, evidence = 0, probTumor = 0, probNormal = 0;
    private double[] ImageFeatures;

    public void setImageFeatures(double[] ImageFeatures) 
    {
        this.ImageFeatures = new double [ImageFeatures.length];
        System.arraycopy(ImageFeatures, 0, this.ImageFeatures, 0, ImageFeatures.length);
    }
    
    @Override
    public void calculate() 
    {
        CalculateLikelihood();
        CalculateEvidence();
    }

    private void CalculateEvidence()
    {
        evidence = (probTumor * probTestImageTumor) + (probNormal * probTestImageNormal);
        System.out.println("Evidence P(X) = " + evidence);
    }

    private void CalculateLikelihood()
    {
        double part1, part2, part3, probLikelihood = 0;
        
        for(int i = 0;i < 2; i++ )
        {
            for(int j = 0, k = 0; j < MeanVarianceMatrix[i].length; j+=2, k++)
            {
                part1 = (1 / (Math.sqrt(2 * PI) * Math.sqrt(MeanVarianceMatrix[i][j+1])));
                part2 = - 0.5 *( Math.pow((ImageFeatures[k] - MeanVarianceMatrix[i][j]),2) 
                        / MeanVarianceMatrix[i][j+1]);
                part2 = Math.exp(part2);
                part3 = part1 * part2;
                probLikelihood += log(part3);
            }
            
            probLikelihood = Math.exp(probLikelihood);
            
            if(i == 0)
            {
                probTestImageNormal = probLikelihood;
                System.out.println("Prior Normal P(wi) = " + probNormal);
                System.out.println("likelihood Normal P( X | wi ) = " + probTestImageNormal);
            }
            if(i == 1)
            {
                probTestImageTumor = probLikelihood;
                System.out.println("Prior Tumor P(wi) = " + probTumor);
                System.out.println("likelihood Tumor P( X | wi ) = " + probTestImageTumor);
            }
        }
        
    }

    public String Prediction()
    {
        double posteriorTumor = (probTumor * probTestImageTumor) / evidence;
        System.out.println("Posterior P(wi | X) if Tumor = " + posteriorTumor);

        double posteriorNormal = (probNormal * probTestImageNormal) / evidence;
        System.out.println("Posterior P(wi | X) if Normal = " + posteriorNormal);
        
        if (posteriorTumor > posteriorNormal) 
        {   
            System.out.println("Predicted: It is Tumor");
            return "Tumor";
        }  
        else 
        {
            System.out.println("Predicted: It is Normal");
            return "Normal";
        } 
      
    }
    
    @Override
    public void readFromFile() throws FileNotFoundException, IOException 
    {
        readMeanVarience();
        readprob();
    }
    
    @SuppressWarnings({"ConvertToTryWithResources", "NestedAssignment"})
    private void readMeanVarience() throws FileNotFoundException, IOException
    {
        if (Readable.check("MeanVariance.txt"))
        {
            MeanVarianceMatrix = new double[2][8];
            BufferedReader br = new BufferedReader(new FileReader("MeanVariance.txt"));
            String line;
            int lineNo = 0;
        
            while((line = br.readLine()) != null)
            {
                String temp[] = line.split("::");

                for (int j = 0; j < MeanVarianceMatrix[lineNo].length; j++)
                {
                    if(!Double.isNaN(Double.parseDouble(temp[j])))
                    {
                        MeanVarianceMatrix[lineNo][j] = Double.parseDouble(temp[j]);
                    }
                    else 
                    {
                        MeanVarianceMatrix[lineNo][j] = 0.0;
                    }
                }
            
                lineNo++;
            }  
        
            br.close();
        } 
    }
    
    @SuppressWarnings("ConvertToTryWithResources")
    private void readprob() throws FileNotFoundException, IOException
    {
        if (Readable.check("prob.txt"))
        {
            BufferedReader br = new BufferedReader(new FileReader("prob.txt"));
            String line;

            line = br.readLine();
            String temp[] = line.split("::");
            probNormal = Double.parseDouble(temp[0]);
            probTumor = Double.parseDouble(temp[1]);
            
            br.close();
        }
    }
    
}
