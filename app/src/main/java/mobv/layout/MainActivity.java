package mobv.layout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    // request codes
    static final int PICK_IMAGE_REQUEST = 1;
    static final int MODIFIED_STRING_REQUEST = 2;

    // Bundle keys
    static final String MAIN_IMAGE_BUNDLE_KEY = "main_image_view";
    static final String MAIN_NUMBER_KEY = "main_number";
    static final String INPUT_TEXT_TO_MODIFY_KEY = "input_to_modify";
    static final String INPUT_TEXT_TO_SAVE_KEY = "input_to_save";

    private int number;

    // --------------------------------
    // HANDLE REQUESTS FROM ACTIVITIES
    // --------------------------------
    private void handlePickImageRequest(Intent data){
        ImageView imageView = (ImageView)findViewById(R.id.leadImageView);

        InputStream input = null;
        try {
            if(data != null) {
                input = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                imageView.setImageBitmap(bitmap);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void handleModifiedStringRequest(Intent data){

        // restore modified string
        Bundle bundle = data.getExtras();
        String modifiedString = bundle.getString(ModifyTextActivity.MODIFIED_STRING_KEY);

        // show restored string
        if(modifiedString != null && !modifiedString.isEmpty()){
            Toast.makeText(
                    this,
                    modifiedString,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // ---------------------
    // BUTTON HANDLERS
    // ---------------------

    public void pickImage(View view){
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK); //  MediaStore.Images.Media.INTERNAL_CONTENT_URI
        pickImageIntent.setType("image/*");
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
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

    public void modifyText(View view){

        // get text to modify
        EditText editTextToModify = (EditText)findViewById(R.id.inputTextToModify);
        String inputText = editTextToModify.getText().toString();

        // check isEmpty
        if(!inputText.isEmpty()){

            // set & start ModifyTextActivity
            Intent intent = new Intent(this, ModifyTextActivity.class);
            intent.putExtra(INPUT_TEXT_TO_MODIFY_KEY, inputText);
            startActivityForResult(intent, MODIFIED_STRING_REQUEST);

        }else{
            Toast.makeText(this, getString(R.string.empty_string), Toast.LENGTH_SHORT).show();
        }

    }




    // ---------------------
    // CONTENT FUNCTIONS
    // ---------------------
    public void addSmile(){

        // create width, height params
        int width = (int)getResources().getDimension(R.dimen.smile);
        int height = (int)getResources().getDimension(R.dimen.smile);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

        // create & set imageView
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.face);
        imageView.setLayoutParams(params);
        imageView.setContentDescription(getString(R.string.smile_desc));

        // add view to layout
        LinearLayout llSmiles = (LinearLayout)findViewById(R.id.llSmiles);
        llSmiles.addView(imageView);
    }

    public void printNumber(){
        TextView tv = (TextView)findViewById(R.id.number);
        tv.setText(getString(R.string.value, this.number));
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

    public void saveAppState(){

    }

    public void restoreAppState(Bundle savedInstanceState){
        // restore main number
        this.number = savedInstanceState.getInt(MAIN_NUMBER_KEY);
        printNumber();
        restoreSmiles();

        // restore current main image
        ImageView imageView = (ImageView)findViewById(R.id.leadImageView);
        Bitmap bitmap = savedInstanceState.getParcelable("image");
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        }

        // restore filled text
        String inputText = savedInstanceState.getString(INPUT_TEXT_TO_SAVE_KEY);
        if(inputText != null && !inputText.isEmpty()) {
            EditText editText = (EditText) findViewById(R.id.inputTextToModify);
            editText.setText(inputText);
        }
    }


    // ---------------------
    // OVERRIDES
    // ---------------------

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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("M_LOG", "onSaveInstanceState");

        // save main number
        outState.putInt(MAIN_NUMBER_KEY, this.number);

        // save current main image
        ImageView imageView = (ImageView)findViewById(R.id.leadImageView);
        if(imageView != null){
            BitmapDrawable bitmapDrawable = (BitmapDrawable)imageView.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            outState.putParcelable("image", bitmap);
        }

        // save filled text
        EditText editText = (EditText)findViewById(R.id.inputTextToModify);
        String inputText = editText.getText().toString();
        outState.putString(INPUT_TEXT_TO_SAVE_KEY, inputText);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("M_LOG", "onRestoreInstanceState");

        restoreAppState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle requestCode
        switch (requestCode){

            // picked image
            case PICK_IMAGE_REQUEST:
                handlePickImageRequest(data);
                break;

            // modified string by ModifyTextActivity
            case MODIFIED_STRING_REQUEST:
                handleModifiedStringRequest(data);
                break;
        }
    }


}
