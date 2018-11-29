    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

/**
 *
 * @author Pedro Santos, 76532 /  Beatriz Coronha 92210    
 */
public class ObjectInCache<T> {
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
