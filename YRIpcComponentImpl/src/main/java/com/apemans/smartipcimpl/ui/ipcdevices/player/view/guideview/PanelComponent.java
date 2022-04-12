package com.apemans.smartipcimpl.ui.ipcdevices.player.view.guideview;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apemans.smartipcimpl.R;
import com.apemans.smartipcimpl.ui.ipcdevices.player.view.guideview.Component;


/**
 * Created by binIoter on 16/6/17.
 */
public class PanelComponent implements Component {

    private int mWidth;
    private PanelComponentListener mListener;

    public PanelComponent(int width, PanelComponentListener listener) {
        mWidth = width;
        mListener = listener;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        RelativeLayout container = (RelativeLayout) inflater.inflate(R.layout.layout_panel_component, null);
        TextView btnNext = (TextView)container.findViewById(R.id.btnNext);
        View btnNextContainer = container.findViewById(R.id.btnNextContainer);
        if (btnNextContainer != null) {
            btnNextContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onNextClick();
                    }
                }
            });
        }
        return container;
    }

    @Override
    public int getAnchor() {
        return Component.ANCHOR_BOTTOM;
    }    // LS：表示图层从 View的底部开始显示；

    @Override
    public int getFitPosition() {
        return Component.FIT_START; //LS：表示要显示的图层以View的终点对其；
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return 0;
    }

    public interface PanelComponentListener {
        void onNextClick();
    }
}
