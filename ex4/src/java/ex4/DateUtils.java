package ex4;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * Give system current date and time in deferent formats
 * @author Ilia Gaysinksy AND Andrey Shamis
 */
public class DateUtils {
    
    /**
     * 
     * @param dateFormat get wanted date format from user
     * @return date and time in the wanted format
     */
    public static String now(String dateFormat) { 
        
    Calendar cal = Calendar.getInstance();                  // Init calendar
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);//config date format
    return sdf.format(cal.getTime());                       // retern geted date
    }

    
    /*
     * Example of Usage:
     * =======================================================
     * System.out.println(DateUtils.now("dd MMMMM yyyy"));
     * System.out.println(DateUtils.now("yyyyMMdd"));
     * System.out.println(DateUtils.now("dd.MM.yy"));
     * System.out.println(DateUtils.now("MM/dd/yy"));
     * System.out.println(DateUtils.now("yyyy.MM.dd G 'at' hh:mm:ss z"));
     * System.out.println(DateUtils.now("EEE, MMM d, ''yy"));
     * System.out.println(DateUtils.now("h:mm a"));
     * System.out.println(DateUtils.now("H:mm:ss:SSS"));
     * System.out.println(DateUtils.now("K:mm a,z"));
     * System.out.println(DateUtils.now("yyyy.MMMMM.dd GGG hh:mm aaa"));
     */
}