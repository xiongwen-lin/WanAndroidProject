package com.apemans.base.middleservice;

import static com.apemans.base.middleservice.YRMiddleService.YRMiddleServiceFunctionName;
import static com.apemans.base.middleservice.YRMiddleService.YRMiddleServiceModuleName;
import static com.apemans.base.middleservice.YRMiddleService.YRMiddleServiceProtocolName;
import static com.apemans.base.middleservice.YRMiddleService.YRMiddleServiceProtocolURLName;
import static com.apemans.base.middleservice.YRMiddleService.YRMiddleServiceSubFunctionName;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class YRMiddleServiceTool {
    private static final String TAG = YRMiddleService.class.getSimpleName();

    private List<String> protocolHeaders;
    private Map<String, String> serviceModuleNameToClazzMap;
    private Map<String, String> serviceClassToModuleNameMap;
    private List<String> serviceInitModuleNameList;

    private Class middleServiceClass;

    public YRMiddleServiceTool() {
        protocolHeaders = new ArrayList<>();
        protocolHeaders.add("yrcx://");

        serviceModuleNameToClazzMap = new HashMap<>();
        serviceClassToModuleNameMap = new HashMap<>();

        serviceInitModuleNameList = new ArrayList<>();

        middleServiceClass = YRMiddleService.class;
    }

    /**
     * 添加可被解析的协议头，默认支持yrcx://
     *
     * @param headers
     * @return
     */
    public boolean addURLProtocolHeader(List<String> headers) {
        protocolHeaders.addAll(headers);
        return true;
    }

    /**
     * 注册服务
     *
     * @param moduleName 模块名称
     * @param className  类名，包含包名
     * @param describe   模块的描述信息
     * @return
     */
    public boolean registerServiceClass(String moduleName, String className, String describe) {
        boolean result = true;
        try {
            Class clazz = Class.forName(className);
            if (middleServiceClass.isAssignableFrom(clazz)) {
                serviceModuleNameToClazzMap.put(moduleName, className);
                serviceClassToModuleNameMap.put(className, moduleName);
            } else {
                Log.e(TAG, String.format("%s不是%s的继承类，请继承该类!!", clazz.getSimpleName(), YRMiddleService.class.getSimpleName()));
                result = false;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    /**
     * 获取模块名称通过URL
     *
     * @param url
     * @return
     */
    public String moduleNameWithURL(String url) {
        for (String header : this.protocolHeaders) {
            if (url.startsWith(header)) {
                String moduleStr = url.substring(header.length());
                String module = subSectionNameString(moduleStr, "/");
                if (module.length() > 0)
                    return module.toLowerCase(Locale.ROOT);
            }
        }

        return null;
    }

    private String subSectionNameString(String string, String charStr) {
        while (string.startsWith(charStr)) {
            string = string.substring(charStr.length());
        }

        if (string.contains(charStr)) {
            return string.substring(0, string.indexOf(charStr));
        }
        return string;
    }

    /**
     * 获取模块类名
     *
     * @param moduleName
     * @return
     */
    public Class middleServiceWithModuleName(String moduleName) {
        if (moduleName == null || moduleName.length() <= 0) {
            return YRNoFoundModuleService.class;
        }

        moduleName = moduleName.toLowerCase(Locale.ROOT);

        String clazzName = serviceModuleNameToClazzMap.get(moduleName);
        if (clazzName == null || clazzName.length() <= 0) {
            return YRNoFoundModuleService.class;
        }

        try {
            return Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return YRNoFoundModuleService.class;
        }
    }

    /**
     * 解析url，提取其中的参数字典的key:查看YRMiddleService 中声明
     * yrcx://yrapisdk/get
     * yrcx://yrapisdk/set/header
     *
     * @param url
     * @return
     */
    public Map<String, String> parsingURL(String url) {
        url = url.toLowerCase(Locale.ROOT);

        Map<String, String> map = new HashMap<>();
        map.put(YRMiddleServiceProtocolURLName, url);
        for (String header : this.protocolHeaders) {
            if (url.startsWith(header)) {
                map.put(YRMiddleServiceProtocolName, header);

                String nameStr = url.substring(header.length());
                String name = subSectionNameString(nameStr, "/");
                map.put(YRMiddleServiceModuleName, name);

                nameStr = nameStr.substring(name.length());
                name = subSectionNameString(nameStr, "/");
                map.put(YRMiddleServiceFunctionName, name);

                nameStr = nameStr.substring(name.length()+1);
                name = subSectionNameString(nameStr, "/");
                map.put(YRMiddleServiceSubFunctionName, name);
            }
        }
        return map;
    }

    /**
     * 返回需要首次加载就需要初始化的模块名称及类名
     *
     * @return
     */
    public List<Map<String, String>> serviceInitModules() {
        List<Map<String, String>> services = new ArrayList<>(serviceInitModuleNameList.size());
        for (String module : serviceInitModuleNameList) {
            String clazzName = serviceModuleNameToClazzMap.get(module);
            if (clazzName != null || clazzName.length() > 0) {
                Map<String, String> m = new HashMap<>();
                m.put(clazzName, module);
                services.add(m);
            }
        }
        return services;
    }
}
