package com.capgemini.pccc.manager;

import com.capgemini.pccc.util.Constants;
import com.capgemini.pccc.util.IA_DateUtils;
import com.capgemini.pccc.util.IaUtils;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.configuration2.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class PCCCManagerTest extends TestCase {

    public final static String CFG_FILENAME = "pccc.properties";
    // ------------------------------------- static variables
    private final static Logger LOG = LoggerFactory.getLogger(PCCCManagerTest.class);
    public static Configuration _config = IaUtils.loadConfig(CFG_FILENAME);

    // ------------------------------------- instance variables

    // ------------------------------------- static methods

    // ------------------------------------- constructors

    // ------------------------------------- public methods

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(PCCCManagerTest.class);
    }

    /**
     * ISO-8859-1
     */
    public void testReplaceAll() {
        LOG.info(Constants.C_LINE);
        String testLine = "r,t,y,u,i,r";
        testLine = testLine.replaceAll("\\,", "");
        LOG.info(testLine);

        assertEquals(1, 1);
    }

    public void testLongCompare() {
        String tracker_idStr = "5";
        Long tracker_id = Long.parseLong(tracker_idStr);
        LOG.info("tracker_idStr : [" + tracker_idStr + "]");
        LOG.info("tracker_id : " + tracker_id);

    }

    public void testDateFormat() {
        Calendar calNoTz = GregorianCalendar.getInstance();
        IaUtils.quotedTZ_df.setTimeZone(IA_DateUtils.utcTz);
        LOG.info("  CalNoTz : [" + IaUtils.quotedTZ_df.format(calNoTz.getTime()) + "]");

        Calendar utcCal = GregorianCalendar.getInstance(IA_DateUtils.utcTz);
        IaUtils.quotedTZ_df.setTimeZone(IA_DateUtils.utcTz);
        LOG.info("UTCCalStr : [" + IaUtils.quotedTZ_df.format(utcCal.getTime()) + "]");

        Calendar nowCal = IA_DateUtils.buildCalendarInstance();
        String nowCalTZ = IaUtils.quotedTZ_df.format(nowCal.getTime());
        LOG.info("nowCalStr : [" + nowCalTZ + "]");
    }

    public void testDaoManager() {
        Configuration _config = IaUtils.loadConfig(AbstractHttpServlet.CFG_FILENAME);

        DaoManager daoMgr = DaoManager.getInstance(_config);

        try {
            daoMgr.createDbConnection();
            String queryName = "resourceByWorkLocation";
            String sqlQuery = _config.getString("pccc.sql.select." + queryName);
            int aggColumnId = _config.getInt("pccc.sql.select." + queryName + ".aggColumnId") - 1;

            daoMgr.executeSQL(sqlQuery, aggColumnId, queryName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void testDaoServlet() {
        Configuration _config = IaUtils.loadConfig(AbstractHttpServlet.CFG_FILENAME);
        DaoServlet daoServlet = new DaoServlet();

        Map<String, String> httpValMap = new HashMap<>();
        httpValMap.put("queryName", "resourceByWorkLocation");

        daoServlet.doSelect("doSelect/moreURIstuff", null, httpValMap);

    }
    // ------------------------------------- protected methods

    // ------------------------------------- private methods

}
