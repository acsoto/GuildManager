# 简介

基于Minecraft Spigot服务端开发的插件

# 效果展示

## 启动

![image-20201027170041530](C:\Users\soto\AppData\Roaming\Typora\typora-user-images\image-20201027170041530.png)

![image-20201027174737996](C:\Users\soto\AppData\Roaming\Typora\typora-user-images\image-20201027174737996.png)

# 源码

每一个class的内容

## GuildManager

主类，用于接入Spigot的插件启动，关闭，注册以及插件的具体操作和存放静态变量

## Guild

描述一个公会的类，包括一些成员变量和一些对成员变量的操作

### 成员变量

```java
String name;
String chairman;
int MaxAdvancedPlayers;
int level;
```

name支持设置和获取

chairman支持设置和获取

## GuildAdmin

管理员指令处理器

## GuildCommand

一般指令处理器

