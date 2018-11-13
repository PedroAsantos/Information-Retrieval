/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Pedro Santos, 76532 /  Beatriz Coronha 92210    
 */
class Node<K>{
    K key;
    ObjectInCache value;
    Node pre;
    Node next;
 
    public Node(K key, ObjectInCache value){
        this.key = key;
        this.value = value;
    }
    
    public ObjectInCache getValue(){
        return value;
    }
    
    
}

public class LRUCache<K> {
    private int capacity;
    private HashMap<K, Node> map;
    private Node head=null;
    private Node end=null;
 
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
    }
    public int size(){
        return map.size();
    }
    public Iterator<Map.Entry<K, Node>> mapIterator(){
        return map.entrySet().iterator();
    }
    public ObjectInCache get(K key) {
        if(map.containsKey(key)){
            Node n = map.get(key);
            removeNode(n);
            setHead(n);
            return n.value;
        }
 
        return null;
    }
 
    public void remove(K key){
        if(map.containsKey(key)){
            Node n = map.get(key);
            removeNode(n);    
            map.remove(key);
        }
    }
    
    private void removeNode(Node n){
        if(n.pre!=null){
            n.pre.next = n.next;
        }else{
            head = n.next;
        }
 
        if(n.next!=null){
            n.next.pre = n.pre;
        }else{
            end = n.pre;
        }
    }
 
    private void setHead(Node n){
        n.next = head;
        n.pre = null;
 
        if(head!=null)
            head.pre = n;
 
        head = n;
 
        if(end ==null)
            end = head;
    }
 
    public void put(K key, ObjectInCache value) {
        if(map.containsKey(key)){
            Node old = map.get(key);
            old.value = value;
            removeNode(old);
            setHead(old);
        }else{
            Node created = new Node(key, value);
            if(map.size()>=capacity){
                map.remove(end.key);
                removeNode(end);
                setHead(created);
 
            }else{
                setHead(created);
            }    
            map.put(key, created);
        }
    }
}
