/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evaluate_queries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import reader.CorpusReader;
import retrieval.ScoreRetrieval;

/**
 *
 * @author rute
 */
public class EvaluationQueries {
    private List<Double> precision;
    private List<Double> recall;
    private List<Double> fMeasures;
    private List<Double> apRes;
    private List<Double> apResRankedTopLimited;
    private Map<Integer, List<QuerieDocRelevance>> queriesRelevanceMap;
    private double map;
    private double mapLimited;
    public EvaluationQueries(){
        precision = new ArrayList<>();
        recall = new ArrayList<>();
        fMeasures = new ArrayList<>();
        apRes = new ArrayList<>();
        apResRankedTopLimited = new ArrayList<>();
        queriesRelevanceMap = new HashMap<Integer, List<QuerieDocRelevance>>();
        readDocQueriesRelevance();
    }
    
    private void readDocQueriesRelevance(){
         File f = new File("cranfield.query.relevance.txt");
         if(f.exists() && !f.isDirectory()){
            try {
                FileReader fileReader=null;
                try {
                    fileReader = new FileReader(f);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CorpusReader.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                String[] querieDocRel;
                List<QuerieDocRelevance> tempQueriesRelevance;
                while ((line = bufferedReader.readLine()) != null) {
                    querieDocRel = line.split("\\s+");
                    int querieNumber = Integer.parseInt(querieDocRel[0]);
                    if(queriesRelevanceMap.containsKey(querieNumber)){
                        tempQueriesRelevance = queriesRelevanceMap.get(querieNumber);
                        tempQueriesRelevance.add(new QuerieDocRelevance(Integer.parseInt(querieDocRel[1]),Integer.parseInt(querieDocRel[2])));
                        queriesRelevanceMap.put(querieNumber,tempQueriesRelevance);
                        
                    }else{
                        tempQueriesRelevance = new ArrayList<>();
                        tempQueriesRelevance.add(new QuerieDocRelevance(Integer.parseInt(querieDocRel[1]),Integer.parseInt(querieDocRel[2])));
                        queriesRelevanceMap.put(querieNumber,tempQueriesRelevance); 
                    }
                                      
                }
                
            } catch (IOException ex) {
                Logger.getLogger(EvaluationQueries.class.getName()).log(Level.SEVERE, null, ex);
            }           
         }     
                   
    }
    
    private void updatePrecisionAndRecallAndAp(List<ScoreRetrieval> results, List<QuerieDocRelevance> querieRelevance){
        List<Double> precisionQuerieAP = new ArrayList<>();
        List<Double> precisionQuerieAPRankedTop = new ArrayList<>();
        int tp=0;
        int fp=0;
        int fn=0;
        int cont=0;
        int rankMax=10;
        
        //results.stream().map(ScoreRetrieval::getDocId).filter( docId -> querieRelevance.contains(docId)).mapToInt(List::size).sum();
        
        for(ScoreRetrieval result : results){
            if(querieRelevance.stream().filter(o -> o.getDocID()==result.getDocId()).findFirst().isPresent()){ 
                tp++;
                precisionQuerieAP.add(tp/(double)(tp+fp));
                if(cont<rankMax){
                    precisionQuerieAPRankedTop.add(tp/(double)(tp+fp));
                }
            }else{
                fp++;
            }
            cont++;
        }
        if(precisionQuerieAP.isEmpty()){
            apRes.add(0.0);
        }else{
             apRes.add(precisionQuerieAP.stream().mapToDouble(d->d).sum()/precisionQuerieAP.size());
        }
        if(precisionQuerieAPRankedTop.isEmpty()){
             apResRankedTopLimited.add(0.0);
        }else{
             apResRankedTopLimited.add(precisionQuerieAPRankedTop.stream().mapToDouble(d->d).sum()/precisionQuerieAPRankedTop.size());
        }
       
        
        for(QuerieDocRelevance querieRel : querieRelevance){
            if(!(results.stream().filter(q -> q.getDocId()==querieRel.getDocID()).findFirst().isPresent())){
                fn++; 
            }
        }
        
        precision.add(tp/(double)(tp+fp));
        recall.add(tp/(double)(tp+fn));
    }
    
    private void updateFMeasure(){
        double precisionValue = precision.get(precision.size()-1);
        double recallValue = recall.get(recall.size()-1);
        fMeasures.add((2*precisionValue*recallValue)/(precisionValue+recallValue));
    }
    
    
    public void updateMetrics(List<ScoreRetrieval> results, int querieID){
        List<QuerieDocRelevance> querieRelevance = queriesRelevanceMap.get(querieID);
        
        updatePrecisionAndRecallAndAp(results,querieRelevance);
        updateFMeasure();
        
    }
    
    public void calculateMAP(){
        map = apRes.stream().mapToDouble(d->d).sum()/apRes.size();
        mapLimited = apResRankedTopLimited.stream().mapToDouble(d->d).sum()/apResRankedTopLimited.size();
        
    }
    
    
    public void teste(){
        System.out.println("precision");
        System.out.println(precision);
        System.out.println("recall");
        System.out.println(recall);
        System.out.println("fMeasures");
        System.out.println(fMeasures);
        System.out.println("apRes");
        System.out.println(apRes);
        System.out.println("apResLimited");
        System.out.println(apResRankedTopLimited);
        System.out.println("map");
        System.out.println(map);
        System.out.println("mapLimited");
        System.out.println(mapLimited);
        int max = -1;
        
        for (Map.Entry<Integer, List<QuerieDocRelevance>> entry : queriesRelevanceMap.entrySet()) {
		if(entry.getValue().size()>max){
                    max=entry.getValue().size();
                }
	}
        System.out.println(max);
        
    }
    
  
    
}
