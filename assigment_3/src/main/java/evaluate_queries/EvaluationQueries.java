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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import reader.CorpusReader;
import retrieval.ScoreRetrieval;

/**
 *
 * @author Pedro Santos
 */
public class EvaluationQueries {
    private List<Double> precision;
    private List<Double> recall;
    private List<Double> fMeasures;
    private List<Double> apRes;
    private List<Double> apResRankedTopLimited;
    private List<List<Double>> ndcgResults;
    private Map<Integer, List<QuerieDocRelevance>> queriesRelevanceMap;
    private double mapFinal;
    private double mapLimitedFinal;
    private double precisionFinal;
    private double recallFinal;
    private double fMeausureFinal;
    private double ndcgResultsFinal;
    public EvaluationQueries(){
        precision = new ArrayList<>();
        recall = new ArrayList<>();
        fMeasures = new ArrayList<>();
        apRes = new ArrayList<>();
        ndcgResults = new ArrayList<>();
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
        if(precisionValue==0.0 || recallValue==0.0){
            fMeasures.add(0.0);
        }else{
            fMeasures.add((2*precisionValue*recallValue)/(precisionValue+recallValue));
        }
        
    }
    
    private void calculateNDCG(List<ScoreRetrieval> results,List<QuerieDocRelevance> querieRelevance){
        List<Double> ndcgResultsQuery = new ArrayList<>();
        List<Double> realdcgResults = new ArrayList<>();
        List<Double> idealDcgResults = new ArrayList<>();
        ScoreRetrieval resultT = results.get(0);
        Optional<QuerieDocRelevance> docMatch = querieRelevance.stream().filter(q -> resultT.getDocId()==q.getDocID()).findFirst();
        QuerieDocRelevance doc = docMatch.orElse(null);        
        
        
        Collections.sort(querieRelevance);
      
        idealDcgResults.add((double) querieRelevance.get(0).getRelevance());
        
        if (doc != null) {
            realdcgResults.add((double)doc.getRelevance());
        } else {
            realdcgResults.add(0.0);
        }
        
        if(!(realdcgResults.isEmpty()) && !(idealDcgResults.isEmpty())){
            ndcgResultsQuery.add(realdcgResults.get(0)/idealDcgResults.get(0));
        }
       
        
        for(int i = 1;i<results.size() && i<querieRelevance.size();i++){
            ScoreRetrieval result = results.get(i);
            docMatch = querieRelevance.stream().filter(q -> result.getDocId()==q.getDocID()).findFirst();
            doc = docMatch.orElse(null);
          
            if(doc != null){
                realdcgResults.add(realdcgResults.get(i-1)+(double)doc.getRelevance()/(Math.log(i+1)/Math.log(2)));
            }else{
                realdcgResults.add(realdcgResults.get(i-1));
            }
            

            idealDcgResults.add(idealDcgResults.get(i-1)+(double) querieRelevance.get(i).getRelevance()/(Math.log(i+1)/Math.log(2)));

            
            ndcgResultsQuery.add(realdcgResults.get(i)/idealDcgResults.get(i));
            
        }   
       
        
        ndcgResults.add(ndcgResultsQuery);
        
    }
    
    public void updateMetrics(List<ScoreRetrieval> results, int querieID){
        List<QuerieDocRelevance> querieRelevance = queriesRelevanceMap.get(querieID);
        
        updatePrecisionAndRecallAndAp(results,querieRelevance);
        updateFMeasure();
        calculateNDCG(results,querieRelevance);
    }
    
    public void calculateMetrics(){
        //precision
        precisionFinal = precision.stream().mapToDouble(f -> f).sum()/precision.size();
        System.out.print("Precision: ");
        System.out.println(precisionFinal);
        //recall
        recallFinal = recall.stream().mapToDouble(f -> f).sum()/recall.size();
        System.out.print("Recall: ");
        System.out.println(recallFinal);
        //fMeasures
        fMeausureFinal = fMeasures.stream().mapToDouble(f -> f).sum()/fMeasures.size();
        System.out.print("Fmeasure: ");
        System.out.println(fMeausureFinal);

         //MAP
        mapFinal = apRes.stream().mapToDouble(d->d).sum()/apRes.size();
        System.out.print("Mean Avarage Precision: ");
        System.out.println(mapFinal);
      
        mapLimitedFinal = apResRankedTopLimited.stream().mapToDouble(d->d).sum()/apResRankedTopLimited.size();
        System.out.print("Mean Precision at rank 10: ");
        System.out.println(mapLimitedFinal);
        
        //NDCG
        ndcgResultsFinal =0;
        ndcgResults.stream().forEach(l-> {
            //it is always true, only added to ensure that doesnt' break with another examples where some queries doens't have results.
            if(l.size()>0){
                 ndcgResultsFinal= l.get(l.size()-1)+ndcgResultsFinal;
            }
        });
        ndcgResultsFinal=ndcgResultsFinal/ndcgResults.size();
        
        System.out.print("NDCG: ");
        System.out.println(ndcgResultsFinal);
    }
    
    
}
