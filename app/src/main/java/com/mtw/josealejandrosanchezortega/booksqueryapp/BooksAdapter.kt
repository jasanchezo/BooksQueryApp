package com.mtw.josealejandrosanchezortega.booksqueryapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.mtw.josealejandrosanchezortega.booksqueryapp.api.BooksModel
import com.mtw.josealejandrosanchezortega.booksqueryapp.api.BooksModel.BookItem
import kotlinx.android.synthetic.main.book_card_item.view.*

class BooksAdapter(booksList: List<BookItem>, listener: OnItemClickListener) : RecyclerView.Adapter<BooksAdapter.BooksViewHolder>() {

    private var listBookItems: List<BooksModel.BookItem> = booksList

    private var listenerBook: OnItemClickListener = listener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        return BooksViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.book_card_item, parent, false))
    }


    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        var current: BooksModel.BookItem = listBookItems[position]

        holder.bind(current, listenerBook)
    }

    override fun getItemCount() = listBookItems.size

    interface OnItemClickListener {
        fun onItemClick(bookItem: BooksModel.BookItem)
    }

    fun setBooks(bookItems:List<BooksModel.BookItem>){
        listBookItems = bookItems
        notifyDataSetChanged()
    }

    class BooksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO 11
        fun bind(bookItem: BooksModel.BookItem, listener: OnItemClickListener) {
            itemView.tvTitle.text = bookItem.volumeInfo.title
            if (bookItem.volumeInfo?.authors != null) {
                if (bookItem.volumeInfo.authors.isNotEmpty()) {
                    itemView.tvAuthor.text = bookItem.volumeInfo.authors[0]
                }
            }
            if (bookItem.volumeInfo?.imageLinks?.smallThumbnail != null) {
                Glide.with(itemView.context).load(bookItem.volumeInfo.imageLinks.smallThumbnail).into(itemView.imgCover)
            }

            itemView.setOnClickListener {
                listener.onItemClick(bookItem)
            }
        }

    }
}