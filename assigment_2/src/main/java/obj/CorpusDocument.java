/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obj;

import java.io.Serializable;

/**
 *
 * @author rute
 */
public class CorpusDocument implements Serializable{
        private String corpus;
        private int docId;
        
        public CorpusDocument(String corpus, int docId){
            this.corpus=corpus;
            this.docId=docId;
        }
        
        public String getCorpus(){
            return corpus;
        }
        public int getDocId(){
            return docId;
        }
}
