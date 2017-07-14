# Calculate simple LTV

This project is built in java to ingest JSON events presented in an input file and store them into an in memory HashMap. The hashmap key is the customer id present in the data. This allows us to store all events related to the same customer in one easily referencable location. 

Metrics such as site visits and total order amounts are calculated and stored during ingestion. The raw data associated to each type of web site event is also stored for reference. The ingested data is then used to calculate the simple LTV using the 52(a) * r where a is averge value per week and r is customer retention which is 10 years.

From the ingested data a is calculated by dividing the total order amount by the number of weeks for which the data exists.

## External libraries used

I have used the org.json library for parsing the JSON events into java objects. The dependancy is specified in the pom.xml file located in the same place as this README file.

### Assumptions

I made the following assumptions when building this solution

1. The date format for the event_time attribute will follow the standard ISO 861 format.

```
Sample date format assumed
2017-07-03T12:46:46.384Z
```

2. Orders are identified by key in the JSON data. An update to an order will have the same key.

3. Customers are identified by customer_id or key in the customer event, i.e. events related to the same customer will have the same customer_id or key.

4. Begining of the week is considered as Monday per ISO standards used in java.time

5. Order amounts will contain some numeric values and may or may not have USD in it.

6. Each type of event is assumed to be a site visit.

### Out of order events

The input events can be presented in any order and the application doesn't assume any order. If the event matches an existing customerid then the information is captured in that customer id's bucket using the HashMap.

Updates to orders are handled but the application doesnt assume the order in which the Order events are provided as input is the correct order. The event_time is used to confirm that an update to an existing order has a newer event_time than the existing order.

If Orders are received out of sequence, i.e. an update Order event for a customer was received and processed before a new order event then the new order event is ignored.

## Running the application

Ensure the org.json dependancies are met and execute the Java application called JSONEventProcessor

### Sample input and output explained

The input file provided contains data for 25 customers with orders spanning the month of July. Each customer has orders and there are some special cases.

Customer_Id|Total orders|Total order amount
-----|----|----
00100|2|100+2450 = 2550
00101|1|200
00102|1|300
00103|1|400
00104|1|500
00105|1|600
00106|1|700
00107|1|800
...increasing by 100$ and each has 1 order
00115|1|Original order is 1600, update is 3000, received in correct sequence
00124|1|Update is received first for 2350, followed by original for 2500

Special cases are for customer id 00115 and 00124

In the output, customer id 00115 spent the most has the highest LTV in the output, followed by 00100, then 00123 followed by 00124 due to the lower order total received.

### Class design

To store the JSON events I used a HashMap of type <String, CustomerActivity>. The key for the hashmap is the customer id found in the data for each event. This allows me to store all the events for one customer in one place.

CustomerActivity contains two objects 
1. CustomerActivityMetrics
2. ArrayList of type SiteEvent

CustomerActivityMetrics is simple class holding the customer's acitivity metrics such as total site visits, total order amount etc

SiteEvent is a abstract class to support each type of event. This class sub types for each event type such as CustomerEvent, ImageUploadEvent, OrderEvent and SiteVisitEvent. Each of these has attributes specific to its type.

The main processing is done using methods inside the EventDataProcessor class namely ingest and TopXSimpleLTVCustomers. EventDataProcessor has the following private fields

1. LocalDate minEventDate
2. LocalDate maxEventDate
3. int numberOfWeeks
4. HashMap<String, CustomerActivity> dataSet

The values for the above are set during ingestion in the ingest method.

The minEventDate is the least event date across all events and maxEventDate is the maximum. The numberOfWeeks is calculated as difference between the week numbers + 1. 

During ingestion of the events the customers metrics such as total order amount is precalculated eliminating the need for re-iterating through the list of events for each customer when calculating the simpleLTV.

The results of the simpleLTV calculation is stored in an ArrayList of CustomerActivityMetrics. This allows us to sort the results based on the simpleLTV value using the Collections.sort method. Of course to do this CustomerActivityMetrics needs to implement Comparable.

## Built With

* [Eclipse](http://www.eclipse.org) - IDE used
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Shrish Kadival** 

