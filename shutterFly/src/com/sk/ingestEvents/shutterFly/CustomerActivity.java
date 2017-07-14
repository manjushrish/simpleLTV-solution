/**
 * 
 */
package com.sk.ingestEvents.shutterFly;

import java.util.ArrayList;

/**
 * This holds all web site activity for a customer
 * 
 * It also holds the customer metrics for a customers activity
 * 
 * @author skadival
 *
 */
public class CustomerActivity {

    // Fields
    CustomerAcitivityMetrics activityMetrics;
    ArrayList<SiteEvent> activityDetails;

    /**
     * Constructor
     */
    public CustomerActivity() {
	activityMetrics = new CustomerAcitivityMetrics();
	activityDetails = new ArrayList<SiteEvent>();
    }

    // getter and setter

    /**
     * Returns the activityMetrics
     * 
     * @return CustomerAcitivityMetrics
     */
    public CustomerAcitivityMetrics getActivityMetrics() {
	return activityMetrics;
    }

    /**
     * Sets the activityMetrics
     * 
     * @param activityMetrics CustomerAcitivityMetrics
     */
    public void setActivityMetrics(CustomerAcitivityMetrics activityMetrics) {
	this.activityMetrics = activityMetrics;
    }

    /**
     * Returns activityDetails
     * 
     * @return ArrayList&lt;SiteEvent&gt;
     */
    public ArrayList<SiteEvent> getActivityDetails() {
	return activityDetails;
    }

    /**
     * Sets the activityDetails
     * 
     * @param activityDetails ArrayList&lt;SiteEvent&gt;
     */
    public void setActivityDetails(ArrayList<SiteEvent> activityDetails) {
	this.activityDetails = activityDetails;
    }

    /**
     * Applies only to an OrderEvent, checks if an existing order with the
     * same order id is currently present in the memory structure for a customer
     * 
     * If found returns the index, else returns -1
     * 
     * @param orderId String
     * @return int
     */
    public int findMatchingOrderIdIndex(String orderId) {
	int matchingOrderIdIndex = -1;
	int i = 0;
	while (matchingOrderIdIndex < 0 && i < activityDetails.size()) {
	    SiteEvent E = activityDetails.get(i);
	    switch (E.getType()) {
	    case "CUSTOMER":
	    case "SITE_VISIT":
	    case "IMAGE":
		break;
	    case "ORDER":
		String existingOrderId = ((OrderEvent) E).getOrder_id();
		if (orderId.equalsIgnoreCase(existingOrderId)) {
		    matchingOrderIdIndex = i;
		}
		break;
	    }
	    i++;
	}
	return matchingOrderIdIndex;
    }

}
