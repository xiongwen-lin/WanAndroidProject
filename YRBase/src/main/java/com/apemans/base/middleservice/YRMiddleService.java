package com.apemans.base.middleservice;

import static com.apemans.base.middleservice.YRMiddleConst.MIDDLE_NOT_FOUND_FUNCTION_ERROR;
import static com.apemans.base.middleservice.YRMiddleConst.MIDDLE_PARSE_PARAMETERS_ERROR;
import static com.apemans.base.middleservice.YRMiddleConst.MIDDLE_PARSE_URL_ERROR;
import static com.apemans.base.middleservice.YRMiddleConst.MIDDLE_SUCCESS;

import android.app.Application;

import java.util.Map;

public abstract class YRMiddleService {

    // 协议名称
    public static final String YRMiddleServiceProtocolName = "YRMiddleServiceProtocolName";

    //  模块名称
    public static final String YRMiddleServiceModuleName = "YRMiddleServiceModuleName";

    // 方法名称
    public static final String YRMiddleServiceFunctionName = "YRMiddleServiceFunctionName";

    // 子方法名称
    public static final String YRMiddleServiceSubFunctionName = "YRMiddleServiceSubFunctionName";

    // 完整URL
    public static final String YRMiddleServiceProtocolURLName = "YRMiddleServiceProtocolURLName";

    /**
     * Service 被初始化后的一些配置，自动调用
     */
    protected void configurationInitialization() {
    }

    /**
     * url异常的Response
     *
     * @param errorMsg
     * @return
     */
    protected YRMiddleServiceResponse errorURLResponse(String errorMsg) {
        return new YRMiddleServiceResponse(MIDDLE_PARSE_URL_ERROR, "URL解析异常❌❌❌", null);
    }

    /**
     * Parameters异常的Response
     *
     * @param errorMsg
     * @return
     */
    protected YRMiddleServiceResponse errorParametersResponse(String errorMsg) {
        return new YRMiddleServiceResponse(MIDDLE_PARSE_PARAMETERS_ERROR, "Parameters解析异常❌❌❌", null);
    }

    /**
     * 方法不存在的异常Response
     *
     * @return
     */
    protected YRMiddleServiceResponse errorNoFunctionResponse() {
        return new YRMiddleServiceResponse(MIDDLE_NOT_FOUND_FUNCTION_ERROR, "方法不存在，请查看README文档❌❌❌", null);
    }

    /**
     * 成功的Response
     *
     * @param data
     * @param <T>
     * @return
     */
    protected <T> YRMiddleServiceResponse okResponse(T data) {
        return new YRMiddleServiceResponse(MIDDLE_SUCCESS, "成功✅✅✅", data);
    }

    /**
     * 注册自己到YRMiddleServiceManager
     */
    protected abstract void registerSelf(Application application);

    /**
     * 发起异步请求，确保listener在main thread里！！！！
     *
     * @param protocol
     * @param parameters
     * @param listener
     */
    public abstract void requestAsync(Map<String, String> protocol, Map<String, Object> parameters, YRMiddleServiceListener listener);

    /**
     * 发起同步请求
     *
     * @param protocol
     * @param parameters
     * @return
     */
    public abstract YRMiddleServiceResponse request(Map<String, String> protocol, Map<String, Object> parameters);

    /**
     * 监听service内某些属性变化
     *
     * @param protocol
     * @param lifeCycle
     * @param parameters
     * @param listener
     */
    public abstract void listening(Map<String, String> protocol, Object lifeCycle, Map<String, Object> parameters, YRMiddleServiceListener listener);
}
