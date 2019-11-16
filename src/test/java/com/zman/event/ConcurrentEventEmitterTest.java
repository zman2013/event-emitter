package com.zman.event;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

public class ConcurrentEventEmitterTest {

    Consumer consumer1 = mock(Consumer.class);
    Consumer consumer2 = mock(Consumer.class);
    Consumer consumer3 = mock(Consumer.class);

    /**
     * 验证on、off好用
     */
    @Test
    public void onOff(){

        String event = "bomb";

        ConcurrentEventEmitter eventEmitter = new ConcurrentEventEmitter();
        eventEmitter.on(event, consumer1);
        Assert.assertEquals(1, eventEmitter.events().size());
        Assert.assertEquals(1, eventEmitter.listeners(event).size());

        eventEmitter.off(event, consumer1);
        Assert.assertEquals(0, eventEmitter.events().size());
        Assert.assertEquals(0, eventEmitter.listeners(event).size());

        eventEmitter.on(event, consumer1);
        eventEmitter.on(event, consumer2);
        Assert.assertEquals(2, eventEmitter.listeners(event).size());
        eventEmitter.off(event, consumer2);
        Assert.assertEquals(1, eventEmitter.listeners(event).size());

        eventEmitter.off("noListener", consumer3);
    }

    /**
     * 参数校验
     */
    @Test
    public void argumentsIllegal(){
        String event = "bomb";

        ConcurrentEventEmitter eventEmitter = new ConcurrentEventEmitter();

        try {
            eventEmitter.on(null, null);
        }catch (IllegalArgumentException e){
            Assert.assertEquals("event can't be null", e.getMessage());
        }

        try{
            eventEmitter.on(event, null);
        }catch (IllegalArgumentException e){
            Assert.assertEquals("consumer can't be null", e.getMessage());
        }

        try {
            eventEmitter.off(null, null);
        }catch (IllegalArgumentException e){
            Assert.assertEquals("event can't be null", e.getMessage());
        }

        try{
            eventEmitter.off(event, null);
        }catch (IllegalArgumentException e){
            Assert.assertEquals("consumer can't be null", e.getMessage() );
        }

    }

    /**
     * 发射事件
     */
    @Test
    public void emitEvent(){
        String event1 = "bomb1";
        String event2 = "bomb2";

        EventEmitter eventEmitter = new ConcurrentEventEmitter();

        eventEmitter.on(event1, consumer1);
        eventEmitter.on(event1, consumer2);
        eventEmitter.on(event2, consumer3);

        eventEmitter.emit(event1);
        verify(consumer1, times(1)).accept(null);
        verify(consumer2, times(1)).accept(null);

        verify(consumer3, never()).accept(null);

        eventEmitter.emit(event2, 1000);
        verify(consumer3, times(1)).accept(1000);

    }

    @Test
    public void emitEventNoListener(){
        String event1 = "bomb1";
        String event2 = "bomb2";
        String event3 = "bomb3";

        EventEmitter eventEmitter = new EventEmitter();

        eventEmitter.on(event1, consumer1);
        eventEmitter.on(event1, consumer2);
        eventEmitter.on(event2, consumer3);

        eventEmitter.emit(event3);
        verify(consumer1, never()).accept(null);
        verify(consumer2, never()).accept(null);
        verify(consumer3, never()).accept(null);
    }

    /**
     * 参数校验
     */
    @Test
    public void emitEventException(){
        ConcurrentEventEmitter eventEmitter = new ConcurrentEventEmitter();

        try {
            eventEmitter.emit(null);
        }catch (IllegalArgumentException e){
            Assert.assertEquals("event can't be null", e.getMessage());
        }

        try {
            eventEmitter.emit(null, null);
        }catch (IllegalArgumentException e){
            Assert.assertEquals("event can't be null", e.getMessage());
        }

    }

    /**
     * 并发操作emitter
     */
    @Test
    public void onOffEmitConcurrently() throws InterruptedException {

        String event1 = "bomb1";
        String event2 = "bomb2";
        String event3 = "bomb3";

        ConcurrentEventEmitter eventEmitter = new ConcurrentEventEmitter();
        eventEmitter.on(event1, consumer1);

        CountDownLatch latch = new CountDownLatch(4);
        new Thread(()->{
            for(int i =0; i < 10000; i++) {
                eventEmitter.emit(event1);
                eventEmitter.emit(event2);
            }
            latch.countDown();
        }).start();

        new Thread(()->{
            for (int i = 0; i < 10000; i ++){
                eventEmitter.on(event1, consumer2);
                eventEmitter.on(event2, consumer2);
                eventEmitter.on(event3, consumer2);

                eventEmitter.on(event1, consumer3);
                eventEmitter.on(event2, consumer3);
                eventEmitter.on(event3, consumer3);
            }
            latch.countDown();
        }).start();

        new Thread(()->{
            for (int i = 0; i < 10000; i ++){
                eventEmitter.off(event1, consumer2);
                eventEmitter.off(event2, consumer2);
                eventEmitter.off(event3, consumer2);

            }
            latch.countDown();
        }).start();

        new Thread(()->{
            for (int i = 0; i < 10000; i ++){

                eventEmitter.off(event1, consumer3);
                eventEmitter.off(event2, consumer3);
                eventEmitter.off(event3, consumer3);
            }
            latch.countDown();
        }).start();

        latch.await();

        verify(consumer1, times(10000)).accept(null);
    }

}
