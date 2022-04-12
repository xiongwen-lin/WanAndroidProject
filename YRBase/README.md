#### Osaio 项目模块命名规则

## 一、包名命名规则  
1、APP包名：com.afar.osaio  

2、模块包名：com.apemans.xxx 模块命名统一为com.apemans作为顶级作用域，xxx为自己定义的包名作用域。如YRLogger模块，包名com.apemans.logger

## 二、模块命名规则  

1、业务组件模块（涉及UI）命名格式为YRX+自定义名字，如YRXRouter  
2、Lib库模块(一般不设计UI的模块组件)命名格式为YR+自定义名字，如YRLogger  
3、中间件服务类命名，命名格式为“对应模块名”，且必须继承YRMiddleService，如YRBase    
4、普通类命名，命名格式为YRXXX  

## 三、中间件协议规则
1、中间件协议统一用小写，如yrcx://yripccomponentdevice/startp2pconnection  

## 四、模块使用独立工程规则  
1、调试工程名称和模块名称一致
2、调试工程包名格式com.apemans.xxx.exmaple，且不能与模块包名相同
3、如有中间件service，则应该放在模块工程根目录且名称和模块名一致
4、模块工程目录gradle不添加无关依赖、资源目录不放无用资源依赖和（如app图标ic_launcher,app名称等）
5、所有模块必须配置可上传maven

## 更新日志
* 2022-2-22 更新模块名规则
* 2022-3-5 模块使用独立工程规则