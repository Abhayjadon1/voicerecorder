package com.ispl.voice.recorder;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.core.content.ContextCompat;

import com.ispl.voice.recorder.R;


public class CDialog {
    public static Dialog m_loadingDialog;

    public static void hideLoading() {
        try {
            if (m_loadingDialog != null && m_loadingDialog.isShowing()) {
                m_loadingDialog.dismiss();
            }
        } catch (Exception unused) {
        } catch (Throwable th) {
            m_loadingDialog = null;
            throw th;
        }
        m_loadingDialog = null;
    }

    public static void showLoading(Context context) {
        if (m_loadingDialog == null) {
            m_loadingDialog = new Dialog(context, R.style.TransDialog);
            ProgressBar progressBar = new ProgressBar(context);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -2);
            progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
            m_loadingDialog.addContentView(progressBar, layoutParams);
            m_loadingDialog.setCancelable(false);
        }
        m_loadingDialog.show();
    }
}
