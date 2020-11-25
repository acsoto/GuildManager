# 简介

对Minecraft Spigot服务端开发的Bukkit插件

github: https://github.com/acsoto/GuildManager

## 效果展示

### 启动



# 功能介绍

## 指令全集



## 基本操作

### 普通玩家

/gmg

| 操作                      | 功能                                                         |
| ------------------------- | ------------------------------------------------------------ |
| list                      | 列出公会列表（遍历GuildManager.Guilds）                      |
| check                     | 查看某公会的具体数据                                         |
| tp                        | 传送到某公会的广场（调用指令warp）                           |
| t                         | 传送到自己的公会广场                                         |
| s                         | 查看公会状态                                                 |
| name/chairman/cash/points | 查看公会具体参数                                             |
| mems/amems                | 查看公会玩家/高级玩家列表                                    |
| offer                     | 为自己的公会捐献资金（检测必须为10000的倍数，对应1公会资金，调用vault模块操作玩家的货币） |
| create                    | 创建公会（需要500000，调用vault模块操作玩家的货币）          |

### 会长

| 操作              | 功能                                                         |
| ----------------- | ------------------------------------------------------------ |
| levelup           | 升级公会（若条件不满足会返回具体条件）                       |
| add/remove        | 增删成员                                                     |
| adda/removea      | 增删高级成员                                                 |
| setname           | 设置公会名（任意字符串，用于展示）                           |
| res create/remove | 公会领地创建/删除（直接命令调用Residence插件，需要提前选好点） |
| setwarp/delwarp   | 创建/删除公会地标（调用指令setwarp/delwarp）                 |
| buytpall          | 购买1份公会[召集令]()（5个）                                 |
| tpall             | 召集全公会玩家（消耗1个公会召集令）                          |



## 道具

### 召集令

![公会召集令](\pics\公会召集令.png)

手持输入/tpall 可以召集公会全体玩家

# 源码

源码介绍

## GuildManager

主类，用于接入Spigot的插件启动，关闭，注册以及插件的具体操作和存放一些需要全局使用的变量

## Guild

#### 成员变量

```java
private final String ID; //标记ID
private String GuildName; //公会名
private String ChairMan; //会长
private int Level; //等级
private int MaxPlayers; //最大成员数
private int AdvancedPlayers; //高级成员数
private int MaxAdvancedPlayers; //最大高级成员数
private int Points; //积分
private int RemoveMemLimitFlag; //成员删除限制flag
private boolean ResidenceFLag; //是否拥有领地
private HashMap<String , Member> Members= new HashMap<>(); //成员
private int Cash; //资金
```

#### 方法

- 成员变量的增删改查
- 实现了序列化
- 调用控制台发送指令赋予玩家领地权限
- 强制使用命令调用领地创建删除操作
- 保存配置文件

## Member

#### 成员变量

```java
private final String ID;     //标记玩家ID
private int contribution;    //贡献度
private boolean isAdvanced;  //是否为高级成员
```

#### 方法

- 成员变量的增删改查
- 实现了序列化

## GuildAdmin

管理员指令处理器

## GuildCommand

一般指令处理器

## GuildItem

相关道具的数据和获取方法

## JoinListener

监听玩家上线并作出相应操作

## GuildPAPI

实现papi变量的接入