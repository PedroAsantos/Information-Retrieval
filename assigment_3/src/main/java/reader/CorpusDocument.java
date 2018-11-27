/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reader;

/**
 *
 * @author rute
 */
public class CorpusDocument {
    
    private String corpus;
    private int id;
    
    public CorpusDocument(String corpus, int id){
        this.corpus = corpus;
        this.id = id;
    }
    
    public int getDocId(){
        return id;
    }
    public String getCorpus(){
        return corpus;
    }
    
    @Override
    public String toString() {
        return id+"-"+ corpus;
    }
    
}
