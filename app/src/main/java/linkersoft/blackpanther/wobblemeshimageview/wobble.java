package linkersoft.blackpanther.wobblemeshimageview;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import linkersoft.blackpanther.wobble.WobbleMeshImageView;

public class wobble extends AppCompatActivity {

    WobbleMeshImageView woah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wobble);
        woah=(WobbleMeshImageView)findViewById(R.id.wobbler);
        woah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bitmap WobbleMask=woah.getWobbleMeshBitmapMask();
//                woah.setWobbleMesh(woah.getWobbleWidth(),woah.getWobbleHeight(),WobbleMask,null);
//                woah.disposeWobbleMask();

//                woah.setWobbleMesh(woah.getWobbleWidth(),woah.getWobbleHeight(),woah.getWobbleMesh(),null);
//                woah.disposeWobbleMask();
            }
        });

    }


}
