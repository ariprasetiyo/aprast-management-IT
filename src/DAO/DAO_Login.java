/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

/**
 *
 * @author arprast
 */
public class DAO_Login {
    private String User;
    private String Password;
    
    public DAO_Login(){
        
    }
    
    public String GetDataUser(){
        return User;
    }
    public void SetDataUser(String Data){
        this.User = Data;
    }
    
    public String GetDataPassword(){
        return Password;
    }
    public void SetDataPassword(String Data){
        this.Password = Data;
    }
}
