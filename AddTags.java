package com.example.SimpleLayout;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 1312548 on 2017/05/17.
 */
public class AddTags extends Activity {
    Button save;
    CheckBox myTagBox, newTagBox;
    LinearLayout myTagsLL, newTagsLL;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtags);
        Context me = this;
        save = (Button)findViewById(R.id.saveChangesButton);
        myTagsLL = (LinearLayout)findViewById(R.id.myTagsLinLayout);
        newTagsLL = (LinearLayout)findViewById(R.id.newTagsLinLayout);

        String user_email = getIntent().getStringExtra("email");
        ContentValues params = new ContentValues();
        params.put("email",user_email);
        ArrayList<String> tags = new ArrayList<>();
        AsyncHTTPRequest AsyncGetMyTags = new AsyncHTTPRequest(
                "http://lamp.ms.wits.ac.za/~s1312548/get_my_tags.php",params) {
            @Override
            protected void onPostExecute(String output) {
                try{
                    JSONArray myTags = new JSONArray(output);
                    if(myTags.length()==0){
                        TextView noTags = new TextView(AddTags.this);
                        noTags.setText("Seems like you have not selected any tags");
                        myTagsLL.addView(noTags);
                    }else{
                        for (int i = 0; i < myTags.length(); i++){
                            JSONObject item=myTags.getJSONObject(i);
                            String tag_name = item.getString("CATEGORY_NAME");
                            myTagBox = new CheckBox(AddTags.this);
                            myTagBox.setText(tag_name);
                            myTagBox.setChecked(true);
                            myTagsLL.addView(myTagBox);
                            tags.add(tag_name);
                        }
                    }
                }catch (Exception e){}

            }
        };
        AsyncGetMyTags.execute();

        AsyncHTTPRequest AsyncGetOtherTags = new AsyncHTTPRequest(
                "http://lamp.ms.wits.ac.za/~s1312548/get_other_tags.php",params) {
            @Override
            protected void onPostExecute(String output) {
                try{
                    JSONArray otherTags = new JSONArray(output);
                    if(otherTags.length()==0){
                        TextView noTags = new TextView(AddTags.this);
                        noTags.setText("No new tags available");
                        newTagsLL.addView(noTags);
                    }else{
                        for (int i = 0; i < otherTags.length(); i++){
                            JSONObject item=otherTags.getJSONObject(i);
                            String tag_name = item.getString("CATEGORY_NAME");
                            if (!tags.contains(tag_name))
                            {
                                newTagBox = new CheckBox(AddTags.this);
                                newTagBox.setText(tag_name);
                                newTagBox.setChecked(false);
                                newTagsLL.addView(newTagBox);
                            }

                        }
                    }
                }catch (Exception e){}

            }
        };
        AsyncGetOtherTags.execute();




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < newTagsLL.getChildCount();i++){
                    CheckBox box = (CheckBox)newTagsLL.getChildAt(i);
                    if(box.isChecked()){
                        String tagName = box.getText().toString();
                        ContentValues params = new ContentValues();
                        params.put("email",user_email);
                        params.put("tag",tagName);
                        AsyncHTTPRequest AsyncAddTag = new AsyncHTTPRequest(
                                "http://lamp.ms.wits.ac.za/~s1312548/add_tags.php",params) {
                            @Override
                            protected void onPostExecute(String output) {
                                Log.d("TAGS",output);
                            }
                        };
                        AsyncAddTag.execute();

                    }
                }
                finish();
            }
        });

    }
}
