[![Project Status: Active - Initial development has started, temporary release; work hasn't been stopped ](http://www.repostatus.org/badges/0.1.0/active.svg)](http://www.repostatus.org/#active)

WobbleImageView
=============
An Imageview with an easy Wobble/Mesh-Warp  capability.

## Appearance

![Demo](shots/appearance.gif)

## Quick Start

> Gradle

```xml
   dependencies {
        implementation 'com.github.54LiNKeR:WobbleImageView:1.0.0'
    }
```

> XML

```xml
    <linkersoft.blackpanther.wobble.WobbleMeshImageView
       android:layout_width="200dp"
       android:layout_height="200dp"
       android:layout_gravity="center"
       android:id="@+id/wobbler"
       android:src="@drawable/dwayne_mesh"
       app:wobbleRows="8"
       app:wobbleColumns="8"
       app:wobble="@drawable/wobble_mask2"
       app:drawMeshGrid="false"
       />
```

> JAVA

  ```java
  
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
                Bitmap WobbleMask=woah.getWobbleMask(R.drawable.your_wobble_mask);
                woah.setWobbleMesh(woah.getWobbleWidth(),woah.getWobbleHeight(),WobbleMask,null);
                woah.disposeWobbleMask();
            }
        });

    }


}
  
  ```
