package com.ribasapps.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ListReposTask extends AsyncTask<String,Void,String> {

    private String resultStr="";
    //private JSONObject jsonObj=new JSONObject();
    private JSONArray jsonArray = new JSONArray();

    public ArrayList<String> myArray = new ArrayList<>();

    public ListReposTask() {
        //parentArray.add("teste1");
    }

    private void parseResult(String result) throws JSONException, ParseException
    {
        JSONParser parser = new JSONParser();
        jsonArray = (JSONArray) parser.parse(result);
        Log.d("TestApp", "jsonObj:");//+jsonObj.toString());

        for (int i=0; i<10 /*jsonArray.size()*/; i++) {
            JSONObject json = (JSONObject) jsonArray.get(i);
            String strUrl = (String) json.get("html_url");
            myArray.add(strUrl);
            //JSONObject langs = (JSONObject) json.get("languages");
        }
    }

    @Override
    protected String doInBackground(String... strings)
    {
        //adding all 'kotlin' repos to arraylist
        //request used https://api.github.com/repositories

        Log.d("TestApp", "addRepos");
        URL aUrl = null;
        try {
            aUrl = new URL("https://api.github.com/repositories");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) aUrl.openConnection();

            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                resultStr = sb.toString();
                Log.d("TestApp", "resultstr:" + resultStr);
                //return sb.toString();
                try {
                    parseResult(resultStr);
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
