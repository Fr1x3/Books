package com.frixe.books;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class BookListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ProgressBar mLoadingProgressBar;
    private RecyclerView mRvBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_book);
        mLoadingProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
        mRvBooks = (RecyclerView) findViewById(R.id.rv_books_list);
        LinearLayoutManager bookLayoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false);
        mRvBooks.setLayoutManager(bookLayoutManager);
        Intent intent = getIntent();
        String query = intent.getStringExtra("Query");
        URL bookUrl;
        try{
            if(query==null || query.isEmpty())
                bookUrl = ApiUtil.buildUrl("cooking");
            else
                bookUrl =  new URL(query);

            new BooksQueryTask().execute(bookUrl);

        }
        catch (Exception e){
            Log.d("Error", e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        final MenuItem searchItem = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        ArrayList<String> recentList = spUtil.getQueryList(getApplicationContext());
        int itemNumber = recentList.size();
        MenuItem recentMenu;
        for(int i = 0; i < itemNumber; i++){
            recentMenu = menu.add(Menu.NONE, i, Menu.NONE, recentList.get(i));
        }
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_advanced_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                int position = item.getItemId() + 1;
                String preferenceName =spUtil.QUERY + String.valueOf(position);
                String query = spUtil.getPrefenceString(getApplicationContext(), preferenceName);
                String[] prefParams = query.split("\\,");
                String[] queryParams = new String[4];
                for(int i = 0; i < prefParams.length; i++){
                    queryParams[i] = prefParams[i];
                }
                URL bookUrl = ApiUtil.buildUrl(
                        (queryParams[0] == null)?"":queryParams[0],
                        (queryParams[1] == null)?"":queryParams[1],
                        (queryParams[2] == null)?"":queryParams[2],
                        (queryParams[3] == null)?"":queryParams[3]
                );
                new BooksQueryTask().execute(bookUrl);
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try{
            URL bookUrl = ApiUtil.buildUrl(query);
            new BooksQueryTask().execute(bookUrl);
        }catch(Exception e){
            Log.d("Error", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    public class BooksQueryTask extends AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String result = null;
            try{
                result = ApiUtil.getJson(searchUrl);
            }catch (IOException e){
                Log.d("Error" , e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView textError = (TextView) findViewById(R.id.text_error_loading);
            mLoadingProgressBar.setVisibility(View.INVISIBLE);
            if(result == null){
                mRvBooks.setVisibility(View.INVISIBLE);
                textError.setVisibility(View.VISIBLE);
            }
            else{
                mRvBooks.setVisibility(View.VISIBLE);
                textError.setVisibility(View.INVISIBLE);
                ArrayList<Book> bookList = ApiUtil.getBooksFromJson(result);
                BooksAdapter booksAdapter = new BooksAdapter(bookList);
                mRvBooks.setAdapter(booksAdapter);
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgressBar.setVisibility(View.VISIBLE);
        }
    }
}
