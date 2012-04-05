package cs2340.todo.view;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MainMapActivity extends MapActivity {
	
	private MapView map;
	private long start;
	private long stop;
	private MapController controller;
	private	ActionBar bar;
	private String sLoc;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_main);
        
        sLoc = getIntent().getStringExtra("sLoc");
        
        map = (MapView)findViewById(R.id.mvMain);
        map.setBuiltInZoomControls(true);
        
        bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        
        Touchy t = new Touchy();
        List<Overlay> overlayList = map.getOverlays();
        overlayList.add(t);
        
        controller = map.getController();
        Geocoder coder = new Geocoder(getBaseContext(), Locale.getDefault());
        Address aPoint = new Address(Locale.getDefault());
		try {
			aPoint = coder.getFromLocationName(sLoc, 1).get(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        GeoPoint point = new GeoPoint((int)(aPoint.getLatitude() * 1E6), (int) (aPoint.getLongitude() * 1E6));
        controller.animateTo(point);
        controller.setZoom(15);
    }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_OK);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	class Touchy extends Overlay {
		public boolean onTouchEvent(MotionEvent e, MapView m) {
			if(e.getAction() == MotionEvent.ACTION_DOWN) {
				start = e.getEventTime();
			}
			if(e.getAction() == MotionEvent.ACTION_UP) {
				stop = e.getEventTime();
			}
			if(stop-start > 15000) {
				
			}
			return false;
		}
	}
}