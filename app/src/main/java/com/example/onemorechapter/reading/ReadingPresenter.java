package com.example.onemorechapter.reading;

import android.net.Uri;

import com.example.onemorechapter.model.App;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

class ReadingPresenter extends MvpBasePresenter<IReadingView> {

    void loadBook(String type) throws IOException {
        boolean isViewAttached = isViewAttached();
        switch (type) {
            case ".pdf":
                if (isViewAttached) {
                    getView().openPdf();
                }
                break;
            case ".doc":
            case ".docx":
            case ".txt":
                if (isViewAttached) {
                    getView().showLoading();
                    readTextFromUri(App.getInstance().getCurrentDir().getUri());
                }
                break;
            case ".fb2":
                if (isViewAttached) {
                    getView().openFb2();
                }
                break;
            case ".epub":
                if (isViewAttached) {
                    getView().openEpub();
                }
                break;
        }
    }

    private void readTextFromUri(Uri uri) {
        Single.create((SingleEmitter<String> emitter) -> {
            InputStream inputStream = App.getInstance().getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream, StandardCharsets.UTF_8));

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
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

}
