package cgresearch.studentprojects.autogenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Analyzer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Car> data = new ArrayList<Car>();

	public List<Car> getData() {
		return data;
	}

	public void setData(List<Car> data) {
		this.data = data;
	}

}
