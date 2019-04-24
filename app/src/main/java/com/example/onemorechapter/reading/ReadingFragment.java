package com.example.onemorechapter.reading;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.onemorechapter.R;
import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.model.App;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.kursx.parser.fb2.FictionBook;
import com.shockwave.pdfium.PdfDocument;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

import static com.example.onemorechapter.model.Constants.CURRENT_BOOK;
import static com.example.onemorechapter.model.Constants.CURRENT_PAGE;
import static com.example.onemorechapter.model.Constants.REQUEST_CODE_PICK_FILE;

public class ReadingFragment
        extends MvpFragment<IReadingView, ReadingPresenter>
        implements IReadingView, OnPageErrorListener{

    private Book currentBook;

    private int pageNumber = 0;

    private ImageView blank, loading;
    private PDFView pdfView;
    private TextView txtView;
    private ScrollView scrollView;


    public ReadingFragment() {
        // Required empty public constructor
    }

    @NotNull
    @Override
    public ReadingPresenter createPresenter() {
        return new ReadingPresenter();
    }

    public static ReadingFragment newInstance(DocumentFile book) {
        ReadingFragment fragment = new ReadingFragment();
        if(book != null){
            Bundle args = new Bundle();
            args.putSerializable(CURRENT_BOOK, new Book(book));
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments() != null) {
            currentBook = (Book) getArguments().getSerializable(CURRENT_BOOK);
            App.getInstance().setCurrentBook(DocumentFile.fromSingleUri(getContext(), currentBook.getUriAsUri()));
        }else if(savedInstanceState != null) {
            currentBook = (Book) savedInstanceState.getSerializable(CURRENT_BOOK);
            pageNumber = savedInstanceState.getInt(CURRENT_PAGE);
        }else{
            currentBook = new Book(App.getInstance().getCurrentBook());
        }

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reading, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        blank = view.findViewById(R.id.blank);
        loading = view.findViewById(R.id.loadingView);
        txtView = view.findViewById(R.id.textView);
        scrollView = view.findViewById(R.id.scrollText);
        pdfView = view.findViewById(R.id.pdfView);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
        });

        if(currentBook == null){
            blank.setVisibility(View.VISIBLE);
        }else{
            loading.setVisibility(View.VISIBLE);
            presenter.loadBook(currentBook);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_PAGE, pageNumber);
        outState.putSerializable(CURRENT_BOOK, currentBook);
    }

    @Override
    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
        pdfView.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.INVISIBLE);
        blank.setVisibility(View.INVISIBLE);
    }

    @Override
    public void openPdf() {
        loading.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.INVISIBLE);
        pdfView.setVisibility(View.VISIBLE);
        pdfView.fromUri(currentBook.getUriAsUri())
                .enableSwipe(true)
                .defaultPage(pageNumber)
                .onPageChange((page, pageCount) -> pageNumber = page)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(getContext()))
                .spacing(6) // in dp
                .onPageError(this)
                .load();
    }

    @Override
    public void openTxt(String s) {
        loading.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        txtView.setText(s);
    }

    @Override
    public void openFb2(FictionBook book) {
        loading.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        txtView.setText(book.getBody().getLang());
    }

    @Override
    public void openEpub() {
        blank.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.INVISIBLE);
        //TODO: open .epub
    }


    @Override
    public void onPageError(int page, Throwable t) {
        Log.d("PDF","Cannot load page" + pageNumber);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_CODE_PICK_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Uri bookUri = data.getData();
                        currentBook = new Book(DocumentFile.fromSingleUri(getContext(), bookUri));
                        App.getInstance().setCurrentBook(DocumentFile.fromSingleUri(getContext(), bookUri));
                        getPresenter().loadBook(currentBook);
                    }
                }else
                    break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
