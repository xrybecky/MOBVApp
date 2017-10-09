package mobv.layout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    static final int PICK_IMAGE_REQUEST = 1;

    private int number;

    public void pickImage(View view){

        Intent pickImageIntent = new Intent(Intent.ACTION_PICK); //  MediaStore.Images.Media.INTERNAL_CONTENT_URI
        pickImageIntent.setType("image/*");
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PICK_IMAGE_REQUEST){
            Toast.makeText(this, "Image picked", Toast.LENGTH_SHORT).show();


            ImageView imageView = (ImageView)findViewById(R.id.leadImageView);

            InputStream input = null;
            try {
                input = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void plus(View view){

        if (this.number < 5){
            this.number++;
            printNumber();
            addSmile();
        }else{
            Toast.makeText(this, getString(R.string.max_value), Toast.LENGTH_SHORT).show();
        }
    }

    public void minus(View view){

        if(this.number > 0){
            this.number--;
            printNumber();
            removeSmile();
        }else{
            Toast.makeText(this, getString(R.string.min_value), Toast.LENGTH_SHORT).show();
        }
    }

    public void printNumber(){
        TextView tv = (TextView)findViewById(R.id.number);
        tv.setText(getString(R.string.value, this.number));
    }

    public void addSmile(){
        // create width, height params
        int width = (int)getResources().getDimension(R.dimen.smile);
        int height = (int)getResources().getDimension(R.dimen.smile);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
//        params.setMargins(
//                (int)getResources().getDimension(R.dimen.mg_8),
//                (int)getResources().getDimension(R.dimen.mg_0),
//                (int)getResources().getDimension(R.dimen.smile),
//                (int)getResources().getDimension(R.dimen.mg_0)
//        );

        // create & set imageView
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.face);
        imageView.setLayoutParams(params);
        imageView.setContentDescription(getString(R.string.smile_desc));

        // add view to layout
        LinearLayout llSmiles = (LinearLayout)findViewById(R.id.llSmiles);
        llSmiles.addView(imageView);
    }

    public void removeSmile(){
        LinearLayout llSmiles = (LinearLayout)findViewById(R.id.llSmiles);

        if(llSmiles.getChildCount() > 0){
            llSmiles.removeViewAt(llSmiles.getChildCount() - 1);
        }
    }

    public void restoreSmiles(){
        LinearLayout llSmiles = (LinearLayout)findViewById(R.id.llSmiles);
        llSmiles.removeAllViews();

        for (int i=0; i< this.number; i++){
            addSmile();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("M_LOG", "onCreate");

        this.number = 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("M_LOG", "onStart");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("M_LOG", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("M_LOG", "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("M_LOG", "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("M_LOG", "onResume");

        printNumber();
        restoreSmiles();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("M_LOG", "onSaveInstanceState");

        outState.putInt("number", this.number);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("M_LOG", "onRestoreInstanceState");

        this.number = savedInstanceState.getInt("number");
    }
}
