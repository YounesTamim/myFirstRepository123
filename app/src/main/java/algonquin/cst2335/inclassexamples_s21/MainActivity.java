package algonquin.cst2335.inclassexamples_s21;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    /**
     * This function is supposed to check the complexity of
     * password
     * @param pw The String Object that is being checked
     * @return this function returns true if the password is complex enough
     * and false if it isn't
     */
    boolean checkPasswordComplexity(String pw){
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.textView);
        EditText et = findViewById(R.id.editTextTextPersonName2);
        Button btn = findViewById(R.id.button);

        btn.setOnClickListener(clk -> {
        String password =et.getText().toString();
        checkPasswordComplexity(password);


        });
        }

    }
