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

    public int getSiteVisitsTotal() {
	return siteVisitsTotal;
    }

    public double getOrderAmountTotal() {
	return orderAmountTotal;
    }

    public void setOrderAmountTotal(double orderAmountTotal) {
        this.orderAmountTotal = orderAmountTotal;
    }

    public double getSimpleLTV() {
	return simpleLTV;
    }

    public String getCustomerId() {
	return customerId;
    }

    public void setCustomerId(String customerId) {
	this.customerId = customerId;
    }

    // general use methods

    public void incrementSiteVisits() {
	siteVisitsTotal++;
    }

    public void incrementOrderAmount(double currentOrderAmount) {
	orderAmountTotal += currentOrderAmount;
    }

    public double returnSiteVisitsPerWeek(int totalWeeks) {
	return (siteVisitsTotal / totalWeeks);
    }

    public double returnOrderAmountPerVisit() {
	return (orderAmountTotal / siteVisitsTotal);
    }

    public double returnSimpleLTV(int totalWeeks) {
	// 52(a) * t
	// a is average customer value per week
	// t is average customer life span
	return (52 * (orderAmountTotal / totalWeeks) * AVERAGE_CUSTOMER_LIFESPAN);
    }

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
