package no.nav.arxaas.hierarchy.interval;

import no.nav.arxaas.hierarchy.Hierarchy;
import no.nav.arxaas.hierarchy.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class IntervalBasedHierarchyBuilderTest {

    private List<Interval> testIntervals;
    private String[] testColumn;
    private List<Level> testLevels;

    @BeforeEach
    void setUp() {
        testIntervals = List.of(new Interval(0L,2L), new Interval(2L, 4L), new Interval(4L, 8L));
        testLevels = List.of(new Level(0, List.of(new Level.Group(2))));
        testColumn = getExampleData();
    }

    private static String[] getExampleData(){

        String[] result = new String[10];
        for (int i=0; i< result.length; i++){
            result[i] = String.valueOf(i);
        }
        return result;
    }


    @Test
    void build() {
        String[] expectedFirstRow = {"0", "[0, 2[", "[0, 4[", "*"};

        IntervalBasedHierarchyBuilder basedHierarchyBuilder = new IntervalBasedHierarchyBuilder(
                testIntervals,
                testLevels,
                new Range(0L, 0L, Long.MIN_VALUE / 4),
                new Range(81L, 100L, Long.MAX_VALUE / 4), IntervalBasedHierarchyBuilder.BuilderDataType.LONG);

        Hierarchy result = basedHierarchyBuilder.build(testColumn);
        Assertions.assertArrayEquals(expectedFirstRow,result.getHierarchy()[0]);

    }


    @Test
    void build_with_labeled_intervals() {

        String[][] expected = {
                {"0", "young", "[0, 4[", "*"},
                {"1", "young", "[0, 4[", "*"},
                {"2", "adult", "[0, 4[", "*"},
                {"3", "adult", "[0, 4[", "*"},
                {"4", "old", "[4, 8[", "*"},
                {"5", "old", "[4, 8[", "*"},
                {"6", "old", "[4, 8[", "*"},
                {"7", "old", "[4, 8[", "*"},
                {"8", "very-old", "[8, 12[", "*"},
                {"9", "very-old", "[8, 12[", "*"}};


        List<Interval> labeledIntervals = List.of(
                new Interval(0L,2L, "young"),
                new Interval(2L, 4L, "adult"),
                new Interval(4L, 8L, "old"),
                new Interval(8L, Long.MAX_VALUE, "very-old"));

        IntervalBasedHierarchyBuilder basedHierarchyBuilder = new IntervalBasedHierarchyBuilder(
                labeledIntervals,
                testLevels,
                new Range(0L, 0L, Long.MIN_VALUE / 4),
                new Range(81L, 100L, Long.MAX_VALUE / 4), IntervalBasedHierarchyBuilder.BuilderDataType.LONG);

        Hierarchy result = basedHierarchyBuilder.build(testColumn);
        Assertions.assertArrayEquals(expected, result.getHierarchy());
    }


    @Test
    void build_with_no_ranges() {

        String[][] expected =
                {{"3", "adult", "[0, 4[", "*",},
                {"4", "old", "[4, 8[", "*",},
                {"5", "old", "[4, 8[", "*",},
                {"6", "old", "[4, 8[", "*",},
                {"7", "old", "[4, 8[", "*",},
                {"8", "old", "[8, 9[", "*",}};

        List<Interval> labeledIntervals = List.of(
                new Interval(0L,2L, "young"),
                new Interval(2L, 4L, "adult"),
                new Interval(4L, 9L, "old"));

        IntervalBasedHierarchyBuilder basedHierarchyBuilder = new IntervalBasedHierarchyBuilder(
                labeledIntervals,
                testLevels, null, null, IntervalBasedHierarchyBuilder.BuilderDataType.LONG);

        String[] column = {"3", "4", "5", "6", "7", "8"};
        Hierarchy result = basedHierarchyBuilder.build(column);
        Assertions.assertArrayEquals(expected, result.getHierarchy());
    }

    @Test
    void build_with_double_type_data(){
        String[][] expected = {{"3.3", "young", "[0.0, 6.1[", "*",},
                {"4.1", "adult", "[0.0, 6.1[", "*",},
                {"5", "adult", "[0.0, 6.1[", "*",},
                {"6.2", "old", "[6.1, 12.2[", "*",},
                {"7.232", "old", "[6.1, 12.2[", "*",},
                {"8.22", "old", "[6.1, 12.2[", "*",}};

        List<Interval> labeledIntervals = List.of(
                new Interval(0.0,3.5, "young"),
                new Interval(3.5, 6.1, "adult"),
                new Interval(6.1, 900, "old"));

        IntervalBasedHierarchyBuilder basedHierarchyBuilder = new IntervalBasedHierarchyBuilder(
                labeledIntervals,
                testLevels, null, null, IntervalBasedHierarchyBuilder.BuilderDataType.DOUBLE);

        String[] column = {"3.3", "4.1", "5", "6.2", "7.232", "8.22"};
        Hierarchy result = basedHierarchyBuilder.build(column);
        Assertions.assertArrayEquals(expected, result.getHierarchy());
    }


    @Test
    void build_with_double_type_data_fails_with_no_intervals(){
        List<Interval> labeledIntervals = List.of();

        IntervalBasedHierarchyBuilder basedHierarchyBuilder = new IntervalBasedHierarchyBuilder(
                labeledIntervals,
                testLevels, null, null, IntervalBasedHierarchyBuilder.BuilderDataType.DOUBLE);

        String[] column = {"3.3", "4.1", "5", "6.2", "7.232", "8.22"};

        Assertions.assertThrows(IllegalArgumentException.class, () -> basedHierarchyBuilder.build(column));
    }

    @Test
    void build_with_single_interval() {
        List<Interval> labeledIntervals = List.of(
                new Interval(0L,2L, "young"));

        IntervalBasedHierarchyBuilder basedHierarchyBuilder = new IntervalBasedHierarchyBuilder(
                labeledIntervals,
                testLevels, null, null, IntervalBasedHierarchyBuilder.BuilderDataType.LONG);

        String[] column = {"3", "4", "5", "6", "7", "8"};
        Assertions.assertThrows(IllegalArgumentException.class, () -> basedHierarchyBuilder.build(column));
    }

}
