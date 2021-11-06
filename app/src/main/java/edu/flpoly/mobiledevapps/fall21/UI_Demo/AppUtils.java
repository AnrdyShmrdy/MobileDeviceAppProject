package edu.flpoly.mobiledevapps.fall21.UI_Demo;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

public class AppUtils {
    public static void goToActivity(android.content.Context currentActivity, Class<?> cls){
        Intent intent = new Intent(currentActivity, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        currentActivity.startActivity(intent);
    }
    public static void determineActivityToGoTo(android.content.Context currentActivity, MenuItem menuItem) {
        //Get the name of the current activity: (NEEDS TO BE FIXED)
        String currentActivityName = currentActivity.getClass().getSimpleName(); //CURRENTLY NOT FUNCTIONING CORRECTLY!!!!!
        //Show the name of the current activity for testing:
        Toast.makeText(currentActivity, currentActivityName, Toast.LENGTH_SHORT).show();
        //if menuItem equals the first entry in the popupMenu(main screen)
        if (menuItem.getTitle().equals("Main") && !currentActivityName.equals("MainActivity")) {
            Toast.makeText(currentActivity, "You clicked to go to the main screen", Toast.LENGTH_SHORT).show();
            goToActivity(currentActivity, SelectPlanActivity.class);
        }
        //if menuItem equals the second entry in the popupMenu (plan screen):
        else if (menuItem.getTitle().equals("Select Plan") && !currentActivityName.equals("SelectPlanActivity")) {
            Toast.makeText(currentActivity, "You clicked to go to the select plan screen", Toast.LENGTH_SHORT).show();
            goToActivity(currentActivity, SelectPlanActivity.class);
        }
        //if menuItem equals the third entry in the popupMenu (leaderboard screen)
        else if (menuItem.getTitle().equals("Leaderboard") && !currentActivityName.equals("LeaderboardActivity")) {
            Toast.makeText(currentActivity, "You clicked to go to the leaderboard screen", Toast.LENGTH_SHORT).show();
            goToActivity(currentActivity, LeaderboardActivity.class);
        }
        //if menuItem is equal to the current activity:
        else {
            // Toast message on menu item clicked
            Toast.makeText(currentActivity, "You Clicked " + menuItem.getTitle() + " but you're already in " + currentActivityName, Toast.LENGTH_SHORT).show();
        }
    }
}
