package linkersoft.blackpanther.wobble;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

import linkersoft.blackpanther.wobble.utils.util;


public class WobbleMeshImageView extends ImageView {
    

    private String Wobble;
    private int WobbleHeight;
    private int WobbleWidth;
    private boolean drawMeshGrid,reset;
    float[] wobbleVerts;


    
    public WobbleMeshImageView(Context context) {
        super(context);
    }
    public WobbleMeshImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        in(context, attrs);
    }
    public WobbleMeshImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        in(context, attrs);
    }
    private void in(Context context,AttributeSet attrs){
        TypedArray musher =context.getTheme().obtainStyledAttributes(attrs, R.styleable.WobbleMeshImageView, 0, 0);
        try{
            Wobble=musher.getString(R.styleable.WobbleMeshImageView_wobble);
            Wobble=TextUtils.isEmpty(Wobble)?null:Wobble;
            WobbleHeight =musher.getInt(R.styleable.WobbleMeshImageView_wobbleRows,0);
            WobbleWidth =musher.getInt(R.styleable.WobbleMeshImageView_wobbleColumns,0);
            drawMeshGrid =musher.getBoolean(R.styleable.WobbleMeshImageView_drawMeshGrid,false);
        }finally{musher.recycle();}
        wobbleVerts=new float[(WobbleHeight+1) * (WobbleWidth+1) * 2];
    }

    Canvas CHUCKY;
    Bitmap WASSABI, WobbleMask;
    int W,H;
    Paint PAN=new Paint(){
        {
          setAntiAlias(true);
          setStrokeWidth(2);
        }
    };


    @Override
    protected void onDraw(Canvas CANVAS){
        if((W= getWidth())==0||(H =getHeight())==0)return;
        if(WASSABI ==null){
            WASSABI =Bitmap.createBitmap(W,H,Bitmap.Config.ARGB_8888);
            CHUCKY = new Canvas(WASSABI);
        }super.onDraw(CHUCKY);
        WobbleBitmap(WASSABI,CANVAS);
    }

    private Bitmap WobbleBitmap(Bitmap toWobble,Canvas canvas){
        if(WobbleWidth==0||WobbleHeight==0)throw new IllegalWobblingException();
        int cellno=0;
        if(!reset){
            float yGap=H/(float)WobbleHeight;
            float xGap=W/(float)WobbleWidth;
            for (int y = 0; y <= WobbleHeight; y++){
                for (int x = 0; x <= WobbleWidth; x++){
                    wobbleVerts[cellno]=x*xGap;
                    wobbleVerts[cellno+1]=y*yGap;
                    cellno+=2;
                }
            }
        }if(Wobble!=null){
            if(Wobble.contains("drawable")){
                setWobbleMask(getResId(Wobble,getContext()));
                setWobbleMesh(WobbleWidth,WobbleHeight, WobbleMask,null);
            }else {
                //samples
                // [c]2#(10,0)   column 2 by x=>10 and y=> 0
                // [c#-1]2#(10,0)   column 2 and column (2-1) by x=>10 and y=> 0
                // [r|c]1,8#(10,20) cell @ (1,8) by x=>10 and y=> 20
                // [r|c#c2]1,8#(10,20)cells @ (1,8),(1,9),(1,10) by x=>10 and y=> 20
                // [r|c#c-2]1,8#(10,20)cells @ (1,8),(1,7),(1,6) by x=>10 and y=> 20
                // [r]4#(6,5)   row 4 by x=>6 and y=> 5
                // [r#-3]5#(6,5)   row 5,rows 4,rows 3,rows 2 by x=>6 and y=> 5
                // [r#3]5#(6,5)   row 5,rows 6,rows 7,rows 9 by x=>6 and y=> 5

                int xShift,yShift;
                boolean hasExtra;
                String extra=null;
                String[] wobbleWords=Wobble.split("~");
                for (int i = 0; i < wobbleWords.length; i++){
                    wobbleWords[i]=wobbleWords[i].trim();
                    String wobbleType= util.StringIO.getStringsInBetween(wobbleWords[i],"\\[","\\]",false,false);
                    hasExtra =false;
                    if(wobbleType.contains("#")){
                        String[] splice=wobbleType.split("#");
                        extra=splice[1];
                        wobbleType=splice[0];
                        hasExtra=true;
                        wobbleWords[i]=wobbleWords[i].replace("#"+extra,"");
                    }
                    Log.i("TAG","wobbleType: "+wobbleType+" wobbleWords[i]: "+wobbleWords[i]+" extra: "+extra);
                    if(wobbleType.contentEquals("r")){
                        String IndexShift[]=wobbleWords[i].split("\\["+wobbleType+"\\]")[1].split("#");
                        int rowIndx=Integer.parseInt(IndexShift[0]);
                        String Shift[]=util.StringIO.getStringsInBetween(IndexShift[1],"\\(","\\)",false,false).split(",");
                        xShift=Integer.parseInt(Shift[0]);
                        yShift=Integer.parseInt(Shift[1]);


                        if(hasExtra){
                            int xtra=Integer.parseInt(extra);
                            int plus=(xtra>0)?1:-1;
                                   for (int j = 0; j < Math.abs(xtra)+1; j++) {
                                       if (rowIndx <0||rowIndx > WobbleHeight)throw new IllegalWobblingException("Index in '[row]Index#(x-shift,y-shift)' must be <= (WobbleHeight=" + (WobbleHeight - 1) + ")");
                                       for (int x = 0; x <= WobbleWidth; x++) {
                                           cellno = getWobbCell(x, rowIndx);
                                           wobbleVerts[cellno] += xShift;
                                           wobbleVerts[cellno + 1] += yShift;
                                       }rowIndx += plus;
                                   }
                        }else {
                            if(rowIndx <0||rowIndx>WobbleHeight)throw new IllegalWobblingException("Index in '[row]Index#(x-shift,y-shift)' must be <= (WobbleHeight="+(WobbleHeight-1)+")");
                            for (int x = 0; x <= WobbleWidth; x++) {
                                cellno=getWobbCell(x,rowIndx);
                                wobbleVerts[cellno]+=xShift;
                                wobbleVerts[cellno+1]+=yShift;
                            }
                        }


                    }else if(wobbleType.contentEquals("c")){
                        String IndexShift[]=wobbleWords[i].split("\\["+wobbleType+"\\]")[1].split("#");
                        int columnIndx=Integer.parseInt(IndexShift[0]);
                        String Shift[]=util.StringIO.getStringsInBetween(IndexShift[1],"\\(","\\)",false,false).split(",");
                        xShift=Integer.parseInt(Shift[0]);
                        yShift=Integer.parseInt(Shift[1]);

                        if(hasExtra){
                            int xtra=Integer.parseInt(extra);
                            int plus=(xtra>0)?1:-1;
                            for (int j = 0; j < Math.abs(xtra)+1; j++) {
                                if(columnIndx <0||columnIndx>WobbleWidth)throw new IllegalWobblingException("Index in '[column]Index#(x-shift,y-shift)' must be <= (WobbleWidth="+(WobbleWidth-1)+")");
                                for (int y = 0; y <= WobbleWidth; y++) {
                                    cellno=getWobbCell(columnIndx,y);
                                    wobbleVerts[cellno]+=xShift;
                                    wobbleVerts[cellno+1]+=yShift;
                                }columnIndx += plus;
                            }
                        }else {
                            if(columnIndx <0||columnIndx>WobbleWidth)throw new IllegalWobblingException("Index in '[column]Index#(x-shift,y-shift)' must be <= (WobbleWidth="+(WobbleWidth-1)+")");
                            for (int y = 0; y <= WobbleWidth; y++) {
                                cellno=getWobbCell(columnIndx,y);
                                wobbleVerts[cellno]+=xShift;
                                wobbleVerts[cellno+1]+=yShift;
                            }
                        }

                    }else if(wobbleType.contentEquals("r|c")){
                        wobbleType="r\\|c";
                        String IndexShift[]=wobbleWords[i].split("\\["+wobbleType+"\\]")[1].split("#");
                        String RowColIndx[]=IndexShift[0].split(",");
                        int rowIndx=Integer.parseInt(RowColIndx[0]);
                        int columnIndx=Integer.parseInt(RowColIndx[1]);
                        String Shift[]=util.StringIO.getStringsInBetween(IndexShift[1],"\\(","\\)",false,false).split(",");
                        xShift=Integer.parseInt(Shift[0]);
                        yShift=Integer.parseInt(Shift[1]);


                        if(hasExtra){
                            if(extra.contains("c")){
                                int xtra=Integer.parseInt(extra.split("c")[1]);
                                int plus=(xtra>0)?1:-1;
                                for (int j = 0; j < Math.abs(xtra)+1; j++) {
                                    if(rowIndx <0||columnIndx <0||columnIndx>WobbleWidth||rowIndx>WobbleHeight)throw new IllegalWobblingException("Index(row,column) in '[row|column]row-Index,column-Index#(x-shift,y-shift)' must be <= (WobbleWidth="+(WobbleWidth-1)+") && <= (WobbleHeight="+(WobbleHeight-1)+")");
                                    cellno=getWobbCell(columnIndx,rowIndx);
                                    wobbleVerts[cellno]+=xShift;
                                    wobbleVerts[cellno+1]+=yShift;
                                    rowIndx+= plus;
                                }
                            }else if(extra.contains("r")){
                                int xtra=Integer.parseInt(extra.split("r")[1]);
                                int plus=(xtra>0)?1:-1;
                                for (int j = 0; j < Math.abs(xtra)+1; j++) {
                                    if(rowIndx <0||columnIndx <0||columnIndx>WobbleWidth||rowIndx>WobbleHeight)throw new IllegalWobblingException("Index(row,column) in '[row|column]row-Index,column-Index#(x-shift,y-shift)' must be <= (WobbleWidth="+(WobbleWidth-1)+") && <= (WobbleHeight="+(WobbleHeight-1)+")");
                                    cellno=getWobbCell(columnIndx,rowIndx);
                                    wobbleVerts[cellno]+=xShift;
                                    wobbleVerts[cellno+1]+=yShift;
                                    columnIndx+= plus;
                                }
                            }
                        }else {
                            if(rowIndx <0||columnIndx <0||columnIndx>WobbleWidth||rowIndx>WobbleHeight)throw new IllegalWobblingException("Index(row,column) in '[row|column]row-Index,column-Index#(x-shift,y-shift)' must be <= (WobbleWidth="+(WobbleWidth-1)+") && <= (WobbleHeight="+(WobbleHeight-1)+")");
                            cellno=getWobbCell(columnIndx,rowIndx);
                            wobbleVerts[cellno]+=xShift;
                            wobbleVerts[cellno+1]+=yShift;
                        }

                    }
                }
            }
        }
        canvas.drawBitmapMesh(toWobble,WobbleWidth,WobbleHeight,wobbleVerts,0,null,0,null);
        if(drawMeshGrid)drawMeshGrid(canvas);
        return toWobble;
    }
    private void drawMeshGrid(Canvas canvas) {
        int cellno;

        PAN.setColor(Color.BLUE);
        PAN.setStyle(Paint.Style.STROKE);
        for (int y = 0; y <= WobbleHeight; y ++){//drawing-ROWS
            Path path=new Path();
            cellno=getWobbCell(0,y);
            path.moveTo(wobbleVerts[cellno],wobbleVerts[cellno+1]);
            for (int x = 0; x <=WobbleWidth; x ++){
                cellno=getWobbCell(x,y);
                path.lineTo(wobbleVerts[cellno],wobbleVerts[cellno+1]);
            }canvas.drawPath(path,PAN);
        }
        for (int x = 0; x <=WobbleWidth; x ++) {//drawing-COLUMNS
            Path path=new Path();
            cellno=getWobbCell(x,0);
            path.moveTo(wobbleVerts[cellno],wobbleVerts[cellno+1]);
            for (int y = 0; y <= WobbleHeight; y ++){
                cellno=getWobbCell(x,y);
                path.lineTo(wobbleVerts[cellno],wobbleVerts[cellno+1]);
            }canvas.drawPath(path,PAN);
        }
        PAN.setStyle(Paint.Style.FILL);
        PAN.setColor(Color.GREEN);
        for (int y = 0; y <= WobbleHeight; y ++) {//drawing-NODES
            for (int x = 0; x <= WobbleWidth; x ++) {
                cellno=getWobbCell(x,y);
                canvas.drawCircle(wobbleVerts[cellno],wobbleVerts[cellno+1],3,PAN);
            }
        }
    }


    /*
    *
    *
    *
     *
     *
    | Fruit         | Price                   | Advantages              |
| ------------- | ----------------------- | ----------------------- |
| Bananas       | first line<br>next line | first line<br>next line |
| Bananas       | first line<br>next line | first line<br>next line |





                               [r]0#(0,25)~[r|c]0,0#(0,10)~[r|c]0,2#(0,-5)~[r|c]0,4#(0,10)~[r|c]0,5#(0,20)~[r|c]0,6#(0,20)~[r|c]0,7#(0,15)~
                   [r]1#(0,15)~[r|c]1,0#(0,10)~[r|c]1,2#(0,-5)~[r|c]1,4#(0,10)~[r|c]1,5#(0,20)~[r|c]1,6#(0,20)~[r|c]1,7#(0,15)~
                   [r]2#(0,10)~[r|c]2,0#(0,10)~[r|c]2,2#(0,-5)~[r|c]2,4#(0,10)~[r|c]2,5#(0,20)~[r|c]2,6#(0,20)~[r|c]2,7#(0,15)~
                   [r]3#(0,5)~[r|c]3,0#(0,10)~[r|c]3,2#(0,-5)~[r|c]3,4#(0,10)~[r|c]3,5#(0,20)~[r|c]3,6#(0,20)~[r|c]3,7#(0,15)~
                   [r]4#(0,0)~[r|c]4,0#(0,10)~[r|c]4,2#(0,-5)~[r|c]4,4#(0,10)~[r|c]4,5#(0,20)~[r|c]4,6#(0,20)~[r|c]4,7#(0,15)~
                   [r]5#(0,-5)~[r|c]5,0#(0,10)~[r|c]5,2#(0,-5)~[r|c]5,4#(0,10)~[r|c]5,5#(0,20)~[r|c]5,6#(0,20)~[r|c]5,7#(0,15)~
                   [r]6#(0,-10)~[r|c]6,0#(0,10)~[r|c]6,2#(0,-5)~[r|c]6,4#(0,10)~[r|c]6,5#(0,20)~[r|c]6,6#(0,20)~[r|c]6,7#(0,15)~
                   [r]7#(0,-15)~[r|c]7,0#(0,10)~[r|c]7,2#(0,-5)~[r|c]7,4#(0,10)~[r|c]7,5#(0,20)~[r|c]7,6#(0,20)~[r|c]7,7#(0,15)~
                   [r]8#(0,-25)~[r|c]8,0#(0,10)~[r|c]8,2#(0,-5)~[r|c]8,4#(0,10)~[r|c]8,5#(0,20)~[r|c]8,6#(0,20)~[r|c]8,7#(0,15)

    *
    * */

    public void setWobble(String Wobble){
        this.Wobble=Wobble;
        this.reset=true;
        invalidate();
    }
    public void setWobbleMesh(int WobbleWidth,int WobbleHeight,float[] wobbleVerts,String Wobble){
        if((WobbleWidth+1)*(WobbleHeight+1)*2!=wobbleVerts.length)throw new IllegalWobblingException("((WobbleWidth+1)*(WobbleHeight+1)*2) must be = wobbleVerts.length");
        this.Wobble=Wobble;
        this.wobbleVerts=wobbleVerts;
        this.WobbleWidth=WobbleWidth;
        this.WobbleHeight=WobbleHeight;
        this.reset=true;
       invalidate();
    }
    public void setWobbleMesh(int WobbleWidth,int WobbleHeight,int WobbleMaskResId,String Wobble){
        WobbleMask= getWobbleMask4rmRes(WobbleMaskResId);
        setWobbleMesh(WobbleWidth,WobbleHeight,WobbleMask,Wobble);
    }
    public void setWobbleMesh(int WobbleWidth,int WobbleHeight,Bitmap WobbleMask,String Wobble){

        ArrayList<Float[]>eqWobbVertsLis=new ArrayList<>();//creating an equally-spaced mesh
        ArrayList<Float[]>inWobbVertsLis=new ArrayList<>();//creating the initial-mesh
        ArrayList<Float[]>WobbVertsLis=new ArrayList<>();//creating the mesh

        int wvW=WobbleMask.getWidth();
        int wvH=WobbleMask.getHeight();
        float scaleX=1;
        float scaleY=1;
        if(W!=wvW||H!=wvH){
             scaleX=W/(float)wvW;
             scaleY=H/(float)wvH;
        }float[] wobbleVerts=new float[(WobbleHeight+1) * (WobbleWidth+1) * 2];

        /*fetching the verts directly from the mask and scaling the coordinates(according 2 the view) 4 the initial mesh*/
        for (int y = 0,cellno=0; y < wvH; y++) {
            for (int x = 0; x < wvW; x++) {
                if(WobbleMask.getPixel(x,y)==Color.GREEN){
                    if(cellno!=wobbleVerts.length){
                        inWobbVertsLis.add(new Float[]{x*scaleX,y*scaleY});
                        cellno+=2;
                    }else throw new IllegalWobblingException(" Make sure the number of green pixels in your WobbleMaskResource = (WobbleHeight+1)*(WobbleWidth+1) ");
                }
            }
        }
        /*filling the equally-spaced mesh to help sort the fetched verts 4rm(the initial-mesh) in2 the mesh*/
        float yGap=H/(float)WobbleHeight;
        float xGap=W/(float)WobbleWidth;
        for (int y = 0; y <= WobbleHeight; y++) {
            for (int x = 0; x <= WobbleWidth; x++)
            eqWobbVertsLis.add(new Float[]{x * xGap, y * yGap});
        }
        /*comparing the equally-spaced mesh and the initial-mesh inorder to sort into the mesh*/
        Float eqWobb[],inWobb[],distance,closest;
        for (int i = 0,index=0; i < eqWobbVertsLis.size(); i++) {
            eqWobb=eqWobbVertsLis.get(i);
            closest=Float.MAX_VALUE;
            for (int j = 0; j < inWobbVertsLis.size(); j++) {
                inWobb=inWobbVertsLis.get(j);
                distance=Math.abs(distance(eqWobb[0],eqWobb[1],inWobb[0],inWobb[1]));
                if(distance<closest){
                    closest=distance;
                    index=j;
                }
            }
            WobbVertsLis.add(inWobbVertsLis.get(index));
            inWobbVertsLis.remove(index);
        }

        /*loading the verts from the mesh*/
        Float[] wobvert;
        for (int y = 0,cell=0,cellno=0; y <= WobbleHeight; y++){
            for (int x = 0; x <= WobbleWidth; x++){
                wobvert=WobbVertsLis.get(cell);
                wobbleVerts[cellno]=wobvert[0];
                wobbleVerts[cellno+1]=wobvert[1];
                cellno+=2;
                cell++;
            }
        }
        setWobbleMesh(WobbleWidth,WobbleHeight,wobbleVerts,Wobble);
        disposeWobbleMask();
    }
    private float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    public void setWobbleMask(int ResId){
        WobbleMask= getWobbleMask4rmRes(ResId);
    }
    public int getWobbleWidth() {
        return WobbleWidth;
    }
    public int getWobbleHeight() {
        return WobbleHeight;
    }
    public float[] getWobbleMesh(){
        return wobbleVerts;
    }
    public Bitmap getWobbleMask(){
        int cellno,xXx,yYy;
        Bitmap WobbleMask =Bitmap.createBitmap(W,H,Bitmap.Config.ARGB_8888);
        for (int y = 0; y <= WobbleHeight; y++) {
            for (int x = 0; x <= WobbleWidth; x++) {
                cellno=getWobbCell(x,y);
                xXx=(int) wobbleVerts[cellno];
                yYy=(int) wobbleVerts[cellno+1];
                if((xXx>=0&& xXx<W)&&(yYy>=0&& yYy<H))WobbleMask.setPixel(xXx,yYy,Color.GREEN);
                else throw new IllegalWobblingException("Not all wobbleVerts[x,y] pairs can fall on the 'WobbleMeshBitmapMask' call getWobbleMesh() instead to access wobbleVerts[]");
            }
        }return WobbleMask;
    }
    private Bitmap getWobbleMask4rmRes(int ResId){
        BitmapFactory.Options IgnoreDpi=new BitmapFactory.Options();
        IgnoreDpi.inScaled=false;
        return BitmapFactory.decodeResource(getResources(),ResId,IgnoreDpi);
    }
    public String getWobble() {
        return Wobble;
    }
    private void disposeWobbleMask(){
        if(WobbleMask !=null){
            WobbleMask.recycle();
            WobbleMask =null;
        }
    }

    private int getWobbCell(int x,int y){
        int cell=(y*(WobbleWidth+1))+x;
        return cell*2;
    }
    private int getResId(String drawableRef, Context context) {
        String rsrcName = drawableRef.split("res/drawable/")[1].split(".png")[0];
        return context.getResources().getIdentifier(rsrcName, "drawable", context.getPackageName());
    }

    @Override
    public String toString() {
        return "@LiNKeR(>_<)~"+super.toString();
    }

    private static class IllegalWobblingException extends IllegalStateException{
        public IllegalWobblingException(){
            super("Wobbling [Width & Height] must be > 0");
        }
        public IllegalWobblingException(String message){
            super(message);
        }
    }


}


/*



 */