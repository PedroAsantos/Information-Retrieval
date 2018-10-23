/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

/**
 *
 * @author rute
 */
public class TermFreqHigher implements Comparable<TermFreqHigher>{
    private String term;
    private int freq;
    
    public TermFreqHigher(String term, int freq){
        this.term = term;
        this.freq = freq;
    }

    @Override
    public String toString() {
        return "Term: "+term+" Frequence: "+ freq; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    public String getTerm(){
        return term;
    }
    
    public int getFreq(){
        return freq;
    }

    @Override
    public int compareTo(TermFreqHigher t) {
         if(freq < t.getFreq()){
            return -1;
        }
         if(freq > t.getFreq()){
            return 1;
        }
         return 0;
    }
}
