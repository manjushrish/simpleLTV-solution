# Calculate simple LTV

This project is built in java to ingest JSON events presented in an input file and store them into an in memory HashMap. The hashmap key is the customer id present in the data. This allows us to store all events related to the same customer in one easily referencable location. 

Metrics such as site visits and total order amounts are calculated and stored during ingestion. The raw data associated to each type of web site event is also stored for reference. The ingested data is then used to calculate the simple LTV using the 52(a) * r where a is averge value per week and r is customer retention which is 10 years.

From the ingested data a is calculated by dividing the total order amount by the number of weeks for which the data exists.

## External libraries used

I have used the org.json library for parsing the JSON events into java objects. The dependancy is specified in the pom.xml file located in the same place as this README file.

### Prerequisites

What things you need to install the software and how to install them

```
Give examples
```

### Installing

A step by step series of examples that tell you have to get a development env running

Say what the step will be

```
Give the example
```

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Eclipse](http://www.eclipse.org) - IDE used
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Shrish Kadival** 

