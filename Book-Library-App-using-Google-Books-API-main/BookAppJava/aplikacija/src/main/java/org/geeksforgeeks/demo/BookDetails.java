package org.geeksforgeeks.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class BookDetails extends AppCompatActivity {


    private String title, subtitle, publisher, publishedDate, description, thumbnail, previewLink, infoLink, buyLink;
    private int pageCount;


    private TextView titleTV, subtitleTV, publisherTV, descTV, pageTV, publishDateTV;
    private Button previewBtn, buyBtn;
    private ImageView bookIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        titleTV = findViewById(R.id.bookTitle);
        subtitleTV = findViewById(R.id.bookSubTitle);
        publisherTV = findViewById(R.id.publisher);
        descTV = findViewById(R.id.bookDescription);
        pageTV = findViewById(R.id.pageCount);
        publishDateTV = findViewById(R.id.publishedDate);
        previewBtn = findViewById(R.id.idBtnPreview);
        buyBtn = findViewById(R.id.idBtnBuy);
        bookIV = findViewById(R.id.bookImage);


        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        subtitle = intent.getStringExtra("subtitle");
        publisher = intent.getStringExtra("publisher");
        publishedDate = intent.getStringExtra("publishedDate");
        description = intent.getStringExtra("description");
        pageCount = intent.getIntExtra("pageCount", 0);
        thumbnail = intent.getStringExtra("thumbnail");
        previewLink = intent.getStringExtra("previewLink");
        infoLink = intent.getStringExtra("infoLink");
        buyLink = intent.getStringExtra("buyLink");

        titleTV.setText(title);
        subtitleTV.setText(subtitle);
        publisherTV.setText(publisher);
        publishDateTV.setText("Published On : " + publishedDate);
        descTV.setText(description);
        pageTV.setText("Pages : " + pageCount);
        Glide.with(this).load(thumbnail).into(bookIV);


        previewBtn.setOnClickListener(v -> {
            if (previewLink == null || previewLink.isEmpty()) {
                Toast.makeText(BookDetails.this, "No preview link present", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(previewLink));
            startActivity(i);
        });


        buyBtn.setOnClickListener(v -> {
            if (buyLink == null || buyLink.isEmpty()) {
                Toast.makeText(BookDetails.this, "No buy page present for this book", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(buyLink));
            startActivity(i);
        });
    }
}