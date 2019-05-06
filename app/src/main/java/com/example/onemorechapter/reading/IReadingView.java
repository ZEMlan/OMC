package com.example.onemorechapter.reading;

import android.graphics.Typeface;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.lang.reflect.Type;

interface IReadingView extends MvpView {

    void openPdf();

    void openTxt(String s);

    void openEpubDialog();

    void showLoading();

    void showError(String message);

    void setDisplayOn();

    void setTextSizeAndFont(int size, Typeface font);
}
