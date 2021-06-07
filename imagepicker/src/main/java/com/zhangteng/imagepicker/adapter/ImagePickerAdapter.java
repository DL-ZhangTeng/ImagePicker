package com.zhangteng.imagepicker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhangteng.imagepicker.R;
import com.zhangteng.imagepicker.bean.ImageInfo;
import com.zhangteng.imagepicker.config.ImagePickerConfig;
import com.zhangteng.imagepicker.config.ImagePickerOpen;
import com.zhangteng.imagepicker.utils.NullUtill;
import com.zhangteng.imagepicker.utils.ScreenUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swing on 2018/4/17.
 */
public class ImagePickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEAD = 0;
    private static final int PHOTO = 1;
    private final Context mContext;
    private List<ImageInfo> imageInfoList;
    private final ImagePickerConfig imagePickerConfig = ImagePickerOpen.getInstance().getImagePickerConfig();
    private final List<ImageInfo> selectImageInfo = new ArrayList<>();
    private int selectable = imagePickerConfig.isVideoPicker() && imagePickerConfig.isImagePicker()
            ? imagePickerConfig.getMaxImageSelectable()
            : imagePickerConfig.isVideoPicker()
            ? imagePickerConfig.getMaxVideoSelectable()
            : imagePickerConfig.getMaxImageSelectable();

    public ImagePickerAdapter(Context context, ArrayList<ImageInfo> imageInfoList) {
        this.mContext = context;
        this.imageInfoList = imageInfoList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEAD) {
            return new CameraViewHolder(LayoutInflater.from(mContext).inflate(R.layout.image_picker_item_camera, parent, false));
        } else {
            return new ImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.image_picker_item_photo, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        int heightOrWidth = Math.min(ScreenUtils.getScreenHeight(mContext) / 3, ScreenUtils.getScreenWidth(mContext) / 3);
        layoutParams.height = heightOrWidth;
        layoutParams.width = heightOrWidth;
        holder.itemView.setLayoutParams(layoutParams);
        ImageInfo imageInfo = null;
        if (imagePickerConfig.isShowCamera()) {
            if (position == 0) {
                holder.itemView.setOnClickListener(view -> {
                    if (onItemClickListener != null) {
                        onItemClickListener.onCameraClick(selectImageInfo);
                    }
                    notifyDataSetChanged();
                });
            } else {
                imageInfo = imageInfoList.get(position - 1);
                imagePickerConfig.getImageLoader().loadImage(mContext, ((ImageViewHolder) holder).imageView, imageInfo.getPath());
                final ImageInfo finalImageInfo = imageInfo;
                ((ImageViewHolder) holder).imageView.setOnClickListener(view -> {
                    selectItem(finalImageInfo);
                    if (onItemClickListener != null)
                        onItemClickListener.onImageClick(selectImageInfo, selectable);
                    notifyDataSetChanged();
                });
                initView(holder, imageInfo);
            }
        } else {
            imageInfo = imageInfoList.get(position);
            imagePickerConfig.getImageLoader().loadImage(mContext, ((ImageViewHolder) holder).imageView, imageInfo.getPath());
            final ImageInfo finalImageInfo1 = imageInfo;
            ((ImageViewHolder) holder).imageView.setOnClickListener(view -> {
                selectItem(finalImageInfo1);
                if (onItemClickListener != null)
                    onItemClickListener.onImageClick(selectImageInfo, selectable);
                notifyDataSetChanged();
            });
            initView(holder, imageInfo);
        }
        //设置已选中图片状态（根据imagePickerConfig.getPathList()中的图片判断）
        List<String> selectImage = imagePickerConfig.getPathList();
        if (imageInfo != null && !selectImageInfo.contains(imageInfo)) {
            for (String imagePath : selectImage) {
                if (NullUtill.getNotNull(imageInfo.getPath()).equals(imagePath)) {
                    selectItem(imageInfo);
                    initView(holder, imageInfo);
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void initView(RecyclerView.ViewHolder holder, ImageInfo imageInfo) {
        if (imagePickerConfig.isMultiSelect()) {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
            if (selectImageInfo.contains(imageInfo)) {
                ((ImageViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
                ((ImageViewHolder) holder).mask.setVisibility(View.VISIBLE);
                ((ImageViewHolder) holder).checkBox.setChecked(true);
                ((ImageViewHolder) holder).checkBox.setButtonDrawable(R.mipmap.image_picker_select_checked);
            } else {
                ((ImageViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
                ((ImageViewHolder) holder).mask.setVisibility(View.GONE);
                ((ImageViewHolder) holder).checkBox.setChecked(false);
                ((ImageViewHolder) holder).checkBox.setButtonDrawable(R.mipmap.image_picker_select_unchecked);
            }
        } else {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.GONE);
            ((ImageViewHolder) holder).mask.setVisibility(View.GONE);
        }
        if (NullUtill.getNotNull(imageInfo.getMime()).toLowerCase().contains("video")) {
            ((ImageViewHolder) holder).duration.setVisibility(View.VISIBLE);
            ((ImageViewHolder) holder).duration.setText(new SimpleDateFormat("mm:ss").format(imageInfo.getDuration()));
        } else {
            ((ImageViewHolder) holder).duration.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return imagePickerConfig.isShowCamera() ? imageInfoList.size() + 1 : imageInfoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            if (imagePickerConfig.isShowCamera()) {
                return HEAD;
            }
        }
        return PHOTO;
    }

    public void setImageInfoList(List<ImageInfo> imageInfoList) {
        this.imageInfoList = imageInfoList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onCameraClick(List<ImageInfo> selectImageInfo);

        void onImageClick(List<ImageInfo> selectImageInfo, int selectable);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }

    /**
     * 选择或取消选择文件,
     */
    private void selectItem(ImageInfo imageInfo) {
        if (imagePickerConfig.isVideoPicker() && NullUtill.getNotNull(imageInfo.getMime()).toLowerCase().contains("video")) {
            int selected = 0;
            for (ImageInfo info : selectImageInfo) {
                if (NullUtill.getNotNull(info.getMime()).toLowerCase().contains("video")) {
                    selected++;
                }
            }
            if (selectImageInfo.contains(imageInfo)) {
                selectImageInfo.remove(imageInfo);
                if (imagePickerConfig.isImagePicker())
                    selectable = imagePickerConfig.getMaxImageSelectable();
            } else if (selectImageInfo.size() < imagePickerConfig.getMaxVideoSelectable() - selected) {
                selectImageInfo.add(imageInfo);
                selectable = imagePickerConfig.getMaxVideoSelectable();
            }
        } else {
            if (selectImageInfo.contains(imageInfo)) {
                selectImageInfo.remove(imageInfo);
            } else {
                if (selectImageInfo.size() < selectable) {
                    selectImageInfo.add(imageInfo);
                }
            }
        }
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final View mask;
        private final CheckBox checkBox;
        private final TextView duration;

        public ImageViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image_picker_im_photo);
            this.mask = itemView.findViewById(R.id.image_picker_v_photo_mask);
            this.checkBox = itemView.findViewById(R.id.image_picker_cb_select);
            this.duration = itemView.findViewById(R.id.image_picker_tv_duration);
        }
    }

    private static class CameraViewHolder extends RecyclerView.ViewHolder {

        public CameraViewHolder(View itemView) {
            super(itemView);
        }
    }
}
