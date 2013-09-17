package com.td.dirtwalls.adapters;

import java.util.ArrayList;

import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.td.dirtwalls.R;
import com.td.dirtwalls.types.Wallpaper;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WallpaperPreviewAdapter extends ArrayAdapter<Wallpaper> {
	public final static int SHOW_ALL_CATS = -1;
	
	ArrayList<Wallpaper> mWallList;

	private int mCat = -1;
	
	public WallpaperPreviewAdapter(Context context, int resource,  
			ArrayList<Wallpaper> objects) {
		super(context, resource,  objects);
		// TODO init this 
		mWallList = objects;
	
		//TODO refresh the list CLEAR ALL and start over (image cache should stop any crapyness)
		this.notifyDataSetChanged();
	}
	
	public void updateArray(ArrayList<Wallpaper> objects){
		mWallList = objects;
		this.notifyDataSetChanged();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO draw a veiw that looks epic sauces... 
		
		// TODO recylce the old views
        View v = convertView;
        
        if (v == null){
        	v = View.inflate(getContext(), R.layout.thumbnail, null);
        }
		// TODO Fix Tylers broken stuffs
        
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView author = (TextView) v.findViewById(R.id.author);
        ImageView thumbnail = (ImageView) v.findViewById(R.id.thumb);
        
        name.setText(mWallList.get(position).getName());
        author.setText(mWallList.get(position).getAuthor());
        
        //thumbnail.setAnimation(null);
        // yep, that's it. it handles the downloading and showing an interstitial image automagically.
        UrlImageViewHelper.setUrlDrawable(thumbnail, mWallList.get(position).getThumbUrl(), R.drawable.ghost_icon, new UrlImageViewCallback() {
            @Override
            public void onLoaded(ImageView imageView, Drawable loadedBitmap, String url, boolean loadedFromCache) {
        		Log.d("lsitcrap","loaded " + url);
                //author.setText(mWallList.get(position).getAuthor());
            }
        });     
        try {
        AnimationDrawable anim = (AnimationDrawable) thumbnail.getDrawable();
    	if (anim != null){
    		anim.stop();
    		anim.start();
    	}
        } catch (ClassCastException e ){
        	Log.d("failzor", "failing");
        }
        
        Log.d("lsitcrap"," " + v.getWidth() + " " + v.getHeight());
        return v;
	}
	
	

}
