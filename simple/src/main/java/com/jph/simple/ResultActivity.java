package com.jph.simple;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.jph.takephoto.model.TImage;
import java.io.File;
import java.util.ArrayList;

/**
 * - 支持通过相机拍照获取图片
 * - 支持从相册选择图片
 * - 支持从文件选择图片
 * - 支持多图选择
 * - 支持批量图片裁切
 * - 支持批量图片压缩
 * - 支持对图片进行压缩
 * - 支持对图片进行裁剪
 * - 支持对裁剪及压缩参数自定义
 * - 提供自带裁剪工具(可选)
 * - 支持智能选取及裁剪异常处理
 * - 支持因拍照Activity被回收后的自动恢复
 * Author: crazycodeboy
 * Date: 2016/9/21 0007 20:10
 * Version:4.0.0
 * 技术博文：http://www.cboy.me
 * GitHub:https://github.com/crazycodeboy
 * Eamil:crazycodeboy@gmail.com
 */
public class ResultActivity extends Activity {
    ArrayList<TImage> images;

    int width;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_layout);
        images = (ArrayList<TImage>) getIntent().getSerializableExtra("images");

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        width = (dm.widthPixels - (int) (dm.density * 10 + 0.5f)) / 2;

        showImg();
    }


    private void showImg() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llImages);
        for (int i = 0, j = images.size(); i < j - 1; i += 2) {
            View view = LayoutInflater.from(this).inflate(R.layout.image_show, null);
            SimpleDraweeView imageView1 = (SimpleDraweeView) view.findViewById(R.id.imgShow1);
            SimpleDraweeView imageView2 = (SimpleDraweeView) view.findViewById(R.id.imgShow2);
            TImage image1 = images.get(i);
            TImage image2 = images.get(i + 1);
            String path1 = TextUtils.isEmpty(image1.getCompressPath())
                           ? image1.getOriginalPath()
                           : image1.getCompressPath();
            String path2 = TextUtils.isEmpty(image2.getCompressPath())
                           ? image2.getOriginalPath()
                           : image2.getCompressPath();
            ImageRequest request1 = ImageRequestBuilder
                    .newBuilderWithSource(Uri.fromFile(new File(path1)))
                    .setLocalThumbnailPreviewsEnabled(true)
                    .setResizeOptions(new ResizeOptions(width, width)).build();
            AbstractDraweeController controller1 = Fresco.newDraweeControllerBuilder()
                                                         .setImageRequest(request1)
                                                         .setAutoPlayAnimations(true).build();
            ImageRequest request2 = ImageRequestBuilder
                    .newBuilderWithSource(Uri.fromFile(new File(path2)))
                    .setLocalThumbnailPreviewsEnabled(true)
                    .setResizeOptions(new ResizeOptions(width, width)).build();
            AbstractDraweeController controller2 = Fresco.newDraweeControllerBuilder()
                                                         .setImageRequest(request2)
                                                         .setAutoPlayAnimations(true).build();
            imageView1.setController(controller1);
            imageView2.setController(controller2);
            linearLayout.addView(view);
        }
        if (images.size() % 2 == 1) {
            View view = LayoutInflater.from(this).inflate(R.layout.image_show, null);
            SimpleDraweeView imageView1 = (SimpleDraweeView) view.findViewById(R.id.imgShow1);
            TImage image1 = images.get(images.size() - 1);
            String path1 = TextUtils.isEmpty(image1.getCompressPath())
                           ? image1.getOriginalPath()
                           : image1.getCompressPath();
            ImageRequest request1 = ImageRequestBuilder
                    .newBuilderWithSource(Uri.fromFile(new File(path1)))
                    .setLocalThumbnailPreviewsEnabled(true)
                    .setResizeOptions(new ResizeOptions(width, width)).build();
            AbstractDraweeController controller1 = Fresco.newDraweeControllerBuilder()
                                                         .setImageRequest(request1)
                                                         .setAutoPlayAnimations(true).build();
            imageView1.setController(controller1);
            linearLayout.addView(view);
        }
    }
}
