package algonquin.cst2335.inclassexamples_s21;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;

/**This page holds the code for checking the complexity of the password
 * entered by the user.
 * @author Younes Tamim
 * @version 1.0
 */

public class MainActivity extends AppCompatActivity {
    /**This function checks whether the password has a special character or not
     *
     * @param c is representing each character of the String
     * @return true if one of the cases is true, and false if one of the cases is false
     */
    private boolean isSpecialCharacter(char c) {
        //return true if c is one of: #$%^&*!@?
        switch (c) {
            case '#':
                return true;
            case '?':
                return true;
            case '*':
                return true;
            case '%':
                return true;
            case '^':
                return true;
            case '&':
                return true;
            case '@':
                return true;
            //return false otherwise
            default:
                return false;

        }

    }

    /**
     * This function is supposed to check the complexity of
     * password
     *
     * @param pw The String Object that is being checked
     * @return this function returns true if the password is complex enough
     * and false if it isn't
     */


    boolean checkPasswordComplexity(String pw) {


        for (int i = 0; i < pw.length(); i++) {

            char c = pw.charAt(i);
            boolean foundUpperCase = isUpperCase(c);
            boolean foundLowerCase = isLowerCase(c);
            boolean foundNumber = isDigit(c);
            boolean foundSpecial = isSpecialCharacter(c);

            if (!foundUpperCase) {

                Toast.makeText(MainActivity.this, "Your password is missing an upper case letter.",
                        Toast.LENGTH_LONG).show();// Say that they are missing an upper case letter;

                return false;

            } else if (!foundLowerCase) {
                Toast.makeText(MainActivity.this, "Your password is missing a lower case letter.",
                        Toast.LENGTH_LONG).show(); // Say that they are missing a lower case letter;

                return false;

            } else if (!foundNumber) {

                Toast.makeText(MainActivity.this, "Your password is missing a number.",
                        Toast.LENGTH_LONG).show();

                return false;
            } else if (!foundSpecial) {

                Toast.makeText(MainActivity.this, "Your password is missing a special character",
                        Toast.LENGTH_LONG).show();

                return false;
            } else
                return true; //only get here if they're all true


        }
        return false;

    }
    /**this holds the text at the center of the screen */
    private TextView tv = findViewById(R.id.textView);
    /**This hold the text where the user enters his password */
    private EditText et = findViewById(R.id.editTextTextPersonName2);
    /**This holds the Login Button */
    private Button btn = findViewById(R.id.button);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn.setOnClickListener(clk -> {
            String password = et.getText().toString();
            if(checkPasswordComplexity(password))
            {
                tv.setText("The password is complex enough.");
            }
            else
                tv.setText("The password is not complex enough.");


        });
    }
}


