#  OSiao客服反馈方案设计

## 一、概述
    客服反馈，是客户向产品反馈的关于产品的问题,如提供文字说明、图片等相关资料,不但可以收集客户反馈的信息给有关部门,还能解答用户在使用产品中的疑问,及时对产品进行改进.

## 二、需求描述
1、填写反馈信息
          （1）输入联系方式（邮箱），默认用户邮箱，支持手动输入
         （2）提交图片媒体，支持用户拍照、选择图库添加图片
         （3）文字描述，用于作为反馈问题的详细说明
 2、提交反馈
         调用 feedback/create接口，提交反馈
  3、回复反馈
          后台收到反馈后，保存相关反馈，并支持在后台网站回复反馈 （https://iot.osaio.net/）
   4、收到反馈
         用户收到反馈后，会在APP收到系统通知，得到相关回复信息，用户可以打开浏览具体内容

##三、反馈流程示意图









![Alt text](http://172.16.26.46/yrcx/doc/site/%E9%A1%B9%E7%9B%AE/Osaio/Android/%E6%A8%A1%E5%9D%97/%E5%8F%8D%E9%A6%88%E6%B5%81%E7%A8%8B%E5%9B%BE.png)




___

##4、接入说明
   1、在项目build.gradle文件下，添加Osiao仓库:
 ```
        maven {
            allowInsecureProtocol = true
            url 'http://172.16.26.46:8081/repository/osaio/'
        } ```
    

    添加依赖： api  'com.apemans:YRXCustomer:+' 
   
    
  2、跳转反馈页面
     路由跳转： `startRouterActivity("/feedback/feedback_faq")`
    

    
##五、功能说明

     interface CustomService : IProvider {
       /**
     * 检查反馈消息是否频发
     */
    fun checkFeedback(): Flow<YRApiResponse<Any>>
  
    /**
     * 上传媒体文件
     */
    fun upLoadFileToCloud(url: String, contentType: String, usenameParam: String, uploadFile: RequestBody) : Call<ResponseBody>?
   
    /**
     * 创建反馈
     */
    fun createFeedback(body: CreateFeedbackBody): Flow<YRApiResponse<Any>>
    
      /**
     * 客服回复反馈消息
     */
    fun getMessageList(rows :Int,time :Int,type: Int): Flow<YRApiResponse<List<MessageData>>>
  
}





 
   
    
    

##可能存在风险
####需求变动:
       （1）1.4.0新增草稿箱功能
       （2）未来zendesk SDK 方案有可能加入反馈模块 
                   
   
       