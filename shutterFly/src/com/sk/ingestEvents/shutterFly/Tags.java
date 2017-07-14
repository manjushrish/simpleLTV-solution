package com.sk.ingestEvents.shutterFly;

/**
 * Tags class to capture tags found in site visit
 * 
 * Used by {@link com.sk.ingestEvents.shutterFly.SiteVisitEvent}
 * 
 * @author skadival
 *
 */
public class Tags {
    String key;
    String value;

    public Tags(String key, String value) {
	super();
	this.key = key;
	this.value = value;
    }

    public String getKey() {
	return key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

}
