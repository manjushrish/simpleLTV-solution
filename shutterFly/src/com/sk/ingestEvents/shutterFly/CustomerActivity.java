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
     * 
     */
    public CustomerActivity() {
	activityMetrics = new CustomerAcitivityMetrics();
	activityDetails = new ArrayList<SiteEvent>();
    }

    // getter and setter

    public CustomerAcitivityMetrics getActivityMetrics() {
	return activityMetrics;
    }

    public void setActivityMetrics(CustomerAcitivityMetrics activityMetrics) {
	this.activityMetrics = activityMetrics;
    }

    public ArrayList<SiteEvent> getActivityDetails() {
	return activityDetails;
    }

    public void setActivityDetails(ArrayList<SiteEvent> activityDetails) {
	this.activityDetails = activityDetails;
    }

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
