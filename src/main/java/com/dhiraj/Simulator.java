package com.dhiraj;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

/**
 * @author dkumar Simulation of retirement class based on Monte Carlo simulation
 *         The class is extending the Runnable so that it can be invoked by
 *         multiple threads
 *
 */
public class Simulator implements Runnable {

	private double principal;
	private int years;
	private double meanReturn;
	private double standardDeviation;
	private double inflationIndex;
	private int noIterations;
	private String description;

	/**
	 * Constructor to initialize values
	 * 
	 * @param principal
	 * @param years
	 * @param meanReturn
	 * @param standardDeviation
	 * @param inflationIndex
	 * @param noIterations
	 * @param description
	 */

	public Simulator(double principal, int years, double meanReturn, double standardDeviation, double inflationIndex,
			int noIterations, String description) {
		this.principal = principal;
		this.years = years;
		this.meanReturn = meanReturn;
		this.standardDeviation = standardDeviation;
		this.inflationIndex = inflationIndex;
		this.noIterations = noIterations;
		this.description = description;
	}

	/**
	 * Create a gaussian number for the mean and standard deviation and
	 * calculate the amount for the number of years . Adjust the final amount
	 * with inflation index and repeat the steps for number of iterations
	 *
	 */
	public void run() {

		double[] valuesAtYearEnd = new double[noIterations];

		for (int i = 0; i < noIterations; i++) {

			double vlaueAtYrEnd = 0, initialPrincipal = this.principal;

			for (int j = 1; j <= years; j++) {

				double gaussianReturn = new NormalDistribution(meanReturn, standardDeviation).sample();
				vlaueAtYrEnd = initialPrincipal * (1 + (gaussianReturn / 100));
				// System.out.print("Appreciation %% of " + gaussianReturn + "
				// Achieved for principal "
				// + initialPrincipal + "at end of year " + j + " is " +
				// vlaueAtYrEnd);
				initialPrincipal = vlaueAtYrEnd;
			}

			valuesAtYearEnd[i] = compoundAdjustment(vlaueAtYrEnd, inflationIndex, years);
			// System.out.println("Depreciated value of principal
			// "+initialPrincipal+ " at end of " +years+ "years is "+
			// valuesAtYearEnd[i]);
		}

		displayResults(valuesAtYearEnd, description);

	}

	/**
	 * 
	 * Return amount at the end of the year adjusted for inflation Calculate as
	 * A=P(1+r/100)^t
	 * 
	 * @param yrlyReturn
	 *            - normally distributed return value
	 * @param principal
	 *            - Principal amount at the start of the year
	 * @param inflationPct
	 *            - inflation
	 * @return return amount at the end of the year adjusted for inflation
	 */

	public static double compoundAdjustment(double principal, double inflationPct, int years) {

		// inflation as a percentage of principal at end of each year
		double depreciationRate = inflationPct / (100 + inflationPct);
		return principal * Math.pow((1 + (-depreciationRate)), years);

	}

	/**
	 * Helper method to display statistics
	 * 
	 * @param returns
	 * @param description
	 */
	public static void displayResults(double[] returns, String description) {

		Percentile percentile = new Percentile();
		System.out.println("Results for " + description + " Stratergy ");
		System.out.println("10 Percentire " + percentile.evaluate(returns, 10.0));
		System.out.println("50 Percentire " + percentile.evaluate(returns, 50.0));
		System.out.println("90 Percentire " + percentile.evaluate(returns, 90.0) + "\n");

	}

}
