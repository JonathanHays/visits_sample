package org.launchcode.docvisiting.models.comparator;
// <!-- 
// created by: Jonathan Hays
//  -->
import java.time.LocalDate;
import java.util.Comparator;

public class DateComparator implements Comparator<LocalDate> {
    @Override
    public int compare(LocalDate o1, LocalDate o2) {
        return o2.compareTo(o1);
    }
}
