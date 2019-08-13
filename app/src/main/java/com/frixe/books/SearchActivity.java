package com.frixe.books;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final EditText etTitle = (EditText) findViewById(R.id.et_title);
        final EditText etAuthor = (EditText) findViewById(R.id.et_author);
        final EditText etPublisher = (EditText) findViewById(R.id.et_publisher);
        final EditText etIsbn = (EditText) findViewById(R.id.et_isbn);
        final Button button = (Button) findViewById(R.id.button_search);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String title = etTitle.getText().toString().trim();
                 String author = etAuthor.getText().toString().trim();
                 String publisher = etPublisher.getText().toString().trim();
                 String isbn = etIsbn.getText().toString().trim();
                 if(title.isEmpty() && author.isEmpty() && publisher.isEmpty() && isbn.isEmpty()){
                     String message = getString(R.string.no_search_data);
                     Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                 }
                 else{
                     URL queryURL = ApiUtil.buildUrl(title, author, publisher, isbn);
                     //sharedpreerence]
                     Context context = getApplicationContext();
                     int position = spUtil.getPreferenceInt(context, spUtil.POSITION);
                     if(position == 0 || position == 5)
                         position = 1;
                     else
                         position++;
                     String key = spUtil.QUERY + String.valueOf(position);
                     String value = title + "," + author + "," + publisher + "," + isbn;
                     spUtil.setPreferenceString(context, key, value);
                     spUtil.setPreferenceInt(context, spUtil.POSITION, position);
                             Intent intent = new Intent(getApplicationContext(), BookListActivity.class);
                     intent.putExtra("Query", queryURL.toString());
                     startActivity(intent);
                 }
            }
        });
    }
}
