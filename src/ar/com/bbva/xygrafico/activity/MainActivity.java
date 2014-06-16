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
	
	//private Tooltip tooltip;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		setContentView(R.layout.activity_main);
		
		//tooltip = (Tooltip)findViewById(R.id.tooltip);
		grafico = (XYPlot)findViewById(R.id.xygraph);
		
		grafico.getXyGraphWidget().setCountRangeLines(6);
		grafico.getXyGraphWidget().setCountDomainLines(5);
		
		XYSerie<String, Number> xySerie = new XYSerie<String, Number>();

		xySerie.add("05/05/14", 6070.60);
		xySerie.add("06/05/14", 2150.25);
		xySerie.add("07/05/14", 2154.24);
		xySerie.add("08/05/14", 1654.24);
		xySerie.add("09/05/14", 1724.96);
/*		xySerie.add("12/05/14", 1034.96);
		xySerie.add("14/05/14", 807.72 );
		xySerie.add("15/05/14", 3269.79);
		xySerie.add("16/05/14", 2569.79);
		xySerie.add("19/05/14", 1657.87);
		xySerie.add("20/05/14", 1576.01);
		xySerie.add("21/05/14", 1588.28);
		xySerie.add("23/05/14", 1331.28);
		xySerie.add("26/05/14", 104.20 );
		xySerie.add("27/05/14", 89.21  );
		xySerie.add("28/05/14", 91.45  );
		xySerie.add("30/05/14", 11754.45);
		xySerie.add("31/05/14", 6364.45);
		xySerie.add("02/06/14", 6364.96);
		xySerie.add("02/06/14", 6259.62);
		xySerie.add("04/06/14", 5829.60);
		xySerie.add("05/06/14", 4529.65);*/


	
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
