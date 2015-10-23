package com.conestogac.msd.mycamera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void takePhoto(View v)
    {
        dispatchTakePictureIntent(99);
    }

    private void dispatchTakePictureIntent(int actionCode) {
        Intent takePictureIntent = new
                Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, actionCode);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if( requestCode != 0) {
            ImageView imgView = (ImageView) findViewById(R.id.imageView_preview);
            Bundle extras = data.getExtras();
            Bitmap mImageBitmap = (Bitmap) extras.get("data");
            imgView.setImageBitmap(toGrayscale(mImageBitmap));

            File f = createImageFile();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        }
    }

    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    private File createImageFile()throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp +"_";
        File image =File.createTempFile( imageFileName, JPEG_FILE_SUFFIX, getAlbumDir());
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic()
    {
        Intent mediaScanIntent =new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f =new File(mCurrentPhotoPath);
        Uri contentUri =Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    private void setPic()
    {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions =new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds =true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        // Determine how much to scale down the image
        int scaleFactor =Math.min(photoW/targetW, photoH/targetH);
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds =false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable =true;
        Bitmap bitmap =BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

}
