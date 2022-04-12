# YRUIBusiness 猿人创新视图跳转

**目录**<br />
[1. 模块](#1)<br />
[2. 方法](#3)<br />
&emsp;[2.1 push](#2.1)<br />
&emsp;[2.2 pop](#2.2)<br />
&emsp;[2.3 rootview](#2.3)<br />
&emsp;[2.4 poptoview](#2.4)<br />

## <a name="1"> 1. 模块 </a>
协议地址：**yrcx://yruibusiness**

## <a name="2"> 2. 方法 </a>
### <a name="2.1"> 2.1 push </a>

**使用:** 异步调用

**功能:** 跳转下一个视图

**参数:** 
```
{"vc":xxxxx, "parames":xxxxx}
//vc：视图名称(字符串)
//parames: 传递的数据
```
**返回值:** 
```
{code:1000, msg:"", data:{"result":ture/false}}
```

### <a name="2.2"> 2.2 pop </a>
**使用:** 异步调用

**功能:** 返回上一个视图

**参数:** 
```
无
```
**返回值:** 
```
{code:1000, msg:"", data:{"result":ture/false}}
```

### <a name="2.3"> 2.3 rootview </a>
**使用:** 异步调用

**功能:** 返回到根视图

**参数:** 
```
无
```
**返回值:** 
```
{code:1000, msg:"", data:{"result":ture/false}}
```

### <a name="2.4"> 2.4 poptoview </a>
**使用:** 异步调用

**功能:** 返回到指定视图

**参数:** 
```
{"vc":xxxxx, "parames":xxxxx}
//vc：视图名称(字符串)
//parames: 传递的数据
```
**返回值:** 
```
{code:1000, msg:"", data:{"result":ture/false}}
```

