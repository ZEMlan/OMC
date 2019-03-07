package com.example.onemorechapter.controller.fragments;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.onemorechapter.R;
import com.example.onemorechapter.controller.reader.Reader;
import com.example.onemorechapter.model.entities.Book;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;

import androidx.constraintlayout.widget.ConstraintSet;

public class ReadingFragment extends androidx.fragment.app.Fragment implements Reader {
    private final static String CURRENT_BOOK = "current_book";
    private static final String TAG = "BOOK";
    Book currentBook;

    PDFView pdfView;
    private int pageNumber = 0;

    public ReadingFragment() {
        // Required empty public constructor
    }


    public static ReadingFragment newInstance(Book book) {
        ReadingFragment fragment = new ReadingFragment();
        if(book != null){
            Bundle args = new Bundle();
            args.putSerializable(CURRENT_BOOK, book);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentBook = (Book)getArguments().getSerializable(CURRENT_BOOK);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout;
        if(currentBook == null) {
            layout = inflater.inflate(R.layout.fragment_reading_blank, container, false);
        }else {
            layout = inflater.inflate(R.layout.reading_pdf, container, false);

        }
        return layout;
    }

    @Override
    public void read(View view) {
         pdfView.fromFile(new File(currentBook.getPath()))
                 .enableSwipe(true).swipeHorizontal(true)
                 .defaultPage(pageNumber)
                 .onPageChange((page, pageCount) -> pageNumber = page)
                 .enableAnnotationRendering(true)
                 .onLoad(Pages -> {
                     PdfDocument.Meta meta = pdfView.getDocumentMeta();
                     Log.e(TAG, "title = " + meta.getTitle());
                     Log.e(TAG, "author = " + meta.getAuthor());
                     Log.e(TAG, "subject = " + meta.getSubject());
                     Log.e(TAG, "keywords = " + meta.getKeywords());
                 })
                .scrollHandle(new DefaultScrollHandle(getContext()))
                .spacing(10) // in dp
                .pageFitPolicy(FitPolicy.BOTH)
                .load();
    }


}

