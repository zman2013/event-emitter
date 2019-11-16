package com.zman.event;

import java.util.*;
import java.util.function.Consumer;

/**
 * 事件订阅、通知器
 */
public class EventEmitter {

    /**
     * 事件 -> 函数列表
     * 每当发生事件时，会按订阅顺序依次调用函数
     */
    private Map<Object, List<Consumer>> eventListenersMap = new HashMap<>();

    /**
     * 发射一个没有body的事件
     * @param event 事件标识
     */
    public void emit(Object event){
        if(event == null){
            throw new IllegalArgumentException("event can't be null");
        }

        emit(event, null);
    }

    /**
     * 发射一个携带body的事件
     * @param event 事件标识
     * @param data  body
     */
    public void emit(Object event, Object data){
        if(event == null){
            throw new IllegalArgumentException("event can't be null");
        }

        eventListenersMap.computeIfAbsent(event,(e)-> new LinkedList<>()).forEach(f -> f.accept(data));
    }

    /**
     * 订阅事件
     * @param event     事件标识
     * @param consumer  函数引用，事件发生时调用此函数
     */
    public void on(Object event, Consumer consumer){
        if(event == null){
            throw new IllegalArgumentException("event can't be null");
        }
        if(consumer == null){
            throw new IllegalArgumentException("consumer can't be null");
        }

        eventListenersMap.computeIfAbsent(event, (e)->new LinkedList<>()).add(consumer);
    }

    /**
     * 取消订阅
     * @param event     事件标识
     * @param consumer  函数引用，事件发生时不会再调用此函数
     */
    public void off(Object event, Consumer consumer){
        if(event == null){
            throw new IllegalArgumentException("event can't be null");
        }
        if(consumer == null){
            throw new IllegalArgumentException("consumer can't be null");
        }

        if( eventListenersMap.containsKey(event)) {
            eventListenersMap.get(event).remove(consumer);
            if (eventListenersMap.get(event).size()==0){
                eventListenersMap.remove(event);
            }
        }

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
    public List<Consumer> listeners(Object event){
        return eventListenersMap.getOrDefault(event, Collections.emptyList());

    }

}
