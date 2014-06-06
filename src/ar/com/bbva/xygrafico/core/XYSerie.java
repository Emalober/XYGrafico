package ar.com.bbva.xygrafico.core;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.util.Pair;

public class XYSerie<X, Y> implements Iterable<Pair<X,Y>>{

	private List<Pair<X,Y>> xySerie = new LinkedList<Pair<X,Y>>();
	
	public void add(X domain, Y value) {
		this.xySerie.add(new Pair<X, Y>(domain, value));
	}
	
	public Iterator<Pair<X,Y>> iterator() {
		return xySerie.iterator();
	}

	public int getSize() {
		return xySerie.size();
	}

	public Pair get(int index) {
		return xySerie.get(index);
	}
}
