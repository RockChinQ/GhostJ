# GhostJ

## 说明
此项目想法来源于我自己开发的更前一代远程执行命令的程序  
那个项目并没有开源，而且不能实时执行命令  
所以开发了此项目，现在GhostJ实现了实时执行指令、截图、文件操作等功能  
已被我部署到了学校一些电脑上面

此项目不是被设计为一个类库，不具有被支持调用的类  
此项目只适合你借鉴一些处理方法  
因此，此项目内的一些代码并不是为复用性而设计的，这可能会产生阅读上的障碍

## 目录结构
- `src` 源代码
- `libs` 依赖库

## 项目结构

### 包

- `com.ghostj.client` 客户端主程序，被控端
- `com.ghostj.master` 主控端程序，javaSwing开发的可视化操作桌面程序
- `com.ghostj.server` 服务端程序，处理客户端和主控端的连接、管理文件、转发消息等功能

#### client

已使用策略模式重构

- `cmd` 命令处理逻辑的model(独立出来的代码见 https://github.com/rockchinq/rcp)
- `conn` 建立并管理客户端到服务端的连接
- `core` 客户端的启动类，包含命令行执行线程类、控制命令解析类

*注意：此处`控制命令`(指客户端收到的由主控端或服务端发出的控制指令)有别于`命令行的命令`(将被cmd.exe执行的指令)  
      事实上，客户端执行命令行指令的逻辑也被归类为一个控制指令

## 开发环境
jdk11  
idea

Master包是控制端，此处代码是基于桌面java环境的
Android上的master客户端代码可以找到在https://gitee.com/soulter/GhostJ_for_Android
由Soulter编码
在github上面也有（版本较旧）：https://github.com/RockChinQ