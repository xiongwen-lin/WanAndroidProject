package com.apemans.base.middleservice;

public class YRMiddleConst {

    /**
     * 成功
     */
    public static final int MIDDLE_SUCCESS = 1000;

    /**
     * 解析URL异常
     */
    public static final int MIDDLE_PARSE_URL_ERROR = -1000;

    /**
     * 解析参数异常
     */
    public static final int MIDDLE_PARSE_PARAMETERS_ERROR = -1001;

    /**
     * 子类未重写该方法
     */
    public static final int MIDDLE_NOT_OVERRIDE_ERROR = -1002;

    /**
     * 没有找到模块
     */
    public static final int MIDDLE_NO_FOUND_MODULE_ERROR = -2000;

    /**
     * 没有找到方法
     */
    public static final int MIDDLE_NOT_FOUND_FUNCTION_ERROR = -3000;

}
