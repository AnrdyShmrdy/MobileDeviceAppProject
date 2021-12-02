package edu.flpoly.mobiledevapps.fall21.UI_Demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class LeaderboardActivity extends AppCompatActivity{
    TextView txtLeaderBoard;
    Button menu_button_leaderboard;
    PopupMenu popupMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);
        menu_button_leaderboard = (Button) findViewById(R.id.menu_button_leaderboard);
        LinearLayout leaderLL= (LinearLayout) findViewById(R.id.leaderBoardLinearLayout);

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
        menu_button_leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initializing the popup menu and giving the reference as current context
                popupMenu = new PopupMenu(getApplicationContext(), menu_button_leaderboard);

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

        //uploads trigger.txt to Amazon
        File trigger = new File(getApplicationContext().getFilesDir(), "trigger.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(trigger));
            writer.append("Example file contents");
            writer.close();
        } catch (Exception exception) {
            Log.e("MyAmplifyApp", "Upload failed", exception);
        }
        //Checks is leaderboard exists, if it does, deletes to update
        File leaderboard = new File(getApplicationContext().getFilesDir(), "leaderboard.json");
        if(leaderboard.exists()){
            if (leaderboard.delete()) {
                System.out.println("Deleted the file: leaderboard.json");
            } else {
                System.out.println("leaderboard.json does not exist");
            }
        }
        //Downloads leaderboard

        boolean isDownloaded = false;
        Amplify.Storage.downloadFile(
                "leaderboard.json",
                new File(getApplicationContext().getFilesDir() + "/leaderboard.json"),
                result -> Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getName()),
                error -> Log.e("MyAmplifyApp",  "Download Failure", error)
        );

        //Wait for file to download
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            FileReader fileReader = new FileReader(leaderboard);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            System.out.println(stringBuilder);
            JSONArray leaderBoardData = new JSONArray(stringBuilder.toString());

            for(int i = leaderBoardData.length()-1; i >= 0; i--){
                TextView leaderBoardTxt = new TextView(this);
                View leaderBoardDivider = new View(this);
                leaderBoardDivider.setBackgroundColor(Color.BLACK);
                leaderBoardDivider.setMinimumHeight(10);
                leaderBoardDivider.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                leaderBoardTxt.setText(
                        leaderBoardData.length()-i + ". "
                        + leaderBoardData.getJSONObject(i).getString("username")
                        + " ("
                        + leaderBoardData.getJSONObject(i).getString("exp")
                        + "XP)" + "\n");
                leaderBoardTxt.setTextSize(24);
                leaderBoardTxt.setGravity(Gravity.TOP);
                leaderBoardTxt.setBackgroundColor(Color.parseColor("#902196F3"));
                leaderBoardTxt.setTextColor(Color.BLACK);
                leaderBoardTxt.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                leaderLL.addView(leaderBoardDivider);
                leaderLL.addView(leaderBoardTxt);
            }


        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

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
        //if menuItem is equal to the current activity:
        else {
            // Toast message on menu item clicked
            Toast.makeText(getApplicationContext(), "You Clicked " + menuItem.getTitle() + " but you're already in " + currentActivity, Toast.LENGTH_SHORT).show();
        }
    }

}

