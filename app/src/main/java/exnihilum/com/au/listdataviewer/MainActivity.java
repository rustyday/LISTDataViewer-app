package exnihilum.com.au.listdataviewer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import Utilities.ParametersHelper;


/**
 * Created by david on 28/07/2017.
 *
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private boolean userIsInteracting = false;
    private HashMap<String, String> categoryMap;
    ArrayList<LayerType> layers = ParametersHelper.layerTypes();
    Set<String> categories = ParametersHelper.getCategorySet();
    ArrayList<String> layerLabels = new ArrayList<>();
    ArrayAdapter<String> detailAdapter;
    TextView goButton;
    HashMap<String, String> geologyLayerMap;
    HashMap<String, String> cOLLayerMap;
    HashMap<String, String> hCCLayerMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // make maps
        geologyLayerMap = ParametersHelper.makeGeologyLayerMap();
        categoryMap = ParametersHelper.makeCategoryMap();
        cOLLayerMap = ParametersHelper.makeCOLLayerMap();
        hCCLayerMap = ParametersHelper.makeHCCLayerMap();

        Spinner spinnerDetail = (Spinner) findViewById(R.id.layers_spinner);
        Spinner spinnerCategory = (Spinner) findViewById(R.id.category_spinner);
        // Create ArrayAdapters using the string array and a default spinner layout
        detailAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, layerLabels);
        // have to covert set to list for use in array adapter
        List<String> categoryList = Arrays.asList(categories.toArray(new String[0]));
        java.util.Collections.sort(categoryList);
        ArrayAdapter<String> categoryAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        List<String> list = Arrays.asList(ParametersHelper.getGeologyLayers().toArray(new String[0]));
        java.util.Collections.sort(list);

        // Specify the layout to use when the list of choices appears
        detailAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerDetail.setAdapter(detailAdapter);
        spinnerCategory.setAdapter(categoryAdapter);
        spinnerDetail.setOnItemSelectedListener(this);
        spinnerCategory.setOnItemSelectedListener(this);

        // set initial values for detail
        String category = spinnerCategory.getSelectedItem().toString();
        for (LayerType type : layers) {
            String combined = type.getServer() + type.getClassification();
            if (combined.equals(categoryMap.get(category))) {
                layerLabels.add(type.getLayerName());
            }
        }

        goButton = (TextView) findViewById(R.id.go);
        goButton.setAlpha((float) 0.4);
        goButton.setOnClickListener(view -> {
                    if (isNetworkAvailable()) {
                        String item = spinnerDetail.getSelectedItem().toString();
                        for (LayerType type : layers) {
                            if (type.isNameEqualTo(item)) {
                                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                switch (type.getServer()) {
                                    case "COL":
                                        intent.putExtra("layerName", cOLLayerMap.get(item));
                                        intent.putExtra("server", "COL");
                                        break;
                                    case "MRT":
                                        intent.putExtra("layerName", geologyLayerMap.get(item));
                                        intent.putExtra("server", "MRT");
                                        break;
                                    case "HCC":
                                        intent.putExtra("layerName", item);
                                        intent.putExtra("mapValue", hCCLayerMap.get(item));
                                        intent.putExtra("server", "HCC");
                                        break;
                                    default:
                                        intent.putExtra("layerName", type.getLayerName());
                                        intent.putExtra("server", "LIST");
                                        break;
                                }
                                startActivity(intent);
                            }
                        }
                    } else {
                        Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.category_spinner) {
            String item = (String) parent.getItemAtPosition(pos);
            layerLabels.clear();
            for (LayerType type : layers) {
                String combined = type.getServer() + type.getClassification();
                if (combined.equals(categoryMap.get(item))) {
                    layerLabels.add(type.getLayerName());
                }
            }
            detailAdapter.notifyDataSetChanged();
        } else if (spinner.getId() == R.id.layers_spinner) {
            goButton.setAlpha(1);
        } else if (!isNetworkAvailable()) {
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // don't do anything
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar, menu);
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                PorterDuffColorFilter porterDuffColorFilter
                        = new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                drawable.setColorFilter(porterDuffColorFilter);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                showNavigationConfirmationDialog();
                return true;

            case R.id.action_about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void showNavigationConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        SharedPreferences.Editor prefEditor =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.use_location_heading);
        builder.setPositiveButton(R.string.yes, (dialog, id) -> {
            // User clicked Yes, so use navigation
            prefEditor.putBoolean("canNavigate", true);
            prefEditor.apply();
        });
        builder.setNegativeButton(R.string.no, (dialog, id) -> {
            // User clicked No, so don't use navigation
            prefEditor.putBoolean("canNavigate", false);
            prefEditor.apply();
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
