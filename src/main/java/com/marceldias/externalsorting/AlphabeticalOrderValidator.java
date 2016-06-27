package com.marceldias.externalsorting;

import com.marceldias.externalsorting.exception.ExternalSortingException;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Verifies if a file is in Alphabetical Order
 */
public class AlphabeticalOrderValidator {

    /**
     * Iterates each file line verifying if the file is in Alphabetical Order
     * @param file the file to be verified
     * @return @{code true} if the file is in Alphabetical Order
     *         @{code false} if the file isn't in Alphabetical Order
     */
    public Boolean validate(String file) {
        TimeMetric timeMetric = new TimeMetric("AlphabeticalOrderValidator");
        Path path = Paths.get(file);
        Sorter sorter = new Sorter();
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String left = br.readLine();
            String right;
            while ((right = br.readLine()) != null ) {
                if (sorter.isLeftPrecedent(left, right)) {
                    left = right;
                } else {
                    System.out.println(" LEFT == "+left);
                    System.out.println(" RIGHT == "+right);
                    throw new ExternalSortingException("The file " + path.toFile().getAbsolutePath() + " isn't sorted!", null );
                }
            }
        } catch (NoSuchFileException e) {
            throw new ExternalSortingException("File not Found!", e);
        } catch (Exception e) {
            throw new ExternalSortingException("Unexpected error occured!", e);
        }
        System.out.println("The file " + path.toFile().getAbsolutePath() + " is sorted!");
        timeMetric.print();
        return Boolean.TRUE;
    }
}
