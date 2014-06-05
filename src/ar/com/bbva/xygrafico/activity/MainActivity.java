package ar.com.bbva.xygrafico.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import ar.com.bbva.xygrafico.activity.R;
import ar.com.bbva.xygrafico.XYPlot;
import ar.com.bbva.xygrafico.core.XYSerie;
import ar.com.bbva.xygrafico.widget.Tooltip;

public class MainActivity extends Activity {

	private XYPlot grafico;
	
	private Tooltip tooltip;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		setContentView(R.layout.activity_main);
		
		tooltip = (Tooltip)findViewById(R.id.tooltip);
		grafico = (XYPlot)findViewById(R.id.xygraph);
		
		grafico.getXyGraphWidget().setCountRangeLines(6);
		grafico.getXyGraphWidget().setCountDomainLines(5);
		
		XYSerie<String, Number> xySerie = new XYSerie<String, Number>();

		xySerie.add("08/02/14", 0);
		xySerie.add("07/02/14", 300);
		xySerie.add("06/02/14", 50);
		xySerie.add("05/02/14", 200);
		xySerie.add("04/02/14", 60);
		xySerie.add("03/02/14", 70);
		xySerie.add("02/02/14", 400);
		xySerie.add("01/02/14", 100);
	
		grafico.addSerie(xySerie);
		
		grafico.getXyGraphWidget().setMaxCountDomainLines(xySerie.getSize());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

}
