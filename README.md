[![Project Status: Active - Initial development has started, temporary release; work hasn't been stopped ](http://www.repostatus.org/badges/0.1.0/active.svg)](http://www.repostatus.org/#active)

WobbleImageView
=============
An Imageview with an easy Wobble/Mesh-Warp  capability.

## Quick Start

> **GRADLE**

```xml
   dependencies {
        implementation 'com.github.54LiNKeR:WobbleImageView:2.0.0'
    }
```

> **XML**

**1. Cr8ting The Mesh**

by default this is how create a new mesh over the ImageView

<img src="shots/wobb0.png" width="49%">

```xml
   <linkersoft.blackpanther.wobble.WobbleMeshImageView
       android:layout_width="200dp"
       android:layout_height="200dp"
       android:layout_gravity="center"
       android:id="@+id/wobbler"
       android:src="@drawable/dwayne_mesh"
       app:wobbleRows="8"
       app:wobbleColumns="8"
       app:drawMeshGrid="true"
       />
```

if you like you could load a custom wobble-mesh via the wobble attribute `app:wobble` i.e. `app:wobble=@drawable/your-wobble-mask` but
your-wobble-mask must be a bitmap in which each row of your mesh is a different color in terms of pixels to the another e.g.

![wobb1](shots/wobb1.png)

also if your-wobble-mask's dimension is not the same as your ImageView's dimension the mesh is scaled to fit the
ImageView.

**2. Warping The Mesh**

This is done via the wobble attribute `app:wobble` and there are three ways to warp the ImageView namely:


   | Warping | Syntax | Example | # |
   |----------|---------|--------|---------|
   | by Shifting the rows | `app:wobble=[r]rowIndex#(xShift,yShift)` | **[r]2#(5,-20)**| cute |
   | by Shifting the columns | `app:wobble=[c]columnIndex#(xShift,yShift)` | **[c]2#(0,10)**| fine |
   | by Shifting any node | `app:wobble=[r|c]rowIndex,columnIndex#(xShift,yShift)` | **[r\|c]1,2#(8,15)**| pretty |

in order to Shift multiple rows/columns/nodes at the same time here's how

- by Shifting the rows
   - `app:wobble=[r#extra-rowIndices]rowIndex#(xShift,yShift)` e.g. **[r#2]2#(5,-20)**
- by Shifting the columns
   - `app:wobble=[c#extra-columnIndices]columnIndex#(xShift,yShift)` e.g. **[c#-2]6#(0,10)** (*the minus indicates direction i.e. 2 columns above the supposed*)
- by Shifting any node
   - `app:wobble=[r|c#extra-nodes]rowIndex,columnIndex#(xShift,yShift)` e.g. **[r\|c#r2]1,2#(8,15)**,**[r\|c#c3]1,2#(8,15)** (*r2 = 2 extra nodes along corresponding row, c3 = 3 extra nodes along corresponding column*)

## Examples

# 1. Simple Ones

```xml

        <linkersoft.blackpanther.wobble.WobbleMeshImageView
               android:layout_width="200dp"
               android:layout_height="200dp"
               android:layout_gravity="center"
               android:id="@+id/wobbler"
               android:src="@drawable/dwayne_mesh"
               app:wobble="[r]3#(15,-10)
               app:wobbleRows="8"
               app:wobbleColumns="8"
               app:drawMeshGrid="true"
               />
```

   | # | `app:wobble` | Result |
   |----------|---------|--------|
   | cute | [r]2#(5,-20) | <img src="shots/dwayne0.png" width="49%"> |
   | fine | [c]2#(0,10) | <img src="shots/dwayne1.png" width="49%"> |
   | pretty | [r\|c]1,2#(8,15) | <img src="shots/dwayne2.png" width="49%"> |
   | cute+fine+pretty | [r]2#(5,-20)~[c]2#(0,10)~[r\|c]1,2#(8,15) | <img src="shots/dwayne3.png" width="49%"> |


# 2. Complex Ones

<img src="shots/dwayne5.png" width="49%">

```xml

      <linkersoft.blackpanther.wobble.WobbleMeshImageView
             android:layout_width="200dp"
             android:layout_height="200dp"
             android:layout_gravity="center"
             android:id="@+id/wobbler"
             android:src="@drawable/dwayne_mesh"
             app:wobble="[r[0#(0,25)~[r|c]0,0#(0,10)~[r|c]0,2#(0,-5)~[r|c]0,4#(0,10)~[r|c]0,5#(0,20)~[r|c]0,6#(0,20)~[r|c]0,7#(0,15)~
                         [r]1#(0,15)~[r|c]1,0#(0,10)~[r|c]1,2#(0,-5)~[r|c]1,4#(0,10)~[r|c]1,5#(0,20)~[r|c]1,6#(0,20)~[r|c]1,7#(0,15)~
                         [r]2#(0,10)~[r|c]2,0#(0,10)~[r|c]2,2#(0,-5)~[r|c]2,4#(0,10)~[r|c]2,5#(0,20)~[r|c]2,6#(0,20)~[r|c]2,7#(0,15)~
                         [r]3#(0,5)~[r|c]3,0#(0,10)~[r|c]3,2#(0,-5)~[r|c]3,4#(0,10)~[r|c]3,5#(0,20)~[r|c]3,6#(0,20)~[r|c]3,7#(0,15)~
                         [r]4#(0,0)~[r|c]4,0#(0,10)~[r|c]4,2#(0,-5)~[r|c]4,4#(0,10)~[r|c]4,5#(0,20)~[r|c]4,6#(0,20)~[r|c]4,7#(0,15)~
                         [r]5#(0,-5)~[r|c]5,0#(0,10)~[r|c]5,2#(0,-5)~[r|c]5,4#(0,10)~[r|c]5,5#(0,20)~[r|c]5,6#(0,20)~[r|c]5,7#(0,15)~
                         [r]6#(0,-10)~[r|c]6,0#(0,10)~[r|c]6,2#(0,-5)~[r|c]6,4#(0,10)~[r|c]6,5#(0,20)~[r|c]6,6#(0,20)~[r|c]6,7#(0,15)~
                         [r]7#(0,-15)~[r|c]7,0#(0,10)~[r|c]7,2#(0,-5)~[r|c]7,4#(0,10)~[r|c]7,5#(0,20)~[r|c]7,6#(0,20)~[r|c]7,7#(0,15)~
                         [r]8#(0,-25)~[r|c]8,0#(0,10)~[r|c]8,2#(0,-5)~[r|c]8,4#(0,10)~[r|c]8,5#(0,20)~[r|c]8,6#(0,20)~[r|c]8,7#(0,15)"
             app:wobbleRows="8"
             app:wobbleColumns="8"
             app:drawMeshGrid="true"
             />
```

<img src="shots/dwayne6.png" width="49%">

```xml

     <linkersoft.blackpanther.wobble.WobbleMeshImageView
            android:layout_width="200dp"
            android:layout_height="200dp"
            android:layout_gravity="center"
            android:id="@+id/wobbler"
            android:src="@drawable/dwayne_mesh"
            app:wobble=" [r|c#r2]0,3#(0,25)~[r|c]0,0#(0,0)~[r|c]0,1#(0,10)~[r|c]0,2#(0,20)~[r|c]0,6#(0,20)~[r|c]0,7#(0,10)~
                         [r]1#(0,-5)~[r|c#r2]1,3#(0,25)~[r|c]1,0#(0,-15)~[r|c]1,1#(0,10)~[r|c]1,2#(0,20)~[r|c]1,6#(0,20)~[r|c]1,7#(0,10)~[r|c]1,8#(0,-15)~
                         [r]2#(0,-5)~[r|c#r2]2,3#(0,20)~[r|c]2,0#(0,-10)~[r|c]2,1#(0,5)~[r|c]2,2#(0,15)~[r|c]2,6#(0,15)~[r|c]2,7#(0,5)~[r|c]2,8#(0,-10)~
                         [r]3#(0,-8)~[r|c#r2]3,3#(0,20)~[r|c]3,0#(0,-10)~[r|c]3,1#(0,5)~[r|c]3,2#(0,15)~[r|c]3,6#(0,15)~[r|c]3,7#(0,5)~[r|c]3,8#(0,-10)~

                         [r]5#(0,8)~[r|c#r2]5,3#(0,-20)~[r|c]5,0#(0,10)~[r|c]5,1#(0,-5)~[r|c]5,2#(0,-15)~[r|c]5,6#(0,-15)~[r|c]5,7#(0,-5)~[r|c]5,8#(0,10)~
                         [r]6#(0,-5)~[r|c#r2]6,3#(0,-20)~[r|c]6,0#(0,10)~[r|c]6,1#(0,-5)~[r|c]6,2#(0,-15)~[r|c]6,6#(0,-15)~[r|c]6,7#(0,-5)~[r|c]6,8#(0,10)~
                         [r]7#(0,-5)~[r|c#r2]7,3#(0,-25)~[r|c]7,0#(0,15)~[r|c]7,1#(0,-10)~[r|c]7,2#(0,-20)~[r|c]7,6#(0,-20)~[r|c]7,7#(0,-10)~[r|c]7,8#(0,15)~
                                     [r|c#r2]8,3#(0,-25)~[r|c]8,1#(0,-10)~[r|c]8,2#(0,-20)~[r|c]8,6#(0,-20)~[r|c]8,7#(0,-10)""
            app:wobbleRows="8"
            app:wobbleColumns="8"
            app:drawMeshGrid="true"
            />
```

> Just incase all have said is Jargon, here are some samples with some more meaningful meanings

 | sample  | meaning |
 |---------|---------|
 | [c]0#(0,5) | shift column 0 by x=>0 and y=> 5  |
 | [c]2#(10,0) | shift column 2 by x=>10 and y=> 0  |
 | [c#-1]2#(10,0) | shift column 2 and column (2-1) by x=>10 and y=> 0 |
 | [r|c]1,8#(10,20) | shift node @ (1,8) by x=>10 and y=> 20 |
 | [r|c#c2]1,8#(10,20) | shift nodes @ (1,8),(1,9),(1,10) by x=>10 and y=> 20 |
 | [r|c#c-2]1,8#(10,20) | shift nodes @ (1,8),(1,7),(1,6) by x=>10 and y=> 20 |
 | [r]4#(6,5) | shift row 4 by x=>6 and y=> 5 |
 | [r#-3]5#(6,5) | shift row 5,rows 4,rows 3,rows 2 by x=>6 and y=> 5 |
 | [r#3]5#(6,5) | shift row 5,rows 6,rows 7,rows 9 by x=>6 and y=> 5 |


> **JAVA**

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
                  ((WobbleMeshImageView)v).setWobbleMesh(woah.getWobbleWidth(),woah.getWobbleHeight(),R.drawable.wobble_mask1,null);
            }
        });

    }


}
  
  ```



 | **Public-methods**       | **return**  |
|  :---: | :---: |
| `setWobbleMesh(int WobbleWidth,int WobbleHeight,float[] wobbleVerts,String Wobble)`| *void* |
| `setWobbleMesh(int WobbleWidth,int WobbleHeight,Bitmap WobbleMask,String Wobble)`| *void* |
| `setWobbleMesh(int WobbleWidth,int WobbleHeight,int WobbleMaskResId,String Wobble)`| *void* |
| `getWobbleWidth()`| *int* |
| `getWobbleHeight()`| *int* |
| `getWobbleMesh()`| *float[]* |
| `getWobbleMask()`| *Bitmap* |
| `getWobble()`| *String* |
