package edu.flpoly.mobiledevapps.fall21.UI_Demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.amplifyframework.auth.cognito.*;

import androidx.appcompat.app.AppCompatActivity;

public class SelectUserActivity extends AppCompatActivity{
    Button menu_button_select_user, selectPlan1Button, selectPlan2Button;
    private TextView fileContent;
    PopupMenu popupMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_user);
        menu_button_select_user = (Button) findViewById(R.id.menu_button_select_user);
        selectPlan1Button = (Button) findViewById(R.id.selectPlan1Button);
        selectPlan2Button = (Button) findViewById(R.id.selectPlan2Button);
        fileContent = (TextView) findViewById(R.id.selectPlanOutput);
        //Initializing Amazon API stuff
        try {
            // Add these lines to add the AWSCognitoAuthPlugin and AWSS3StoragePlugin plugins
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.configure(getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }


        // Setting onClick behavior to the button
        menu_button_select_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initializing the popup menu and giving the reference as current context
                popupMenu = new PopupMenu(SelectUserActivity.this, menu_button_select_user);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        determineActivityToGoTo(menuItem);
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });
        //setting onClick behavior for plan 1 button:
        /*selectPlan1Button.setOnClickListener(new View.OnClickListener(){
            @Override
           public void onClick(View view){
                String string = " ";
                AssetManager am = SelectPlanActivity.this.getAssets();
                try {
                    InputStream inputStream = getAssets().open("plan1.json");
                    int size = inputStream.available();
                    byte[] buffer = new byte[size];
                    inputStream.read(buffer);
                    string = new String(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fileContent.setText(string);

                //uploads plan1.json to Amazon
                File plan1file = new File(getApplicationContext().getFilesDir(), "plan1.json");
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(plan1file));
                    writer.append("Example file contents");
                    writer.close();
                } catch (Exception exception) {
                    Log.e("MyAmplifyApp", "Upload failed", exception);
                }
            }


        });*/
        //setting onClick behavior for plan 2 button:
        /*selectPlan2Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                /*String string = " ";
                AssetManager am = SelectPlanActivity.this.getAssets();
                try {
                    InputStream inputStream = getAssets().open("plan2.json");
                    int size = inputStream.available();
                    byte[] buffer = new byte[size];
                    inputStream.read(buffer);
                    string = new String(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fileContent.setText(string);
            }
        });*/
    }
    public void goToActivity(Class<?> cls){
        Intent intent = new Intent(getApplicationContext(), cls);
        startActivity(intent);
    }
    public void determineActivityToGoTo(MenuItem menuItem) {
        //Get the name of the current activity:
        String currentActivity = getClass().getSimpleName();
        //Show the name of the current activity for testing:
        Toast.makeText(getApplicationContext(), currentActivity, Toast.LENGTH_SHORT).show();
        //if menuItem equals the first entry in the popupMenu(main screen)
        if (menuItem.getTitle().equals("Main") && !currentActivity.equals("MainActivity")) {
            Toast.makeText(getApplicationContext(), "You clicked to go to the main screen", Toast.LENGTH_SHORT).show();
            goToActivity(MainActivity.class);
        }
        //if menuItem equals the second entry in the popupMenu (plan screen):
        else if (menuItem.getTitle().equals("Select Plan") && !currentActivity.equals("SelectPlanActivity")) {
            Toast.makeText(getApplicationContext(), "You clicked to go to the select plan screen", Toast.LENGTH_SHORT).show();
            goToActivity(SelectPlanActivity.class);
        }
        //if menuItem equals the third entry in the popupMenu (leaderboard screen)
        else if (menuItem.getTitle().equals("Leaderboard") && !currentActivity.equals("LeaderboardActivity")) {
            Toast.makeText(getApplicationContext(), "You clicked to go to the leaderboard screen", Toast.LENGTH_SHORT).show();
            goToActivity(LeaderboardActivity.class);
        }
        //if menuItem equals the fourth entry in the popupMenu (select user screen)
        else if (menuItem.getTitle().equals("Select User") && !currentActivity.equals("SelectUserActivity")) {
            Toast.makeText(getApplicationContext(), "You clicked to go to the select user screen", Toast.LENGTH_SHORT).show();
            goToActivity(SelectUserActivity.class);
        }
        //if menuItem is equal to the current activity:
        else {
            // Toast message on menu item clicked
            Toast.makeText(getApplicationContext(), "You Clicked " + menuItem.getTitle() + " but you're already in " + currentActivity, Toast.LENGTH_SHORT).show();
        }
    }
}
