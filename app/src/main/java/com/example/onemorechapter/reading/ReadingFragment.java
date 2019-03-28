package com.example.onemorechapter.reading;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onemorechapter.R;
import com.example.onemorechapter.database.entities.Book;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.shockwave.pdfium.PdfDocument;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import static com.example.onemorechapter.model.Constants.CURRENT_BOOK;
import static com.example.onemorechapter.model.Constants.CURRENT_PAGE;

public class ReadingFragment
        extends MvpFragment<IReadingView, ReadingPresenter>
        implements IReadingView{
    private static final String TAG = "BOOK";

    private Book currentBook;

    private PDFView pdfView;
    private int pageNumber = 0;

    public ReadingFragment() {
        // Required empty public constructor
    }

    @NotNull
    @Override
    public ReadingPresenter createPresenter() {
        return new ReadingPresenter();
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_PAGE, pageNumber);
        outState.putSerializable(CURRENT_BOOK, currentBook);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments() != null) {
            currentBook = (Book)getArguments().getSerializable(CURRENT_BOOK);
        }else if(savedInstanceState != null) {
            currentBook = (Book) savedInstanceState.getSerializable(CURRENT_BOOK);
            pageNumber = savedInstanceState.getInt(CURRENT_PAGE);
        }

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
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
    public void openPdf() {
        //pdfView = new PDFView(getContext(), );
        pdfView.fromFile(new File(currentBook.path))
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

    @Override
    public void openTxt() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //TODO: open {.txt; .xml; .doc}
    }

    @Override
    public void openFb2() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //TODO: ope .fb2
    }

    @Override
    public void openEpub() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //TODO: open .epub
    }


}

