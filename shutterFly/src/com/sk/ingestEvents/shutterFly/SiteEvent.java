package com.sk.ingestEvents.shutterFly;

/**
 * SiteEvent is an abstract class to capture web site events
 * 
 * The properties stored are type and verb
 * 
 * @author skadival
 *
 */
public abstract class SiteEvent {
    // Fields

    private String type;
    private String verb;

    // Constructor
    public SiteEvent(String type, String verb) {
	super();
	this.type = type;
	this.verb = verb;
    }

    // getter and setter methods

    /**
     * Returns the value of verb
     * 
     * @return String
     */
    public String getVerb() {
	return verb;
    }

    /**
     * Allows to set the value of verb
     * 
     * @param verb
     */
    public void setVerb(String verb) {
	this.verb = verb;
    }

    /**
     * Returns the value of type
     * 
     * @return
     */
    public String getType() {
	return type;
    }

    /**
     * Allows to set the value of type
     * 
     * @param type
     */
    public void setType(String type) {
	this.type = type;
    }

}
