package com.example.onemorechapter.reading;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

class ReadingPresenter extends MvpBasePresenter<IReadingView> {

    void loadBook(String type){
        boolean isViewAttached = isViewAttached();
        switch (type) {
            case ".pdf":
                if (isViewAttached) {
                    getView().openPdf();
                }
                break;
            case ".txt":
                if (isViewAttached) {
                    getView().openTxt();
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

}
