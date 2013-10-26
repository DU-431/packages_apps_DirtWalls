
package com.td.dirtwalls;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.td.dirtwalls.adapters.NavigationBarCategoryAdapater;
import com.td.dirtwalls.parsers.ManifestXmlParser;
import com.td.dirtwalls.types.WallpaperCategory;
import com.td.dirtwalls.ui.WallpaperPreviewFragment;
import com.td.dirtwalls.util.helpers;

public class WallpaperActivity extends Activity {

    public final String TAG = "DirtWalls";
    protected static final String MANIFEST = "wallpaper_manifest.xml";
    /*
     * pull the manifest from the web server specified in config.xml or pull
     * wallpaper_manifest.xml from local assets/ folder for testing
     */
    public static final boolean USE_LOCAL_MANIFEST = false;

    ArrayList<WallpaperCategory> categories = null;
    ProgressDialog mLoadingDialog;
    WallpaperPreviewFragment mPreviewFragment;
    NavigationBarCategoryAdapater mCategoryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setIndeterminate(true);
        mLoadingDialog.setMessage("Retreiving wallpapers from server...");

        mLoadingDialog.show();
        new LoadWallpaperManifest().execute();
        
    }

    protected void loadPreviewFragment() {
        mPreviewFragment = new WallpaperPreviewFragment();
        mPreviewFragment.setArray(categories);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(android.R.id.content, mPreviewFragment);
        ft.commit();

        ActionBar ab = getActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ab.setListNavigationCallbacks(mCategoryAdapter = new NavigationBarCategoryAdapater(
                getApplicationContext(),
                categories),
                new ActionBar.OnNavigationListener() {
                    @Override
                    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                        setCategory(itemPosition);
                        return true;
                    }
                });
        ab.setDisplayShowTitleEnabled(false);
        ab.setSelectedNavigationItem(1);

        setCategory(1);
    }

    protected void setCategory(int cat) {
    	mPreviewFragment.setCategory(cat);
    }



    private class LoadWallpaperManifest extends
            AsyncTask<Void, Boolean, ArrayList<WallpaperCategory>> {

        @Override
        protected ArrayList<WallpaperCategory> doInBackground(Void... v) {

            try {
                InputStream input = null;

                if (USE_LOCAL_MANIFEST) {
                    input = getApplicationContext().getAssets().open(MANIFEST);
                } else {
                    URL url = new URL(helpers.getResourceString(WallpaperActivity.this.getApplicationContext(), R.string.config_wallpaper_manifest_url));
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    connection.getContentLength();
                    input = new BufferedInputStream(url.openStream());
                }
                
                OutputStream output = getApplicationContext().openFileOutput(
                        MANIFEST, MODE_PRIVATE);
                byte data[] = new byte[1024];
                int count;
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

                // file finished downloading, parse it!
                ManifestXmlParser parser = new ManifestXmlParser();
                return parser.parse(new File(getApplicationContext().getFilesDir(), MANIFEST),
                        getApplicationContext());
            } catch (Exception e) {
                Log.d(TAG, "Exception!", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<WallpaperCategory> result) {
            categories = result;
            if (categories != null)
                loadPreviewFragment();

            mLoadingDialog.cancel();
            super.onPostExecute(result);
        }
    }

}
