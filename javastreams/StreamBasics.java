package javastreams;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StreamBasics {
  static Logger logger = Logger.getLogger(StreamBasics.class.getName());

  public static void main(String... args) {
    List<String> strs = Arrays.asList(
        "The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog");
    List<BigDecimal> numsBD = Arrays.asList(new BigDecimal(2353),
        new BigDecimal(3445), new BigDecimal(3954), new BigDecimal(4191),
        new BigDecimal(3920), new BigDecimal(6791));
    List<Integer> numsInt = Arrays.asList(2353, 3445, 3954, 4191, 3920, 6791);
    // Old way
    logger.info("Old way");
    for (String s : strs) {
      logger.info(s);
    }

    // Java with lambda
    logger.info("Java with lambda");
    strs.forEach(logger::info);

    // Default sorting
    logger.info("Default sorting");
    strs.stream().sorted().forEach(logger::info);

    // Custom sorting
    logger.info("Custom sorting");
    strs.stream()
        .sorted((v1, v2) -> {
          // For demonstration we'll sort by string length.
          if (v1.length() > v2.length()) {
            return 1;
          } else if (v2.length() > v1.length()) {
            return -1;
          } else {
            return 0;
          }
        })
        .forEach(logger::info);

    // Filtering data
    logger.info("Filtering data");
    strs.stream().filter(s -> s.contains("o")).forEach(logger::info);

    // You can combine them
    logger.info("Combine sort and filter");
    strs.stream()
        .filter(s -> s.contains("o"))
        .sorted((v1, v2) -> {
          // For demonstration we'll sort by string length.
          if (v1.length() > v2.length()) {
            return 1;
          } else if (v2.length() > v1.length()) {
            return -1;
          } else {
            return 0;
          }
        })
        .forEach(logger::info);
    // You read the above code as "filter out words that don't have 'o',
    // sort the results, output each element.
    // Get the results
    List<String> strList2 = strs.stream()
        .filter(s -> s.contains("o"))
        .collect(Collectors.toList());

    logger.info("Aggregation");
    Integer sum = numsInt.stream().collect(Collectors.summingInt(i -> i));
    logger.log(Level.INFO, "Sum Integer : {0}", sum);
    Double avg = numsInt.stream().collect(Collectors.averagingInt(i -> i));
    logger.log(Level.INFO, "Avg Integer : {0}", avg);
    BigDecimal total = numsBD.stream()
        .reduce(BigDecimal::add)
        .get(); // Using a custom accumulator.
    logger.log(Level.INFO, "Sum BigDecimal: {0}", total);
  }
}