package com.zman.event;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * thread-safe
 */
public class ConcurrentEventEmitter extends EventEmitter{

    /**
     * 事件 -> 函数列表
     * 每当发生事件时，会按订阅顺序依次调用函数
     */
    private Map<Object, Map<Consumer,Object>> eventListenersMap = new ConcurrentHashMap<>();

    private final Object lock = new Object();


    /**
     * 发射一个携带body的事件
     * @param event 事件标识
     * @param data  body
     */
    public void emit(Object event, Object data){
        if(event == null){
            throw new IllegalArgumentException("event can't be null");
        }

        if( eventListenersMap.containsKey(event)){
            synchronized (lock) {
                if(eventListenersMap.containsKey(event)) {
                    eventListenersMap.get(event).forEach((f,v) -> f.accept(data));
                }
            }
        }

    }

    /**
     * 订阅事件
     * @param event     事件标识
     * @param consumer  函数引用，事件发生时调用此函数
     */
    public EventEmitter on(Object event, Consumer consumer){
        if(event == null){
            throw new IllegalArgumentException("event can't be null");
        }
        if(consumer == null){
            throw new IllegalArgumentException("consumer can't be null");
        }

        synchronized (lock){
            if(!eventListenersMap.containsKey(event)){
                Map<Consumer, Object> set = new ConcurrentHashMap<>();
                eventListenersMap.put(event, set);
            }

            eventListenersMap.get(event).put(consumer, 0);
        }
        return this;
    }

    /**
     * 取消订阅
     * @param event     事件标识
     * @param consumer  函数引用，事件发生时不会再调用此函数
     */
    public EventEmitter off(Object event, Consumer consumer){
        if(event == null){
            throw new IllegalArgumentException("event can't be null");
        }
        if(consumer == null){
            throw new IllegalArgumentException("consumer can't be null");
        }

        if( eventListenersMap.containsKey(event)) {
            synchronized (lock) {
                if( eventListenersMap.containsKey(event)) {
                    eventListenersMap.get(event).remove(consumer);
                    if (eventListenersMap.get(event).size() == 0) {
                        eventListenersMap.remove(event);
                    }
                }
            }
        }

        return this;
    }

    /**
     * @return 所有被订阅的事件标识
     */
    public Set<Object> events(){
        return eventListenersMap.keySet();
    }


    /**
     * @param event 事件标识
     * @return 订阅此事件的监听者列表
     */
    public Set<Consumer> listeners(Object event){
        return eventListenersMap.getOrDefault(event, Collections.emptyMap()).keySet();
    }



}
