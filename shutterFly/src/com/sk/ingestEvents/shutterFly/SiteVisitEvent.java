package com.sk.ingestEvents.shutterFly;

import java.util.List;

/**
 * SiteVisitEvent captures the information related to a customer visiting the
 * shutterFly sites, it extends {@link com.sk.ingestEvents.shutterFly.SiteEvent}
 * 
 * @author skadival
 *
 */
public class SiteVisitEvent extends SiteEvent {

    String page_id;
    String event_time;
    String customer_id;
    List<Tags> tags;

    public SiteVisitEvent(String type, String verb) {
	super(type, verb);
    }

    @Override
    public String toString() {
	final int maxLen = 10;
	StringBuilder builder = new StringBuilder();
	builder.append("SiteVisitEvent [page_id=");
	builder.append(page_id);
	builder.append(", event_time=");
	builder.append(event_time);
	builder.append(", customer_id=");
	builder.append(customer_id);
	builder.append(", tags=");
	builder.append(tags != null ? tags.subList(0, Math.min(tags.size(), maxLen)) : null);
	builder.append(", type=");
	builder.append(this.getType());
	builder.append(", verb=");
	builder.append(this.getVerb());
	builder.append("]");
	return builder.toString();
    }

    public String getPage_id() {
	return page_id;
    }

    public void setPage_id(String page_id) {
	this.page_id = page_id;
    }

    public String getEvent_time() {
	return event_time;
    }

    public void setEvent_time(String event_time) {
	this.event_time = event_time;
    }

    public String getCustomer_id() {
	return customer_id;
    }

    public void setCustomer_id(String customer_id) {
	this.customer_id = customer_id;
    }

    public List<Tags> getTags() {
	return tags;
    }

    public void setTags(List<Tags> tags) {
	this.tags = tags;
    }
}
