/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package People;

import java.io.*;
import java.util.*;


public abstract class Person 
{
    private final String id;
    private final String password;
    private static ArrayList<Person> people;
    
    public Person(String id,String password)
    {
       this.id = id;
       this.password = password;
    }
    
    public Person()
    {
        this.id = "";
        this.password = "";
    }
    
    @SuppressWarnings({"ConvertToTryWithResources", "NestedAssignment"})
    public static void setPeople() throws IOException
    {
        if (new File("People.txt").exists())
        {
            BufferedReader br = new BufferedReader(new FileReader("People.txt"));
            String line;
            people = new ArrayList<>();
        
            while((line = br.readLine()) != null)
            {
                String temp[] = line.split("::");
                
                switch (temp[0]) 
                {
                    case "Doctor":
                        people.add(new Doctor(temp[1], temp[2]));
                        break;
                    case "Admin":
                        people.add(new Admin(temp[1], temp[2]));
                        break;
                    
                    default:
                        break;
                }
            }
        
            br.close();
        }
        else 
        {
            people = new ArrayList<>();            
        }
       
    }

    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public static ArrayList<Person> getPeople() 
    {
        return people;
    }
    
    public String getId() 
    {
        return id;
    }

    public String getPassword() 
    {
        return password;
    }
    
    public static Person checkPassword(String user, String id, String password)
    {
        if (user.equals("Admin") && Admin.checkPassword(id, password)) 
        {
            return new Admin(id, password);
        } 
        else if (user.equals("Doctor") && Doctor.checkPassword(id, password)) 
        {
            return new Doctor(id, password);
        }
        else if (user.equals("Patient")) 
        {
            return Patient.checkPassword(id, password);
        }
        
        return null;
    }
    
}