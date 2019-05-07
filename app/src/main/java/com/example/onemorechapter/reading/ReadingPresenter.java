package com.example.onemorechapter.reading;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.onemorechapter.R;
import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.model.App;
import com.folioreader.Config;
import com.folioreader.Constants;
import com.folioreader.FolioReader;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
                    getView().openEpubDialog();
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

    void readEpub(Uri uri) {
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

    void getSharedPref(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance());

        boolean is_on = preferences.getBoolean("is_on", false);
        String textSize = preferences.getString("text_size", "18");
        int size = textSize != null ? Integer.valueOf(textSize) : 18;
        String font = preferences.getString("font", "Old Standard TT");
        Typeface typeface = getFont(font);

        if(isViewAttached()){
            if(is_on)
                getView().setDisplayOn();
            getView().setTextSizeAndFont(size, typeface);
        }
    }

    private Typeface getFont(@NotNull String font){
        Typeface typeface = null;
        switch (font){
            case ("Old Standard TT"):
                typeface = Typeface.createFromAsset(
                        App.getInstance().getAssets(),
                        "fonts/old_standard_regular.ttf");
            break;
            case ("Droid Sans Mono"):
                typeface = Typeface.createFromAsset(
                        App.getInstance().getAssets(),
                        "fonts/droid_sans_mono.ttf");
                break;
            case ("Times New Roman"):
                typeface = Typeface.createFromAsset(
                        App.getInstance().getAssets(),
                        "fonts/times_new_roman.ttf");
                break;
            case ("Bookman Old Style"):
                typeface = Typeface.createFromAsset(
                        App.getInstance().getAssets(),
                        "fonts/bookman_old_style.ttf");
                break;
            case ("Theano Didot"):
                typeface = Typeface.createFromAsset(
                        App.getInstance().getAssets(),
                        "fonts/theano_didot_regular.ttf");
                break;
            case ("America XIX"):
                typeface = Typeface.createFromAsset(
                        App.getInstance().getAssets(),
                        "fonts/am_xix.otf");
                break;
        }
        return typeface;
    }
}
