package com.example.onemorechapter.reading;

import android.net.Uri;

import com.example.onemorechapter.R;
import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.model.App;
import com.folioreader.Config;
import com.folioreader.Constants;
import com.folioreader.FolioReader;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.kursx.parser.fb2.FictionBook;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ReadingPresenter extends MvpBasePresenter<IReadingView> {

    void loadBook(Book book){
        if(isViewAttached()) {
            getView().showLoading();
            switch (book.getType()) {
                case ".pdf":
                    getView().openPdf();
                    break;
                case ".doc":
                case ".txt":
                case ".html":
                case ".xml":
                    readTextFromUri(book.getUriAsUri(), book.getType());
                    break;
                case ".epub":
                    getView().askForOpenEpub();
                    break;
                default:
                    getView().showError("Данный формат не поддерживается");
            }
        }
    }

    private void readTextFromUri(Uri uri, String type) {
        Single.create((SingleEmitter<String> emitter) -> {

            InputStream inputStream = App.getInstance().getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream, StandardCharsets.UTF_8));

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            if (type.equals(".doc"))
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
                .doOnError(e -> getView().showError(e.getMessage()))
                .doOnSuccess(s -> getView().openTxt(s))
                .subscribe();

    }


    void readEpub(Uri uri) throws URISyntaxException{
        String path = PathUtil.getPath(App.getInstance(), uri);
        Config config = new Config()
                .setDirection(Config.Direction.VERTICAL)
                .setFont(Constants.FONT_LATO)
                .setFontSize(2)
                .setNightMode(false)
                .setThemeColorRes(R.color.primaryColor)
                .setShowTts(true);

        FolioReader.get()
                .setConfig(config, true)
                .openBook(path);
    }


}
