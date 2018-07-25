package linkersoft.blackpanther.wobblemeshimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;


import linkersoft.blackpanther.wobble.WobbleMeshImageView;

public class wobble extends AppCompatActivity {

    WobbleMeshImageView woah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wobble);
//        setContentView(new wobble.SampleView(this));
        woah=(WobbleMeshImageView)findViewById(R.id.wobbler);
        woah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                woah.setWobbleMesh(woah.getWobbleWidth(),woah.getWobbleHeight(),R.drawable.wobble_mask1,null);
//                woah.setWobble("[r|c]4,5#(-25,0)");
            }
        });

    }




}



