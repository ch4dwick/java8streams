# Java Streams API

This is an advanced example from the [java8filters](https://github.com/ch4dwick/java8filters) Project.


Use case:
A requirement to generate total loan amounts sorted by account number and only loop through records flagged as strict for reports, iterating over all these objects can be very tedious. Java Streams streamline this process by providing a step-by-step approach to data aggregation.

Consider the code in thie repository. To better understand the code you should visualize this as a series of steps or processes in your data.
Say, in your SettlementFile you have SettlementRecords containing details about a retailer’s loan amounts. So visualize these steps when
tackling the problem:
1. Get the records of this settlement.
2. Convert the collection into a stream.
3. Since we have a requirement to filter out certain details from the collection we use the filter() API.
4. Collect the filtered out results. Unlike the earlier examples, we’re dealing with a more complex object so using the out-of-the-box Collectors won’t do it for us. This step is the most complicated.
 
    a. First, we designate the account number as the grouping key.

    b. Create a new Collector defining:

    c. The target object (SettlementSummary)

    d. The accumulator or the intermediate logic that maps the SettlementRecord data to the SettlementSummary object then adds all the loan amounts (the first two mapping may seem redundant in a loop but Java uses this to identify related records). In other scenarios,
    this is used to map/transform one object to another. It is important to remember that at this point SettlementSummary addition in this part are for records sharing the same account number. (I know this appears like a mouthful. I'm working on it.)

    e. The combiner that sums up all the loan amounts in SettlementSummary objects from the accumulator.

5. The last four lines converts the result to a collection, convert it back to a stream so we can sort the output (note that we need to create our
own sorting logic since this is a more complex object), then generate the list again. (The first collect() call creates a ConcurrentMap<> result which we don’t want in this use case).

**groupingByConcurrent()** uses CPU specific computing for efficiency compared to **groupingBy()**.

For standalone apps you can use **parallelStream()** for a more efficient processing of a collection’s details.

### Some limitations:
Just like the Java 6 for-each loop, you cannot manipulate the collection (add/remove the original collection) while in flight (iterating over the loop).
