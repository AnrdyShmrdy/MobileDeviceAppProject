package edu.flpoly.mobiledevapps.fall21.UI_Demo;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.amazonaws.mobileconnectors.lambdainvoker.*;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import java.io.FileInputStream;
import java.io.IOException;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity{
    private Button menu_button_main, plan1Button, plan2Button;
    private PopupMenu popupMenu;
    private String plan1FileName = "plan1.txt";
    private String plan2FileName = "plan2.txt";
    private TextView fileContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menu_button_main = (Button) findViewById(R.id.menu_button_main);
        plan1Button = (Button) findViewById(R.id.mainPlan1Button);
        plan2Button = (Button) findViewById(R.id.mainPlan2Button);
        fileContent = (TextView) findViewById(R.id.planOutput);
        // Setting onClick behavior for the menu button
        menu_button_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initializing the popup menu and giving the reference as current context
                popupMenu = new PopupMenu(MainActivity.this, menu_button_main);

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
        plan1Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String string = " ";
                AssetManager am = MainActivity.this.getAssets();
                try {
                    InputStream inputStream = getAssets().open("plan1.txt");
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
        //setting onClick behavior for plan 2 button:
        plan2Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String string = " ";
                AssetManager am = MainActivity.this.getAssets();
                try {
                    InputStream inputStream = getAssets().open("plan2.txt");
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
        //if menuItem is equal to the current activity:
        else {
                // Toast message on menu item clicked
                Toast.makeText(getApplicationContext(), "You Clicked " + menuItem.getTitle() + " but you're already in " + currentActivity, Toast.LENGTH_SHORT).show();
            }
        }
    public void printMessage(String m) {
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }
    private void writeData(String filename) {

        try {
            FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
            String data = "This is a test";
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        printMessage("writing to file " + filename + "completed...");
    }

    private void readData(String filename) {
        try {
            FileInputStream fin = openFileInput(filename);
            int a;
            StringBuilder temp = new StringBuilder();
            while ((a = fin.read()) != -1) {
                temp.append((char) a);
            }

            // setting text from the file.
            fileContent.setText(temp.toString());
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        printMessage("reading to file " + filename + " completed..");
    }
    }