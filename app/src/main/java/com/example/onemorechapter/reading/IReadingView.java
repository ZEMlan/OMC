package com.example.onemorechapter.reading;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface IReadingView extends MvpView {

    void openPdf();

    void openTxt();

    void openFb2();

    void openEpub();
}
