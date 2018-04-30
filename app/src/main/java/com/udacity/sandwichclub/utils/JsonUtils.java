package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String NAME = "name";
    private static final String MAIN_NAME = "mainName";
    private static final String AKA = "alsoKnownAs";
    private static final String ORIGIN = "placeOfOrigin";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE = "image";
    private static final String INGREDIENTS = "ingredients";

    public static Sandwich parseSandwichJson(String json) {

        Sandwich sandwich = null;
        try {

            JSONObject jsonObject = new JSONObject(json);

            sandwich = new Sandwich(
                    jsonObject.getJSONObject(NAME).getString(MAIN_NAME),
                    getAliases(jsonObject),
                    jsonObject.getString(ORIGIN),
                    jsonObject.getString(DESCRIPTION),
                    jsonObject.getString(IMAGE),
                    getIngredients(jsonObject)
            );

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sandwich;
    }

    // Convert a JSONArray into a List
    private static List<String> convertArrayToList(JSONArray jsonArray) throws JSONException {

        List<String> resultList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            resultList.add(jsonArray.getString(i));
        }

        return resultList;
    }

    // Get a JSONArray and return it as a List for Aliases and Ingredients
    private static List<String> getAliases(JSONObject jsonObjectAliases) throws JSONException {

        JSONArray jsonArray = jsonObjectAliases.getJSONObject(NAME).getJSONArray(AKA);

        return convertArrayToList(jsonArray);

    }

    private static List<String> getIngredients(JSONObject jsonObjectIngredients) throws JSONException {

        JSONArray jsonArray = jsonObjectIngredients.getJSONArray(INGREDIENTS);

        return convertArrayToList(jsonArray);

    }

}
