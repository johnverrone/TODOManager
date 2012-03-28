package cs2340.todo.view;

import java.util.List;
import android.os.Bundle;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class MainMapActivity extends MapActivity {
	
	MapView map;
	long start;
	long stop;
	MyLocationOverlay compass;
	MapController controller;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_main);
        
        map = (MapView)findViewById(R.id.mvMain);
        map.setBuiltInZoomControls(true);
        
        Touchy t = new Touchy();
        List<Overlay> overlayList = map.getOverlays();
        overlayList.add(t);
        compass = new MyLocationOverlay(MainMapActivity.this, map);
        overlayList.add(compass);
        
        controller = map.getController();
        GeoPoint point = new GeoPoint(33451800, 8423240);
        controller.animateTo(point);
        controller.setZoom(6);
    }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		compass.disableCompass();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		compass.enableCompass();
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
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