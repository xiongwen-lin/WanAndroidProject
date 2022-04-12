package com.apemans.base.middleservice;

import android.app.Application;
import android.util.Log;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dalvik.system.DexFile;

public class YRMiddleServiceManager {
    private static final String TAG = YRMiddleServiceManager.class.getSimpleName();

    private YRMiddleServiceTool serviceTool;
    private YRNoFoundModuleService noFoundModuleService;

    private Map<String, YRMiddleService> middleServiceMap;

    private Object middleServiceLock;
    private Object configurationLock;
    private Object parsingLock;

    private YRMiddleServiceManager() {
        serviceTool = new YRMiddleServiceTool();
        noFoundModuleService = new YRNoFoundModuleService();

        middleServiceMap = new HashMap<>();

        middleServiceLock = new Object();
        configurationLock = new Object();
        parsingLock = new Object();

        configurationInitialization();
    }

    private static class YRMiddleServiceManagerInstance {
        private static final YRMiddleServiceManager INSTANCE = new YRMiddleServiceManager();
    }

    private void configurationInitialization() {
        synchronized (configurationLock) {
            for (Map<String, String> map : serviceTool.serviceInitModules()) {

                if (map.keySet().size() == 0 || map.values().size() == 0) continue;

                String clazzName = map.keySet().iterator().next();
                String moduleName = map.values().iterator().next();
                try {
                    Class clazz = Class.forName(clazzName);
                    if (YRMiddleService.class.isAssignableFrom(clazz)) {
                        YRMiddleService service = (YRMiddleService) clazz.newInstance();
                        service.configurationInitialization();
                        middleServiceMap.put(moduleName, service);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InstantiationException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static YRMiddleServiceManager getInstance() {
        return YRMiddleServiceManagerInstance.INSTANCE;
    }

    // 私有类实例方法
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void _registerAllService(Application application, List<String> excludeRegisterPackageList) {
        synchronized (middleServiceLock) {
            try {
                DexFile df = new DexFile(application.getPackageCodePath());//通过DexFile查找当前的APK中可执行文件
                Enumeration<String> enumeration = df.entries();//获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
                while (enumeration.hasMoreElements()) {//遍历
                    String className = (String) enumeration.nextElement();

                    // 跳过系统Class
                    if (className.contains("com.google") ||
                            className.contains("android.support") ||
                            className.contains("javassist.bytecode") ||
                            className.contains("android") ||
                            className.contains("androidx") ||
                            className.contains("com.blankj") ||
                            className.contains("javassist") ||
                            className.contains("kotlin") ||
                            className.contains("$") ||
                            className.contains("org.reflections") ||
                            className.contains("org.intellij") ||
                            className.contains("org.jetbrains") ||
                            className.contains("java") ||
                            className.contains("dalvik")) {
                        continue;
                    }

                    if (excludeRegisterPackageList != null && !excludeRegisterPackageList.isEmpty()) {
                        boolean exclude = false;
                        for (String pkg : excludeRegisterPackageList) {
                            if (className.startsWith(pkg)) {
                                exclude = true;
                                break;
                            }
                        }
                        if (exclude) continue;
                    }

                    // YRMiddleService 不再去注册
                    if (className.equals(YRMiddleService.class.getName())) continue;

                    // YRNoFoundModuleService 不再去注册
                    if (className.equals(YRNoFoundModuleService.class.getName())) continue;

                    try {
                        /*
                        if (!YRMiddleServiceUtil.INSTANCE.checkIsAssignableMiddleService(className, "yrcx")) {
                            continue;
                        }

                         */
                        Log.d("debug", "-->> debug YRMiddleServiceManager " + className);
                        Class clazz = Class.forName(className);
                        if (YRMiddleService.class.isAssignableFrom(clazz)) {
                            YRMiddleService service = (YRMiddleService) clazz.newInstance();
                            service.registerSelf(application);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            catch (IOException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            }
        }
    }

    private boolean _addURLProtocolHeader(List<String> headers) {
        synchronized (middleServiceLock) {
            return serviceTool.addURLProtocolHeader(headers);
        }
    }

    private boolean _registerServiceClass(String moduleName, String className, String describe) {
        boolean result = false;
        synchronized (middleServiceLock) {
            moduleName = moduleName.toLowerCase(Locale.ROOT);
            if (!middleServiceMap.containsKey(moduleName)) {
                serviceTool.registerServiceClass(moduleName, className, describe);

                try {
                    Class clazz = Class.forName(className);
                    if (YRMiddleService.class.isAssignableFrom(clazz)) {
                        YRMiddleService service = (YRMiddleService) clazz.newInstance();
                        service.configurationInitialization();
                        middleServiceMap.put(moduleName, service);
                        result = true;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InstantiationException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return result;
    }

    private YRMiddleService middleServiceWithURL(String url) {
        synchronized (middleServiceLock) {
            String moduleName = serviceTool.moduleNameWithURL(url);
            if (moduleName != null) {
                moduleName = moduleName.toLowerCase(Locale.ROOT);
            }

            YRMiddleService service = middleServiceMap.get(moduleName);
            if (moduleName != null && moduleName.length() > 0 && service == null) {
                Class clazz = serviceTool.middleServiceWithModuleName(moduleName);

                try {
                    service = (YRMiddleService) clazz.newInstance();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InstantiationException e2) {
                    e2.printStackTrace();
                }
                if (service != null) service.configurationInitialization();
                middleServiceMap.put(moduleName, service);
            }

            if (service == null) service = middleServiceMap.get(moduleName);
            if (service == null) service = noFoundModuleService;

            return service;
        }
    }

    private Map<String, String> parseMiddleServiceURL(String url) {
        synchronized (parsingLock) {
            return serviceTool.parsingURL(url);
        }
    }

    private void _requestAsync(String url, Map<String, Object> parameters, YRMiddleServiceListener listener) {
        YRMiddleService service = noFoundModuleService;
        if (url != null) service = middleServiceWithURL(url);

        service.requestAsync(url == null ? null : parseMiddleServiceURL(url), parameters, listener);
    }

    private void _listening(String url, Object lifeCycle, Map<String, Object> parameters, YRMiddleServiceListener listener) {
        YRMiddleService service = noFoundModuleService;
        if (url != null) service = middleServiceWithURL(url);

        service.listening(url == null ? null : parseMiddleServiceURL(url), lifeCycle, parameters, listener);
    }

    private YRMiddleServiceResponse _request(String url, Map<String, Object> parameters) {
        YRMiddleService service = noFoundModuleService;
        if (url != null) service = middleServiceWithURL(url);

        return service.request(url == null ? null : parseMiddleServiceURL(url), parameters);
    }

    // 公共静态方法
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 注册保利的服务
     */
    public static void registerAllService(Application application, List<String> excludeRegisterPackageList) {
        YRMiddleServiceManager.getInstance()._registerAllService(application, excludeRegisterPackageList);
    }

    /**
     * 添加可被解析的协议头，默认支持yrcx://
     *
     * @param headers
     * @return
     */
    public static boolean addURLProtocolHeader(List<String> headers) {
        return YRMiddleServiceManager.getInstance()._addURLProtocolHeader(headers);
    }

    /**
     * 注册服务
     *
     * @param moduleName 模块名称
     * @param className  类名，包含包名
     * @param describe   模块的描述信息
     * @return
     */
    public static boolean registerServiceClass(String moduleName, String className, String describe) {
        return YRMiddleServiceManager.getInstance()._registerServiceClass(moduleName, className, describe);
    }

    /**
     * 发起异步请求，@{listener}一定在main thread里
     *
     * @param url
     * @param parameters
     * @param listener
     */
    public static void requestAsync(String url, Map<String, Object> parameters, YRMiddleServiceListener listener) {
        YRMiddleServiceManager.getInstance()._requestAsync(url, parameters, listener);
    }

    /**
     * 监听service内某些属性变化，@{listener}一定在main thread里
     *
     * @param url
     * @param lifeCycle
     * @param parameters
     * @param listener
     */
    public static void listening(String url, Object lifeCycle, Map<String, Object> parameters, YRMiddleServiceListener listener) {
        YRMiddleServiceManager.getInstance()._listening(url, lifeCycle, parameters, listener);
    }

    /**
     * 发起同步请求
     *
     * @param url
     * @param parameters
     * @return
     */
    public static YRMiddleServiceResponse request(String url, Map<String, Object> parameters) {
        return YRMiddleServiceManager.getInstance()._request(url, parameters);
    }
}
