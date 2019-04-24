package com.example.onemorechapter.reading;

import android.net.Uri;

import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.model.App;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.kursx.parser.fb2.FictionBook;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

class ReadingPresenter extends MvpBasePresenter<IReadingView> {

    void loadBook(Book book){
        if(isViewAttached()){
            getView().showLoading();
        }
        switch (book.getType()) {
            case ".pdf":
                getView().openPdf();
                break;
            case ".doc":
            case ".docx":
            case ".txt":
            case ".html":
            case ".xml":
                readTextFromUri(book.getUriAsUri(), book.getType());
                break;
            case ".fb2":
               // readFb2(App.getInstance().getCurrentBook().getUri());
                break;
            case ".epub":
                getView().openEpub();
                break;
        }
    }

    private void readTextFromUri(Uri uri, String type) {
        Single.create((SingleEmitter<String> emitter) -> {
            InputStream inputStream = App.getInstance().getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream, StandardCharsets.UTF_8));

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            if (type.equals(".doc") || type.equals(".docx"))
                reader.skip(2000);
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n\r");
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if(getView() != null){
                emitter.onSuccess(stringBuilder.toString());
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(s -> getView().openTxt(s))
                .subscribe();

    }

    private void readFb2(Uri uri) {
        Single.create((SingleEmitter<FictionBook> emitter) -> {
            File temp = new File(uri.getPath());
            FictionBook fb2 = new FictionBook(new File(temp.getAbsolutePath()));
            emitter.onSuccess(fb2);
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(s -> getView().openFb2(s))
                .subscribe();
    }

}
