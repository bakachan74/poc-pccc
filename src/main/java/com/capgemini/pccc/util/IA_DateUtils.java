package com.capgemini.pccc.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;

import org.apache.commons.configuration2.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sf06990s
 */

/**
 * @author sf06990s
 */
/**
 * @author sf06990s
 */
public class IA_DateUtils {
    // =========================================== class variables
    private final static Logger LOG = LoggerFactory.getLogger(IA_DateUtils.class);

    public static String VERBOSE_DATE_FORMAT = "yyyy-MM-dd HH'h'mm'min'ss'sec'";

    public final static DateFormat yMd_hms_verboseDF = new SimpleDateFormat(VERBOSE_DATE_FORMAT);

    public final static DateFormat dash_yMd_df = new SimpleDateFormat("yyyy-MM-dd");

    public final static DateFormat dot_yMd_df = new SimpleDateFormat("yyyy.MM.dd");

    public final static DateFormat dash_yMd_Hms_df = new SimpleDateFormat("yyyy-MM-dd-HHmmss");

    public final static TimeZone parisTz = TimeZone.getTimeZone("Europe/Paris");

    public final static TimeZone utcTz = TimeZone.getTimeZone("UTC");

    // =========================================== instance variables
    // INTERDITES DANS CETTE CLASSE !!!

    // =========================================== constructors

    // =========================================== class methods

    /**
     * "11:44:44" => 11
     * "09:44:44" => 9
     * 
     * @param hHmmSS
     * @return
     */
    public static int extractHourFromHHmmSS(String hHmmSS) {
        int hour = 0;
        String hourStr = hHmmSS.substring(0, 2);
        if (hourStr.startsWith("0"))
            hourStr = hourStr.substring(1);

        hour = Integer.parseInt(hourStr);
        return hour;
    }

    /**
     * @param utcDateAsString
     * @return
     * @throws ParseException
     * @throws IOException 
     */
    public static long extractUTC_MinFromNow(String utcDateAsString, DateFormat sdf) throws ParseException, IOException {
        long minFromNow = Long.MIN_VALUE;
        Calendar utcDateCal = IA_DateUtils.buildCalendarInstance();
        utcDateCal.setTimeZone(utcTz);

        sdf.setTimeZone(utcTz);

        utcDateCal.setTime(sdf.parse(utcDateAsString));// all done
        // LOG.info("utcDateCal en UTC : " + sdf.format(utcDateCal.getTime()));

        Calendar nowCal = IA_DateUtils.buildCalendarInstance();
        nowCal.setTimeZone(utcTz);
        // LOG.info("nowCal en UTC : " + sdf.format(nowCal.getTime()));

        minFromNow = (long) (nowCal.getTimeInMillis() - utcDateCal.getTimeInMillis()) / (1000 * 60);
        // LOG.info("minFromNow : ["+nowCal.getTimeInMillis()+"] - ["+utcDateCal.getTimeInMillis()+"] = " + minFromNow);

        if (minFromNow < 0) {
            String text = "'"+utcDateAsString+"' is ahead of now ! => it is not UTC !!!";
            LOG.error(text);
            throw new IOException(text);
        }
        
        return minFromNow;
    }

    /**
     * @param utcDateAsString
     * @param ymdHmsVerbose_DF 
     * @return
     * @throws ParseException
     * @throws IOException 
     */
    public static String convertUTC_To_UserFriendlyFormat(String utcDateAsString, DateFormat sdfIn, DateFormat ymdHmsVerbose_DF) throws ParseException, IOException {
        String userFriendly = null;
        Calendar utcDateCal = IA_DateUtils.buildCalendarInstance();
        utcDateCal.setTimeZone(utcTz);

        sdfIn.setTimeZone(utcTz);

        utcDateCal.setTime(sdfIn.parse(utcDateAsString));// all done
        // LOG.info("utcDateCal en UTC : " + sdfIn.format(utcDateCal.getTime()));

        userFriendly = ymdHmsVerbose_DF.format(utcDateCal.getTime());
        
        return userFriendly;
    }


    /**
     * @return
     */
    public static String getTimeZoneOffset() {
        Calendar cal = buildCalendarInstance();
        int offsetInMillis = parisTz.getOffset(cal.getTimeInMillis());

        String offset = String.format("%02d%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
        offset = (offsetInMillis >= 0 ? "+" : "-") + offset;

        return offset;
    }

    /**
     * @return
     */
    public static int getTimeZoneOffsetInHour() {
        Calendar cal = buildCalendarInstance();
        int offsetInMillis = parisTz.getOffset(cal.getTimeInMillis()) * -1;

        return offsetInMillis / 3600000;
    }

    /**
     * @param cal
     * @return "Aujourd'hui" ou "Hier" ou sinon, la valeur en entrée
     */
    public static String getFriendlyTextForDate(Calendar cal) {
        String calStr = dash_yMd_df.format(cal.getTime());

        String textualDay = IA_DateUtils.getTextualDay(cal);

        String todayStr = dash_yMd_df.format(buildCalendarInstance().getTime());

        String yesterdayStr = dash_yMd_df.format(yesterday());

        String twoDaysAgoStr = dash_yMd_df.format(twoDaysAgo());

        if (calStr.equals(twoDaysAgoStr))
            return "Avant-hier " + textualDay;

        if (calStr.equals(yesterdayStr))
            return "Hier " + textualDay;

        if (calStr.equals(todayStr))
            return "Aujourd'hui " + textualDay;

        return calStr + " " + textualDay;
    }

    public static Date twoDaysAgo() {
        final Calendar cal = buildCalendarInstance();
        cal.add(Calendar.DATE, -2);
        return cal.getTime();
    }

    /**
     * @return
     */
    public static Date yesterday() {
        final Calendar cal = buildCalendarInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    /**
     * Linky-Sup (LSP), Linky-Parc (LPC), Linky-Coeur (AQZ), STM (SDT) => 365
     * working days Les autres NNA => no Saturday, no Sunday, no
     * NationalHolidays
     * 
     * @param nbOfPastDays
     * @return
     */
    public static List<String> getLast_WorkingDayList(Calendar cal, int nbOfPastDays, String nna, Configuration _config) {

        List<String> nnasWorking365Days = getNNAsWorking365Days(_config);
        List<String> holidayList = getAllNationalHolidays(_config);

        List<String> workingDays = new ArrayList<String>();
        String thatDay = cal.get(Calendar.YEAR) + "." + getMonth(cal) + "." + getDayOfMonth(cal);
        // LOG.debug("\n\ngetLastN_WorkingDays (" + thatDay + ", " +
        // nbOfPastDays + ", " + nna + ")");

        // set hours, minutes, seconds and millis to 0
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        int bizDayCounter = 0;

        if (nnasWorking365Days.contains(nna)) {
            LOG.debug("==== NNA is a 365 days -> I will list the last " + nbOfPastDays + " days");
            // si le nna travaille 365 jours, on prend les 10 jours pr�c�dents
            while (bizDayCounter < nbOfPastDays) {
                cal.add(Calendar.DAY_OF_MONTH, -1);

                thatDay = cal.get(Calendar.YEAR) + "." + getMonth(cal) + "." + getDayOfMonth(cal);
                workingDays.add(thatDay);
                bizDayCounter++;
            }

        } else if (isWorkingDay(cal, holidayList)) {

            // LOG.debug("==== Today is a WORKING day -> I will list the last "
            // + nbOfPastDays + " WORKING days");
            // aujourd'hui est un workingDay => on veut une liste de non working
            // days
            while (bizDayCounter < nbOfPastDays) {
                cal.add(Calendar.DAY_OF_MONTH, -1);

                if (isWorkingDay(cal, holidayList)) {
                    thatDay = cal.get(Calendar.YEAR) + "." + getMonth(cal) + "." + getDayOfMonth(cal);
                    workingDays.add(thatDay);
                    bizDayCounter++;
                }

            }

        } else {
            // LOG.debug("==== today is a HOLIDAY -> I will list the last " +
            // nbOfPastDays + " HOLIDAYS");
            // aujourd'hui n'est PAS un workingDay => on veut une liste de non
            // working days
            while (bizDayCounter < nbOfPastDays) {
                cal.add(Calendar.DAY_OF_MONTH, -1);

                if (!isWorkingDay(cal, holidayList)) {
                    thatDay = cal.get(Calendar.YEAR) + "." + getMonth(cal) + "." + getDayOfMonth(cal);
                    workingDays.add(thatDay);
                    bizDayCounter++;
                }

            }

        }

        return workingDays;
    }

    /**
     * @return
     */
    public static int getCurrentTimezoneOffsetInSec() {

        TimeZone tzDefault = TimeZone.getDefault();
        Calendar cal = buildCalendarInstance();
        int offsetInMillis = tzDefault.getOffset(cal.getTimeInMillis());

        return offsetInMillis / 1000;
    }

    /**
     * Max is 24h = 86400sec
     */
    public static float convertSecsToTimestamp(int secondsSinceMidnight) {
        Calendar cal = buildCalendarInstance();
        // On se met � minuit
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        // LOG.debug("Timestamp � minuit : [" + cal.getTimeInMillis() + "]");

        cal.add(Calendar.SECOND, secondsSinceMidnight * 1000);
        // LOG.debug("Timestamp d�sormais : [" + cal.getTimeInMillis() + "]");

        return cal.getTimeInMillis();

    }

    /**
     * @param daysBack
     * @return the date in the format yyyy.MM.dd
     */
    public static String giveDotTodayMinusDays(int daysBack) {
        Calendar myTimestampCal = buildCalendarInstance();
        myTimestampCal.add(Calendar.DAY_OF_MONTH, -1 * daysBack);

        Date curDate = myTimestampCal.getTime();

        String retVal = dot_yMd_df.format(curDate);
        return retVal;
    }

    /**
     * @param daysBack
     * @return the date in the format yyyy.MM.dd
     */
    public static String giveDashTodayMinusDays(int daysBack) {
        Calendar myTimestampCal = buildCalendarInstance();
        myTimestampCal.add(Calendar.DAY_OF_MONTH, -1 * daysBack);

        Date curDate = myTimestampCal.getTime();

        String retVal = dash_yMd_df.format(curDate);
        return retVal;
    }

    /**
     * tient compte de la diff�rence de timezone avec UTC = 1h ou 2h avec la France
     * 
     * @param
     * @return the date in seconds
     */
    public static int giveNowLessHourInSec(int lessHour) {
        Calendar cal = buildCalendarInstance();
        cal.add(Calendar.HOUR_OF_DAY, -1 * lessHour);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        int tzOffsetInSec = getCurrentTimezoneOffsetInSec();

        return ((hour * 3600) + (min * 60) + sec) - tzOffsetInSec;
    }

    /**
     * @param cal
     * @return
     */
    public static String getYear(Calendar cal) {
        int year = cal.get(Calendar.YEAR);

        return Integer.toString(year);
    }

    /**
     * @param cal
     * @return
     */
    public static String getMonth(Calendar cal) {
        int monthJavaApi = cal.get(Calendar.MONTH) + 1;
        String retVal = Integer.toString(monthJavaApi);
        if (monthJavaApi < 10)
            retVal = "0" + retVal;

        return retVal;
    }

    /**
     * @param cal
     * @return
     */
    public static String getDayOfMonth(Calendar cal) {
        int dayJavaApi = cal.get(Calendar.DAY_OF_MONTH);
        String retVal = Integer.toString(dayJavaApi);
        if (dayJavaApi < 10)
            retVal = "0" + retVal;

        return retVal;
    }

    /**
     * @param cal
     * @return Monday, Tuesday, ...
     */
    public static String getTextualDay(Calendar cal) {

        return cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.FRENCH);
    }

    /**
     * @param textForDate
     *            should be "2017.07.07"
     * @param givenSdf
     * @return
     */
    public static Integer getWeekOfYear(String textForDate, DateFormat givenSdf) {

        int dayOfWeek = 0;
        int weekOfYear = 0;
        Calendar cal = buildCalendarInstance();
        // 2017-09-14T16:02:43.123+0200
        try {
            cal.setTime(givenSdf.parse(textForDate));
            dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);

            // /*
            // * La semaine en US commence le Dimanche
            // */
            // if (dayOfWeek == Calendar.SUNDAY) {
            // weekOfYear--;
            // }

        } catch (ParseException e) {
            LOG.error("Failed to get weekOfYear from [" + textForDate + "] ", e);
        }

        return weekOfYear;
    }

    // =========================================== protected methods
    /**
     * @param cal
     * @return bool
     */
    protected static boolean isWorkingDay(Calendar cal, List<String> holidayList) {
        String thatDay = cal.get(Calendar.YEAR) + "-" + getMonth(cal) + "-" + getDayOfMonth(cal);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        // si on est un samedi ou un dimanche ou un jour f�ri�
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY || holidayList.contains(thatDay)) {
            // LOG.debug(thatDay + " is holiday");
            return false;
        }

        // LOG.debug(thatDay + " is a working day");
        return true;
    }

    /**
     * @return
     */
    public static List<String> getAllNationalHolidays(Configuration _config) {

        String holidayValues = _config.getString("holly.days");
        String[] holidayAr = holidayValues.split(",");
        List<String> holidaysList = new ArrayList<String>();

        for (String value : holidayAr) {
            value = value.trim();
            holidaysList.add(value);
        }

        // LOG.debug("==== holidays found : " + holidaysList);
        return holidaysList;
    }

    /**
     * @return
     */
    public static List<String> getNNAsWorking365Days(Configuration _config) {

        String nNAWorking365DaysValues = _config.getString("nnas.working.365days");
        String[] valuesAr = nNAWorking365DaysValues.split(",");
        List<String> nNAWorking365DaysList = new ArrayList<String>();

        for (String value : valuesAr) {
            value = value.trim();
            nNAWorking365DaysList.add(value);
        }

        // LOG.debug("==== NNAs found : " + nNAWorking365DaysList);
        return nNAWorking365DaysList;
    }

    // /**
    // * @param year
    // * @param month
    // * @param day
    // * @return
    // */
    // public static Calendar buildCalendarFromyyyyMMdd(int year, int month, int day) {
    // Calendar docCal = IA_DateUtils.buildCalendarInstance();
    // docCal.set(year, month - 1, day);
    // return docCal;
    // }

    /**
     * @param qftValue
     * @return
     */
    public static List<String> getMonthsForWeek(int qftValue) {
        List<String> monthesForWeekList = new ArrayList<String>();

        Calendar mondayCal = buildCalendarInstance();
        mondayCal.set(Calendar.WEEK_OF_YEAR, qftValue);
        mondayCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        int mondayMonth = mondayCal.get(Calendar.MONTH) + 1;
        String mondayMonthStr = new String(mondayMonth + "");

        Calendar sundayCal = buildCalendarInstance();
        sundayCal.set(Calendar.WEEK_OF_YEAR, qftValue);
        sundayCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        int sundayMonth = sundayCal.get(Calendar.MONTH) + 1;
        String sundayMonthStr = new String(sundayMonth + "");

        // de toute facon on ajoute le mois du lundi
        // on gère les cas Janvier à Octobre : 8 devient "08" par exemple
        if (mondayMonth < 10)
            monthesForWeekList.add("0" + mondayMonth);
        else
            monthesForWeekList.add("" + mondayMonth);

        if (mondayMonth != sundayMonth) {
            if (sundayMonth < 10)
                monthesForWeekList.add("0" + sundayMonth);
            else
                monthesForWeekList.add("" + sundayMonth);
        }
        return monthesForWeekList;
    }

    /**
     * @param qftValue
     * @return
     */
    public static boolean isWeekAcross2Monthes(int qftValue) {

        Calendar mondayCal = buildCalendarInstance();
        mondayCal.set(Calendar.WEEK_OF_YEAR, qftValue);
        mondayCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        int mondayMonth = mondayCal.get(Calendar.MONTH) + 1;

        Calendar sundayCal = buildCalendarInstance();
        sundayCal.set(Calendar.WEEK_OF_YEAR, qftValue);
        sundayCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        int sundayMonth = sundayCal.get(Calendar.MONTH) + 1;

        if (mondayMonth != sundayMonth)
            return true;

        return false;
    }

    /**
     * add front "0" only if first char is not already "0"
     * 
     * @param nbStr
     * @return
     */
    public static String addFrontZeroIfNeeded(String nbStr) {
        if (nbStr.length() > 1) {
            // LOG.warn("addFrontZeroIfNeeded(" + nbStr + ") -- more than one digit -- Are you sure you need this ? ");
            return nbStr;
        }

        if (!nbStr.startsWith("0"))
            nbStr = "0" + nbStr;

        return nbStr;
    }

    /**
     * @param nbStr
     * @return
     */
    public static int removeFrontZeroIfNeeded(String nbStr) {
        if (nbStr.startsWith("0"))
            nbStr = nbStr.substring(1);

        Integer intMonth = Integer.parseInt(nbStr);

        return intMonth;
    }

    /**
     * XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
     * CAUTION!!!! ["48", "49", "50", "51", "52"] is returned, NOT [48, 49, 50, 51, 52]
     * XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
     * 
     * @param monthNb
     * @return
     */
    public static SortedSet<String> computeWeekListFromMonth(int monthNb) {
        LOG.info("computeWeekListFromMonth(" + monthNb + ")");

        SortedSet<String> weekSet = new TreeSet<String>();

        Calendar cal = buildCalendarInstance();

        cal.set(Calendar.MONTH, monthNb - 1);

        LOG.info(Constants.C_LINE + yMd_hms_verboseDF.format(cal.getTime()));

        // 1er et dernier jour du mois
        int startDayId = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        int lastDayId = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        cal.set(Calendar.DAY_OF_MONTH, startDayId);
        int startWeek = cal.get(Calendar.WEEK_OF_YEAR);
        LOG.info("startWeek : " + startWeek);

        cal.set(Calendar.DAY_OF_MONTH, lastDayId);
        int lastWeek = cal.get(Calendar.WEEK_OF_YEAR);
        LOG.info("lastWeek : " + lastWeek);
        System.out.println("lastWeek : " + lastWeek);

        for (int i = startWeek; i <= lastWeek; i++) {
            weekSet.add("\"" + IA_DateUtils.addFrontZeroIfNeeded(i + "") + "\"");
        }

        LOG.info("weekSet : " + weekSet);

        return weekSet;
    }

    /**
     * @param nowCal
     * @return
     */
    public static long computeMsSinceMidnight(Calendar nowCal) {
        long msAtMidnight = 0L;
        Calendar midnightCal = buildCalendarInstance();

        int year = nowCal.get(Calendar.YEAR);
        int month = nowCal.get(Calendar.MONTH);
        int day = nowCal.get(Calendar.DAY_OF_MONTH);

        midnightCal.set(year, month, day, 0, 0, 0);

        msAtMidnight = midnightCal.getTimeInMillis();

        // LOG.info("midnightCal " + yMd_hms_basicDF.format(midnightCal.getTime()));
        // LOG.info("nowCal " + yMd_hms_basicDF.format(nowCal.getTime()));

        long delta = nowCal.getTimeInMillis() - msAtMidnight;
        return delta;
    }

    /**
     * Algo pourri : pour reinjecter dans le bon index IDATHA (=! du mois en cours) :
     * SI (mois de mdgRun != mois de ctxTypeCal)
     * ALORS c'est ctxTypeCal qui fait foi on set le TS au dernier jour du mois de ctxTypeCal
     * => on doit mettre ici : 30 Septembre 2017
     *
     * @param ctxTypeCal
     * @param mdgRunCal
     * @return
     */
    public static Calendar ComputeTS_ToPickRightIdathaIndex(Calendar ctxTypeCal, Calendar mdgRunCal) {

        // Calendar cal = IA_DateUtils.buildCalendarInstance();
        // cal.set(2017, 8, 01);
        /*
         * DONE added on 2018 02 02
         */
        IaUtils.quotedTZ_df.setTimeZone(utcTz);

        String ctxTypeCalStr = IaUtils.quotedTZ_df.format(ctxTypeCal.getTime());
        // LOG.info(Constants.C_LINE + "ctxTypeCalStr 1er Septembre [" + ctxTypeCalStr + "]");

        // Calendar mdgRunCal = IA_DateUtils.buildCalendarInstance();
        String mdgRunCalStr = IaUtils.quotedTZ_df.format(mdgRunCal.getTime());
        // LOG.info(Constants.C_LINE + "mdgRunCalStr 6 Octobre @Timestamp[" + mdgRunCalStr + "]");

        int dayCst = Calendar.DAY_OF_MONTH;
        int monthCst = Calendar.MONTH;
        int yearCst = Calendar.YEAR;

        if (ctxTypeCal.get(monthCst) != mdgRunCal.get(monthCst)) {
            /*
             * On set le TS au DERNIER jour du mois de ctxTypeCal pour avoir
             * une chance de rentrer dans les 15 jours en arrière permis par iDatha.
             */
            int lastDayOfMonth = ctxTypeCal.getActualMaximum(Calendar.DAY_OF_MONTH);
            ctxTypeCal.set(dayCst, lastDayOfMonth);
            ctxTypeCalStr = IaUtils.quotedTZ_df.format(ctxTypeCal.getTime());
            // LOG.info(Constants.C_LINE + "le @Timestamp sera [" + ctxTypeCalStr + "]");
            return (Calendar) ctxTypeCal.clone();
        } else {
            // LOG.info(Constants.C_LINE + "le @Timestamp sera [" + mdgRunCalStr + "]");
            return mdgRunCal;
        }
    }

    /**
     * @param requestedWeek
     * @return
     */
    public static boolean isWeekIdTheCurrentWeek(int requestedWeek) {
        Calendar nowCal = buildCalendarInstance();
        int nowWeek = nowCal.get(Calendar.WEEK_OF_YEAR);
        if (nowWeek == requestedWeek)
            return true;
        else
            return false;
    }

    /**
     * @param requestedMonth
     * @return
     */
    public static boolean isMonthIdTheCurrentMonth(int requestedMonth) {
        Calendar nowCal = buildCalendarInstance();
        int nowMonth = nowCal.get(Calendar.MONTH) + 1;
        if (nowMonth == requestedMonth)
            return true;
        else
            return false;
    }

    /**
     * IA_DateUtils.buildCalendarInstance();
     * 
     * @return
     */
    public synchronized static Calendar buildCalendarInstance() {
        Calendar cal = GregorianCalendar.getInstance(parisTz, Locale.FRENCH);

        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(7);

        return cal;
    }

    // =========================================== private methods

}
