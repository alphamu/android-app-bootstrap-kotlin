package com.alimuzaffar.blank.net.mock;

import android.widget.Toast;
import com.alimuzaffar.blank.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MockHelper {

    public static String getJsonStringFromAssets(String filePath) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(App.Companion.getContext().getAssets().open(filePath), "UTF-8"));

            // do reading, usually loop until end of file reading
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                //process line
                sb.append(line);
            }
            if (sb.length() > 0) {
                return sb.toString();
            } else {
                return "{}";
            }
        } catch (IOException e) {
            Toast.makeText(App.Companion.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return "{}";
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }
}
