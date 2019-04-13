package com.example.onemorechapter.reading;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onemorechapter.R;
import com.example.onemorechapter.database.entities.Book;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.shockwave.pdfium.PdfDocument;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

import static com.example.onemorechapter.model.Constants.CURRENT_BOOK;
import static com.example.onemorechapter.model.Constants.CURRENT_PAGE;

public class ReadingFragment
        extends MvpFragment<IReadingView, ReadingPresenter>
        implements IReadingView, OnLoadCompleteListener, OnPageErrorListener {
    private static final String TAG = "BOOK";

    private Book currentBook;

    private int pageNumber = 0;

    private ImageView blank, loading;
    private PDFView pdfView;
    private TextView txtView;


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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        blank = view.findViewById(R.id.blank);
        loading = view.findViewById(R.id.loadingView);
        txtView = view.findViewById(R.id.txtView);
        pdfView = view.findViewById(R.id.pdfView);

        if(currentBook == null){
            blank.setVisibility(View.VISIBLE);
        }else{
            try {
                presenter.loadBook(currentBook.getType());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            currentBook = (Book) getArguments().getSerializable(CURRENT_BOOK);
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
    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
        pdfView.setVisibility(View.INVISIBLE);
        txtView.setVisibility(View.INVISIBLE);
        blank.setVisibility(View.INVISIBLE);
    }

    @Override
    public void openPdf() {
        loading.setVisibility(View.INVISIBLE);
        pdfView.setVisibility(View.VISIBLE);
        pdfView.fromUri(currentBook.getUri())
                .enableSwipe(true).swipeHorizontal(true)
                .defaultPage(pageNumber)
                .onPageChange((page, pageCount) -> pageNumber = page)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(getContext()))
                .spacing(10) // in dp
                .onPageError(this)
                .load();
    }

    @Override
    public void openTxt(String s) {
        loading.setVisibility(View.INVISIBLE);
        blank.setVisibility(View.INVISIBLE);
        txtView.setVisibility(View.VISIBLE);
        txtView.setText(s);
    }

    @Override
    public void openFb2() {
        blank.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.INVISIBLE);
        //TODO: ope .fb2
    }

    @Override
    public void openEpub() {
        blank.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.INVISIBLE);
        //TODO: open .epub
    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        Log.e(TAG, "title = " + meta.getTitle());
        Log.e(TAG, "author = " + meta.getAuthor());
        Log.e(TAG, "subject = " + meta.getSubject());
        Log.e(TAG, "keywords = " + meta.getKeywords());

        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    private void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.d("PDF","Cannot load page" + pageNumber);
    }
}
