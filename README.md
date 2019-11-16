# event-emitter
a lightweight event emitter.

# Goals
lightweight  
high performance  
no dependency  
not thread-safe  

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
List<Consumer> listeners(Object event)
```

# examples
``` java


```
