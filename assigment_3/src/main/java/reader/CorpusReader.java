/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author rute
 */
public class CorpusReader {
    private int docId=1;
    private static final int MAXIMUN_DOCS=1000;
    private String basePath;
    public CorpusReader(String basePath){
      this.basePath = basePath;
    }
    
    
     /**
     *
     * 
     * @return the documents read in a list. Each position of the list is a document
     * @throws java.io.FileNotFoundException 
    */
    public List<CorpusDocument> corpusReader() {
        
        
        String line = null;
        String[] currentLineTokens;
        //each index of array corpus corresponds to a document 
        List<CorpusDocument> corpus = new ArrayList<>();
        String lineToSave;
        int countBlockDocs =0; 
        boolean betweenTags;
        File f;
        f = new File(basePath+String.format("cranfield"+"%04d", docId));
       
        if(f.exists() && !f.isDirectory()){
            do {
                try {
                    FileReader fileReader=null;
                    try {
                        fileReader = new FileReader(f);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(CorpusReader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    Pattern p = Pattern.compile("<TITLE>|<TEXT>");
                    Pattern pT = Pattern.compile("</TITLE>|</TEXT>");
                    lineToSave = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        Matcher m = p.matcher(line);
                        if(m.find()){
                            betweenTags=true;
                            while (betweenTags && (line = bufferedReader.readLine()) != null){
                                Matcher mT = pT.matcher(line);
                                if(mT.find()){
                                    betweenTags=false;
                                }else{
                                    lineToSave+= line;
                                }
                                
                            }
                        }
                    }
                    
                    corpus.add(new CorpusDocument(lineToSave,docId));
                    
                    docId++;
                    countBlockDocs++;
                    f = new File(basePath+String.format("cranfield" + "%04d", docId));
                    try {
                        bufferedReader.close();
                    } catch (IOException ex) {
                        Logger.getLogger(CorpusReader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if (countBlockDocs == MAXIMUN_DOCS) {
                        return corpus;
                    }
                    
                } catch (IOException ex) {
                    Logger.getLogger(CorpusReader.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } while (f.exists() && !f.isDirectory());
          
        }
        
        if(corpus.isEmpty()){
            writeToFileTotalNumberOfDocs();
        }
                 
        // returns the contents of each document in a collection (corpus)
        return corpus;
    }
    
     /**
     * 
     * Writes on the file indexer_number_of_docs.txt file the total number of documents 
     */
    private void writeToFileTotalNumberOfDocs(){
        FileWriter fw = null;
        PrintWriter writeLine=null;
        try {
            fw = new FileWriter("indexer_number_of_docs.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            writeLine = new PrintWriter(bw);
           
            writeLine.println("numberOfDocs:"+docId);
        } catch (IOException ex) {
            Logger.getLogger(CorpusReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writeLine.close();
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(CorpusReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
