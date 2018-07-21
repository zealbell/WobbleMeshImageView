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
                Bitmap WobbleMask=woah.setWobbleMask(R.drawable.wobble_mask0);
                woah.setWobbleMesh(woah.getWobbleWidth(),woah.getWobbleHeight(),WobbleMask,null);
                woah.disposeWobbleMask();
            }
        });

    }


}
