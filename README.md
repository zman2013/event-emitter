[![Travis Build](https://api.travis-ci.org/zman2013/event-emitter.svg?branch=master)](https://api.travis-ci.org/zman2013/event-emitter.svg?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/zman2013/event-emitter/badge.svg?branch=master)](https://coveralls.io/github/zman2013/event-emitter?branch=master)

# event-emitter
a lightweight event emitter.

# Goals
lightweight  
high performance  
no dependency  
both thread-safe & non-thread-safe  

# dependency
## maven
```xml
<dependency>
    <groupId>com.zmannotes.event</groupId>
    <artifactId>event-emitter</artifactId>
    <version>0.0.4</version>
</dependency>
```

# intefaces
``` java
// 发射一个没有body的事件 
void emit(Object event)

// 发射一个携带body的事件
void emit(Object event, Object data)

// 订阅事件，事件发生时会回调consumer
void on(Object event, Consumer consumer)

// 取消订阅，事件发生时不会再调用此函数
void off(Object event, Consumer consumer)

// 所有被订阅的事件标识
Set<Object> events()

// 订阅此事件的监听者列表
Set<Consumer> listeners(Object event)
```

# examples
## thread-safe
```java
/**
 * 模拟一架飞机起飞、报告飞行速度、降落三种状态，分别对外广播不同的事件。
 * 当飞机广播事件时，外部监听者可以实时收到。
 */
public class Airplane extends ConcurrentEventEmitter {

    public void takeoff(){
        emit("takeoff");
    }

    public void flying(int speed){
        emit("speed", speed);
    }

    public void landing(){
        emit("landing");
    }

    public static void main(String[] args){
        Airplane airplane = new Airplane();

        airplane.on("takeoff", d -> System.out.println("takeoff"));
        airplane.on("speed",   d -> System.out.println("speed: "+d));
        airplane.on("landing", d -> System.out.println("landing"));

        airplane.takeoff();
        airplane.flying(300);
        airplane.flying(600);
        airplane.flying(1000);
        airplane.flying(600);
        airplane.flying(300);
        airplane.landing();
    }

}
```
## not thread-safe
```java
public class Airplane extends EventEmitter {

    public void takeoff(){
        emit("takeoff");
    }

    public void flying(int speed){
        emit("speed", speed);
    }

    public void landing(){
        emit("landing");
    }

    public static void main(String[] args){
        Airplane airplane = new Airplane();

        airplane.on("takeoff", d -> System.out.println("takeoff"));
        airplane.on("speed",   d -> System.out.println("speed: "+d));
        airplane.on("landing", d -> System.out.println("landing"));

        airplane.takeoff();
        airplane.flying(300);
        airplane.flying(600);
        airplane.flying(1000);
        airplane.flying(600);
        airplane.flying(300);
        airplane.landing();
    }

}
```
