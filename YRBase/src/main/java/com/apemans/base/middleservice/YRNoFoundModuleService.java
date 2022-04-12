package com.apemans.base.middleservice;

import static com.apemans.base.middleservice.YRMiddleConst.MIDDLE_NO_FOUND_MODULE_ERROR;

import android.app.Application;

import java.util.Map;

public class YRNoFoundModuleService extends YRMiddleService {

    @Override
    protected void registerSelf(Application application) {}

    @Override
    public void requestAsync(Map<String, String> protocol, Map<String, Object> parameters, YRMiddleServiceListener listener) {
        if (listener != null) {
            String errorMsg = String.format("url = %s \n协议对应的模块未找到，请检查一、协议是否加入的白名单中，具体参考YRMiddleServiceManager addURLProtocolHeader 方法\n二、协议名称是否设置正确，请在模块README.md文件中查看配置❌❌❌", protocol.get(YRMiddleServiceProtocolURLName));
            YRMiddleServiceResponse rsp = new YRMiddleServiceResponse(MIDDLE_NO_FOUND_MODULE_ERROR, errorMsg, null);
            listener.onCall(rsp);
        }
    }

    @Override
    public void listening(Map<String, String> protocol, Object lifeCycle, Map<String, Object> parameters, YRMiddleServiceListener listener) {
        if (listener != null) {
            String errorMsg = String.format("url = %s \n协议对应的模块未找到，请检查一、协议是否加入的白名单中，具体参考YRMiddleServiceManager addURLProtocolHeader 方法\n二、协议名称是否设置正确，请在模块README.md文件中查看配置❌❌❌", protocol.get(YRMiddleServiceProtocolURLName));
            YRMiddleServiceResponse rsp = new YRMiddleServiceResponse(MIDDLE_NO_FOUND_MODULE_ERROR, errorMsg, null);
            listener.onCall(rsp);
        }
    }

    @Override
    public YRMiddleServiceResponse request(Map<String, String> protocol, Map<String, Object> parameters) {
        String errorMsg = String.format("url = %s \n协议对应的模块未找到，请检查一、协议是否加入的白名单中，具体参考YRMiddleServiceManager addURLProtocolHeader 方法\n二、协议名称是否设置正确，请在模块README.md文件中查看配置❌❌❌", protocol.get(YRMiddleServiceProtocolURLName));
        YRMiddleServiceResponse rsp = new YRMiddleServiceResponse(MIDDLE_NO_FOUND_MODULE_ERROR, errorMsg, null);
        return rsp;
    }
}
