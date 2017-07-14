package com.sk.ingestEvents.shutterFly;

import java.io.FileNotFoundException;

import org.json.JSONException;

/**
 * The JSONEventProcessor program implements the ingestion of events stored in
 * JSON format from shutter fly's public websites into an in memory data
 * structure.
 * 
 * Using the ingested events the top x simple LTV customers are printed in an
 * output file in the output directory
 * 
 * @author skadival
 * @version 1.0
 * @since 7/12/2017
 *
 */
public class JSONEventProcessor {

    public static void main(String[] args) throws FileNotFoundException, JSONException {
	String inputFilePath = "./input/input.txt";
	EventDataProcessor ep = new EventDataProcessor();
	ep.ingest(inputFilePath);
	ep.PrintEventDataSet();
	System.out.println("Min date: " + ep.getMinEventDate());
	System.out.println("Max date: " + ep.getMaxEventDate());
	System.out.println("Calculation will be for: " + ep.getNumberOfWeeks() + " weeks");
	ep.TopXSimpleLTVCustomers(10);	
	
	
    }
}
