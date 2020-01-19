package com.wonbin.fad;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.*;
import java.util.*;

public class Database extends AppCompatActivity {

    private static ArrayList<String> readFile()
    {
        ArrayList<String> myData = new ArrayList<>();
        File myExternalFile = new File("C:\\Users\\pedoh\\eclipse-workspace\\test\\src\\test\\Allergy.txt");
        try {
            FileInputStream fis = new FileInputStream(myExternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            while ((strLine = br.readLine()) != null) {
                String[] temp = strLine.split(", ");

                for (String s : temp)	{
                    myData.add(s);
//	            	System.out.println(s);
                }
            }

            br.close();
            in.close();
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return myData;
    }

    private static ArrayList<Triple<String, ArrayList<String>, ArrayList<String>>> getInfo(ArrayList<String> data)	{
        Triple<String, ArrayList<String>, ArrayList<String>> allergyInfo = new Triple<>();
        ArrayList<Triple<String, ArrayList<String>, ArrayList<String>>> totalList = new ArrayList<>();

        String allergyType = null;
        ArrayList<String> synonyms = new ArrayList<>();
        ArrayList<String> hiddenTypes = new ArrayList<>();
        boolean isSynonym = false;
        int i = 0;

        for (String s : data)	{
//			System.out.println(s);

            // allergy type
            if (s.endsWith(":") && s.compareTo("Synonyms:") != 0  && s.compareTo("Hidden Sources:") != 0)	{
                if (i == 0)	{ // for first time around, there is no allergy information to store
                    allergyType = s.replace(":", "").toLowerCase();
                    i++;
                    continue;
                }

                // insert info
                allergyInfo.insert(allergyType,  synonyms,  hiddenTypes);
                totalList.add(allergyInfo);

                // reset
                allergyType = s.replace(":", "").toLowerCase();
                synonyms = new ArrayList<>();
                hiddenTypes = new ArrayList<>();
                allergyInfo = new Triple<>();
                continue;
            }
            else if (s.equals("Synonyms:"))	{
                isSynonym = true;
                continue;
            }
            else if (s.equals("Hidden Sources:"))	{
                isSynonym = false;
                continue;
            }
            else if (s.equals("canned fruits and vegetables"))	{
                // insert info
                allergyInfo.insert(allergyType,  synonyms,  hiddenTypes);
                totalList.add(allergyInfo);
                return totalList;
            }

            if (isSynonym)	{
                synonyms.add(s);
            }
            else	{
                hiddenTypes.add(s);
            }
        }
        return totalList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        // parse shit
        ArrayList<String> data = new ArrayList<>();
        data = readFile();
        ArrayList<Triple<String, ArrayList<String>, ArrayList<String>>> totalData = new ArrayList<>();
        totalData = getInfo(data);

        // initialize database
        try {
            SQLiteDatabase db = this.openOrCreateDatabase("Fad", MODE_PRIVATE, null);

            // make tables
            db.execSQL("CREATE TABLE IF NOT EXISTS Users (uid INTEGER, aType CHAR(20), PRIMARY KEY (uid))");
            db.execSQL("CREATE TABLE IF NOT EXISTS Words (synonym CHAR(20), PRIMARY KEY (synonym))");

            db.execSQL("CREATE TABLE IF NOT EXISTS Egg (synonym CHAR(20), hidden CHAR(20), aType CHAR(20), PRIMARY KEY (aType))");
            db.execSQL("CREATE TABLE IF NOT EXISTS Fish (synonym CHAR(20), hidden CHAR(20), aType CHAR(20), PRIMARY KEY (aType))");
            db.execSQL("CREATE TABLE IF NOT EXISTS Milk (synonym CHAR(20), hidden CHAR(20), aType CHAR(20), PRIMARY KEY (aType))");
            db.execSQL("CREATE TABLE IF NOT EXISTS Mustard (synonym CHAR(20), hidden CHAR(20), aType CHAR(20), PRIMARY KEY (aType))");
            db.execSQL("CREATE TABLE IF NOT EXISTS Peanut (synonym CHAR(20), hidden CHAR(20), aType CHAR(20), PRIMARY KEY (aType))");
            db.execSQL("CREATE TABLE IF NOT EXISTS Sesame(synonym CHAR(20), hidden CHAR(20), aType CHAR(20), PRIMARY KEY (aType))");
            db.execSQL("CREATE TABLE IF NOT EXISTS Soy (synonym CHAR(20), hidden CHAR(20), aType CHAR(20), PRIMARY KEY (aType))");
            db.execSQL("CREATE TABLE IF NOT EXISTS TreeNut (synonym CHAR(20), hidden CHAR(20), aType CHAR(20), PRIMARY KEY (aType))");
            db.execSQL("CREATE TABLE IF NOT EXISTS Wheat (synonym CHAR(20), hidden CHAR(20), aType CHAR(20), PRIMARY KEY (aType))");
            db.execSQL("CREATE TABLE IF NOT EXISTS Gluten (synonym CHAR(20), hidden CHAR(20), aType CHAR(20), PRIMARY KEY (aType))");

            String[] type = {"Egg", "Gluten", "Milk", "Mustard", "Peanut", "Fish", "Sesame", "Soy", "TreeNut", "Wheat", "Sulphite"};

            // insert ingredients into database
            for (int i = 0; i < totalData.size(); i++)  {
                String aType = totalData.get(i).getAllergyType();
                ArrayList<String> synonyms = totalData.get(i).getSynonyms();
                ArrayList<String> hidden = totalData.get(i).getHidden();

                db.execSQL("INSERT INTO Words VALUES (synonyms)");
                db.execSQL("INSERT INTO type[i] VALUES (synonyms, hidden, aType)");

            }
        }
        catch (Exception e) {
            Log.e("error","error");
        }

    }
}
