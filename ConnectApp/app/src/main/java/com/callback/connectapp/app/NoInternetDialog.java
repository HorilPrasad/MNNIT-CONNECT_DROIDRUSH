package com.callback.connectapp.app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Window;
import android.widget.Button;

import com.callback.connectapp.R;

public class NoInternetDialog {
    Dialog dialog;
    Context context;
    public NoInternetDialog(Context context) {
       this.context = context;
    }

    public void create() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.no_internet);
        dialog.show();
        Button button = dialog.findViewById(R.id.retry_button);
        button.setOnClickListener(v -> {
            context.startActivity(new Intent(context, context.getClass()));
        });
    }
        public void hide(){
            dialog.dismiss();
        }

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
