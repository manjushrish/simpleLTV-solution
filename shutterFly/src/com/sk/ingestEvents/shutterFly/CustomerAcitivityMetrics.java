/**
 * 
 */
package com.sk.ingestEvents.shutterFly;

/**
 * This is a class which holds the aggregated customer metrics and stores each
 * event assocated to a customer in an ArrayList
 * 
 * Metrics such as total site visits and total order amount is set during the
 * ingestion process
 * 
 * Our ingestion data set will contain multiple CustomerAggregateInformation,
 * one for each customer (identified by customer_id)
 * 
 * This implements Comparable so that we can sort a an ArrayList object of CustomerAcitivityMetrics
 * using Collections.sort to print the LTVs of customers 
 * 
 * @author skadival
 *
 */

public class CustomerAcitivityMetrics implements Comparable<CustomerAcitivityMetrics> {

    // Constants
    private final int AVERAGE_CUSTOMER_LIFESPAN = 10;

    // Fields
    private int siteVisitsTotal;
    private double orderAmountTotal;
    private double simpleLTV;
    private String customerId;

    /**
     * Default constructor
     */
    public CustomerAcitivityMetrics() {
	siteVisitsTotal = 0;
	orderAmountTotal = 0;
	simpleLTV = 0;
	customerId = "";
    }

    // getters and setters

    /**
     * Returns the siteVisitsTotal
     * 
     * @return int
     */
    public int getSiteVisitsTotal() {
	return siteVisitsTotal;
    }

    /**
     * Returns the orderAmountTotal
     * 
     * @return double
     */
    public double getOrderAmountTotal() {
	return orderAmountTotal;
    }

    /**
     * Sets the value for orderAmountTotal
     * 
     * @param orderAmountTotal double
     */
    public void setOrderAmountTotal(double orderAmountTotal) {
        this.orderAmountTotal = orderAmountTotal;
    }
    
    /**
     * Returns the simpleLTV value
     * 
     * @return double
     */
    public double getSimpleLTV() {
        return simpleLTV;
    }

    /**
     * Returns the customerId
     * 
     * @return String
     */
    public String getCustomerId() {
	return customerId;
    }

    /**
     * Sets the customerId
     * 
     * @param customerId String
     */
    public void setCustomerId(String customerId) {
	this.customerId = customerId;
    }

    // general use methods

    /**
     * Increments the siteVisitsTotal by 1
     */
    public void incrementSiteVisits() {
	siteVisitsTotal++;
    }

    /**
     * Increases the orderAmountTotal by the currentOrderAmount passed
     * 
     * @param currentOrderAmount double
     */
    public void incrementOrderAmount(double currentOrderAmount) {
	orderAmountTotal += currentOrderAmount;
    }

    /**
     * Calculates and returns the SiteVisitsPerWeek
     * 
     * @param totalWeeks int
     * @return double
     */
    public double returnSiteVisitsPerWeek(int totalWeeks) {
	return (siteVisitsTotal / totalWeeks);
    }

    /**
     * Calculates and returns orderAmountPerVisit
     * 
     * @return double
     */
    public double returnOrderAmountPerVisit() {
	return (orderAmountTotal / siteVisitsTotal);
    }

    /**
     * Calculates the simpleLTV value for totalWeeks passed 
     * 
     * @param totalWeeks int
     * @return double
     */
    public double returnSimpleLTV(int totalWeeks) {
	// 52(a) * t
	// a is average customer value per week
	// t is average customer life span
	return (52 * (orderAmountTotal / totalWeeks) * AVERAGE_CUSTOMER_LIFESPAN);
    }
    
    /**
     * Calculates simple LTV, sets it in the field simpleLTV
     * 
     * @param totalWeeks int
     */
    public void calculateAndSetSimpleLTV(int totalWeeks) {
	simpleLTV = returnSimpleLTV(totalWeeks);
    }

    @Override
    public int compareTo(CustomerAcitivityMetrics o) {
	return new Double(simpleLTV).compareTo(o.simpleLTV);
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("CustomerAggregateInformation [siteVisitsTotal=");
	builder.append(siteVisitsTotal);
	builder.append(", orderAmountTotal=");
	builder.append(orderAmountTotal);
	builder.append("]");
	return builder.toString();
    }

}
