/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package People;

import java.io.*;
import BayesianClassifier.*;
import javafx.util.Pair;
import mri.Readable;
import mri.Writable;
import Image.*;
import java.util.ArrayList;

public class Admin extends Person implements Readable,Writable
{
    private File normalizedPicture;
    private trainBayesianModel Train;
    private testBayesianClassifier Test;
    private final OverAllAccuracy overallAccuracy = new OverAllAccuracy();;
    private ArrayList <Pair<File, Patient>> patientsToTest;
    private ArrayList <Pair<double[], Patient>> matrix;    
    private boolean train;
    private static int pendingToTest;
    
    public Admin(String id, String password) 
    {
        super(id, password);
        setMatrix();
    }

    public static int getPendingToTest() 
    {
        return pendingToTest;
    }

    @SuppressWarnings({"ConvertToTryWithResources", "NestedAssignment"})
    public static void setPendingToTest() throws FileNotFoundException, IOException 
    {
        Admin.pendingToTest = 0;
        
        if (Readable.check("PatientsToBeTested.txt"))  
        {   
            BufferedReader br = new BufferedReader(new FileReader("PatientsToBeTested.txt"));
            String line;
        
            while((line = br.readLine()) != null) 
            {
                Admin.pendingToTest++;
            }
        
            br.close();
        }  
     
    }
    
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public ArrayList<Pair<double[], Patient>> getMatrix() 
    {
        return matrix;
    }

    public final void setMatrix() 
    {
        this.matrix = new ArrayList<>();
    }

    public OverAllAccuracy getOverallAccuracy() 
    {
        return overallAccuracy;
    }

    public static boolean checkPassword(String id, String password) 
    {
        if (!id.startsWith("A")) 
        {
            return false;
        } 
        else 
        {
            for (Person p : Person.getPeople())
            {
                if ((p instanceof Admin) && (p.getId().equals(id) && p.getPassword().equals(password))) 
                {
                    return true;
                }
            }
            
            return false;
        }  
        
    }
    
    public File Normalize(File toCheck) throws IOException
    {
        normalizedPicture = img.checkOnNormality(toCheck);
        return normalizedPicture;          
    }
    
    @SuppressWarnings("unchecked")
    public String[] ExtractFeatures(String method, File toExtract) throws IOException
    {
        Features features = new Features(toExtract);
       
        features.makingFeaturesVector();
        
        if (method.equalsIgnoreCase("Train")) 
        {
            if (toExtract.toString().contains("Tumor")) 
            {
                matrix.add(new Pair<>(features.getFeaturesVector(), new tumorPatient()));
            } 
            else 
            {
                matrix.add(new Pair<>(features.getFeaturesVector(), new normalPatient()));
            }
        } 
        else 
        {
            matrix.add(new Pair<>(features.getFeaturesVector(), null));
        }

        double[] toadd = matrix.get(matrix.size() - 1).getKey();
        String[] data = new String [toadd.length + 2];
        
        if (method.equalsIgnoreCase("Train")) 
        {
            if (toExtract.toString().contains("Tumor")) 
            {
                data[data.length - 1] = "Tumor";
            } 
            else 
            {
                data[data.length - 1] = "Normal";
            }
        } 
        else 
        {
            data[data.length - 1] = "Unknown";
        }
        
        data[0] = toExtract.getName();
        
        for (int i = 0; i < toadd.length; i++) 
        {
            data[i+1] = String.valueOf(toadd[i]);
        }

        return data;
    }
    
    public void Train() throws IOException
    {
        train = true;
        writeToFile();
        Train = new trainBayesianModel(matrix);  
        Train.divideMatrix();
        Train.calculate();
        Train.writeToFile();
       
    }

    public String[] getResults()
    {
        String[] p = new String[this.patientsToTest.size()];
        int i = 0;
        
        for (Pair<File, Patient> image : this.patientsToTest)
        { 
            if (image.getValue() instanceof normalPatient) 
            {
                p[i] = "Normal";
            } 
            else if (image.getValue() instanceof tumorPatient)
            {
                p[i] = "Tumor";
            } 
            else 
            {
                p[i] = "UnPredictable";
            }
            
            i++;
        }
        
        return p;   
    }
    
    public void Test() throws IOException
    {
        Test = new testBayesianClassifier();
        Test.readFromFile();

        for (int i = 0; i < matrix.size(); i++) 
        {
            System.out.println("__________________________________\n");
            Test.setImageFeatures(matrix.get(i).getKey());
            Test.calculate();
            String result = Test.Prediction();
            System.out.println("__________________________________");
            
            if(result.equals("Normal")) 
            {
                patientsToTest.set(i, new Pair<>(this.patientsToTest.get(i).getKey(),
                        new normalPatient(this.patientsToTest.get(i).getValue().getId(),
                                this.patientsToTest.get(i).getValue().getPassword())));
            } 
            else 
            {
                patientsToTest.set(i, new Pair<>(this.patientsToTest.get(i).getKey(),
                        new tumorPatient(this.patientsToTest.get(i).getValue().getId(), 
                                this.patientsToTest.get(i).getValue().getPassword())));
            }
        }
        
        writeToFile();
        
        Writable.deleteFile("PatientsToBeTested.txt");
    }
    
    public Pair<ArrayList<String[]>, ArrayList<String>> OverallAccuracy() throws IOException
    {
        ArrayList<File> files = overallAccuracy.Validate(); 
        ArrayList<String[]> data = new ArrayList<>();
        
        for (File f : files)
        {
            data.add(ExtractFeatures("Train",Normalize(f)));
        }
        
        ArrayList<String> results = overallAccuracy.setConfusionMatrix(matrix);
        overallAccuracy.calculate();
        overallAccuracy.writeToFile();
        
        return new Pair<>(data, results);
    }
    
    @Override
    @SuppressWarnings({"ConvertToTryWithResources", "NestedAssignment"})
    public void readFromFile()throws FileNotFoundException, IOException 
    {
        if (Readable.check("PatientsToBeTested.txt"))  
        {  
            BufferedReader br = new BufferedReader(new FileReader("PatientsToBeTested.txt"));
            String line;
            this.patientsToTest = new ArrayList<>();
        
            while((line = br.readLine()) != null)
            {
                String temp[] = line.split("::");  
            
                patientsToTest.add(new Pair<>( new File (temp[1]), new Patient(temp[0], "Patient_"+temp[0])));
            }
        
            br.close();
        }  
    }

    @Override
    public void writeToFile() throws IOException
    {
        if (train)
        {
            writeFeaturesMatrix();
            train = false;
            return;
        }
        
        writeResults();
    }
    
    @SuppressWarnings("ConvertToTryWithResources")
    public void writeFeaturesMatrix() throws IOException
    {
        File file = new File ("FeaturesMatrix.txt");

        if (file.createNewFile()) 
        {
            System.out.println("File created: " + file.getName());
        } 
        else 
        {
            System.out.println("File already exists.");
        }

        FileWriter myWriter = new FileWriter(file);

        for (Pair<double[],Patient> featurevector : matrix) 
        {
            for (int j = 0; j < featurevector.getKey().length; j++) 
            {
                myWriter.write(String.valueOf(featurevector.getKey()[j]) + "::");
            }
            
            if (featurevector.getValue() instanceof tumorPatient) 
            {
                myWriter.write("Tumor");
            } 
            else 
            {
                myWriter.write("Normal");
            }
            
            myWriter.write("\n");
        }
     
        myWriter.close();
    }
    
    @SuppressWarnings("ConvertToTryWithResources")
    public void writeResults()throws IOException
    {
        File file = new File ("PatientsResults.txt");

        if (file.createNewFile()) 
        {
            System.out.println("File created: " + file.getName());
        } 
        else 
        {
            System.out.println("File already exists.");
        }

        FileWriter myWriter = new FileWriter(file,true);

        for (Pair<File,Patient> patient : this.patientsToTest) 
        {
            myWriter.write(patient.getValue().getId() + "::" + patient.getValue().getPassword() + "::");
            
            if (patient.getValue() instanceof tumorPatient) 
            {
                myWriter.write("Tumor::");
            } 
            else 
            {
                myWriter.write("Normal::");
            }
            
            myWriter.write(patient.getKey().getPath()+"\n");
        }
     
        myWriter.close();
        
    }
    
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public ArrayList<Pair<File, Patient>> getPatientsToTest() 
    {
        return patientsToTest;
    }
    
}