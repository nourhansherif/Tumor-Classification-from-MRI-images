/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public interface Readable 
{
    public void readFromFile() throws FileNotFoundException, IOException;
    
    public static boolean check(String file)
    {
        return new File(file).exists();
    }
    
}
