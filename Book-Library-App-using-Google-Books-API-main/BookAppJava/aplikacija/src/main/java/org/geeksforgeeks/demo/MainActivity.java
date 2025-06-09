package org.geeksforgeeks.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private RequestQueue mRequestQueue;
    private ArrayList<BookInfo> bookInfoArrayList;
    private ProgressBar progressBar;
    private EditText searchEdt;
    private ImageButton searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        progressBar = findViewById(R.id.progressBar);
        searchEdt = findViewById(R.id.searchEditText);
        searchBtn = findViewById(R.id.searchButton);


        searchBtn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String query = searchEdt.getText().toString();
            if (query.isEmpty()) {
                searchEdt.setError("Please enter search query");
                return;
            }
            getBooksInfo(query);
        });
    }


    private void getBooksInfo(String query) {
        bookInfoArrayList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.getCache().clear();


        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;
        RequestQueue queue = Volley.newRequestQueue(this);


        JsonObjectRequest booksObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONArray itemsArray = response.getJSONArray("items");
                        for (int i = 0; i < itemsArray.length(); i++) {
                            JSONObject itemsObj = itemsArray.getJSONObject(i);
                            JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");


                            String title = volumeObj.optString("title", "N/A");
                            String subtitle = volumeObj.optString("subtitle", "N/A");
                            JSONArray authorsArray = volumeObj.optJSONArray("authors");
                            String publisher = volumeObj.optString("publisher", "N/A");
                            String publishedDate = volumeObj.optString("publishedDate", "N/A");
                            String description = volumeObj.optString("description", "N/A");
                            int pageCount = volumeObj.optInt("pageCount", 0);


                            JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                            String thumbnail = (imageLinks != null) ? imageLinks.optString("thumbnail", "") : "";


                            String previewLink = volumeObj.optString("previewLink", "");
                            String infoLink = volumeObj.optString("infoLink", "");


                            JSONObject saleInfoObj = itemsObj.optJSONObject("saleInfo");
                            String buyLink = (saleInfoObj != null) ? saleInfoObj.optString("buyLink", "") : "";


                            ArrayList<String> authorsArrayList = new ArrayList<>();
                            if (authorsArray != null) {
                                for (int j = 0; j < authorsArray.length(); j++) {
                                    authorsArrayList.add(authorsArray.optString(j, "Unknown"));
                                }
                            }


                            BookInfo bookInfo = new BookInfo(
                                    title, subtitle, authorsArrayList, publisher, publishedDate,
                                    description, pageCount, thumbnail, previewLink, infoLink, buyLink
                            );


                            bookInfoArrayList.add(bookInfo);
                        }


                        RecyclerView recyclerView = findViewById(R.id.rv);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                        BookAdapter adapter = new BookAdapter(bookInfoArrayList, this);
                        recyclerView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "No Data Found: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {

                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });


        queue.add(booksObjRequest);
    }
}