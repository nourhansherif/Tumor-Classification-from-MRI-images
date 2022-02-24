/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package People;

import java.io.*;
import java.util.*;
import javafx.util.Pair;


public  class Patient extends Person
{  
    private static ArrayList<Pair<File, Patient>> patients;
    private static int patientID = 1;
   
    public Patient(String id, String password) 
    {
        super(id,password);
    }
    
    public Patient ()
    {
        super();
    }

    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public static ArrayList<Pair<File,Patient>> getPatients() 
    {
        return patients;
    }
    
    public static void setPatientID(int patientID) 
    {
        Patient.patientID = patientID;
    }
    
    public static int getPatientID() 
    {
        return patientID;
    }

    @SuppressWarnings({"ConvertToTryWithResources", "NestedAssignment"})
    public static void setPatients() throws IOException
    {
       if (new File("PatientsResults.txt").exists())
       {
            BufferedReader br = new BufferedReader(new FileReader("PatientsResults.txt"));
            String line;
            patients = new ArrayList<>();
        
            while((line = br.readLine()) != null)
            {
                String temp[] = line.split("::");
                
                if ("Normal".equals(temp[2])) 
                {
                    patients.add(new Pair<>(new File(temp[3]), new normalPatient(temp[0], temp[1])));
                } 
                else 
                {
                    patients.add(new Pair<>(new File(temp[3]), new tumorPatient(temp[0], temp[1])));
                }
                    
            }
        
            br.close();
        }
       else
       {
           patients = new ArrayList<>();
       }
       
    }
    
    public static Person checkPassword(String id, String password) 
    {
        for (Pair<File, Patient> p : Patient.getPatients())
        {
            if (p.getValue().getId().equals(id) && p.getValue().getPassword().equals(password))
            {
                return p.getValue();
            }
        }
        
        return null;
    }
    
}
