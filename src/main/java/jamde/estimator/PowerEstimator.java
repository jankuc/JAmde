package jamde.estimator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jamde.MathUtil;
import jamde.OtherUtils;
import jamde.distribution.Distribution;
import jamde.distribution.NormalDistribution;

public class PowerEstimator extends Estimator {

	private class HelperPowerEstimator {

		private double[] kernelDensity;

		private List<Double> x;
		private List<Double> hist;

		public void prepareKernelDensity(double[] dataArray) {
			if (densityType == DensityType.KERNEL_TRIAG) {
				kernelDensity = getKernelDensityTriang(dataArray);
			} else {
				kernelDensity = getKernelDensity(dataArray);
			}
		}

		public void prepareHistogram(Distribution distr, double[] data) {
			String type = distr.toString();
			x = new ArrayList<Double>();
			hist = new ArrayList<Double>();
			if (densityType == DensityType.HISTOGRAM_STOCH) {
				setHistDistributionStoch(x, hist, data);
			} else {
				setHistDistribution(x, hist, type, data);
			}
		}

		private double countDistanceHist(Distribution distr, double[] data) {

			String type = distr.toString();
			double alpha = getPar();

			double y1, y2;
			double eps = 0.1;
			double h;
			double Integral = 0;

			if (type.equals("Uniform")) {
				// unif density is partially constant function
				double p1, p2, f;
				p1 = distr.getP1();
				p2 = distr.getP2();
				if (p2 <= p1) {
					return 1 / (alpha * (1 - alpha));
				}
				f = distr.getfunctionValue((p2 + p1) / 2.0);
				f = Math.pow(f, 1 - alpha);

				double x1, x2;
				for (int i = 0; i < hist.size(); i++) {
					h = hist.get(i);
					x1 = x.get(i);
					x2 = x.get(i + 1);
					x1 = Math.max(p1, x1);
					x2 = Math.min(x2, p2);
					if (x2 > x1) {
						Integral += (x2 - x1) * f * h;
					}
				}

			} else {
				double x1, x2;
				for (int i = 0; i < hist.size(); i++) {
					h = hist.get(i);
					x1 = x.get(i);
					x2 = x.get(i + 1);
					if (h != 0) {
						double x0 = x1;
						double x0d;
						h = Math.pow(h, alpha);
						y1 = distr.getfunctionValue(x0);
						y1 = Math.pow(y1, 1 - alpha);
						if (type.equals("Weibull")) {
							// need to bound values
							if (y1 > 1) {
								y1 = 1;
							}
						}
						do // integral in < x0, x0 + d )
						{
							x0d = x0 + eps;
							x0d = Math.min(x0d, x2);
							y2 = distr.getfunctionValue(x0d);
							y2 = Math.pow(y2, 1 - alpha);
							if (type.equals("Weibull")) {
								// need to bound values
								if (y2 > 1) {
									y2 = 1;
								}
							}
							Integral += 0.5 * (x0d - x0) * (y1 + y2) * h;
							y1 = y2;
							x0 = x0d;
						} while (x0d < x2);
					}
				}
			}

			if (Integral > 1)
				Integral = 1;
			Integral = 1 / (alpha * (1 - alpha)) * (1 - Integral);
			return Integral;
		}

		private double countDistanceKernel(Distribution distr, double[] data) {

			return countDistanceFromDensity(distr, kernelDensity);
		}

	}

	private HelperPowerEstimator helper;

	public int densityType;

	public int sizeOfData = 0;
	public double[] lastData;

	public static class DensityType {
		public static final int HISTOGRAM = 0;
		public static final int KERNEL = 1;
		public static final int HISTOGRAM_STOCH = 2;
		public static final int KERNEL_TRIAG = 3;
	}

	public PowerEstimator(double par) {
		this(par, DensityType.HISTOGRAM);
	}

	public PowerEstimator(double par, int densityType) {
		///super.par = new ArrayList<>();  doesnt have to be here, because constructor of parent is called implicitly
		addPar(par);
		setDensityType(densityType);
	}

	public PowerEstimator() {
		this(0.5); // creates Hellinger estimator by default
	}

	public void setDensityType(int type) {
		if (type == DensityType.HISTOGRAM || type == DensityType.KERNEL
				|| type == DensityType.HISTOGRAM_STOCH
				|| type == DensityType.KERNEL_TRIAG) {
			this.densityType = type;
		}
	}

	@Override
	public double countDistance(Distribution distr, double[] data) {
		HelperPowerEstimator helperLocal;
		synchronized (this) {

			if (helper == null || !isSameData(data)) {
				helper = new HelperPowerEstimator();
				sizeOfData = data.length;
				lastData = data;
				if (densityType == DensityType.HISTOGRAM
						|| densityType == DensityType.HISTOGRAM_STOCH) {
					helper.prepareHistogram(distr, data);
				} else {
					helper.prepareKernelDensity(data);
				}
			}
			
			helperLocal=helper;
		}

		if (densityType == DensityType.HISTOGRAM
				|| densityType == DensityType.HISTOGRAM_STOCH) {
			return helperLocal.countDistanceHist(distr, data);
		} else {
			return helperLocal.countDistanceKernel(distr, data);
		}
	}
	
	private boolean isSameData(double[] data) {
		/*if (sizeOfData != data.length) {
			return false;
		}
		boolean same = true;
		for (int i=0; i < 10 && same; i++) {
			if (data[i] != lastData[i]) {
				same = false;
			}
		}
		return same;*/
		return lastData == data;
	}

	

	private static double[] getKernelDensity(double[] data) {
		Arrays.sort(data);
		int sizeOfSample = data.length;
		double A;
		double ir; // interquartile range divided by 1.34
		double h;
		double eps = 0.1;
		int pom1, pom2;
		pom1 = (int) (Math.floor(0.25 * sizeOfSample + 0.5) - 1);
		pom2 = (int) (Math.floor(0.75 * sizeOfSample + 0.5) - 1);
		ir = (data[pom2] - data[pom1]) / 1.34; // interquartile range divided by
												// 1.34
		double EV, DV;
		EV = MathUtil.getExpVal(data);
		DV = MathUtil.getStandDev(EV, data);

		A = Math.min(DV, ir);

		h = 0.9 * A * Math.pow(sizeOfSample, -0.2); // the smoothing parameter
		// mult was 0.9!!!!!! // (bandwidth) (if too big -
		// if the bandwidth is too big - data oversmoothed, if too small - data
		// undersmoothed
		// for Normally distributed data - ideal mult is 1.06

		// gausovo jadro

		double x;
		x = data[0] - (h * 4); // the first density value is 4*h from the
								// smallest data point
		// the last density value is 4*h from the biggest data point

		int size = (int) Math.floor((data[sizeOfSample - 1] - data[0] + 8 * h)
				/ eps + 2);
		size = size + 2;
		double[] array = new double[size];
		array[size - 2] = eps; // the size of the step in density values
		array[size - 1] = x; // starting x point of the density

		double k, t;
		double C = 0.39894; // 1/sqrt(2*pi)

		for (int l = 0; l < size - 2; l++) {
			k = 0;
			for (int i = 0; i < sizeOfSample; i++) {
				t = (x - data[i]) / h;
				if (Math.abs(t) < 4) {
					k += C * Math.exp(-Math.pow(t, 2) / 2);
				}
			}
			k = k / (sizeOfSample * h);
			array[l] = k;

			x += eps;
		}

		return array;
	}

	private static double[] getKernelDensityTriang(double[] data) {
		Arrays.sort(data);
		int sizeOfSample = data.length;
		double A;
		double ir; // interquartile range divided by 1.34
		double h;
		double eps = 0.1;
		int pom1, pom2;
		pom1 = (int) (Math.floor(0.25 * sizeOfSample + 0.5) - 1);
		pom2 = (int) (Math.floor(0.75 * sizeOfSample + 0.5) - 1);
		ir = (data[pom2] - data[pom1]) / 1.34; // interquartile range divided by
												// 1.34
		double EV, DV;
		EV = MathUtil.getExpVal(data);
		DV = MathUtil.getStandDev(EV, data);

		A = Math.min(DV, ir);

		//h = 2.58 * A * Math.pow(sizeOfSample, -0.2); // ideal smoothing parameter for noncontaminated data
		h = 2.0 * A * Math.pow(sizeOfSample, -0.2); // the reduced (robust) smoothing parameter
		// if the bandwidth is too big - data oversmoothed, if too small - data undersmooth

		// triangular kernel

		double x;
		x = data[0] - (h * 4); // the first density value is 4*h from the
								// smallest data point
		// the last density value is 4*h from the biggest data point

		int size = (int) Math.floor((data[sizeOfSample - 1] - data[0] + 8 * h)
				/ eps + 2);
		size = size + 2;
		double[] array = new double[size];
		array[size - 2] = eps; // the size of the step in density values
		array[size - 1] = x; // starting x point of the density

		double k, t;

		for (int l = 0; l < size - 2; l++) {
			k = 0;
			for (int i = 0; i < sizeOfSample; i++) {
				t = Math.abs((x - data[i]) / h);
				if (t <= 1) {
					k += 1 - t;
				}
			}
			k = k / (sizeOfSample * h);
			array[l] = k;

			x += eps;
		}

		return array;
	}

	/**
	 * Prints to file 2 lines: x kernelDensity(x)
	 * 
	 * @param file
	 * @param data
	 */
	public static void printKernelToFile(String filename, double[] data) {
		File file = new File(filename);
		printKernelToFile(file, data);

	}

	public static void printKernelToFile(File file, double[] data) {
		printKernelToFile(file, data, false);
	}

	/**
	 * Prints to file 2 lines: x kernelDensity(x)
	 * 
	 * @param file
	 * @param data
	 */
	public static void printKernelToFile(File file, double[] data,
			boolean append) {
		printKernelToFile(file, data, DensityType.KERNEL, append);
	}

	public static void printKernelToFile(File file, double[] data,
			int densityType, boolean append) {
		double[] density;
		if (densityType == DensityType.KERNEL_TRIAG) {
			density = getKernelDensityTriang(data);
		} else {
			density = getKernelDensity(data);
		}

		int size = density.length;
		double eps = density[size - 2];
		double x = density[size - 1];

		try {
			PrintWriter out;
			if (append) {
				out = new PrintWriter(new FileWriter(file, true));
			} else {
				out = new PrintWriter(file);
			}
			for (int i = 0; i < density.length - 2; i++) {
				out.write(x + " ");
				x += eps;
			}
			out.write("\n");
			for (int i = 0; i < density.length - 2; i++) {
				out.write(density[i] + " ");
			}
			out.write("\n");
			out.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Prints to file 2 lines: x histogram value in <x, x + d) where d is the
	 * calculated optimal cell length
	 * 
	 * @param file
	 * @param distr
	 * @param data
	 */
	public static void printHistogramToFile(String filename,
			Distribution distr, double[] data) {
		File file = new File(filename);
		printHistogramToFile(file, distr, data);
	}

	public static void printHistogramToFile(File file, Distribution distr,
			double[] data) {
		printHistogramToFile(file, distr, data, false);
	}

	/**
	 * Prints to file 2 lines: x histogram value in <x, x + d) where d is the
	 * calculated optimal cell length
	 * 
	 * @param file
	 * @param distr
	 * @param data
	 */
	public static void printHistogramToFile(File file, Distribution distr,
			double[] data, int densityType, boolean append) {
		ArrayList<Double> x = new ArrayList<Double>();
		ArrayList<Double> hist = new ArrayList<Double>();
		if (densityType == DensityType.HISTOGRAM_STOCH) {
			setHistDistributionStoch(x, hist, data);
		} else {
			setHistDistribution(x, hist, distr.toString(), data);
		}

		try {
			PrintWriter out;
			if (append) {
				out = new PrintWriter(new FileWriter(file, true));
			} else {
				out = new PrintWriter(file);
			}
			for (int j = 0; j < x.size(); j++) {
				out.write(x.get(j) + " ");
			}
			out.write("\n");
			for (int j = 0; j < hist.size(); j++) {
				out.write(hist.get(j) + " ");
			}
			out.write("\n");
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			ioe.printStackTrace();
		}
	}

	public static void printHistogramToFile(File file, Distribution distr,
			double[] data, boolean append) {
		printHistogramToFile(file, distr, data, DensityType.HISTOGRAM, append);
	}

	public static void setHistDistribution(List<Double> x, List<Double> hist,
			String distrType, double[] data) {
		int sizeOfSample = data.length;
		Arrays.sort(data);

		double d = getHistCellLength(distrType, data);
		int i = 0;
		int count = 0;
		double x0 = Math.floor(data[0]);
		// counts the number of values in <x0, x0+d)
		do {
			while ((i <= sizeOfSample - 1) && (data[i] >= x0)
					&& (data[i] < x0 + d)) {
				count++;
				i++;
			}
			x.add(x0);
			// the value of histogram in <x0, x0+d)
			hist.add(count / (sizeOfSample * d));
			x0 += d;
			count = 0;
		} while (i <= sizeOfSample - 1);
		x.add(x0);

	}

	public static void setHistDistributionStoch(List<Double> x,
			List<Double> hist, double[] data) {
		int sizeOfSample = data.length;
		Arrays.sort(data);
		// the number of buckets rises linearly with upper bound
		// extra cells added for smaller sample sizes
		//int numOfBuckets = (int) (Math.round(0.65 * Math.pow(sizeOfSample, 0.5)));
		//numOfBuckets = Math.max(numOfBuckets, 5);
		int numOfBuckets = (int) (Math.round(0.01*sizeOfSample + 4));
		if (sizeOfSample < 175) {
			numOfBuckets++; // add one more bucket for small sample sizes
			if (sizeOfSample < 125) {
				numOfBuckets++;
				if (sizeOfSample < 75 && sizeOfSample > 40) {
					numOfBuckets++;
				}
			}
		}
		//numOfBuckets = Math.min(numOfBuckets, 13); // upper bound for number of buckets
		
		//numOfBuckets = Math.min(numOfBuckets, 16);
		/*
		 * if (sizeOfSample <= 20) { numOfBuckets = 5; } else if (sizeOfSample
		 * <= 40) { numOfBuckets = 7; } else if (sizeOfSample <= 60) {
		 * numOfBuckets = 8; } else if (sizeOfSample <= 100) { numOfBuckets =
		 * 10; } else if (sizeOfSample <= 250) { numOfBuckets = 20; } else {
		 * numOfBuckets = 25; }
		 */

		double portion = 1 / ((double) numOfBuckets);
		double xStart = (data[0] + data[1]) / 2 - (data[2] - data[0]) / 2;
		xStart = Math.min(xStart, data[0]);
		double xEnd = (data[sizeOfSample - 2] + data[sizeOfSample - 1]) / 2
				+ (data[sizeOfSample - 1] - data[sizeOfSample - 3]) / 2;
		xEnd = Math.max(xEnd, data[sizeOfSample - 1]);
		double ceil = 0;
		int i = 0;
		x.add(xStart);

		int numOfProgressedData = i;
		for (int j = 1; j < numOfBuckets; j++) {
			ceil = j * portion;
			i = (int) (ceil * sizeOfSample);
			// double x0 = (data[i] + data[i+1])/2;
			double x0 = (data[i] + data[i + 1]);
			x0 = x0 + sizeOfSample * (data[i + 1] - data[i])
					* (ceil - ((double) i) / sizeOfSample);
			x0 = x0 / 2;
			if (data[i + 1] < x0) {
				i++;
			}
			x.add(x0);
			double y = (((double) (i - numOfProgressedData)));
			hist.add(y);
			numOfProgressedData = i;
		}
		x.add(xEnd);
		hist.add(((double) (sizeOfSample - numOfProgressedData)));

		double d;
		for (int j = 0; j < hist.size(); j++) {
			d = x.get(j + 1) - x.get(j);
			hist.set(j, hist.get(j) / (sizeOfSample * d));
		}

	}

	private static double getHistCellLength(String distrType, double[] data) {
		int sizeOfSample = data.length;

		// ruzne moznosti, jak nastavit delku bunky v histogramu

		// double d = 2.0;

		/*
		 * int index = Math::Round (sizeOfSample * 0.25 , 0); double x_25 =
		 * data[index]; index = Math::Round (sizeOfSample * 0.75 , 0); double
		 * x_75 = data[index]; double exp = -1.0/3.0; double d = 2*(x_75-x_25)*
		 * pow(sizeOfSample,exp); // d = Math::Round(d,2);
		 */

		double d;
		if (distrType.equals("Cauchy")) {
			int index = (int) MathUtil.round(sizeOfSample * 0.25, 0);
			double x_25 = data[index]; // quartils
			index = (int) MathUtil.round(sizeOfSample * 0.75, 0);
			double x_75 = data[index];
			switch (sizeOfSample) { // needed to add break!!!!
			case 10:
				d = 2.8 * ((x_75 - x_25) / 2);
				break;
			case 20:
				d = 2.7 * ((x_75 - x_25) / 2);
				break;
			case 50:
				d = 2.4 * ((x_75 - x_25) / 2);
				break;
			case 120:
				d = 2.1 * ((x_75 - x_25) / 2);
				break;
			case 250:
				d = 1.6 * ((x_75 - x_25) / 2);
				break;
			default:
				d = (-0.00478 * sizeOfSample + 2.7957) * ((x_75 - x_25) / 2);
			}
		} else {
			if (distrType == "Uniform") {
				/*
				 * int index = Math::Round (sizeOfSample * 0.25 , 0); double
				 * x_25 = data[index]; index = Math::Round (sizeOfSample * 0.75
				 * , 0); double x_75 = data[index]; double exp = -1.0/3.0; d =
				 * 0.8 * 2.0*(x_75-x_25)* pow(sizeOfSample,exp);
				 */
				d = 0.2; // would be good to have a bit more flexible cell
							// length!!!
			} else {
				double EV, DV;
				EV = MathUtil.getExpVal(data);
				DV = MathUtil.getStandDev(EV, data);
				double exp = -1.0 / 3.0;
				d = 3.49 * DV * Math.pow(sizeOfSample, exp);
			}
		}
		d = MathUtil.round(d, 1); // round to just one decimal place
		d = Math.max(0.1, d);
		// would be extremely bad for cell lengths < 0.05!!!
		return d;
	}

	@Override
	public String toString() {
		return ("Power");
	}

	@Override
	public String getClassicTableName() {
		String densType = "";
		switch (densityType) {
		case DensityType.HISTOGRAM:
			densType = "Hist";
			break;
		case DensityType.HISTOGRAM_STOCH:
			densType = "Hist Stoch";
			break;
		case DensityType.KERNEL:
			densType = "Kern Gauss";
			break;
		case DensityType.KERNEL_TRIAG:
			densType = "Kern Triang";
			break;
		}
		return ("$ \\mathrm{Power " + densType + "}, \\alpha="
				+ OtherUtils.num2str(getPar()) + "$");
	}

	private double countDistanceFromDensity(Distribution distr,
			double[] empiricDensity) {
		double alpha = getPar();
		int sizeOfSample = empiricDensity.length;
		double eps = empiricDensity[sizeOfSample - 2]; // the size of the step
														// in numerical
														// integration
		double integral = 0.0; // the resulting integral approximation
		double y1, y2;

		double x = empiricDensity[sizeOfSample - 1]; // the starting point of
														// numerical integration

		// when either of the densities is 0, the increment is also 0
		for (int i = 0; i < sizeOfSample - 2; i++) {
			y1 = empiricDensity[i];
			if (y1 != 0.0) {
				y2 = distr.getfunctionValue(x);
				integral += Math.pow(y1, alpha) * Math.pow(y2, 1 - alpha);
			}
			x += eps;
		}

		integral = integral * eps;
		if (integral > 1) {
			// we don't want to get negative integral as final result
			integral = 1;
		}
		integral = 1 / (alpha * (1 - alpha)) * (1 - integral);
		return integral;
	}
	
	

}
