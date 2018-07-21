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
import android.widget.ImageView;

import linkersoft.blackpanther.wobble.utils.util;


public class WobbleMeshImageView extends ImageView {
    

    private String Wobble;
    private int WobbleHeight;
    private int WobbleWidth;
    private boolean drawMeshGrid,reset;
    float[] wobbleVerts;
    float xGap,yGap;

    
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
    Bitmap WASSABI,WobbleMaskBitmap;
    int W,H;
    Paint PAN=new Paint(){
        {
          setAntiAlias(true);
          setStrokeWidth(2);
        }
    };


    @Override
    protected void onDraw(Canvas canvas){
        if((W= getWidth())==0||(H =getHeight())==0)return;
        if(WASSABI ==null){
            WASSABI =Bitmap.createBitmap(W,H,Bitmap.Config.ARGB_8888);
            CHUCKY = new Canvas(WASSABI);
        }super.onDraw(CHUCKY);
        canvas.drawBitmap(WobbleBitmap(WASSABI),0,0,null);
    }

    private Bitmap WobbleBitmap(Bitmap toWobble){
        if(WobbleWidth==0||WobbleHeight==0)throw new IllegalWobblingException();
        int cellno=0;
        if(!reset){
            yGap=H/(float)WobbleHeight;
            xGap=W/(float)WobbleWidth;
            for (int y = 0; y < WobbleHeight+1; y++){
                for (int x = 0; x < WobbleWidth+1; x++){
                    wobbleVerts[cellno]=x*xGap;
                    wobbleVerts[cellno+1]=y*yGap;
                    cellno+=2;
                }
            }
        }if(Wobble!=null){
            if(Wobble.contains("drawable")){
                Bitmap WobbleVerticeBitmap = setWobbleMask(getResId(Wobble,getContext()));
                setWobbleMesh(WobbleWidth,WobbleHeight, WobbleVerticeBitmap,null);
            }else {
                int xShift,yShift;
                String[] wobbleWords=Wobble.split("~");
                for (int i = 0; i < wobbleWords.length; i++){
                    String wobbleType= util.StringIO.getStringsInBetween(wobbleWords[i],"\\[","\\]",false,false);
                    if(wobbleType.contentEquals("r")){
                        String IndexShift[]=wobbleWords[i].split("\\["+wobbleType+"\\]")[1].split("#");
                        int rowIndx=Integer.parseInt(IndexShift[0]);
                        String Shift[]=util.StringIO.getStringsInBetween(IndexShift[1],"\\(","\\)",false,false).split(",");
                        xShift=Integer.parseInt(Shift[0]);
                        yShift=Integer.parseInt(Shift[1]);

                        if(rowIndx>WobbleHeight)throw new IllegalWobblingException("Index in '[row]Index#(x-shift,y-shift)' must be <= (WobbleHeight="+(WobbleHeight-1)+")");
                        for (int x = 0; x <= WobbleWidth; x++) {
                            cellno=getWobbCell(x,rowIndx);
                            wobbleVerts[cellno]+=xShift;
                            wobbleVerts[cellno+1]+=yShift;
                        }
                    }else if(wobbleType.contentEquals("c")){
                        String IndexShift[]=wobbleWords[i].split("\\["+wobbleType+"\\]")[1].split("#");
                        int columnIndx=Integer.parseInt(IndexShift[0]);
                        String Shift[]=util.StringIO.getStringsInBetween(IndexShift[1],"\\(","\\)",false,false).split(",");
                        xShift=Integer.parseInt(Shift[0]);
                        yShift=Integer.parseInt(Shift[1]);

                        if(columnIndx>WobbleWidth)throw new IllegalWobblingException("Index in '[column]Index#(x-shift,y-shift)' must be <= (WobbleWidth="+(WobbleWidth-1)+")");
                        for (int y = 0; y <= WobbleWidth; y++) {
                            cellno=getWobbCell(columnIndx,y);
                            wobbleVerts[cellno]+=xShift;
                            wobbleVerts[cellno+1]+=yShift;
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

                        if(columnIndx>WobbleWidth||rowIndx>WobbleHeight)throw new IllegalWobblingException("Index(row,column) in '[row|column]row-Index,column-Index#(x-shift,y-shift)' must be <= (WobbleWidth="+(WobbleWidth-1)+") && <= (WobbleHeight="+(WobbleHeight-1)+")");
                        cellno=getWobbCell(columnIndx,rowIndx);
                        wobbleVerts[cellno]+=xShift;
                        wobbleVerts[cellno+1]+=yShift;
                    }
                }
            }
        }Canvas canvas=new Canvas(toWobble);
         canvas.drawBitmapMesh(toWobble,WobbleWidth,WobbleHeight,wobbleVerts,0,null,0,null);
         if(drawMeshGrid) drawMeshGrid(canvas);
         return toWobble;
    }
    private void drawMeshGrid(Canvas canvas) {
        int cellno;

        PAN.setColor(Color.BLUE);
        PAN.setStyle(Paint.Style.STROKE);
        for (int y = 0; y <= WobbleHeight; y ++){//drawing-ROWS
            Path path=new Path();
            path.moveTo(0,y);
            for (int x = 0; x <=WobbleWidth; x ++){
                cellno=getWobbCell(x,y);
                path.lineTo(wobbleVerts[cellno],wobbleVerts[cellno+1]);
            }canvas.drawPath(path,PAN);
        }
        for (int x = 0; x <=WobbleWidth; x ++) {//drawing-COLUMNS
            Path path=new Path();
            path.moveTo(x,0);
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


    public void setWobbleMesh(int WobbleWidth,int WobbleHeight,float[] wobbleVerts,String Wobble){
        if((WobbleWidth+1)*(WobbleHeight+1)*2!=wobbleVerts.length)throw new IllegalWobblingException("((WobbleWidth+1)*(WobbleHeight+1)*2) must be = wobbleVerts.length");
        this.Wobble=Wobble;
        this.wobbleVerts=wobbleVerts;
        this.WobbleWidth=WobbleWidth;
        this.WobbleHeight=WobbleHeight;
        this.reset=true;
       invalidate();
    }
    public void setWobbleMesh(int WobbleWidth,int WobbleHeight,Bitmap WobbleMaskBitmap,String Wobble){
        this.WobbleMaskBitmap=WobbleMaskBitmap;
        int cellno=0;
        int wvW=WobbleMaskBitmap.getWidth();
        int wvH=WobbleMaskBitmap.getHeight();
        float scaleX=1;
        float scaleY=1;
        if(W!=wvW||H!=wvH){
             scaleX=W/(float)wvW;
             scaleY=H/(float)wvH;
        }
        float[] wobbleVerts=new float[(WobbleHeight+1) * (WobbleWidth+1) * 2];
        for (int y = 0; y < wvH; y++) {
            for (int x = 0; x < wvW; x++) {
                if(WobbleMaskBitmap.getPixel(x,y)==Color.GREEN){
                    if(cellno!=wobbleVerts.length){
                        wobbleVerts[cellno]=Math.round(x*scaleX);
                        wobbleVerts[cellno+1]=Math.round(y*scaleY);
                        cellno+=2;
                    }else throw new IllegalWobblingException(" Make sure the number of green pixels in your WobbleMaskResource = (WobbleHeight+1)*(WobbleWidth+1) ");
                }
            }
        }setWobbleMesh(WobbleWidth,WobbleHeight,wobbleVerts,Wobble);

    }
    public int getWobbleWidth() {
        return WobbleWidth;
    }
    public int getWobbleHeight() {
        return WobbleHeight;
    }
    public Bitmap setWobbleMask(int ResId){
        BitmapFactory.Options IgnoreDpi=new BitmapFactory.Options();
        IgnoreDpi.inScaled=false;
        Bitmap WobbleVerticeBitmap=BitmapFactory.decodeResource(getResources(),ResId,IgnoreDpi);
        return WobbleVerticeBitmap;
    }
    public float[] getWobbleMesh(){
        return wobbleVerts;
    }
    public Bitmap setWobbleMask(){
        int cellno,xXx,yYy;
        Bitmap WobbleMaskBitmap =Bitmap.createBitmap(W,H,Bitmap.Config.ARGB_8888);
        for (int y = 0; y <= WobbleHeight; y++) {
            for (int x = 0; x <= WobbleWidth; x++) {
                cellno=getWobbCell(x,y);
                xXx=(int) wobbleVerts[cellno];
                yYy=(int) wobbleVerts[cellno+1];
                if((xXx>=0&& xXx<W)&&(yYy>=0&& yYy<H))WobbleMaskBitmap.setPixel(xXx,yYy,Color.GREEN);
                else throw new IllegalWobblingException("Not all wobbleVerts[x,y] pairs can fall on the 'WobbleMeshBitmapMask' call getWobbleMesh() instead to access wobbleVerts[]");
            }
        }return WobbleMaskBitmap;
    }
    public String getWobble() {
        return Wobble;
    }
    public void disposeWobbleMask(){
        if(WobbleMaskBitmap!=null){
            WobbleMaskBitmap.recycle();
            WobbleMaskBitmap=null;
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