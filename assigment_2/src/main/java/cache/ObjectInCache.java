    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

/**
 *
 * @author rute
 */
public class ObjectInCache<K, T> {
    private long lastAccessed = System.currentTimeMillis();
    private T value;

    protected ObjectInCache(T value) {
        this.value = value;
    }
    
    public long getLastAccessed(){
        return lastAccessed;
    }
    
     public T getValue(){
        return value;
    }

    public void updateLastAccessed(long lastAccessed){
        this.lastAccessed = lastAccessed;
    }
     
}
