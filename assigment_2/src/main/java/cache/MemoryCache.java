/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Pedro Santos, 76532 /  Beatriz Coronha 92210    
 */
public class MemoryCache<K,T> {
    
     private long timeToLive;
     private LRUCache cacheMap;
 
    
    public MemoryCache(long timeToLive, final long timerInterval, int maxItems) {
        this.timeToLive = timeToLive * 1000;

        cacheMap = new LRUCache(maxItems);

        if (timeToLive > 0 && timerInterval > 0) {

            Thread t = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(timerInterval * 1000);
                        } catch (InterruptedException ex) {
                        }
                        cleanup();
                    }
                }
            });

            t.setDaemon(true);
            t.start();
        }
    }
    
     public void put(K key, T value) {
        synchronized (cacheMap) {
            cacheMap.put(key, new ObjectInCache(value));
        }
    }
 
   // @SuppressWarnings("unchecked")
    public T get(K key) {
        synchronized (cacheMap) {
            ObjectInCache c = (ObjectInCache) cacheMap.get(key);
 
            if (c == null)
                return null;
            else {
                c.updateLastAccessed(System.currentTimeMillis());
                return (T) c.getValue();
            }
        }
    }
 
    public void remove(K key) {
        synchronized (cacheMap) {
            cacheMap.remove(key);
        }
    }
 
    public int size() {
        synchronized (cacheMap) {
            return cacheMap.size();
        }
    }
 
  //  @SuppressWarnings("unchecked")
    public void cleanup() {
        long now = System.currentTimeMillis();
        ArrayList<K> deleteKey = null;
 
        synchronized (cacheMap) {
            System.out.println("clean");
            Iterator<Map.Entry<K, Node>> itr = cacheMap.mapIterator();
 
            deleteKey = new ArrayList<K>((cacheMap.size() / 2) + 1);
            Map.Entry key = null;
            ObjectInCache c = null;
            
            while (itr.hasNext()) {
                key = (Map.Entry) itr.next();
                c = (ObjectInCache) cacheMap.get(key.getKey());
                
                if (c != null && (now > (timeToLive + c.getLastAccessed()))) {
                    deleteKey.add((K) key.getKey());
                }
                itr.remove();
            }
        }
        
        for (K key : deleteKey) {
            synchronized (cacheMap) {
                cacheMap.remove(key);
            }
            Thread.yield();
        }
    }


}
