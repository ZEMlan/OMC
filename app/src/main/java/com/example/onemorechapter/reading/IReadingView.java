package com.example.onemorechapter.reading;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.kursx.parser.fb2.FictionBook;

interface IReadingView extends MvpView {

    void openPdf();

    void openTxt(String s);

    void openFb2(FictionBook book);

    void openEpub();

    void showLoading();
}
