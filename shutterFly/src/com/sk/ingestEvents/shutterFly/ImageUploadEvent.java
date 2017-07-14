package com.sk.ingestEvents.shutterFly;

/**
 * ImageUploadEvent captures information related to a customer uploading an
 * image to the shutterFly sites
 * 
 * It extends {@link com.sk.ingestEvents.shutterFly.SiteEvent}
 * 
 * @author skadival
 *
 */
public class ImageUploadEvent extends SiteEvent {

    String image_id;
    String event_time;
    String customer_id;
    String camera_make;
    String camera_model;

    public ImageUploadEvent(String type, String verb) {
	super(type, verb);
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("ImageUploadEvent [image_id=");
	builder.append(image_id);
	builder.append(", event_time=");
	builder.append(event_time);
	builder.append(", customer_id=");
	builder.append(customer_id);
	builder.append(", camera_make=");
	builder.append(camera_make);
	builder.append(", camera_model=");
	builder.append(camera_model);
	builder.append(", type=");
	builder.append(this.getType());
	builder.append(", verb=");
	builder.append(this.getVerb());
	builder.append("]");
	return builder.toString();
    }

    public String getImage_id() {
	return image_id;
    }

    public void setImage_id(String image_id) {
	this.image_id = image_id;
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

    public String getCamera_make() {
	return camera_make;
    }

    public void setCamera_make(String camera_make) {
	this.camera_make = camera_make;
    }

    public String getCamera_model() {
	return camera_model;
    }

    public void setCamera_model(String camera_model) {
	this.camera_model = camera_model;
    }

}
