[![Travis Build](https://api.travis-ci.org/zman2013/event-emitter.svg?branch=master)](https://api.travis-ci.org/zman2013/event-emitter.svg?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/zman2013/event-emitter/badge.svg?branch=master)](https://coveralls.io/github/zman2013/event-emitter?branch=master)

# event-emitter
a lightweight event emitter.

# Goals
lightweight  
high performance  
no dependency  
both thread-safe & non-thread-safe  

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
``` java


```
