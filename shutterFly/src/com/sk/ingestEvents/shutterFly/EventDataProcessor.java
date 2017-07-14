package com.sk.ingestEvents.shutterFly;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.time.temporal.IsoFields;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is the class which has methods to ingest the JSON events
 * and calculating the simple LTV
 * 
 * After ingestion the events the events are stored in an in memory
 * object which is a hashmap of type HashMap<String, CustomerActivity>
 * called {@link com.sk.ingestEvents.shutterFly.EventDataProcessor.dataSet}
 * 
 * The String is the customer id, all activity for a customer is embedded
 * in the {@link com.sk.ingestEvents.shutterFly.CustomerActivity} class
 * 
 * During ingestion the minimum and maximum dates are stored in minEventDate and maxEventDate
 * 
 * An additional field called numberOfWeeks
 * stores for how many weeks the data was seen, for instance if events are present for the first and last
 * week of a month then the calculation of LTV is done for 4 weeks
 * 
 * @author skadival
 *
 */
public class EventDataProcessor {
    
    /**
     * Least event date seen in the data set 
     */
    private LocalDate minEventDate;
    /**
     * Most recent event date seen in the data set
     */
    private LocalDate maxEventDate;
    /**
     * Number of weeks for which LTV will be calculated
     * 
     */
    private int numberOfWeeks;

    /**
     * Stores the events in memory for each customer
     * 
     * For example if we are processing data for customers A and B
     * 
     * after ingestion this dataSet will contain two entries
     * 
     * (A, CA) and (A,CB) where CA and CB are CustomerActivity objects
     * 
     * CA will contain (CAM, CAD) where 
     *  CAM -> CustomerActivityMetrics and
     *  CAD -> ArrayList&lt;SiteEvent&gt;
     * 
     * CB will contain (CBM, CBD) where 
     *  CBM -> CustomerActivityMetrics and
     *  CBD -> ArrayList&lt;SiteEvent&gt;  
     * 
     */
    private HashMap<String, CustomerActivity> dataSet;

    /**
     * Constructor 
     * 
     * Initializes the dataSet
     * 
     */
    public EventDataProcessor() {
	dataSet = new HashMap<>();
    }

    /**
     * Returns the numberOfWeeks field
     * 
     * @return int
     */
    public int getNumberOfWeeks() {
	return numberOfWeeks;
    }

    /**
     * Sets the numberOfWeeks using the values of
     * minEventDate and maxEventDate
     * 
     * Follows java.time and uses ISO 8601 standard
     * 
     * Monday is the start of the week
     * 
     */
    private void setNumberOfWeeks() {

	this.numberOfWeeks = (maxEventDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
		- minEventDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)) + 1;
    }

    /**
     * Returns the value of minEventDate
     * 
     * @return LocalDate
     */
    public LocalDate getMinEventDate() {
	return minEventDate;
    }

    /**
     * Returns the maxEventDate
     * 
     * @return LocalDate
     */
    public LocalDate getMaxEventDate() {
	return maxEventDate;
    }

    /**
     * Sets the maxEventDate and minEventDate based on the event_time
     * input parameter
     * 
     * The assumption is the String event_time follows the standard
     * LocalDate notation
     * 
     * See {@link java.time.LocalDate}
     * 
     * @param event_time String
     */
    private void setEventDateRanges(String event_time) {
	LocalDate current_event_date;
	try {
	    current_event_date = LocalDate.parse(event_time.substring(0, 10));
	} catch (DateTimeParseException e) {
	    e.printStackTrace();
	    return;
	}

	if (minEventDate == null || current_event_date.isBefore(minEventDate)) {
	    minEventDate = current_event_date;
	}

	if (maxEventDate == null || current_event_date.isAfter(maxEventDate)) {
	    maxEventDate = current_event_date;
	}
    }

    /**
     * This is the main ingestion routine and expects a path to a file
     * containing the entire list of JSON events as input
     * 
     * The method constructs a buffer from the input file by calling
     * {@link com.sk.ingestEvents.shutterFly.EventDataProcessor.buildJSONDataFromFile}
     * 
     * After processing entire file the number of weeks for which LTV will be 
     * calculated is determined using {@link com.sk.ingestEvents.shutterFly.EventDataProcessor.setNumberOfWeeks}
     * 
     * The entire JSONArray is parsed using {@link org.json.JSONArray} and {@link org.json.JSONObject}
     * 
     * Each JSONObject is handled using the method @link com.sk.ingestEvents.shutterFly.EventDataProcessor.ingestOneEvent}
     * 
     * @param inputfilepath String
     */
    public void ingest(String inputfilepath) {
	String jsonAsBuffer = buildJSONDataFromFile(inputfilepath);
	JSONArray ja = new JSONArray(jsonAsBuffer);
	for (int i = 0; i < ja.length(); i++) {
	    JSONObject jo = ja.getJSONObject(i);	    
	    ingestOneEvent(jo);
	}
	setNumberOfWeeks();
    }

    /**
     * This method is handling each event represented in JSON based on its
     * type. 
     * 
     * As the data is being ingested the date range is set using 
     * {@link com.sk.ingestEvents.shutterFly.EventDataProcessor.setEventDateRanges}
     * 
     * Optional elements are handled by catching the {@link org.json.JSONException}
     * 
     * When ingesting any event the siteVisitsTotal is incremented in the CustomerActivityMetrics
     * object for that customer_id
     * 
     * When processing an order event the orderAmountTotal is also calculated, corrections are made
     * for updates to existing orders
     * 
     * For an Order event out of sequence events and updates are handled, i,e. if 
     * a new order event is followed by an update to the same order event (match on order_id)
     * then the existing event is updated, else if a matching order event is present
     * but is a newer than the current order event then it is ignored
     * 
     * @param e {@link org.json.JSONObject}
     */
    private void ingestOneEvent(JSONObject e) {
	String type = e.get("type").toString().trim().toUpperCase();
	String verb = e.get("verb").toString().trim().toUpperCase();
	String customer_id;
	String event_time = e.getString("event_time").trim();
	setEventDateRanges(event_time);
	switch (type) {
	case "CUSTOMER":
	    // Get mandatory elements
	    customer_id = e.getString("key").trim();
	    // Handle optional elements inside try catch blocks
	    String last_name;
	    String adr_city;
	    String adr_state;
	    CustomerEvent C;
	    try {
		last_name = e.getString("last_name");
	    } catch (JSONException ex) {
		last_name = "";
	    }

	    try {
		adr_city = e.getString("adr_city");
	    } catch (JSONException ex) {
		adr_city = "";
	    }

	    try {
		adr_state = e.getString("adr_state");
	    } catch (JSONException ex) {
		adr_state = "";
	    }
	    // Build customer event object and add to collection inside hash map
	    // for current customer id

	    C = new CustomerEvent(type, verb);
	    C.setEvent_time(event_time);
	    C.setLast_name(last_name);
	    C.setCustomer_id(customer_id);
	    C.setAdr_city(adr_city);
	    C.setAdr_state(adr_state);

	    // Confirms if an entry exists for that customer id and adds an
	    // entry to the array list

	    dataSet.computeIfAbsent(customer_id, c -> new CustomerActivity());
	    dataSet.get(customer_id).getActivityMetrics().incrementSiteVisits();
	    dataSet.get(customer_id).getActivityDetails().add(C);
	    C = null;
	    break;
	case "SITE_VISIT":
	    customer_id = e.getString("customer_id").trim();
	    String page_id = e.getString("key").trim();
	    ArrayList<Tags> a = new ArrayList<Tags>();
	    JSONArray tags;
	    SiteVisitEvent S;
	    try {
		tags = e.getJSONArray("tags");
		for (int i = 0; i < tags.length(); i++) {
		    JSONObject tag = tags.getJSONObject(i);
		    String[] keys = JSONObject.getNames(tag);

		    for (String key : keys) {
			String val = tag.getString(key);
			Tags t = new Tags(key, val);
			a.add(t);
		    }
		}
	    } catch (JSONException ex) {
		tags = null;
	    }
	    S = new SiteVisitEvent(type, verb);
	    S.setCustomer_id(customer_id);
	    S.setEvent_time(event_time);
	    S.setPage_id(page_id);
	    S.setTags(a);

	    dataSet.computeIfAbsent(customer_id, c -> new CustomerActivity());
	    dataSet.get(customer_id).getActivityMetrics().incrementSiteVisits();
	    dataSet.get(customer_id).getActivityDetails().add(S);
	    S = null;

	    break;
	case "IMAGE":
	    customer_id = e.getString("customer_id").trim();
	    String image_id = e.getString("key").trim();
	    String camera_make;
	    String camera_model;
	    ImageUploadEvent I;
	    try {
		camera_make = e.getString("camera_make");
	    } catch (JSONException ex) {
		camera_make = "";
	    }

	    try {
		camera_model = e.getString("camera_model");
	    } catch (JSONException ex) {
		camera_model = "";
	    }
	    I = new ImageUploadEvent(type, verb);
	    I.setCustomer_id(customer_id);
	    I.setEvent_time(event_time);
	    I.setImage_id(image_id);
	    I.setCamera_make(camera_make);
	    I.setCamera_model(camera_model);

	    dataSet.computeIfAbsent(customer_id, c -> new CustomerActivity());
	    dataSet.get(customer_id).getActivityMetrics().incrementSiteVisits();
	    dataSet.get(customer_id).getActivityDetails().add(I);

	    I = null;
	    break;
	case "ORDER":
	    customer_id = e.getString("customer_id").trim();
	    String order_id = e.getString("key").trim();
	    String total_amount = e.getString("total_amount").trim();
	    double amt = Double.parseDouble(total_amount.replace("USD", ""));
	    OrderEvent O = new OrderEvent(type, verb);
	    O.setCustomer_id(customer_id);
	    O.setEvent_time(event_time);
	    O.setOrder_id(order_id);
	    O.setTotal_amount(total_amount);

	    dataSet.computeIfAbsent(customer_id, c -> new CustomerActivity());

	    dataSet.get(customer_id).getActivityMetrics().incrementSiteVisits();

	    // Confirm this is not an update to an existing order
	    // check if existing order is present for the same
	    // order id
	    int matchingOrderIdIndex = dataSet.get(customer_id).findMatchingOrderIdIndex(order_id);
	    if (matchingOrderIdIndex > 0) {
		OrderEvent existingOrder = (OrderEvent) dataSet.get(customer_id).getActivityDetails().get(matchingOrderIdIndex);
		ZonedDateTime existingEventTime = ZonedDateTime.parse(existingOrder.getEvent_time());
		ZonedDateTime currentEventTime = ZonedDateTime.parse(event_time);
		if (existingEventTime.isBefore(currentEventTime)) {
		    double currentOrderTotal = dataSet.get(customer_id).getActivityMetrics().getOrderAmountTotal();
		    currentOrderTotal -= Double.parseDouble(existingOrder.getTotal_amount().replace("USD", ""));
		    currentOrderTotal += amt;
		    dataSet.get(customer_id).getActivityMetrics().setOrderAmountTotal(currentOrderTotal);
		    dataSet.get(customer_id).getActivityDetails().remove(matchingOrderIdIndex);
		    dataSet.get(customer_id).getActivityDetails().add(O);
		}

	    }

	    else {

		dataSet.get(customer_id).getActivityMetrics().incrementOrderAmount(amt);

		dataSet.get(customer_id).getActivityDetails().add(O);
	    }

	    break;
	default:
	    customer_id = "";
	    break;
	}
    }

    private String buildJSONDataFromFile(String inputFilePath) {
	String jsonData = "";
	BufferedReader br = null;
	try {
	    String line;
	    br = new BufferedReader(new FileReader(inputFilePath));
	    while ((line = br.readLine()) != null) {
		jsonData += line + "\n";
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (br != null)
		    br.close();
	    } catch (IOException ex) {
		ex.printStackTrace();
	    }
	}
	return jsonData;
    }

    public void PrintEventDataSet() {
	System.out.println("-----------------------------------------------------------");
	for (String key : dataSet.keySet()) {
	    CustomerActivity ca = dataSet.get(key);
	    System.out.println("Customer id: " + key + " Metrics " + ca.getActivityMetrics().toString());
	    ArrayList<SiteEvent> events = ca.getActivityDetails();
	    for (int i = 0; i < events.size(); i++) {
		SiteEvent E = events.get(i);
		switch (E.getType()) {
		case "CUSTOMER":
		    CustomerEvent C = (CustomerEvent) E;
		    System.out.println(C.toString());
		    break;
		case "SITE_VISIT":
		    SiteVisitEvent S = (SiteVisitEvent) E;
		    System.out.println(S.toString());
		    break;
		case "IMAGE":
		    ImageUploadEvent I = (ImageUploadEvent) E;
		    System.out.println(I.toString());
		    break;
		case "ORDER":
		    OrderEvent O = (OrderEvent) E;
		    System.out.println(O.toString());
		    break;
		}
	    }
	}
	System.out.println("-----------------------------------------------------------");
    }

    public void TopXSimpleLTVCustomers(int x) {
	ArrayList<CustomerAcitivityMetrics> cmTopX = new ArrayList<>();

	// build sorted list by average customer spend per week in memory

	for (String key : dataSet.keySet()) {
	    CustomerActivity ca = dataSet.get(key);
	    ca.getActivityMetrics().calculateAndSetSimpleLTV(getNumberOfWeeks());
	    ca.getActivityMetrics().setCustomerId(key);
	    CustomerAcitivityMetrics agg = ca.getActivityMetrics();
	    cmTopX.add(agg);
	}

	// sort the collection in reverse order
	Collections.sort(cmTopX, Collections.reverseOrder());

	try {
	    File output = new File("./output/output.txt");

	    // create output file is not already present

	    if (!output.exists()) {
		output.createNewFile();
	    }

	    FileWriter fw = new FileWriter(output, false);
	    BufferedWriter bw = new BufferedWriter(fw);
	    PrintWriter pw = new PrintWriter(bw);

	    pw.println("-----------------------------------------------------------");
	    pw.println("| List of top " + x + " simple LTV customers");
	    pw.println("-----------------------------------------------------------");
	    pw.println();

	    // print each customer line till either the list is exhausted or we
	    // reach the upper limit of customers

	    for (int i = 0; i < Math.min(x, cmTopX.size()); i++) {
		BigDecimal ltv = new BigDecimal(cmTopX.get(i).getSimpleLTV());
		ltv = ltv.setScale(2, BigDecimal.ROUND_HALF_UP);
		pw.println(" Customer id: " + cmTopX.get(i).getCustomerId() + " has simple LTV of: " + ltv + " USD");
	    }

	    pw.println();
	    pw.println("-----------------------------------------------------------");

	    // clear the sorted list collection
	    cmTopX.clear();

	    // close writers
	    bw.close();
	    pw.close();
	}

	catch (IOException i) {
	    i.printStackTrace();
	}

    }

}
