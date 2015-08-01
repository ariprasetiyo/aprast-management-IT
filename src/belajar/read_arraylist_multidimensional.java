/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belajar;

import java.util.ArrayList;

/**
 *
 * @author arprast
 */

// our "read_arraylist_multidimensional"esque class for the linked data we're handling
public class read_arraylist_multidimensional {
    String description;
    float value;
    public read_arraylist_multidimensional(String description, float value) {
        this.description = description;
        this.value = value;
    }
    
    public read_arraylist_multidimensional() {
        coba();
         //and then we iterate
        
        ArrayList<read_arraylist_multidimensional> mylist = coba();
        String d; float v;
        for(read_arraylist_multidimensional m: mylist) {
          d = m.description;
          v = m.value;
          System.out.println(d );
        }
    }
   
    public ArrayList<read_arraylist_multidimensional> coba (){
        // build our list of objects
        ArrayList<read_arraylist_multidimensional> mylist = new ArrayList<read_arraylist_multidimensional>();

        mylist.add(new read_arraylist_multidimensional("description1", 10));
        mylist.add(new read_arraylist_multidimensional("description2", 12));
        mylist.add(new read_arraylist_multidimensional("description3", 14));
        
        return mylist;
       
    }
    
    public static void main (String[] args){
        read_arraylist_multidimensional a = new read_arraylist_multidimensional();
    }
}