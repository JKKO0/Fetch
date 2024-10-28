package com.example.a1027practice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<ListItem> listItems;

    private EditText urlEditText;
    private Button displayButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        urlEditText = findViewById(R.id.urlEditText);
        displayButton = findViewById(R.id.displayButton);
        progressBar = findViewById(R.id.progressBar);

        // Optionally set a default URL
        urlEditText.setText("https://fetch-hiring.s3.amazonaws.com/hiring.json");

        // Set up button click listener
        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlString = urlEditText.getText().toString().trim();
                if (!urlString.isEmpty()) {
                    try {
                        // Validate the URL
                        new URL(urlString);
                        // Start the data fetching task
                        new FetchDataTask().execute(urlString);
                    } catch (MalformedURLException e) {
                        Toast.makeText(MainActivity.this, "Invalid URL", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a URL", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class FetchDataTask extends AsyncTask<String, Void, List<Item>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Show the ProgressBar and hide the RecyclerView
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        @Override
        protected List<Item> doInBackground(String... params) {
            String urlString = params[0];
            List<Item> items = new ArrayList<>();

            try {
                // Fetch data from the URL
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    bufferedReader.close();

                    // Parse the JSON data
                    JSONArray jsonArray = new JSONArray(stringBuilder.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        int id = jsonObject.getInt("id");
                        int listId = jsonObject.getInt("listId");
                        String name = jsonObject.optString("name", null);

                        // Include all items, regardless of name validity
                        items.add(new Item(id, listId, name));
                    }

                    // Sort the items by "listId", then by numeric value in "name", then by "id"
                    Collections.sort(items, new Comparator<Item>() {
                        @Override
                        public int compare(Item o1, Item o2) {
                            int listIdCompare = Integer.compare(o1.getListId(), o2.getListId());
                            if (listIdCompare != 0) {
                                return listIdCompare;
                            } else {
                                int nameNum1 = extractNumber(o1.getName());
                                int nameNum2 = extractNumber(o2.getName());
                                int nameCompare = Integer.compare(nameNum1, nameNum2);
                                if (nameCompare != 0) {
                                    return nameCompare;
                                } else {
                                    return Integer.compare(o1.getId(), o2.getId());
                                }
                            }
                        }
                    });

                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("FetchDataTask", "Error fetching data", e);
            }

            return items;
        }

        @Override
        protected void onPostExecute(List<Item> items) {
            super.onPostExecute(items);
            // Hide the ProgressBar and show the RecyclerView
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            // Maps to hold valid and invalid items for each listId
            Map<Integer, List<Item>> validItemsMap = new LinkedHashMap<>();
            Map<Integer, List<Item>> invalidItemsMap = new LinkedHashMap<>();

            for (Item item : items) {
                int listId = item.getListId();
                String name = item.getName();

                // Determine if the item is valid or invalid
                boolean isValid = name != null && !name.trim().isEmpty() && !name.equalsIgnoreCase("null");

                if (isValid) {
                    if (!validItemsMap.containsKey(listId)) {
                        validItemsMap.put(listId, new ArrayList<Item>());
                    }
                    validItemsMap.get(listId).add(item);
                } else {
                    if (!invalidItemsMap.containsKey(listId)) {
                        invalidItemsMap.put(listId, new ArrayList<Item>());
                    }
                    invalidItemsMap.get(listId).add(item);
                }
            }

            // Build the listItems list with headers and items
            listItems = new ArrayList<>();
            for (Integer listId : validItemsMap.keySet()) {
                // Add valid items section
                HeaderItem headerValid = new HeaderItem(listId, true);
                listItems.add(headerValid);
                listItems.addAll(validItemsMap.get(listId));

                // Add invalid items section if there are invalid items for this listId
                if (invalidItemsMap.containsKey(listId)) {
                    HeaderItem headerInvalid = new HeaderItem(listId, false);
                    listItems.add(headerInvalid);
                    listItems.addAll(invalidItemsMap.get(listId));
                }
            }

            // Handle listIds that have only invalid items
            for (Integer listId : invalidItemsMap.keySet()) {
                if (!validItemsMap.containsKey(listId)) {
                    // Add invalid items section
                    HeaderItem headerInvalid = new HeaderItem(listId, false);
                    listItems.add(headerInvalid);
                    listItems.addAll(invalidItemsMap.get(listId));
                }
            }

            // Set up the adapter
            itemAdapter = new ItemAdapter(listItems);
            itemAdapter.setHeaderItemMap(validItemsMap, invalidItemsMap);
            recyclerView.setAdapter(itemAdapter);
        }

        // Helper method to extract number from name
        private int extractNumber(String name) {
            try {
                String[] parts = name.split(" ");
                if (parts.length > 1) {
                    return Integer.parseInt(parts[1]);
                } else {
                    return Integer.MAX_VALUE;
                }
            } catch (Exception e) {
                return Integer.MAX_VALUE;
            }
        }
    }
}
