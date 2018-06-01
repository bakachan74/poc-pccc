package com.capgemini.pccc.util;

import com.sun.mail.iap.ByteArray;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.math.BigDecimal;

public class Enumeration {

    /**
     *  Enums
     */
    public enum IMgr_Action {
        select, insert, update, delete, N_A
    }

    /**
     * Used as target java type for java.sql.Types
     */
    public enum MappedJavaType {
        java_lang_String, java_lang_Integer, java_lang_Byte__Array, java_math_BigDecimal, java_lang_Boolean, java_lang_Long,
        java_lang_Float, java_lang_Double, java_sql_Date, java_sql_Time, java_sql_TimeStamp, java_null
    }

    /**
     * Used as target json type for java.sql.Types
     */
    public enum MappedJsonType {
        _string, _number, _object, _array, _boolean, _null
    }

    /**
     * MDG Enums
     */
    public enum QueryFilterType {
        FILTER_ON_WEEK, FILTER_ON_DAY, FILTER_ON_MONTH
    }

    public enum WhatType {
        FLOW_GENERIC, FLOW_ACM, FLOWCODE_REGIONAL, FLOWCODE_NATIONAL, FLOWCODE_ERROR, FLOW_GLOBAL_INFO
    }

    public enum JsonQueryType {
        ACM_FOR_FLUX_WITH_SOURCE, ACM_FOR_FLUX_NO_SOURCE, GENERIC_DEST, GENERIC_SOURCE, REGIONAL_QUERY, NATIONAL_QUERY
    }

    public enum AlertStatus {
        NEW, READ, IN_PROGRESS, CLOSED
    }

    public enum AlertGroup {
        MAZER_GEN_ALERT, MAZER_RULE_ALERT, MAZER_TECH_ALERT
    }

    /**
     * GLOBAL Enums
     */
    public enum EvtType {
        SYS, LOG, ALERT, metrics
    }

    public enum Environment {
        DEV, IQ, PREPROD, HOMO, PROD
    }

    public enum AlertSeverity {
        INFO, WARN, ERROR, FATAL
    }
}
