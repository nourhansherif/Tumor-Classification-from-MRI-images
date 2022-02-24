/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package People;

import java.io.*;
import java.util.*;
import javafx.util.Pair;
import mri.Writable;


public class Doctor extends Person implements Writable
{
    private final ArrayList <Pair<Integer, File>> patientsToTest;// ID of patient and Path of images
    
    public Doctor(String id, String password)
    {
        super(id, password);
        patientsToTest = new ArrayList<>();
    }

    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public  ArrayList<Pair<Integer, File>> getPatientsToTest() 
    {
        return patientsToTest;
    }
    
    public static boolean checkPassword(String id, String password) 
    {
        if (!id.startsWith("D")) 
        {
            return false;
        } 
        else 
        {
            for (Person p : Person.getPeople())
            {
               if ((p instanceof Doctor) && (p.getId().equals(id) && p.getPassword().equals(password))) 
               {
                   return true;
               }
            }
            
            return false;
        }
        
    }
    
    @Override
    @SuppressWarnings("ConvertToTryWithResources")
    public void writeToFile()throws IOException 
    {
        File file = new File ("PatientsToBeTested.txt");

        if (file.createNewFile()) 
        {
            System.out.println("File created: " + file.getName());
        } 
        else 
        {
            System.out.println("File already exists.");
        }

        FileWriter myWriter = new FileWriter(file,true);
        
        for (Pair<Integer, File> patient : this.patientsToTest) 
        {
            
            myWriter.write(patient.getKey() + "::" + patient.getValue() + "\n");
        }
        
        myWriter.close();
    } 
    
}