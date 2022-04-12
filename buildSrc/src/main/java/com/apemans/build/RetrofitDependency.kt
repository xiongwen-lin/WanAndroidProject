package com.apemans.build

/***********************************************************
 * @Author : caro
 * @Date   : 2021/8/7
 * @Func:
 *
 *
 * @Description:
 *
 *
 ***********************************************************/
object RetrofitDependency {
    private const val version = "2.9.0"
    const val retrofit = "com.squareup.retrofit2:retrofit:$version"
    const val retrofit_converter_gson = "com.squareup.retrofit2:converter-gson:$version"
    const val retrofit_converter_scalars = "com.squareup.retrofit2:converter-scalars:$version"
    const val retrofit_adapter_flow = "com.github.chenxyu:retrofit-adapter-flow:1.1.4"
    const val retrofit_adapter_rxjava = "com.squareup.retrofit2:adapter-rxjava:$version"
}