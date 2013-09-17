package com.td.dirtwalls.ui;

import java.util.ArrayList;

import com.td.dirtwalls.R;
import com.td.dirtwalls.WallpaperActivity;
import com.td.dirtwalls.adapters.WallpaperPreviewAdapter;
import com.td.dirtwalls.types.Wallpaper;
import com.td.dirtwalls.types.WallpaperCategory;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class WallpaperPreviewFragment extends Fragment {

    static final String TAG = "PreviewFragment";
    
    View mView;
    GridView mGrid;
    TextView pageNum;

	ArrayList<Wallpaper> mWallList;

    ArrayList<WallpaperCategory> mArray;
    ArrayAdapter<Wallpaper> mAdapter; 
    public int selectedCategory = 1; 

    public void setArray(ArrayList<WallpaperCategory> list){
    	this.mArray = list;
    	setCategory(1);
    }
    
    public void setCategory(int cat) {
        selectedCategory = cat;
    	mWallList = new ArrayList<Wallpaper>();
    	if (selectedCategory == WallpaperPreviewAdapter.SHOW_ALL_CATS){
    		for (int i = 0; i < mArray.size() ; i++){
    			ArrayList<Wallpaper> mTemp = (ArrayList<Wallpaper>) mArray.get(i).getWallpapers();
    			for (int j = 0; j<mTemp.size();j++){
    				// TODO Loop it up and ADD ALL THE WALLPAPERS
    				mWallList.add(mTemp.get(j));
    			}
    		}
    	} else {
    		// Only add the current cat
    		mWallList = (ArrayList<Wallpaper>) mArray.get(selectedCategory).getWallpapers();
    	}
    	if ( mAdapter != null && mGrid != null && mAdapter != null){
	        mAdapter = new WallpaperPreviewAdapter(this.getActivity(), R.layout.thumbnail,  mWallList );
	        mGrid.setAdapter(mAdapter);
	        
	        // TODO update the view to show the list
	        mAdapter.notifyDataSetChanged();
	        pageNum.setText(mWallList.size() + " Wallpapers Available");
	        
    	}

        //update this bad boy!
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        mView = inflater.inflate(R.layout.activity_wallpaper, container, false);
        pageNum = (TextView) mView.findViewById(R.id.textView1);
        pageNum.setText("total :");
        
        mGrid = (GridView) mView.findViewById(R.id.GridView1);
        
        // TODO attach the adapter with the lasy ass loader
        mAdapter = new WallpaperPreviewAdapter(this.getActivity(), R.layout.thumbnail,  mWallList );
        mGrid.setAdapter(mAdapter);
        // TODO update the view to show the list
        mAdapter.notifyDataSetChanged();
        mGrid.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
	            Intent preview = new Intent(WallpaperPreviewFragment.this.getActivity(), Preview.class);
	            preview.putExtra("wp", mWallList.get(arg2).getUrl());
	            startActivity(preview);

			}
        	
        });
        setCategory(1);
        return mView;
    }
    
    /*
    public ArrayList<WallpaperCategory> getCategories() {
        return mActivity.mCategoryAdapter.getCategories();
    }
     
    protected Wallpaper getWallpaper(int realIndex) {
        return getCategories().get(selectedCategory).getWallpapers().get(realIndex);
    }
    */
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*class ThumbnailClickListener implements View.OnClickListener {
        Wallpaper wall;

        public ThumbnailClickListener(Wallpaper wallpaper) {
            this.wall = wallpaper;
        }

        @Override
        public void onClick(View v) {
            Intent preview = new Intent(mActivity, Preview.class);
            preview.putExtra("wp", wall.getUrl());
            startActivity(preview);
        }
    }*/
}

