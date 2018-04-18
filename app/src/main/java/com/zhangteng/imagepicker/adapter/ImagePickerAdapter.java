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

import com.zhangteng.imagepicker.R;
import com.zhangteng.imagepicker.bean.ImageInfo;
import com.zhangteng.imagepicker.config.ImagePickerConfig;
import com.zhangteng.imagepicker.config.ImagePickerOpen;
import com.zhangteng.imagepicker.imageloader.GlideImageLoader;
import com.zhangteng.imagepicker.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/4/17.
 */
public class ImagePickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEAD = 0;
    private static final int PHOTO = 1;
    private Context mContext;
    private List<ImageInfo> imageInfoList;
    private ImagePickerConfig imagePickerConfig = ImagePickerOpen.getInstance().getImagePickerConfig();
    private List<String> selectImage = new ArrayList<>();

    public ImagePickerAdapter(Context context, ArrayList<ImageInfo> imageInfoList) {
        this.mContext = context;
        this.imageInfoList = imageInfoList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEAD) {
            CameraViewHolder cameraViewHolder = new CameraViewHolder(LayoutInflater.from(mContext).inflate(R.layout.image_picker_item_camera, parent, false));
            return cameraViewHolder;
        } else {
            ImageViewHolder imageViewHolder = new ImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.image_picker_item_photo, parent, false));
            return imageViewHolder;
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
                ((CameraViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onItemClickListener != null) {
                            if (imagePickerConfig.isMultiSelect() && selectImage.size() < imagePickerConfig.getMaxSize()) {
                                onItemClickListener.onCameraClick(selectImage);
                            } else {
                                onItemClickListener.onCameraClick(selectImage);
                            }
                        }
                        notifyDataSetChanged();
                    }
                });
            } else {
                imageInfo = imageInfoList.get(position - 1);
                imagePickerConfig.getImageLoader().loadImage(mContext, ((ImageViewHolder) holder).imageView, imageInfo.getPath());
                final ImageInfo finalImageInfo = imageInfo;
                ((ImageViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selectImage.contains(finalImageInfo.getPath())) {
                            selectImage.remove(finalImageInfo.getPath());
                        } else {
                            if (selectImage.size() < imagePickerConfig.getMaxSize()) {
                                selectImage.add(finalImageInfo.getPath());
                            }
                        }
                        if (onItemClickListener != null)
                            onItemClickListener.onImageClick(selectImage);
                        notifyDataSetChanged();
                    }
                });
                initView(holder, imageInfo);
            }
        } else {
            imageInfo = imageInfoList.get(position);
            imagePickerConfig.getImageLoader().loadImage(mContext, ((ImageViewHolder) holder).imageView, imageInfo.getPath());
            final ImageInfo finalImageInfo1 = imageInfo;
            ((ImageViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectImage.contains(finalImageInfo1.getPath())) {
                        selectImage.remove(finalImageInfo1.getPath());
                    } else {
                        if (selectImage.size() < imagePickerConfig.getMaxSize())
                            selectImage.add(finalImageInfo1.getPath());
                    }
                    if (onItemClickListener != null)
                        onItemClickListener.onImageClick(selectImage);
                    notifyDataSetChanged();
                }
            });
            initView(holder, imageInfo);
        }

    }

    private void initView(RecyclerView.ViewHolder holder, ImageInfo imageInfo) {
        if (imagePickerConfig.isMultiSelect()) {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
        } else {
            ((ImageViewHolder) holder).checkBox.setVisibility(View.GONE);
        }
        if (selectImage.contains(imageInfo.getPath())) {
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
    }

    @Override
    public int getItemCount() {
        return imageInfoList.isEmpty() ? 0 : imagePickerConfig.isShowCamera() ? imageInfoList.size() + 1 : imageInfoList.size();
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
        void onCameraClick(List<String> selectImage);

        void onImageClick(List<String> selectImage);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private View mask;
        private CheckBox checkBox;

        public ImageViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.image_picker_im_photo);
            this.mask = (View) itemView.findViewById(R.id.image_picker_v_photo_mask);
            this.checkBox = (CheckBox) itemView.findViewById(R.id.image_picker_cb_select);
        }
    }

    private static class CameraViewHolder extends RecyclerView.ViewHolder {

        public CameraViewHolder(View itemView) {
            super(itemView);
        }
    }
}
