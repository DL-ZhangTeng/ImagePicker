package com.zhangteng.imagepicker.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.zhangteng.imagepicker.R;
import com.zhangteng.imagepicker.adapter.FolderListAdapter;
import com.zhangteng.imagepicker.utils.ScreenUtils;

/**
 * Created by Swing on 2018/4/17.
 */
public class FolderPopupWindow extends PopupWindow {
    private Context mContext;
    private FolderListAdapter mFolderListAdapter;
    private View popupWindow;
    private RecyclerView recyclerView;

    public FolderPopupWindow(Context context, FolderListAdapter folderListAdapter) {
        super(context);
        this.mContext = context;
        this.mFolderListAdapter = folderListAdapter;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupWindow = inflater.inflate(R.layout.image_picker_popup_folder, null);
        initView();
        init();

    }

    private void initView() {
        recyclerView = (RecyclerView) popupWindow.findViewById(R.id.image_picker_rv_folder_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mFolderListAdapter);
        recyclerView.addItemDecoration(new BottomItemDecoration());
    }

    private void init() {
        //设置PopupWindow的View
        this.setContentView(popupWindow);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(ScreenUtils.getScreenHeight(mContext)*2/3);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(false);
        //设置非PopupWindow区域是否可触摸
        this.setOutsideTouchable(true);
        // 设置是否获取焦点
        this.setFocusable(true);
        //设置PopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(mContext.getResources().getColor(R.color.image_picker_folder_bg));
        //设置PopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

    }

}
