# GhostJ

## 说明
GhostJ实现了实时执行指令、截图、文件操作、鼠标键盘控制、操作监控等功能 

此项目不是被设计为一个类库，不具有被支持调用的类  
此项目只适合你借鉴一些处理方法  
因此，此项目内的一些代码并不是为复用性而设计的，这可能会产生阅读上的困难

## 声明

使用此类代码进行未经允许的远程控制属于违法行为，本项目及作者不向违法行为提供任何支持

## 联系方式

问题发至issue
讨论细节邮件至1010553892@qq.com或QQ联系

## 依赖

- `fastjson` 阿里巴巴的json解析类库
- `RFT` 自己开发的文件上传类库 可在主页找到开源仓库
- `RFTX2` 自己开发的文件双向传输类库 可在主页找到开源仓库

## 分支

- `master` 正在开发的主分支
- `classic` 重构服务端之前的经典服务端代码

## 目录结构

- `src` 源代码
- `libs` 依赖库

## 项目结构

### 主要包

- `com.ghostj.client` 客户端主程序，被控端
- `com.ghostj.master` 主控端程序，javaSwing开发的可视化操作桌面程序
- `com.ghostj.server_old` 服务端程序，处理客户端和主控端的连接、管理文件、转发消息等功能
- `com.ghostj.util` 通用的类和方法

### client

已使用策略模式重构

- `cmd` 命令处理逻辑的model(独立出来的代码见 https://github.com/rockchinq/rcp)
- `conn` 建立并管理客户端到服务端的连接
- `core` 客户端的启动类，包含命令行执行线程类、控制命令解析类
- `func` 所有客户端支持的控制指令，由Processor类解析控制指令并调用
- `util` 客户端实用类

*注意：此处`控制命令`(指客户端收到的由主控端或服务端发出的控制指令)有别于`命令行的命令`(将被cmd.exe执行的指令)  
      事实上，客户端执行命令行指令的逻辑也被归类为一个控制指令

#### 执行逻辑
被启动后，启动HandleConn线程，尝试建立与服务端的连接，堵塞接收指令解析并执行。  
当检测到连接断开后，重新尝试连接

### master

有待重构，目前可读性很低

- `conn` 管理与服务端的连接
- `core` 批处理类和模拟控制台的缓冲区
- `gui` 管理所有界面
- `util` 实用类

![master界面](./readmeRes/3master.jpg)

### server

服务端代码有待重构，目前未使用设计模式来构建服务端代码。可读性很低
![server启动](./readmeRes/1start.jpg)
![server命令](./readmeRes/2help_list.jpg)

## 开发环境
jdk11  
idea

## .old
Master包是控制端，此处代码是基于桌面java环境的
Android上的master客户端代码可以找到在https://gitee.com/soulter/GhostJ_for_Android
由Soulter编码
在github上面也有（版本较旧）：https://github.com/RockChinQ