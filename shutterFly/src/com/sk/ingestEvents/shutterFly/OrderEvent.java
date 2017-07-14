package com.sk.ingestEvents.shutterFly;

/**
 * OrderEvent captures information to an order placed (new and update) by a
 * customer on the site
 * 
 * It extends {@link com.sk.ingestEvents.shutterFly.SiteEvent}
 * 
 * @author skadival
 *
 */
public class OrderEvent extends SiteEvent {

    String order_id;
    String event_time;
    String customer_id;
    String total_amount;

    public OrderEvent(String type, String verb) {
	super(type, verb);
	// TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("OrderEvent [order_id=");
	builder.append(order_id);
	builder.append(", event_time=");
	builder.append(event_time);
	builder.append(", customer_id=");
	builder.append(customer_id);
	builder.append(", total_amount=");
	builder.append(total_amount);
	builder.append(", type=");
	builder.append(this.getType());
	builder.append(", verb=");
	builder.append(this.getVerb());
	builder.append("]");
	return builder.toString();
    }

    public String getOrder_id() {
	return order_id;
    }

    public void setOrder_id(String order_id) {
	this.order_id = order_id;
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

    public String getTotal_amount() {
	return total_amount;
    }

    public void setTotal_amount(String total_amount) {
	this.total_amount = total_amount;
    }
}
