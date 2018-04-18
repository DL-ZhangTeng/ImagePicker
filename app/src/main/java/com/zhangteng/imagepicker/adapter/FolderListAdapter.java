package com.zhangteng.imagepicker.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhangteng.imagepicker.R;
import com.zhangteng.imagepicker.bean.FolderInfo;
import com.zhangteng.imagepicker.imageloader.GlideImageLoader;
import com.zhangteng.imagepicker.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/4/17.
 */
public class FolderListAdapter extends RecyclerView.Adapter<FolderListAdapter.FolderViewHolder> {
    private Context mContext;
    private List<FolderInfo> folderInfos;
    private int clicked = 0;

    public FolderListAdapter(Context context, ArrayList<FolderInfo> folderInfos) {
        this.mContext = context;
        this.folderInfos = folderInfos;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FolderViewHolder folderViewHolder = new FolderViewHolder(LayoutInflater.from(mContext).inflate(R.layout.image_picker_item_folder, parent, false));
        return folderViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, final int position) {
        if (position == 0) {
            new GlideImageLoader().loadImage(mContext, holder.ivImagePickerFolderImage, folderInfos.get(0).getImageInfo().getPath());
            holder.tvImagePickerFolderName.setText(mContext.getString(R.string.image_picker_all_folder));
            holder.tvImagePickerPhotoNum.setText(mContext.getString(R.string.image_picker_photo_num, getTotalImageSize()));
        } else {
            FolderInfo folderInfo = folderInfos.get(position - 1);
            new GlideImageLoader().loadImage(mContext, holder.ivImagePickerFolderImage, folderInfo.getImageInfo().getPath());
            holder.tvImagePickerFolderName.setText(folderInfo.getName());
            holder.tvImagePickerPhotoNum.setText(mContext.getString(R.string.image_picker_photo_num, folderInfo.getImageInfoList().size()));
        }
        if (clicked == position) {
            holder.ivImagePickerIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.ivImagePickerIndicator.setVisibility(View.INVISIBLE);
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null)
                    onItemClickListener.onClick(view, position);
                clicked = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return folderInfos.isEmpty() ? 0 : folderInfos.size() + 1;
    }

    /**
     * @return 所有图片数量
     */
    private int getTotalImageSize() {
        int result = 0;
        if (this.folderInfos != null && this.folderInfos.size() > 0) {
            for (FolderInfo folderInfo : this.folderInfos) {
                result += folderInfo.getImageInfoList().size();
            }
        }
        return result;
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }

    public static class FolderViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImagePickerFolderImage;
        private TextView tvImagePickerFolderName;
        private TextView tvImagePickerPhotoNum;
        private ImageView ivImagePickerIndicator;
        private RelativeLayout relativeLayout;

        public FolderViewHolder(View itemView) {
            super(itemView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.image_picker_rl_folder);
            ivImagePickerFolderImage = (ImageView) itemView.findViewById(R.id.image_picker_iv_folder_image);
            tvImagePickerFolderName = (TextView) itemView.findViewById(R.id.image_picker_tv_folder_name);
            tvImagePickerPhotoNum = (TextView) itemView.findViewById(R.id.image_picker_tv_file_num);
            ivImagePickerIndicator = (ImageView) itemView.findViewById(R.id.image_picker_iv_indicator);
        }
    }
}
