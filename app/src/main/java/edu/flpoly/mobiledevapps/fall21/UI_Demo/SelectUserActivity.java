package edu.flpoly.mobiledevapps.fall21.UI_Demo;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.amplifyframework.auth.cognito.*;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class SelectUserActivity extends AppCompatActivity{
    Button menu_button_select_user, createUserButton, loadUserButton;
    private TextView fileContent;
    private EditText usernameInput;
    private String usernameFileName;
    PopupMenu popupMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_user);
        menu_button_select_user = (Button) findViewById(R.id.menu_button_select_user);
        createUserButton = (Button) findViewById(R.id.createUserBtn);
        loadUserButton = (Button) findViewById(R.id.loadUserBtn);
        fileContent = (TextView) findViewById(R.id.selectUserOutput);
        usernameInput = (EditText) findViewById(R.id.usernameInput);
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
        //setting onClick behavior for createUser button:
        createUserButton.setOnClickListener(new View.OnClickListener(){
            @Override
           public void onClick(View view){
                String string = " ";
                usernameFileName = usernameInput.getText().toString() + ".json";
                AssetManager am = SelectUserActivity.this.getAssets();
                try {
                    InputStream inputStream = getAssets().open(usernameFileName);
                    int size = inputStream.available();
                    byte[] buffer = new byte[size];
                    inputStream.read(buffer);
                    string = new String(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fileContent.setText(string);

                //uploads username.json to Amazon
                File usernameFile = new File(getApplicationContext().getFilesDir(), usernameFileName);
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(usernameFile));
                    writer.append("Example file contents");
                    writer.close();
                } catch (Exception exception) {
                    Log.e("MyAmplifyApp", "Upload failed", exception);
                }
            }


        });
        //setting onClick behavior for loadUser button:
        loadUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameFileName = usernameInput.getText().toString() + ".json";
                //Checks if username.json exists, if it does, deletes to update
                File user = new File(getApplicationContext().getFilesDir(), usernameFileName);
                if (user.exists()) {
                    if (user.delete()) {
                        System.out.println("Deleted the file: " + usernameFileName);
                    } else {
                        Toast.makeText(getApplicationContext(), usernameFileName + " does not exist", Toast.LENGTH_SHORT).show();
                    }
                }
                //Downloads user data
                Amplify.Storage.downloadFile(
                        usernameFileName,
                        new File(getApplicationContext().getFilesDir() + usernameFileName),
                        result -> Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getName()),
                        error -> Log.e("MyAmplifyApp", "Download Failure", error)
                );

                //Wait for file to download
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    FileReader fileReader = new FileReader(user);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = bufferedReader.readLine();
                    while (line != null) {
                        stringBuilder.append(line).append("\n");
                        line = bufferedReader.readLine();
                    }
                    bufferedReader.close();
                    System.out.println(stringBuilder);
                    JSONObject userData = new JSONObject(stringBuilder.toString());

                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }

                String string = " ";
                String usernameFileName = usernameInput.getText().toString() + ".json";
                AssetManager am = SelectUserActivity.this.getAssets();
                try {
                    InputStream inputStream = getAssets().open(usernameFileName);
                    int size = inputStream.available();
                    byte[] buffer = new byte[size];
                    inputStream.read(buffer);
                    string = new String(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fileContent.setText(string);
            }
        });
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
