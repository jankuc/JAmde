package jamde.estimator;
import jamde.estimator.PowerEstimator;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a modified empirical distribution function
 * The distribution function is based on a data histogram (classic or stochastically equivalent),
 * grows linearly in all histogram bins and is constant in the bins where there is no data
 * @author hanakluc
 *
 */
public class DistributionFunctionCont {
	
	private List<Double> hist = new ArrayList<>();
	private List<Double> x = new ArrayList<>();
	private List<Double> histArea;
	private int histType;
	private String dataType;
	
	public static class HistogramType {
		public static final int HISTOGRAM_CLASSIC = 0;
		public static final int HISTOGRAM_STOCH = 1;
	}
	
	public DistributionFunctionCont(double[] data) {
		this(data, HistogramType.HISTOGRAM_STOCH);
		
	}
	
	public DistributionFunctionCont(double[] data, int histType) {
		this(data, histType, "Normal");
	}
	
	public DistributionFunctionCont(double[] data, int histType, String dataType) {
		// set histogram type to derive the distribution function from
		this.dataType = dataType;
		if (histType == HistogramType.HISTOGRAM_CLASSIC || histType == HistogramType.HISTOGRAM_STOCH) {
			this.histType = histType;
		} else {
			this.histType = HistogramType.HISTOGRAM_STOCH;
		}
		prepareHistogram(data);
		prepareHistArea();
	}
	
	/**
	 * gives the value of our distribution function in the point y
	 * the distribution function is continuous, piecewise linear (in all histogram bins)
	 * @param y
	 * @return
	 */
	public double getFunctionValue(double y) {
		double xStart = x.get(0);
		double xEnd = x.get(x.size() - 1);
		double value = 0;
		if (y > xStart) {
			if (y < xEnd) {
				int i = 1;
				while (y > x.get(i)) {
					i++;
				}
				double xDif = x.get(i) - x.get(i-1);
				double yDif = histArea.get(i) - histArea.get(i-1);
				value = histArea.get(i-1) + yDif/xDif * (y - x.get(i-1));
			} else {
				value = 1;
			}
		}
		return value;
	}
	
	/**
	 * prepares cumulative histogram area
	 */
	private void prepareHistArea() {
		histArea = new ArrayList<>();
        histArea.add(0.0);
		for (int i=0; i< hist.size() - 1; i++) {
			double area = hist.get(i) * (x.get(i+1) - x.get(i));
			if (i != 0) {
				area += histArea.get(i-1);
			}
			histArea.add(area);
		}
		histArea.add(1.0);
	}
	
	/**
	 * calculates the histogram to derive the distribution function from
	 * @param data
	 */
	private void prepareHistogram(double[] data) {
		if (histType == HistogramType.HISTOGRAM_STOCH) {
			PowerEstimator.setHistDistributionStoch(x, hist, data);
		} else if (histType == HistogramType.HISTOGRAM_CLASSIC) {
			PowerEstimator.setHistDistribution(x, hist, dataType, data);
		}
	}
	
}
