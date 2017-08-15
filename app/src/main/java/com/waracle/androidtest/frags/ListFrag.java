package com.waracle.androidtest.frags;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.waracle.androidtest.R;
import com.waracle.androidtest.models.Cake;

import java.util.Collections;
import java.util.List;

/**
 * Created by Bam on 8/15/2017.
 */
public class ListFrag extends ListFragment implements AdapterView.OnItemClickListener {

    @NonNull
    protected List<Cake> cakes = Collections.emptyList();

    public static ListFrag newInstance( @NonNull List<Cake> listItems ) {
        ListFrag frag = new ListFrag();
        frag.cakes = listItems;
        return frag;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        return inflater.inflate(R.layout.frag_list, container, false);
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState ) {
        super.onActivityCreated(savedInstanceState);
        CakeAdapter adapter = new CakeAdapter(getContext(), cakes);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }
}