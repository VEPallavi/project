package com.utalli.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
//import com.amindset.ve.beyond.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utalli.R;
import de.hdodenhof.circleimageview.CircleImageView;

import java.io.ByteArrayOutputStream;

public class ImageUtils {

    public static final int PICK_IMAGE_REQUEST = 201;
    public interface Storage{
        String DIR_MSG="messagePics";
        String DIR_GROUP="groupPicture";
        String DIR_USER="usersProfilePics";
    }

    public static void displayImageFromUrl(Context context, CircleImageView imageView, String url){
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_user_placeholdr)
                .error(R.drawable.ic_user_placeholdr);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    public static void displayImageFromUrl(Context context, ImageView imageView, String url){

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);

    }

    public static void chooseImage(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
