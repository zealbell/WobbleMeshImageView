package linkersoft.blackpanther.wobblemeshimageview;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import linkersoft.blackpanther.wobble.WobbleMeshImageView;

public class wobble extends AppCompatActivity {

    WobbleMeshImageView woah;//Yes I thought about Noah too

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wobble);

        final Context context=this;
        woah=(WobbleMeshImageView)findViewById(R.id.wobbler);
        woah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                woah.setWobbleMesh(woah.getWobbleWidth(),woah.getWobbleHeight(),R.drawable.wobble_mask1,null);
//                woah.setWobble("[r|c]4,5#(-25,0)");

          Bitmap Mask = woah.getWobbleMask(new int[]{ 0xFFA8A8A8,0xFFBDCBB7,0xFFA3BDBF,
                                                      0xFFF3F2E6, 0xFFBECEC2,0xFFF4E8F7,
                                                      0xFFC3D9D9, 0xFF4E4F4F,0xFFFAC8CA });
          verifyStoragePermission((Activity)context);
          saveMask(Mask,"masquerade.png");

            }
        });
        

    }



    private boolean verifyStoragePermission(Activity activity) {

        int GRANTED = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean GRNTD = (GRANTED == PackageManager.PERMISSION_GRANTED);
        if (!GRNTD)
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        return GRNTD;
    }
    private void saveMask(Bitmap Mask,String name){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Mask.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byt = baos.toByteArray();
        File f = new File( Environment.getExternalStorageDirectory().getPath()+"/"+name);
        try {
            f.createNewFile();
            FileOutputStream tasha = new FileOutputStream(f);
            tasha.write(byt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}



