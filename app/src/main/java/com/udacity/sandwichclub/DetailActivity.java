package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    ImageView ingredientsIv;
    TextView tvOrigin;
    TextView tvAlias;
    TextView tvIngredients;
    TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setupLayout();

        Intent intent = getIntent();
        if (intent == null) {
            Log.i("data", "Intent was null");
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            Log.i("data", "Couldn't get a position");
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);

        Log.i("sandwich", sandwich.getMainName());
        // Log the sandwich data
        Log.i("data", json);
        Log.i("data", "The position of the sandwich " + position);

        if (sandwich == null) {
            // Sandwich data unavailable
            Log.i("data", "There was no sandwich data");
//            closeOnError();
            return;
        }

        populateUI(sandwich);

        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    // Match IDs to the view
    private void setupLayout() {
        ingredientsIv = findViewById(R.id.image_iv);
        tvOrigin = findViewById(R.id.origin_tv);
        tvAlias = findViewById(R.id.also_known_tv);
        tvIngredients = findViewById(R.id.ingredients_tv);
        tvDescription = findViewById(R.id.description_tv);
    }

    // Populate the layout with information from Sandwich Object
    private void populateUI(Sandwich sandwich) {

        // Use a StringBuilder to append a list together for aka and ingredients
        StringBuilder aliasBuilder = new StringBuilder();
        String alsoKnownAsList = "";
        String ingredientsList = "";

        int counterOne = 0; // So last iteration doesn't have a new line break;
        for (String aka : sandwich.getAlsoKnownAs()) {
            if (counterOne++ == sandwich.getAlsoKnownAs().size() - 1) {
                aliasBuilder.append(aka);
            } else {
                aliasBuilder.append(aka).append("\n");
            }
        }

        alsoKnownAsList = aliasBuilder.toString();

        StringBuilder ingredientsBuilder = new StringBuilder();

        int counterTwo = 0; // So last iteration doesn't have a new line break;
        for (String ingredients : sandwich.getIngredients()) {
            if (counterTwo++ == sandwich.getIngredients().size() - 1) {
                ingredientsBuilder.append(ingredients);
            } else {
                ingredientsBuilder.append(ingredients).append("\n");
            }
        }

        ingredientsList = ingredientsBuilder.toString();

        tvOrigin.setText(sandwich.getPlaceOfOrigin());
        tvAlias.setText(alsoKnownAsList);
        tvIngredients.setText(ingredientsList);
        tvDescription.setText(sandwich.getDescription());

    }
}
