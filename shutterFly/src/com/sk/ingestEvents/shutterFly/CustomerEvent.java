package com.sk.ingestEvents.shutterFly;

/**
 * CustomerEvent is a sub type of SiteEvent and represents an event when a new
 * customer visits the site or an existing customer updates his information
 * 
 * @author skadival
 *
 */
public class CustomerEvent extends SiteEvent {

    // Fields
    private String customer_id;
    private String event_time;
    private String last_name;
    private String adr_city;
    private String adr_state;

    // Constructor
    public CustomerEvent(String type, String verb) {
	super(type, verb);
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("CustomerEvent [customer_id=");
	builder.append(customer_id);
	builder.append(", event_time=");
	builder.append(event_time);
	builder.append(", last_name=");
	builder.append(last_name);
	builder.append(", adr_city=");
	builder.append(adr_city);
	builder.append(", adr_state=");
	builder.append(adr_state);
	builder.append(", type=");
	builder.append(this.getType());
	builder.append(", verb=");
	builder.append(this.getVerb());
	builder.append("]");
	return builder.toString();
    }

    // getter and setter methods
    public String getCustomer_id() {
	return customer_id;
    }

    public void setCustomer_id(String customer_id) {
	this.customer_id = customer_id;
    }

    public String getEvent_time() {
	return event_time;
    }

    public void setEvent_time(String event_time) {
	this.event_time = event_time;
    }

    public String getLast_name() {
	return last_name;
    }

    public void setLast_name(String last_name) {
	this.last_name = last_name;
    }

    public String getAdr_city() {
	return adr_city;
    }

    public void setAdr_city(String adr_city) {
	this.adr_city = adr_city;
    }

    public String getAdr_state() {
	return adr_state;
    }

    public void setAdr_state(String adr_state) {
	this.adr_state = adr_state;
    }

}
