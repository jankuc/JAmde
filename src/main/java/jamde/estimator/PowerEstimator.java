package jamde.estimator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import jamde.MathUtil;
import jamde.OtherUtils;
import jamde.distribution.Distribution;

public class PowerEstimator extends Estimator {

	public int densityType;

	public static class DensityType {
		public static final int HISTOGRAM = 0;
		public static final int KERNEL = 1;
	}

	// TODO read density type from file
	public PowerEstimator(double par) {
		addPar(par);
		setDensityType(DensityType.HISTOGRAM);
	}

	public PowerEstimator() {
		this(0.5); // creates Hellinger estimator by default
	}

	public void setDensityType(int type) {
		if (type == DensityType.HISTOGRAM || type == DensityType.KERNEL) {
			this.densityType = type;
		}
	}

	@Override
	public double countDistance(Distribution distr, double[] data) {
		if (densityType == DensityType.HISTOGRAM) {
			return countDistanceHist(distr, data);
		} else {
			return countDistanceKernel(distr, data);
		}
	}

	private double countDistanceHist(Distribution distr, double[] data) {
		Arrays.sort(data);
		double alpha = getPar();
		double y1, y2;
		double x0 = data[0];
		double eps = 0.1;
		double h;
		double Integral = 0;

		String type = distr.toString();
		int sizeOfSample = data.length;

		double d = getHistCellLength(type, data); // calculates the best cell length for histogram

		x0 = Math.floor(x0); // the starting point of histogram

		int i = 0;

		if (type == "Uniform") {
			// unif density is partially constant function
			double p1, p2, f;
			p1 = distr.getP1();
			p2 = distr.getP2();
			f = distr.getfunctionValue((p2 + p1) / 2.0);
			f = Math.pow(f, 1 - alpha);

			double x1, x2;
			do {
				int count = 0;
				// counts the number of values in <x0, x0+d)
				if ((i <= sizeOfSample - 1) && (data[i] < x0 + d)) {
					while ((i <= sizeOfSample - 1) && (data[i] >= x0)
							&& (data[i] < x0 + d)) {
						count++;
						i++;
					}
				}

				// the value of histogram in <x0, x0+d)
				h = count / (sizeOfSample * d);
				h = Math.pow(h, alpha);

				x1 = Math.max(p1, x0);
				x2 = Math.min(x0 + d, p2);
				if (x2 > x1) {
					Integral += (x2 - x1) * f * h;
				}
				x0 = x0 + d;
			} while (x0 < data[sizeOfSample - 1]);
			// stop when histogram values are zeros

		} else {
			if (type == "Weibull") {
				// need to bound the values
				do {
					// counts the number of values in <x0, x0+d)
					int count = 0;
					if ((i <= sizeOfSample - 1) && (data[i] < x0 + d)) {
						while ((i <= sizeOfSample - 1) && (data[i] >= x0)
								&& (data[i] < x0 + d)) {
							count++;
							i++;
						}
					}
					// histogram value in <x0, x0 + d)
					h = count / (sizeOfSample * d);
					double end = MathUtil.round(x0 + d, 3);

					if (h != 0) { // integral na < x0,x0+d >
						h = Math.pow(h, alpha);
						y1 = distr.getfunctionValue(x0);
						y1 = Math.pow(y1, 1 - alpha);
						if (y1 > 1) {
							y1 = 1;
						}
						do {
							y2 = distr.getfunctionValue(x0 + eps);
							y2 = Math.pow(y2, 1 - alpha);
							if (y2 > 1) {
								y2 = 1;
							}
							Integral += 0.5 * eps * (y1 + y2) * h;
							x0 = MathUtil.round(x0 + eps, 3);
							y1 = y2;
						} while (x0 < end);
					} else {
						x0 = end;
					}
				} while (x0 < data[sizeOfSample - 1]);
			} else {
				do {
					int count = 0;
					if ((i <= sizeOfSample - 1) && (data[i] < x0 + d)) {
						while ((i <= sizeOfSample - 1) && (data[i] >= x0)
								&& (data[i] < x0 + d)) {
							count++;
							i++;
						}
					}

					h = count / (sizeOfSample * d);
					double end = MathUtil.round(x0 + d, 3);

					if (h != 0) {
						h = Math.pow(h, alpha);
						y1 = distr.getfunctionValue(x0);
						y1 = Math.pow(y1, 1 - alpha);
						do // integral in < x0, x0 + d )
						{
							y2 = distr.getfunctionValue(x0 + eps);
							y2 = Math.pow(y2, 1 - alpha);
							Integral += 0.5 * eps * (y1 + y2) * h;
							x0 = MathUtil.round(x0 + eps, 3);
							y1 = y2;
						} while (x0 < end);
					} else {
						x0 = end;
					}
				} while (x0 < data[sizeOfSample - 1]);
			}
		}

		if (Integral > 1)
			Integral = 1;
		Integral = 1 / (alpha * (1 - alpha)) * (1 - Integral);
		return Integral;
	}

	private double countDistanceKernel(Distribution distr, double[] data) {
		double alpha = getPar();
		double[] empiricDensity = getKernelDensity(data);
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

	private double[] getKernelDensity(double[] data) {
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

		h = 1.05 * A * Math.pow(sizeOfSample, -0.2); // the smoothing parameter
		// mult was 0.9!!!!!! // (bandwidth) (if too big -
		// if the bandwidth is too big - data oversmoothed, if too small - data
		// undersmoothed
		// for Normally distributed data - ideal mult is 1.06
		// where did we get 0.9?????

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
	
	/**
	 * Prints to file 2 lines:
	 * 		x
	 * 		kernelDensity(x)
	 * @param file
	 * @param data
	 */
	public void printDensityToFile(String file, double[] data) {
		double[] density = getKernelDensity(data);

		int size = density.length;
		double eps = density[size - 2];
		double x = density[size - 1];

		try {
			PrintWriter out = new PrintWriter(new File(file));
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
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}
	
	/**
	 * Prints to file 2 lines:
	 * 		x
	 * 		histogram value in <x, x + d) where d is the calculated optimal cell length
	 * @param file
	 * @param distr
	 * @param data
	 */
	public void printHistogramToFile(String file, Distribution distr, double[] data) {
		Arrays.sort(data);
		double x0 = data[0];
		double h;

		int sizeOfSample = data.length;
		// calculates the best cell length
		double d = getHistCellLength(distr.toString(), data); 

		x0 = Math.floor(x0); // the starting point of histogram

		int i = 0;

		ArrayList<Double> hist = new ArrayList<Double>();
		ArrayList<Double> x = new ArrayList<Double>();

		do {
			int count = 0;
			// counts the number of values in <x0, x0+d)
			if ((i <= sizeOfSample - 1) && (data[i] < x0 + d)) {
				while ((i <= sizeOfSample - 1) && (data[i] >= x0)
						&& (data[i] < x0 + d)) {
					count++;
					i++;
				}
			}

			// the value of histogram in <x0, x0+d)
			h = count / (sizeOfSample * d);
			x.add(x0); // add the x point to the list
			hist.add(h); // add the histogram value to the list
			x0 = x0 + d;
		} while (x0 < data[sizeOfSample - 1]);
		// stop when histogram values are zeros
		
		try {
			PrintWriter out = new PrintWriter(new File(file));
			for (int j = 0; j < x.size(); j++) {
				out.write(x.get(j) + " ");
			}
			out.write("\n");
			for (int j = 0; i < x.size(); i++) {
				out.write(hist.get(j) + " ");
			}
			out.write("\n");
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}

	private double getHistCellLength(String distrType, double[] data) {
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
		if (distrType == "Cauchy") {
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
		// would be extremely bad for cell lengths < 0.05!!!
		return d;
	}

	@Override
	public String toString() {
		return ("Power");
	}

	@Override
	public String getClassicTableName() {
		String densType = (densityType == DensityType.HISTOGRAM) ? "Hist" : "Kern";
		return ("$ \\mathrm{Power " + densType + "}, \\alpha=" + OtherUtils.num2str(getPar()) + "$");
	}
}
