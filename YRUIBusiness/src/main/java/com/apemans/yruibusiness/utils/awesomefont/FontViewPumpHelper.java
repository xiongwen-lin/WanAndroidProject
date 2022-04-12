package com.apemans.yruibusiness.utils.awesomefont;


import com.apemans.base.R;

import io.github.inflationx.viewpump.ViewPump;

/***********************************************************
 * @Author : caro
 * @Date   : 2020/11/3
 * @Func:
 *
 *
 * @Description:
 *
 *
 ***********************************************************/
public class FontViewPumpHelper {
    public static void configViewPump(String fontAssetsPath) {
        ViewPump.init(ViewPump.builder().addInterceptor(
                new CalligraphyInterceptor(new CalligraphyConfig.Builder()
                        .setDefaultFontPath(fontAssetsPath)
                        //.setDefaultFontPath("fonts/PingFang_Bold.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .setFontMapper(new FontMapper() {
                            @Override
                            public String map(String font) {
                                return font;
                            }
                        })
                        .addCustomViewWithSetTypeface(CustomViewWithTypefaceSupport.class)
                        .addCustomStyle(TextField.class, R.attr.textFieldStyle)
                        .build()))
                .build());
    }
}
