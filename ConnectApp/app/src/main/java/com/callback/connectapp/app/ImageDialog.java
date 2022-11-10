package com.callback.connectapp.app;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ImageView;

import com.callback.connectapp.R;
import com.squareup.picasso.Picasso;

public class ImageDialog {

    private final Context context;
    private final String imageUrl;
    public ImageDialog(Context context, String imageUrl) {
        this.context = context;
        this.imageUrl = imageUrl;
    }

    public void createDialog(){
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.image_dialog);
        ImageView imageView = dialog.findViewById(R.id.image_dialog_image_view);
        if(imageUrl != null && !imageUrl.isEmpty())
            Picasso.get().load(imageUrl).placeholder(R.drawable.avatar1).into(imageView);
        dialog.show();
    }

}
