package com.example.b07demosummer2024;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainScreenPresenter implements MainScreenPresenterInterface {
    MainScreenView view;
    MainScreenModel model;

    private static int pageNumber = 1;
    int maxPages;

    public MainScreenPresenter(MainScreenView view, MainScreenModel model) {
        this.view = view;
        this.model = model;
        this.model.setPresenterInterface(this);

    }

    @Override
    public void update(int maxPages) {
        setMaxPages(maxPages);
        updatePage();
    }

    void loadItems() {
        if (model.areItemsLoaded()) {
            update((model.getItemList().size() + 9) / 10);
        } else {
            model.loadItemsFromDb();
        }
    }

    void handlePageUp() {
        if (pageNumber < maxPages) {
            pageNumber++;
            updatePage();
            view.scrollToTop();
        }
    }

    void handlePageDown() {
        if (pageNumber > 1) {
            pageNumber--;
            updatePage();
            view.scrollToTop();
        }
    }

    void paginateList() {
        List<Item> pagedList = new ArrayList<>();
        int start = (pageNumber - 1) * 10;
        int end = Math.min(start + 10, model.getItemList().size());

        for (int i = start; i < end; i++) {
            pagedList.add(model.getItemList().get(i));
        }

        view.updateRecyclerList(pagedList);
        view.scrollToTop();
    }

    public void updatePage() {
        String pageInfo = makePageString();
        view.updatePageInfo(pageInfo);
        paginateList();
    }


    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages;
        if (maxPages < pageNumber && maxPages != 0) {
            Log.d("BUG", "Setting page number to " + maxPages);
            pageNumber = maxPages;
        }
    }

    String makePageString() {
        if (maxPages == 0 && pageNumber == 1)
            return "No Items";
        return "Page " + pageNumber + " of " + maxPages;
    }


}
