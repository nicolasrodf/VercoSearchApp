package com.example.nicolas.vercoapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolas.vercoapp.model.Product;
import com.example.nicolas.vercoapp.adapters.ProductAdapter;
import com.example.nicolas.vercoapp.R;
import com.example.nicolas.vercoapp.service.ProductService;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


public class SearchProductActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private FirebaseAnalytics mFirebaseAnalytics;

    public static final int COLUMNS_NUMBER = 2;
    SearchView searchView;
    AlertDialog alertDialog;
    String queryEntered="";
    TextView selectedTypeTextView;
    LinearLayout filtersLinearLayout;
    RecyclerView productRecyclerView;
    TextView emptyView;
    boolean queryFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fabric.with(this, new Crashlytics()); //iniciar charshlytics
        FirebaseCrash.log("Activity created");

        //forzar crash
//        Button crashButton = new Button(this);
//        crashButton.setText("Crash!");
//        crashButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Crashlytics.getInstance().crash(); // Force a crash
//            }
//        });
//        addContentView(crashButton,
//                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT));
        //

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        productRecyclerView = (RecyclerView) findViewById(R.id.productsRecyclerView);
        emptyView = (TextView) findViewById(R.id.empty_view);
        final CharSequence[] values = {"Hombre","Mujer","No filtrar"};

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
//        searchView.setFocusable(true);
//        searchView.setIconified(false);
//        searchView.requestFocusFromTouch();

        selectedTypeTextView = (TextView) findViewById(R.id.selectedTypeTextView);
        filtersLinearLayout = (LinearLayout) findViewById(R.id.filtersLinearLayout);
        filtersLinearLayout.setVisibility(View.INVISIBLE);

        Button filterButton = (Button) findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(SearchProductActivity.this);
                builder.setTitle("Filtrar resultados");
                builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //Filtro de acuerdo al género
                        ProductService productService = new ProductService();
                        String filterType=productService.getNameFilterType(item);
                        selectedTypeTextView.setText(filterType.toUpperCase());
                        doQuery(queryEntered, filterType);
                        dialog.cancel();
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        queryEntered = query;
        doQuery(queryEntered, "mostrar todo");

        //activar analytics para chequear estadisticas de boton
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "button query");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "queryButton");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        //simula busqueda para mostrar items de recyclerview sin buscar en database
        if (query.equals("zapatillas") || query.equals("camisetas") || query.equals("pantalones")
                ||query.equals("nike") || query.equals("puma") || query.equals("reebok") || query.equals("running")) {
            filtersLinearLayout.setVisibility(View.VISIBLE);
            selectedTypeTextView.setText("MOSTRAR TODOS");
            emptyView.setVisibility(View.INVISIBLE);
            productRecyclerView.setVisibility(View.VISIBLE);
        } else{
            productRecyclerView.setVisibility(View.INVISIBLE);
            filtersLinearLayout.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService((INPUT_METHOD_SERVICE));
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        return false;


    }

    @Override
    public boolean onQueryTextChange(final String newText) {

        if(TextUtils.isEmpty(newText)){
            productRecyclerView.setVisibility(View.INVISIBLE);
            filtersLinearLayout.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.INVISIBLE);
        }
        return false;
    }

    private void doQuery(String queryEntered, String filterSelected){

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, COLUMNS_NUMBER);
        productRecyclerView.setLayoutManager(gridLayoutManager);
        final ArrayList<Product> productList = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Product");
        query.whereContains("labels", queryEntered);
        if(!"mostrar todo".equalsIgnoreCase(filterSelected)) {
            query.whereEqualTo("sex", filterSelected);
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {

                        queryFound = true;

                        for (final ParseObject product : objects) {
                            final String trademark = product.getString("trademark");
                            final String model = product.getString("model");
                            final Double price = product.getDouble("price");
                            final Double discount = product.getDouble("discount");
                            final String sex = product.getString("sex");
                            final String type = product.getString("type");
                            final String labels = product.getString("labels");
                            //**********get bitmap from parse file
                            ParseFile photo = product.getParseFile("image");
                            if (photo == null) {
                                return;
                            }
                            photo.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null) {
                                        if (data.length == 0) {
                                            return;
                                        }
                                        Bitmap photoBmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        productList.add(new Product(trademark, model, price, discount, sex, type, labels, photoBmp));
                                        ProductAdapter productAdapter = new ProductAdapter(getApplicationContext(), productList);
                                        productRecyclerView.setAdapter(productAdapter);
                                        productAdapter.setOnProductClickListener(new ProductAdapter.OnProductClickListener() {
                                            @Override
                                            public void onProductList(Product product) {
                                                Intent intent = new Intent(SearchProductActivity.this, ProductDetailActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                }
                            });
                        }

                    } else {
                        Toast.makeText(SearchProductActivity.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Crashlytics.logException(e);
                        queryFound = false;


                    }
                } else {
                    Toast.makeText(SearchProductActivity.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

}
