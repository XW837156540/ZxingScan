package com.xd.zxingscan;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.client.android.BaseCaptureActivity;
import com.google.zxing.client.android.result.ResultHandler;

public class MainActivity extends BaseCaptureActivity {

    @Override
    public void handleDecodeInternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode) {

        Toast.makeText(this, rawResult.getText(), Toast.LENGTH_SHORT).show();
        continuePreview();
    }
}
