package com.apemans.yruibusiness.utils.awesomefont;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.apemans.base.R;

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
@SuppressLint("AppCompatCustomView")
public class TextField extends TextView {

    public TextField(final Context context, final AttributeSet attrs) {
        super(context, attrs, R.attr.textFieldStyle);
    }

}
