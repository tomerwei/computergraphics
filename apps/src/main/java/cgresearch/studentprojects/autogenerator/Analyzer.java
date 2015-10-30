package cgresearch.studentprojects.autogenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Analyzer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Data2D> data = new ArrayList<Data2D>();

	public List<Data2D> getData() {
		return data;
	}

	public void setData(List<Data2D> data) {
		this.data = data;
	}

}
