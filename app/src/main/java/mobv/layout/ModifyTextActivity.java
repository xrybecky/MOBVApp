package mobv.layout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ModifyTextActivity extends AppCompatActivity {

    static final String MODIFIED_STRING_KEY = "modified_string";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get input string from bundle
        Bundle bundle = this.getIntent().getExtras();
        String string = bundle.getString(MainActivity.INPUT_TEXT_TO_MODIFY_KEY);

        // modify to uppercase
        string = (string != null) ? string.toUpperCase() : "";

        // set result for MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MODIFIED_STRING_KEY, string);
        setResult(MainActivity.MODIFIED_STRING_REQUEST, intent);

        // finish current activity
        finish();

    }
}
