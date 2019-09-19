package com.flowrspot.service.impl;

import com.flowrspot.service.QuoteService;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Service Implementation for managing Quotes by https://quotes.rest/.
 */
@Service
@Transactional
public class QuotesRestServiceImpl implements QuoteService {

    @Override
    public String getQuoteOfTheDay() {
        JSONObject object = getQuoteOfTheDayJson();
        if(object!=null){
            try {
                JSONArray quotes = object.getJSONObject("contents").getJSONArray("quotes");
                for(int i=0; i < quotes.length() ; i++) {
                    JSONObject quoteObject = quotes.getJSONObject(i);
                    return (String) quoteObject.get("quote");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    public JSONObject getQuoteOfTheDayJson(){
        URL url;
        try {
            url = new URL("http://quotes.rest/qod.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            StringBuilder sb = new StringBuilder();
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }


            return new JSONObject(sb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
