package com.frixe.books;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BooksAdapter extends  RecyclerView.Adapter<BooksAdapter.BookViewHolder>{
    ArrayList<Book> books;
    public BooksAdapter(ArrayList<Book> books){
        this.books = books;
    }
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View viewItem = LayoutInflater.from(context)
                        .inflate(R.layout.row_book_item, parent, false);
        return new BookViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }



    public class BookViewHolder extends RecyclerView.ViewHolder
            implements  View.OnClickListener{

        TextView tvTitle;
        TextView tvAuthor;
        TextView tvPublisher;
        TextView tvPublishedDate;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.text_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.text_author);
            tvPublisher = (TextView) itemView.findViewById(R.id.text_publisher);
            tvPublishedDate = (TextView) itemView.findViewById(R.id.text_publish_date);
            itemView.setOnClickListener(this);
        }

        public void bind(Book book){
            tvTitle.setText(book.title);
            tvAuthor.setText(book.authors);
            tvPublisher.setText(book.publisher);
            tvPublishedDate.setText(book.publishedDate);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Book selectedBook = books.get(position);
            Intent intent = new Intent(view.getContext(), BookDetail.class);
            intent.putExtra("Book", selectedBook);
            view.getContext().startActivity(intent);
        }
    }
}
