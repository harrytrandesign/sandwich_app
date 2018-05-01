package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    // Set TAGS for Logging
    public static final String LOG_TAG_DATA = "data";
    public static final String LOG_TAG_SAND = "sandwich";
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @BindView(R.id.image_iv) ImageView ingredientsIv;
    @BindView(R.id.origin_tv) TextView tvOrigin;
    @BindView(R.id.also_known_tv) TextView tvAlias;
    @BindView(R.id.ingredients_tv) TextView tvIngredients;
    @BindView(R.id.description_tv) TextView tvDescription;

    // Match IDs to the view
    // No longer using this method, switching to Butterknife library
    private void setupLayout() {
        ingredientsIv = findViewById(R.id.image_iv);
        tvOrigin = findViewById(R.id.origin_tv);
        tvAlias = findViewById(R.id.also_known_tv);
        tvIngredients = findViewById(R.id.ingredients_tv);
        tvDescription = findViewById(R.id.description_tv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Testing out Butterknife bindview
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null) {
            Log.i(LOG_TAG_DATA, "Intent was null");
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            Log.i(LOG_TAG_DATA, "Couldn't get a position");
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);

        // Log the sandwich data
        Log.i(LOG_TAG_SAND, sandwich.getMainName());
        Log.i(LOG_TAG_DATA, json);
        Log.i(LOG_TAG_DATA, "The position of the sandwich " + position);

        if (sandwich == null) {
            // Sandwich data unavailable
            Log.i(LOG_TAG_DATA, "There was no sandwich data");
            closeOnError();
            return;
        }

        populateUI(sandwich);

        // Used placeholder and error methods per suggestion from review
        Picasso.with(this)
                .load(sandwich.getImage())
                .placeholder(R.drawable.allblackhorizontal)
                .error(R.drawable.allblackhorizontal)
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    // Populate the layout with information from Sandwich Object
    private void populateUI(Sandwich sandwich) {

        String alsoKnownAsList = TextUtils.join("\n", sandwich.getAlsoKnownAs());;
        String ingredientsList = TextUtils.join("\n", sandwich.getIngredients());

        toggleViewVisibility(tvOrigin, sandwich.getPlaceOfOrigin());
        toggleViewVisibility(tvAlias, alsoKnownAsList);
        toggleViewVisibility(tvIngredients, ingredientsList);
        toggleViewVisibility(tvDescription, sandwich.getDescription());

    }

    // Helper function to populate the textviews
    public void toggleViewVisibility(TextView textView, String text) {

        if (text.length() < 1) {
            textView.setText(this.getResources().getString(R.string.unknown_details_label));
        } else {
            textView.setText(text);
        }

    }
}
