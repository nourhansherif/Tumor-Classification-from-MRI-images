/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Image;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import javax.imageio.ImageIO;


public class Features 
{ 
    private final File input;
    private final double[] FeaturesVector;
    private double mean, variance, smoothness, skewness, kurtosis;
    private double standarddeviation;
    private BufferedImage image;
    
    public Features(File input)
    {
        this.input = input;
        FeaturesVector = new double[5];
    }
    
    private double calcMean(BufferedImage image) 
    {
        double sum = 0f;
        Raster r = image.getRaster();
        
        for (int i = 0; i < image.getHeight(); i++) 
        {
            for (int j = 0; j < image.getWidth(); j++) 
            {
                sum += r.getSample(j, i, 0);
            }
        }
        
        mean = sum / (image.getHeight() * image.getWidth());  
        
        return mean;  
    }
    
    private double calcVarience(BufferedImage image) 
    {
        double sum = 0f;
        Raster r = image.getRaster();
        
        for (int i = 0; i < image.getHeight(); i++) 
        {
            for (int j = 0; j < image.getWidth(); j++) 
            {
                sum += pow((r.getSample(j, i, 0) - mean),2);
            }
        }
        
        variance = sum / (image.getHeight() * image.getWidth());
        
        return variance;  
    }
    
    private double calcSmoothness()
    {
        smoothness = 1 - (1 / (1 + variance));
        return smoothness;
    }
    
    private double calcSkewness(BufferedImage image)
    {
        standarddeviation = sqrt(variance);
        double sum = 0f;
        Raster r = image.getRaster();
        
        for (int i = 0; i < image.getHeight(); i++) 
        {
            for (int j = 0; j < image.getWidth(); j++) 
            {
                sum += pow((r.getSample(j, i, 0) - mean),3);
            }
        }
        
        skewness = sum / ((image.getWidth() * image.getHeight()) * pow(standarddeviation, 3));
       
        return skewness;
    }
    
    private double calcKurtosis(BufferedImage image)
    {
        double sum = 0f;
        Raster r = image.getRaster();
        
        for (int i = 0;i < image.getHeight(); i++) 
        {
            for (int j = 0; j < image.getWidth(); j++) 
            {
                sum += pow((r.getSample(j,i,0) - mean),4);
            }
        }
        
        kurtosis = ( sum / ((image.getWidth() * image.getHeight()) * pow(standarddeviation,4))) - 3;
        
        return kurtosis;
    }
     
    public void makingFeaturesVector() throws IOException
    {
        try 
        {
            image = ImageIO.read(input);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
        
        FeaturesVector[0] = calcMean(image);
        FeaturesVector[1] = calcVarience(image);
        FeaturesVector[2] = calcSkewness(image);
        FeaturesVector[3] = calcSmoothness();
        FeaturesVector[4] = calcKurtosis(image);
       
    }

    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public double[] getFeaturesVector() 
    {
        return FeaturesVector;
    }

}