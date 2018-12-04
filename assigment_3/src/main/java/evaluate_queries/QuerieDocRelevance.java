/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evaluate_queries;

/**
 *
 * @author Pedro Santos
 */
public class QuerieDocRelevance implements Comparable<QuerieDocRelevance> {
    
    private int docId;
    private int relevance;
    
    public QuerieDocRelevance(int docId, int relevance){
        this.docId=docId;
        this.relevance = 5 - relevance;
    }
    
    public int getDocID(){
        return docId;
    }
    
    public int getRelevance(){
        return relevance;
    }
    
    @Override
    public int compareTo(QuerieDocRelevance t) {
        if(this.relevance > t.getRelevance()){
            return -1;
        }
        if(this.relevance< t.getRelevance()){
            return 1;
        }
        
        return 0;
    }
}
