package cgresearch.studentprojects.autogenerator;

import cgresearch.core.math.PrincipalComponentAnalysis;
import cgresearch.core.math.VectorMatrixFactory;

public class Analyzer {

	private PrincipalComponentAnalysis pca = new PrincipalComponentAnalysis();

	public void initPCA(Data2D data) {

		for (int i = 0; i < data.getX().size(); i++) {
			// this.pca.add(VectorMatrixFactory.newIVector3(data.getX().get(i),
			// data.getY().get(i), data.getZ().get(i)));
		}

	}

	public PrincipalComponentAnalysis getPca() {
		return pca;
	}

	public void setPca(PrincipalComponentAnalysis pca) {
		this.pca = pca;
	}

}
