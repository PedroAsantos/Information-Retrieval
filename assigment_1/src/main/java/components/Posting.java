/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.io.Serializable;

/**
 *
 * @author rute
 */
public class Posting implements Serializable, Comparable<Posting>{
    
    private int id;
    private int nOcurrences;
    
    public Posting(int id, int nOcurrences){
        this.id = id;
        this.nOcurrences=nOcurrences;
    }

    @Override
    public String toString() {
        return id + ":" + nOcurrences; //To change body of generated methods, choose Tools | Templates.
    }

    public int getId(){
        return id;
    }
    public int getNOcurrences(){
        return nOcurrences;
    }
        
    @Override
    public int compareTo(Posting t) {
        if(this.id < t.getId()){
            return -1;
        }
        if(this.id> t.getId()){
            return 1;
        }
        
        return 0;
    }
    
    
    
}
