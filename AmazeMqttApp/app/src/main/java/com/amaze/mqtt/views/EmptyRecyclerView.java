package com.amaze.mqtt.views;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amaze.mqtt.R;


/**
 * Created by arpitkh996 on 10-07-2016.
 */

public class EmptyRecyclerView extends RecyclerView {
    boolean empty_view_visible;
    private View emptyView;
    private String empty_text;
    boolean runningtask=false;
    private boolean if_Filter_applied;
    private TextView nodata_textview;
    private ContentLoadingProgressBar progressBar;
    final private RecyclerView.AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    public EmptyRecyclerView(Context context) {
        super(context);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    void checkIfEmpty() {
        if (emptyView != null && getAdapter() != null) {

            final boolean emptyViewVisible = getAdapter().getItemCount() == 0;
            empty_view_visible = emptyViewVisible;
            emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            setVisibility(emptyViewVisible ? GONE : VISIBLE);
        }
        if (nodata_textview != null) {
            if(runningtask){
                progressBar.show();
                nodata_textview.setText("");
            }
            else {
                nodata_textview.setText(empty_text);
                progressBar.hide();
            }
        }
    }

    public boolean isEmpty_view_visible() {
        return empty_view_visible;
    }

    public void setif_Filter_applied(boolean if_Filter_applied) {
        this.if_Filter_applied = if_Filter_applied;
    }


    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        checkIfEmpty();
    }

    public void setRunningtask(boolean runningtask) {
        this.runningtask = runningtask;
    }

    public void setEmptyView(View emptyView, String empty_text, TextView nodata_textview,ContentLoadingProgressBar contentLoadingProgressBar) {
        this.emptyView = emptyView;
        this.empty_text=empty_text;
        this.nodata_textview =nodata_textview;
        this.progressBar=contentLoadingProgressBar;
        checkIfEmpty();
    }
}
