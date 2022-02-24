/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package People;


public class normalPatient extends Patient
{
    public normalPatient()
    {
        super(); 
    }
    
    public normalPatient(String id, String password) 
    {
        super(id, password);
        Patient.setPatientID(Patient.getPatientID() + 1);
    }

}
