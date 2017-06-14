package com.matteoveroni.wordsremember.quizgame.model;

import com.matteoveroni.myutils.Int;
import com.matteoveroni.myutils.IntRange;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Matteo Veroni
 */

// TODO: move this class into a util library && test this class
public class UniqueRandomNumberGenerator {

    private final Set<Integer> extractedUniqueRandNumbers = new HashSet<>();
    private final int maxNumberOfExtractions;
    private final IntRange rangeOfExtractableIntegers;

    public UniqueRandomNumberGenerator(int minNumber, int maxNumber) {
        rangeOfExtractableIntegers = new IntRange(minNumber, maxNumber);
        checkRangeDimensionValidity();
        this.maxNumberOfExtractions = rangeOfExtractableIntegers.getDimension();
    }

    public UniqueRandomNumberGenerator(int minNumber, int maxNumber, int maxNumberOfExtractions) {
        rangeOfExtractableIntegers = new IntRange(minNumber, maxNumber);
        checkRangeDimensionValidity();
        if (maxNumberOfExtractions < rangeOfExtractableIntegers.getDimension()) {
            this.maxNumberOfExtractions = maxNumberOfExtractions;
        } else {
            this.maxNumberOfExtractions = rangeOfExtractableIntegers.getDimension();
        }
    }

    private void checkRangeDimensionValidity() {
        if (rangeOfExtractableIntegers.getDimension() <= 0) {
            throw new IllegalArgumentException("Range dimension of extractable integers must be positive");
        }
    }

    public int extractNext() throws NoMoreUniqueRandNumberExtractableException {
        int initialSizeOfExtractedNumbers = extractedUniqueRandNumbers.size();
        if (initialSizeOfExtractedNumbers >= maxNumberOfExtractions) {
            throw new NoMoreUniqueRandNumberExtractableException();
        }
        int number;
        do {
            number = Int.getRandomInt(rangeOfExtractableIntegers);
            extractedUniqueRandNumbers.add(number);
        } while (extractedUniqueRandNumbers.size() == initialSizeOfExtractedNumbers);
        return number;
    }

    public class NoMoreUniqueRandNumberExtractableException extends Exception {
    }
}
