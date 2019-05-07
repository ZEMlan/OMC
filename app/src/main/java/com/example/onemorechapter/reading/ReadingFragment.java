package com.example.onemorechapter.reading;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.onemorechapter.R;
import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.model.App;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.jetbrains.annotations.NotNull;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.documentfile.provider.DocumentFile;


import java.net.URISyntaxException;

import static com.example.onemorechapter.model.Constants.CURRENT_BOOK;
import static com.example.onemorechapter.model.Constants.CURRENT_PAGE;
import static com.example.onemorechapter.model.Constants.REQUEST_CODE_PICK_FILE;

public class ReadingFragment
        extends MvpFragment<IReadingView, ReadingPresenter>
        implements IReadingView, OnPageErrorListener{

    private Book currentBook;

    private int pageNumber;

    private ImageView blank;
    private PDFView pdfView;
    private TextView txtView;
    private ScrollView scrollView;

    private ProgressBar progressBar;

    public ReadingFragment() {
        // Required empty public constructor
    }

    @NotNull
    @Override
    public ReadingPresenter createPresenter() {
        return new ReadingPresenter();
    }

    public static ReadingFragment newInstance(DocumentFile book, int pageNumber) {
        ReadingFragment fragment = new ReadingFragment();
        if(book != null){
            Bundle args = new Bundle();
            args.putSerializable(CURRENT_BOOK, new Book(book));
            args.putInt(CURRENT_PAGE, pageNumber);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            currentBook = (Book) getArguments().getSerializable(CURRENT_BOOK);
            App.getInstance().setCurrentBook(DocumentFile.fromSingleUri(getContext(), currentBook.getUriAsUri()));

            pageNumber = getArguments().getInt(CURRENT_PAGE);
            App.getInstance().setCurrentPage(pageNumber);

        }else if(savedInstanceState != null) {
            currentBook = (Book) savedInstanceState.getSerializable(CURRENT_BOOK);
            pageNumber = savedInstanceState.getInt(CURRENT_PAGE);
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
        txtView = view.findViewById(R.id.textView);
        scrollView = view.findViewById(R.id.scrollText);
        pdfView = view.findViewById(R.id.pdfView);

        progressBar = view.findViewById(R.id.progressBar);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
        });

        if(currentBook == null){
            blank.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }else{
            presenter.loadBook(currentBook);
        }

        presenter.getSharedPref();
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_PAGE, pageNumber);
        outState.putSerializable(CURRENT_BOOK, currentBook);
    }


    @Override
    public void showLoading() {
        blank.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.INVISIBLE);
        pdfView.setVisibility(View.INVISIBLE);

        progressBar.setVisibility(View.VISIBLE);
    }

    public void showError(String message){
        currentBook = null;
        blank.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }


    @Override
    public void openPdf() {
        progressBar.setVisibility(View.INVISIBLE);

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
        progressBar.setVisibility(View.INVISIBLE);

        txtView.setText(s);
        scrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageError(int page, Throwable t) {
        showError("Cannot load page " + page);
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
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        App.getInstance().setCurrentPage(pageNumber);
        super.onDestroy();
    }

    @Override
    public void openEpubDialog() {
        progressBar.setVisibility(View.INVISIBLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.epub_dialog, null);

        builder.setView(view)
                .setIcon(R.drawable.dip_switch);

        builder.setPositiveButton("Да", (dialog, id) -> getPresenter().readEpub(App.getInstance().getCurrentBook().getUri()))
                .setNegativeButton("Отмена", (dialog, id) -> showError("Действие было отменено"));
       builder.create().show();
    }

    @Override
    public void setDisplayOn(){
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void setTextSizeAndFont(int size, Typeface font) {
        txtView.setTextSize(size);
        txtView.setTypeface(font);
    }


}
