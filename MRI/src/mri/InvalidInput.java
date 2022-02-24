/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mri;


@SuppressWarnings("serial")
class InvalidInput extends Exception 
{
    public InvalidInput(String message)
    {
        super(message);
    }

}
