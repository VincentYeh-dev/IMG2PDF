package org.vincentyeh.IMG2PDF.concrete.util.file;


import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSorter implements Comparator<File> {
    private final Sortby sortby;
    private final Sequence sequence;

    public FileSorter(Sortby sortby, Sequence sequence) {
        if (sortby == null)
            throw new IllegalArgumentException("sortby==null");
        if (sequence == null)
            throw new IllegalArgumentException("sequence==null");

        this.sortby = sortby;
        this.sequence = sequence;
    }

    @Override
    public int compare(File o1, File o2) {
        if (o1 == null)
            throw new IllegalArgumentException("o1==null");
        if (o2 == null)
            throw new IllegalArgumentException("o2==null");
        switch (sortby) {
            case NAME:
                if (sequence == Sequence.INCREASE)
                    return o1.getName().compareTo(o2.getName());
                else if (sequence == Sequence.DECREASE)
                    return o2.getName().compareTo(o1.getName());
            case DATE:
                if (sequence == Sequence.INCREASE)
                    return (int) (o1.lastModified() - o2.lastModified());
                else if (sequence == Sequence.DECREASE)
                    return (int) (o2.lastModified() - o1.lastModified());
            case NUMERIC:
                if (sequence == Sequence.INCREASE)
                    return compareNumeric(o1.getName(), o2.getName());

                else if (sequence == Sequence.DECREASE)
                    return compareNumeric(o2.getName(), o1.getName());

            default:
                throw new RuntimeException("Multiple files need to be sorted by sort and sequence arguments.");
        }
    }

    private int compareNumeric(String ThisStr, String OStr) {
        String noNumThis = ThisStr.replaceAll("[0-9]+", "*");
        String noNumO = OStr.replaceAll("[0-9]+", "*");

        if (noNumThis.equals(noNumO)) {
            int[] a = getNumber(ThisStr);
            int[] b = getNumber(OStr);
            for (int i = 0; i < a.length; i++) {
                int r = a[i] - b[i];
                if (r != 0) {
                    return r;
                }
            }
            return 0;
        } else {
            return ThisStr.compareTo(OStr);
        }
    }

    private int[] getNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(str);
        ArrayList<Integer> buf = new ArrayList<>();
        while (matcher.find()) {
            buf.add(Integer.valueOf(matcher.group()));
        }
        Integer[] result = new Integer[buf.size()];
        buf.toArray(result);
        int[] ints = new int[result.length];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = result[i];
        }
        return ints;
    }

    @Override
    public String toString() {
        return String.format("%s-%s", sortby, sequence);
    }

    public enum Sequence {
        INCREASE, DECREASE

    }

    public enum Sortby {
        NAME, DATE, NUMERIC

    }

}