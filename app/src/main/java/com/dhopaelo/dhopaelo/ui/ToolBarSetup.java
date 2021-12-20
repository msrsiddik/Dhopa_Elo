package com.dhopaelo.dhopaelo.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.dhopaelo.dhopaelo.R;

/**
 *
 * <p>setHasOptionMenu(true)</p></br>
 * <h3>withBackButton is true</h3>
 * <p>please override <br>
 * public boolean onOptionsItemSelected(@NonNull MenuItem item) { <br>
 *         int id = item.getItemId();<br>
 *
 *         if (id == android.R.id.home) {<br>
 *             getActivity().onBackPressed();  return true;<br>
 *         }<br>
 *
 *         return super.onOptionsItemSelected(item);<br><br>
 *     }
 */

public class ToolBarSetup {

    public static void setTitle(FragmentActivity activity, String title, boolean withBackButton) {
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        actionBar.setTitle(title);
        if (withBackButton) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.arrow_back_ic);
        } else {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

}
