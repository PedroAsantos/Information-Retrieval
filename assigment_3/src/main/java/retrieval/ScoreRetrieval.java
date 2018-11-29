/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrieval;


/**
 *
 * @author rute
 */
public class ScoreRetrieval implements Comparable<ScoreRetrieval> {
    private int docId;
    private double score;
    
    public ScoreRetrieval(int docId, double score) {
        this.docId = docId;
        this.score = score;
    }
    
    public int getDocId(){
        return docId;
    }
    public double getScore(){
        return score;
    }
    
    @Override
    public int compareTo(ScoreRetrieval t) {
        if(this.score < t.getScore()){
            return 1;
        }
        if(this.score > t.getScore()){
            return -1;
        }      
        return 0;
    }
     @Override
    public String toString() {
        return "docId: "+docId+" score: "+score; 
    }  
}
