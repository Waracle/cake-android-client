package com.waracle.androidtest.presentation.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.waracle.androidtest.R;
import com.waracle.androidtest.core.GetCakes;
import com.waracle.androidtest.core.base.UseCase;
import com.waracle.androidtest.core.model.Cake;
import com.waracle.androidtest.extras.MutableData;
import com.waracle.androidtest.presentation.ImageLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Improve any performance issues
 * Use good coding practices to make code more secure
 */
public final class PlaceholderFragment extends ListFragment {

  private MyAdapter mAdapter;
  private GetCakes getCakes;

  public PlaceholderFragment() { /**/ }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_main, container, false);
    getCakes = CakesFragmentComponent.inject();
    setupViews(rootView);
    return rootView;
  }

  private void setupViews(View rootView) {
    mAdapter = new MyAdapter();
    ((ListView) rootView.findViewById(android.R.id.list)).setAdapter(mAdapter);
  }

  @Override
  public void onStart() {
    super.onStart();
    if (mAdapter.isEmpty()) {
      getCakes.execute(new UseCase.Callback<List<Cake>>() {
        @Override
        public void onSuccess(List<Cake> cakes) {
          mAdapter.withItems(cakes);
          mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onError(Exception e) {
          Toast.makeText(getContext(), R.string.cakes_content_error, Toast.LENGTH_LONG).show();
        }
      });
    }
  }

  @Override
  public void onStop() {
    getCakes.cancel();
    super.onStop();
  }

  private class MyAdapter extends BaseAdapter { // THIS COULD LEAK THE FRAGMENT.

    private ImageLoader mImageLoader = new ImageLoader();
    @MutableData private final List<Cake> cakes = new ArrayList<>();

    @Override
    public int getCount() {
      return cakes.size();
    }

    @Override
    public Cake getItem(int position) {
      return cakes.get(position);
    }

    @Override
    public long getItemId(int position) {
      return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      LayoutInflater inflater = LayoutInflater.from(getActivity());
      View root = inflater.inflate(R.layout.list_item_layout, parent, false);
      if (root != null) {
        Cake cake = getItem(position);
        TextView title = root.findViewById(R.id.title);
        title.setText(cake.title());
        TextView desc = root.findViewById(R.id.desc);
        desc.setText(cake.description());
        ImageView image = root.findViewById(R.id.image);
        mImageLoader.load(cake.imageURL(), image);
      }

      return root;
    }

    void withItems(List<Cake> cakes) {
      this.cakes.addAll(cakes);
    }
  }
}
