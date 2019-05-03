package com.example.onemorechapter.reading;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface IReadingView extends MvpView {

    void openPdf();

    void openTxt(String s);

    void askForOpenEpub();

    void showLoading();

    void showError(String message);
}
