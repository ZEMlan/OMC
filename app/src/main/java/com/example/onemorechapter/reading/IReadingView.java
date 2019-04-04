package com.example.onemorechapter.reading;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface IReadingView extends MvpView {

    void openPdf();

    void openTxt(String s);

    void openFb2();

    void openEpub();

    void showLoading();
}
