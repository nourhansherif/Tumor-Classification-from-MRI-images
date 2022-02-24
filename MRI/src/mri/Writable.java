/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mri;

import java.io.File;
import java.io.IOException;

public interface Writable 
{
    public void writeToFile() throws IOException;
    
    public static void deleteFile(String string)
    {
        File file = new File(string);
        file.delete();   
    }
   
}
