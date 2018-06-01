package com.capgemini.pccc.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sf06990s
 */
public class IaUtils {
	// =========================================== class variables
	private final static Logger LOG = LoggerFactory.getLogger(IaUtils.class);

	// private static TSAUtils _INSTANCE = null;
	private static Configuration _config = null;

	private static final String EMPTY = "";

	// public static final String TZ_FORMADT_SUFFIX = "T00:00:00.000Z";

	public final static DateFormat yMdHms_df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public final static DateFormat dot_yM_df = new SimpleDateFormat("yyyy.MM");

	public final static String quotedTZ_df_STR = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	public final static DateFormat quotedTZ_df = new SimpleDateFormat(quotedTZ_df_STR);

	public final static DateFormat sdfTZ_df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	public final static DateFormat yyyy_df = new SimpleDateFormat("yyyy");
	/*
	 * Pour parser le format lu dans ES : "2017-05-31T08:22:56.000Z"
	 */
	public final static DateFormat sdfTz = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

	public final static DateFormat sdfTXXX = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

	public final static String PATTERN_LIST_DATE = "(\\d\\d\\d\\d-\\d\\d-\\d\\d)(,\\d\\d\\d\\d-\\d\\d-\\d\\d)*";

	// =========================================== instance variables
	// INTERDITES DANS CETTE CLASSE !!!

	// =========================================== constructors

	// =========================================== class methods

	// =========================================== public methods
	/**
	 * if stringToScan contains one of the elts in the list => should NOT keep
	 * => return false
	 * 
	 * @param filterOutList
	 * @param stringToScan
	 * @return
	 */
	public static boolean shouldKeepDoc(List<String> filterOutList, String stringToScan) {

		for (String filterOutStr : filterOutList) {
			if (stringToScan.contains(filterOutStr))
				return false;
		}

		return true;
	}

	/**
	 * @param stringIntegerMap
	 * @param splitChar
	 * @return
	 */
	public static String buildCsvString(Map<String, Integer> stringIntegerMap, String splitChar) {
		String csvLine = "";
		int count = 0;
		for (String level2Key : stringIntegerMap.keySet()) {
			count = stringIntegerMap.get(level2Key);
			csvLine += level2Key + splitChar + count + splitChar;
		}

		if (csvLine.endsWith(splitChar))
			csvLine = csvLine.trim().substring(0, csvLine.length() - 1);

		return csvLine;
	}

	/**
	 * @return
	 */
	public static String findHostname() {

		String OS = System.getProperty("os.name").toLowerCase();
		String hostname = "UNKNOWN_HOST";

		try {
			if (OS.indexOf("win") >= 0) {
				hostname = execReadToString("hostname");
			} else {
				if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0) {
					hostname = execReadToString("hostname");
					// System.out.println("Linux computer name throguh env:\"" +
					// System.getenv("HOSTNAME") + "\"");
					// System.out.println("Linux computer name through
					// /etc/hostname:\"" + execReadToString("cat /etc/hostname")
					// +
					// "\"");
				}
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}

		return hostname.trim();
	}

	/**
	 * @return
	 */
	public static boolean isLinuxOS() {

		String OS = System.getProperty("os.name").toLowerCase();
		String hostname = "UNKNOWN_HOST";

		if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0) {
			return true;
		}

		return false;
	}

	/**
	 * @return
	 */
	public static String findLocalIP() {
		String ipStr = "";
		try {
			InetAddress IP = InetAddress.getLocalHost();
			ipStr = IP.getHostAddress();
			// LOG.info("IP of my system is '" + ipStr + "'");

		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}

		return ipStr.trim();
	}

	/**
	 * @return
	 */
	public static String findNNI() {
		String nni = System.getProperty("user.name").toUpperCase();
		return nni.trim();
	}

	/**
	 * @param execCommand
	 * @return
	 * @throws IOException
	 */
	public static String execReadToString(String execCommand) throws IOException {
		Process proc = Runtime.getRuntime().exec(execCommand);
		InputStream stream = null;
		try {

			stream = proc.getInputStream();
			Scanner s = null;
			try {
				s = new Scanner(stream);
				s.useDelimiter("\\A");
				return s.hasNext() ? s.next() : "";
			} finally {
				if (s != null) {
					s.close();
				}
			}
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	/**
	 * @param aggName
	 * @return
	 */
	public static String trimAggName(String aggName) {

		return aggName.substring(0, aggName.length() - 4);
	}

	/**
	 * public pour acc�s par les tests de regression
	 * 
	 * @param configFileName
	 * @return
	 */
	public static Configuration loadConfig(String configFileName) {

		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
				PropertiesConfiguration.class)
						.configure(params.properties().setFileName(configFileName)
								.setListDelimiterHandler(new DefaultListDelimiterHandler(';')));

		// FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new
		// FileBasedConfigurationBuilder<FileBasedConfiguration>(
		// PropertiesConfiguration.class)
		// .configure(params.properties().setFileName(configFileName)
		// .setListDelimiterHandler(new DefaultListDelimiterHandler(',')));

		try {
			_config = builder.getConfiguration();

		} catch (ConfigurationException cex) {
			LOG.error("Erreur de lecture du fichier de properties [" + configFileName + "] -- " + cex.getMessage());
			LOG.error("==== EXIT");
			System.exit(0);
		}

		return _config;
	}

	/**
	 * @param nb
	 * @param nbToCompareTo
	 * @param marginInPct
	 * @return
	 */
	public static boolean isRoughlyEqual(double nb, double nbToCompareTo, double marginInPct) {

		double marginInDouble = 0D;
		if (marginInPct > 0)
			marginInDouble = marginInPct / 100.0;

		if (nb > nbToCompareTo + (nbToCompareTo * marginInDouble))
			return false;

		if (nb < nbToCompareTo - (nbToCompareTo * marginInDouble))
			return false;

		return true;
	}

	/**
	 * @param nbPointsOut
	 * @param nbPointsTotal
	 * @param smallPctOfThem
	 * @return
	 */
	public static boolean isStrictlyMore(double nbPointsOut, double nbPointsTotal, double smallPctOfThem) {

		// si %age tol�r� est nul, aucun point ne peut �tre out
		if ((smallPctOfThem == 0) && (nbPointsOut > 0))
			return false;

		double marginInDouble = smallPctOfThem / 100.0;

		if (nbPointsOut > nbPointsTotal * marginInDouble)
			return true;

		return false;
	}

	// =========================================== protected methods
	/**
	 * @param indexPrefix
	 * @param last_WorkingDayList
	 * @return
	 */
	protected static List<String> makeIndexList(String indexPrefix, List<String> last_WorkingDayList) {
		List<String> indexList = new ArrayList<String>();

		for (String wDay : last_WorkingDayList) {
			indexList.add(indexPrefix + wDay);
		}
		return indexList;
	}

	/**
	 *
	 * @param nFirst
	 * @param set
	 * @return
	 */
	public static String showNFirst(int nFirst, Set<String> set) {
		String retval = "";
		int counter = nFirst;
		for (String elt : set) {
			if (counter-- == 0)
				break;
			retval += elt + ", ";
		}

		if (set.size() > nFirst)
			return "[" + retval + "...]";
		else
			return "[" + retval.substring(0, retval.length() - 2) + "]";

	}

	/**
	 * @param integerList
	 * @return
	 */
	public static int[] convertToPrimitiveArray(ArrayList<Integer> integerList) {
		int[] arr = new int[integerList.size()];
		for (int i = 0; i < integerList.size(); i++) {
			arr[i] = integerList.get(i);
		}
		return arr;
	}

	/**
	 * @param stringSetMap
	 * @return
	 */
	public static Map<String, String[]> convertStringSetToArrayMap(Map<String, SortedSet<String>> stringSetMap) {
		Map<String, String[]> stringArMap = new TreeMap<String, String[]>();
		for (String dateStr : stringSetMap.keySet()) {
			SortedSet<String> stringSet = stringSetMap.get(dateStr);
			String[] stringAr = stringSet.toArray(new String[stringSet.size()]);
			stringArMap.put(dateStr, stringAr);
		}
		return stringArMap;
	}

	/**
	 * @param stringIntMap
	 * @return
	 */
	public static String[] convertMapKeysToArray(Map<String, Integer> stringIntMap) {
		Set<String> mySet = stringIntMap.keySet();
		String[] stringAr = mySet.toArray(new String[mySet.size()]);
		return stringAr;
	}

	/**
	 * @param map
	 * @return
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {

			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	/**
	 * @param set1
	 * @param set2
	 * @return
	 */
	public static SortedSet<String> extractCommonValueSet(Set<String> set1, Set<String> set2) {
		SortedSet<String> commonSet = new TreeSet<String>();

		for (String strSet1 : set1) {
			strSet1 = strSet1.trim();

			for (String strSet2 : set2) {
				strSet2 = strSet2.trim();

				if (strSet1.equals(strSet2)) {
					commonSet.add(strSet2);
				}
			}
		}

		return commonSet;
	}

	/**
	 * @param aggObj
	 * @param aggName
	 * @return
	 */
	public static boolean hasFoundMissingDocs(JSONObject aggObj, String aggName) {
		int doc_count_error_upper_bound = aggObj.getInt("doc_count_error_upper_bound");
		int sum_other_doc_count = aggObj.getInt("sum_other_doc_count");

		if (sum_other_doc_count + doc_count_error_upper_bound > 0) {
			LOG.error(Constants.X_LINE);
			LOG.error(buildFoundMissingDocText(aggObj, aggName));
			LOG.error(Constants.X_LINE);
			return true;
		}

		return false;
	}

	/**
	 * @param aggObj
	 * @param aggName
	 * @return
	 */
	public static String buildFoundMissingDocText(JSONObject aggObj, String aggName) {
		int doc_count_error_upper_bound = aggObj.getInt("doc_count_error_upper_bound");
		int sum_other_doc_count = aggObj.getInt("sum_other_doc_count");
		String errorText = "sum_other_doc_count [" + sum_other_doc_count + "] -- doc_count_error_upper_bound ["
				+ doc_count_error_upper_bound
				+ "] : Veuillez augmenter l'attribut \"size\" dans votre query elasticSearch pour le niveau ["
				+ IaUtils.trimAggName(aggName) + "]";
		return errorText;
	}

	/**
	 *
	 * @param strSet
	 * @param separator
	 * @param prefix
	 * @param suffix
	 * @return
	 */
	public static String joinString(Collection<?> strSet, final String separator, final String prefix,
			final String suffix) {
		if (strSet == null) {
			return null;
		}
		final int noOfItems = strSet.size();
		if (noOfItems <= 0) {
			return EMPTY;
		}
		final StringBuilder buf = new StringBuilder(noOfItems * 16);

		int i = 0;
		for (Object key : strSet) {
			if (i > 0) {
				buf.append(separator);
			}
			if (key != null) {
				buf.append(prefix).append(key).append(suffix);
			}
			i++;
		}
		return buf.toString();
	}

	/**
	 * @param someMetricsList
	 * @return
	 */
	public static <T> boolean isNullOrEmpty(List<T> someMetricsList) {
		if (someMetricsList == null || someMetricsList.isEmpty())
			return true;

		return false;
	}

	/**
	 * @param someMap
	 * @return
	 */
	public static <K, V> boolean isNullOrEmpty(Map<K, V> someMap) {
		if (someMap == null || someMap.isEmpty())
			return true;

		return false;
	}

	/**
	 * @param valueAr
	 * @return
	 */
	public static boolean isNullOrEmptyOrGreaterThanOne(String[] valueAr) {
		if (valueAr == null)
			return true;

		if (valueAr.length > 1) {
			LOG.warn("WARNING ! -- String[] valueAr has " + valueAr.length + " (> 1) elts");
			return true;
		}

		return false;
	}

	/**
	 * @param value
	 * @return
	 */
	public static boolean isNullOrEmpty(String value) {
		if ((value == null) || value.isEmpty())
			return true;
		return false;
	}

	/**
	 *
	 * @param collectionMap
	 * @return
	 */
	protected static int getmaxSizeInSetCollection(Collection<SortedSet<String>> collectionMap) {
		int maxSize = 0;

		for (Set<String> stringSet : collectionMap) {

			if (stringSet.size() > maxSize)
				maxSize = stringSet.size();
		}

		return maxSize;
	}

	/**
	 * @param perDayStringIntMapMap
	 * @return
	 */
	protected static int getmaxSizeInMapValues(Map<String, Map<String, Integer>> perDayStringIntMapMap) {
		int maxSize = 0;

		for (Map<String, Integer> stringIntMap : perDayStringIntMapMap.values()) {

			if (stringIntMap.size() > maxSize)
				maxSize = stringIntMap.size();
		}

		return maxSize;
	}

	/**
	 * @param dataAr
	 * @return
	 */
	public static int ana_getMaxValueIndex(Integer[] dataAr) {
		Integer maxVal = Integer.MIN_VALUE;
		int maxValIndex = 0;

		for (int i = 0; i < dataAr.length; i++) {
			Integer curVal = dataAr[i];
			if (curVal == null)
				continue;

			if (curVal.intValue() > maxVal) {
				maxVal = curVal;
				maxValIndex = i;
			}
		}

		return maxValIndex;
	}

	/**
	 * @param dataAr
	 * @return
	 */
	public static int ana_getMinValueIndex(Integer[] dataAr) {
		Integer minVal = Integer.MAX_VALUE;
		int idxOfMinVal = dataAr.length;

		for (int i = 0; i < dataAr.length; i++) {
			Integer curVal = dataAr[i];
			if (curVal == null)
				continue;

			if (curVal.intValue() < minVal) {
				minVal = curVal;
				idxOfMinVal = i;
			}
		}

		return idxOfMinVal;
	}

	/**
	 * we ignore the pics min+10% and max-10% to have a more "realistic" average
	 * 
	 * @param dataAr
	 * @param maxVal_index
	 * @param minVal_index
	 * @return
	 */
	public static Double ana_AverageOnTrimmedAr(Integer[] dataAr, List<Integer> valFoundIdList, int minVal_index,
			int maxVal_index) {
		Integer sum = 0;
		double CONST = 10.0D;

		if (dataAr.length == 0)
			return null;

		Integer minVal = dataAr[minVal_index];
		Integer maxVal = dataAr[maxVal_index];
		double minThreashold = (double) minVal * (1.0D + (CONST / 100.0D)); // minVal
																			// 10
																			// =>
																			// seuil
																			// 12
		double maxThreashold = (double) maxVal * (1.0D - (CONST / 100.0D));// maxVal
																			// 100
																			// =>
																			// seuil
																			// 80
		double retval = 0L;
		Integer curVal = null;
		int valFoundQty = valFoundIdList.size();

		if (valFoundQty == 0)
			return null;

		if (valFoundQty == 1)
			return (double) dataAr[valFoundIdList.get(0)];

		// just return the average of the 2 values
		if (valFoundQty == 2) {
			retval = Math.round((double) dataAr[valFoundIdList.get(0)] / (double) dataAr[valFoundIdList.get(1)]);
			return retval;
		}

		// if qty >= 4, we trim
		int counter = 0;
		for (Integer valFoundId : valFoundIdList) {
			curVal = dataAr[valFoundId];

			if (valFoundIdList.size() < 5) {
				sum += curVal;
				counter++;
			} else if ((curVal >= minThreashold) && (curVal <= maxThreashold)) {
				sum += curVal;
				counter++;
			}

		}
		retval = Math.round((double) sum / (double) counter);

		return retval;
	}

	/**
	 * @param dataAr
	 * @return
	 */
	public static List<Integer> ana_giveValueFoundIdList(Integer[] dataAr) {
		List<Integer> valFoundIdList = new ArrayList<Integer>();
		Integer curVal = null;

		// on doit d'abord savoir combien on a de valeurs non nulles
		for (int i = 0; i < dataAr.length; i++) {
			curVal = dataAr[i];

			if (curVal == null)
				continue;

			valFoundIdList.add(i);
		}

		return valFoundIdList;
	}

	// /**
	// * @param keySpecificMetricSet
	// * @return
	// */
	// public static Integer[] ana_trim_MinMaxVals_fromAr(Integer[] dataAr, int
	// minVal_index, int maxVal_index) {
	//
	// LOG.info("SEB ana_trim_MinMaxVals_fromAr ==========> " +
	// IaUtils.display(dataAr));
	// Integer[] trimmedAr = new Integer[dataAr.length - 2];
	//
	// int trimJ = 0;
	// for (int i = 0; i < dataAr.length; i++) {
	// if (i == minVal_index)
	// continue;
	//
	// if (i == maxVal_index)
	// continue;
	//
	// trimmedAr[trimJ] = dataAr[i];
	// trimJ++;
	// }
	//
	// return trimmedAr;
	// }

	/**
	 * @param dataAr
	 * @return
	 */
	public static List<Integer> ana_idxValuesOffCorridor(double average, Integer[] dataAr, double corridorHight) {
		List<Integer> idxOfValues_offCorridor_list = new ArrayList<Integer>();

		Integer curVal = 0;
		double lowValueFromAvg = average - (average * corridorHight / 100D);
		double highValueFromAvg = average + (average * corridorHight / 100D);

		for (int i = 0; i < dataAr.length; i++) {
			curVal = dataAr[i];
			if (curVal == null)
				continue;

			if ((curVal.intValue() > highValueFromAvg) || (curVal.intValue() < lowValueFromAvg)) {
				idxOfValues_offCorridor_list.add(new Integer(i));
			}

		}

		return idxOfValues_offCorridor_list;
	}

	/**
	 * @param requestURI
	 * @return
	 */
	public static Map<String, Integer> convertQueryString(URI requestURI) {
		Map<String, Integer> keyValMap = new HashMap<String, Integer>();

		String[] pairs = requestURI.getQuery().split("&");
		// convert queryString to key/value Map
		for (String pair : pairs) {
			String[] keyValAr = pair.split("=");
			String key = keyValAr[0];
			if (keyValAr.length == 1)
				keyValMap.put(key, 0);
			else
				keyValMap.put(key, Integer.parseInt(keyValAr[1]));
		}
		return keyValMap;
	}

	public static Charset getWindows1252Charset() {
		return Charset.forName("Cp1252");
	}

	public static Charset getUTF8Charset() {
		return Charset.forName("UTF-8");
	}

	/**
	 * en parcourant un JSONObject on cherche parmi ses éléments fils le
	 * premier dont le nom fini par "_agg"
	 * 
	 * @param jso
	 * @return
	 */
	public static String findInnerJsAggName(JSONObject jso) {
		// String nextAggName = null;
		String[] elementAr = JSONObject.getNames(jso);
		for (String element : elementAr) {
			if (element.endsWith("_agg"))
				return element;
		}
		return null;
	}

	/**
	 * @param fileName
	 */
	public static synchronized JSONObject readJsonFromFile(String fileName) {
		int lineCounter = 0;
		FileReader dataFileReader = null;
		BufferedReader br = null;
		StringBuffer sb = null;
		String sCurrentLine = "";

		try {
			// on lit le fichier et on charge tout en m�moire dans un tableau
			// de Metriques
			dataFileReader = new FileReader(fileName);
			br = new BufferedReader(dataFileReader);
			sb = new StringBuffer(3500);

			/*
			 * Pour chaque ligne du fichier
			 */
			while (sCurrentLine != null) {

				sCurrentLine = br.readLine();
				sb.append(sCurrentLine);
				lineCounter++;
			}

		} catch (IOException e) {
			LOG.error("Problem while reading line[" + lineCounter + "]. We continue... - " + e.getMessage(), e);
		} finally {
			try {
				br.close();
				dataFileReader.close();
			} catch (IOException e) {
				LOG.error("IOException on br.close() or dfReader.close() -- finally -- silently ignoring error"
						+ e.getMessage(), e);
			}
		}
		JSONObject json = new JSONObject(sb.toString());

		return json;
	}

	/**
	 * @param fileName
	 */
	public static synchronized List<String> readFileToStringList(String fileName) {
		int lineCounter = 0;
		FileReader dataFileReader = null;
		BufferedReader br = null;
		String sCurrentLine = "";
		List<String> lineList = new ArrayList<String>(100);

		try {
			// on lit le fichier et on charge tout en m�moire dans un tableau
			// de Metriques
			dataFileReader = new FileReader(fileName);
			br = new BufferedReader(dataFileReader);
			lineList = new ArrayList<String>(100);

			/*
			 * Pour chaque ligne du fichier
			 */
			while (sCurrentLine != null) {

				sCurrentLine = br.readLine();
				if (sCurrentLine.contains("MATCHES -- "))
					continue;

				lineList.add(sCurrentLine);
				lineCounter++;
			}

		} catch (IOException e) {
			LOG.error("Problem while reading line[" + lineCounter + "]. We continue... - " + e.getMessage(), e);
		} finally {
			try {
				br.close();
				dataFileReader.close();
			} catch (IOException e) {
				LOG.error("IOException on br.close() or dfReader.close() -- finally -- silently ignoring error"
						+ e.getMessage(), e);
			}
		}

		return lineList;
	}

	/**
	 *
	 * @param outFileName
	 * @param jsonObject
	 */
	public static synchronized void writeJsonToFile(String outFileName, JSONObject jsonObject) {
		FileWriter dataFileWriter = null;
		BufferedWriter bw = null;

		try {
			// on lit le fichier et on charge tout en m�moire dans un tableau
			// de Metriques
			dataFileWriter = new FileWriter(outFileName);
			bw = new BufferedWriter(dataFileWriter);

			bw.write(jsonObject.toString(4));

		} catch (IOException e) {
			LOG.error("Problem while writing jsonObject" + Constants.DASHES + e.getMessage(), e);
		} finally {
			try {
				bw.close();
				dataFileWriter.close();
			} catch (IOException e) {
				LOG.error("IOException on bw.close() or dfWriter.close() -- finally -- silently ignoring error"
						+ e.getMessage(), e);
			}
		}

	}

	/**
	 * @param text
	 */
	public static void displaySectionSeparator(String text) {
		LOG.info(Constants.EQUAL_LINE + "  " + text);

	}

	// /**
	// * @param map
	// * @param Map<String,
	// * Integer>
	// * @return
	// */
	// public static String prettyPrintGlobalMap(Map<String, Integer>
	// globalInfoMap, Map<String, String> globalKoFoundKeyMap) {
	//
	// Map<String, String> bothMapsMap = new TreeMap<String, String>();
	// bothMapsMap.putAll(globalKoFoundKeyMap);
	//
	// for (String key : globalInfoMap.keySet()) {
	// String value = globalInfoMap.get(key) + "";
	// bothMapsMap.put(key, value);
	// }
	//
	// StringBuffer sb = new StringBuffer();
	// sb.append("\n");
	// for (String key : bothMapsMap.keySet()) {
	// sb.append("==== ").append(key.toLowerCase()).append(" :
	// ").append(bothMapsMap.get(key)).append("\n");
	// }
	// return sb.toString();
	// }

	/**
	 *
	 * @param globalInfoMap
	 * @return
	 */
	public static String prettyPrintGlobalInfoMap(Map<String, String> globalInfoMap) {

		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		for (String key : globalInfoMap.keySet()) {
			sb.append("==== ").append(key.toLowerCase()).append(" : ").append(globalInfoMap.get(key)).append("\n");
		}
		return sb.toString();
	}

	/**
	 * @param metaObj
	 * @param fieldName
	 * @return
	 */
	public static String getJsonStringValue(JSONObject metaObj, String fieldName) {
		String value = null;
		if (!JSONObject.NULL.equals(metaObj.get(fieldName)))
			value = metaObj.getString(fieldName);
		// sinon null
		return value;
	}

	/**
	 *
	 * @param ctx_CURRENT_KEY_COL
	 * @return
	 */
	public static String computeLeafKeyPath(Map<String, String> ctx_CURRENT_KEY_COL) {
		StringBuffer sb = new StringBuffer();

		for (int i = 1; i < ctx_CURRENT_KEY_COL.size() + 1; i++) {
			if (i == ctx_CURRENT_KEY_COL.size())
				sb.append(ctx_CURRENT_KEY_COL.get("level" + i));
			else
				sb.append(ctx_CURRENT_KEY_COL.get("level" + i) + Constants.TRIM_DASHES);
		}

		return sb.toString();
	}

	/**
	 * @param encSAMLRequest
	 * @return
	 */
	public static String base64_Decode(String encSAMLRequest) {
		String ret = null;

		// SamlRequest samlRequest = null; //the xml is compressed (deflate) and
		// encoded (base64)
		byte[] decodedBytes = null;
		try {
			LOG.info("SAMLRequest : decodedBytes = Base64.getDecoder().decode(urlDecodedSamlRequest)");
			decodedBytes = Base64.getDecoder().decode(encSAMLRequest.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			LOG.error("SAMLRequest : UnsupportedEncodingException", e);
			e.printStackTrace();
		}

		try {
			// try DEFLATE (rfc 1951) -- according to SAML spec
			LOG.info("SAMLRequest : InflaterInputStream(decodedBytes)");
			ret = new String(inflate(decodedBytes, true));
			// return new SamlRequest(new String(inflate(decodedBytes, true)));
		} catch (Exception ze) {
			// try zlib (rfc 1950) -- initial impl didn't realize java docs are
			// wrong
			try {
				LOG.error(new String(inflate(decodedBytes, false)));
			} catch (Exception e) {
				LOG.error("SAMLRequest : Exception [" + e.getClass().getSimpleName() + "]", e);
				e.printStackTrace();
			}
			// return new SamlRequest(new String(inflate(decodedBytes, false)));
		}
		return ret;
	}

	private static byte[] inflate(byte[] bytes, boolean nowrap) throws Exception {

		Inflater decompressor = null;
		InflaterInputStream decompressorStream = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			decompressor = new Inflater(nowrap);
			decompressorStream = new InflaterInputStream(new ByteArrayInputStream(bytes), decompressor);
			byte[] buf = new byte[1024];
			int count;
			while ((count = decompressorStream.read(buf)) != -1) {
				out.write(buf, 0, count);
			}
			return out.toByteArray();
		} finally {
			if (decompressor != null) {
				decompressor.end();
			}
			try {
				if (decompressorStream != null) {
					decompressorStream.close();
				}
			} catch (IOException ioe) {
				/* ignore */
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ioe) {
				/* ignore */
			}
		}
	}

	/**
	 * @param esIndex
	 * @param prefix
	 * @return
	 */
	public static String extractMonthNbrFromIndexName(String esIndex, String prefix) {
		/*
		 * +5 pour "2017." et +5+2 avec "
		 */
		int prefix_yyyy = prefix.length() + 5;
		int idxNameLength = esIndex.length();
		int maxLength = (prefix_yyyy + 2) >= idxNameLength ? idxNameLength : prefix_yyyy + 2;

		String monthNbStr = esIndex.substring(prefix_yyyy, maxLength);
		return monthNbStr;
	}

	/**
	 * @param flow_metric_jso
	 * @param cLine
	 */
	public static void displayJson(JSONObject flow_metric_jso, String cLine) {
		LOG.info(cLine);
		LOG.info(flow_metric_jso.toString(4));
		LOG.info(cLine);
	}

	/**
	 * @param perKey_string_int_Map
	 * @param keyPrefix
	 * @param removePrefix
	 *            true/false
	 * @return
	 */
	public static <T> Map<String, T> filterMapOnKeyPrefix_ObjectValue(Map<String, T> perKey_string_int_Map,
			String keyPrefix, boolean removePrefix) {
		Map<String, T> newMap = new TreeMap<String, T>();

		for (String key : perKey_string_int_Map.keySet()) {

			if (key.startsWith(keyPrefix)) {
				String trimedKey = key;
				if (removePrefix)
					trimedKey = key.substring(keyPrefix.length());

				newMap.put(trimedKey, perKey_string_int_Map.get(key));
			}
		}
		return newMap;
	}

	/**
	 * @param perKey_string_int_Map
	 * @param toRemovePrefix
	 * @param toKeepLength
	 * @return
	 */
	public static Map<String, Map<String, Integer>> trimInnerMapKey_ObjectValue(
			Map<String, Map<String, Integer>> perKey_string_int_Map, String toRemovePrefix, int toKeepLength) {
		Map<String, Map<String, Integer>> newMap = new TreeMap<String, Map<String, Integer>>();
		Map<String, Integer> innerMap = null;
		Map<String, Integer> newInnerMap = null;
		String newInnerKey = null;

		for (String key : perKey_string_int_Map.keySet()) {
			innerMap = perKey_string_int_Map.get(key);

			newInnerMap = new TreeMap<String, Integer>();
			for (String innerKey : innerMap.keySet()) {
				newInnerKey = innerKey;
				if (innerKey.startsWith(toRemovePrefix))
					newInnerKey = innerKey.substring(toRemovePrefix.length(), toRemovePrefix.length() + toKeepLength);

				newInnerMap.put(newInnerKey, innerMap.get(innerKey));
			}

			newMap.put(key, newInnerMap);
		}
		return newMap;
	}

	/**
	 * @param filteredMetricName_docId_int_Map
	 * @return
	 */
	public static SortedSet<String> extractUniqueDateSet_fromInnerKeyMaps(
			Map<String, Map<String, Integer>> filteredMetricName_docId_int_Map) {
		SortedSet<String> uniqueDateSet = new TreeSet<String>();

		for (String key : filteredMetricName_docId_int_Map.keySet()) {
			Map<String, Integer> innerMap = filteredMetricName_docId_int_Map.get(key);
			for (String innerDatekey : innerMap.keySet()) {
				uniqueDateSet.add(innerDatekey);
			}
		}
		return uniqueDateSet;
	}

	/**
	 * @param resultLocalFileName
	 * @param dirName
	 * @return
	 * @throws IOException
	 */
	public static boolean isFileLocallyStored(String resultLocalFileName, String dirName) throws IOException {
		File dir = new File(dirName);
		if (!dir.exists()) {
			dir.mkdirs();
			return false;
		}

		if (resultLocalFileName == null)
			return false;

		File jsonFile = new File("./" + dirName + "/" + resultLocalFileName);
		if (jsonFile.exists())
			return true;

		return false;
	}

	/**
	 * @param resultFileName
	 * @return
	 */
	public static JSONObject readJsonFileLocallyStored(String resultFileName) {
		JSONObject jso = IaUtils.readJsonFromFile(resultFileName);
		return jso;
	}

	/**
	 * @param arValueMap
	 * @return
	 */
	public static Map<String, String> convertToUniqueValueMap(Map<String, String[]> arValueMap) {
		Map<String, String> uniqueValueMap = new TreeMap<String, String>();
		String[] valAr = null;
		for (String key : arValueMap.keySet()) {
			valAr = arValueMap.get(key);
			if (!isNullOrEmptyOrGreaterThanOne(valAr))
				uniqueValueMap.put(key, valAr[0]);
		}

		return uniqueValueMap;
	}

	/**
	 * @param valAr
	 * @return
	 */
	public static <T> String display(T[] valAr) {
		StringBuffer sb = new StringBuffer();

		for (T val : valAr) {
			if (val == null)
				continue;

			sb.append(val.toString()).append(", ");
		}

		String finalStr = sb.toString().trim();
		finalStr = finalStr.substring(0, finalStr.length() - 1);

		return finalStr;
	}

	/**
	 * @param valList
	 * @return
	 */
	public static <T> String display(List<T> valList) {
		StringBuffer sb = new StringBuffer();

		for (T val : valList) {
			if (val == null)
				continue;

			sb.append(val.toString()).append(", ");
		}

		String finalStr = sb.toString().trim();
		finalStr = finalStr.substring(0, finalStr.length() - 1);

		return finalStr;
	}

	/**
	 * @param valSet
	 * @return
	 */
	public static <T> String display(Set<T> valSet) {
		StringBuffer sb = new StringBuffer();

		for (T val : valSet) {
			if (val == null)
				continue;

			sb.append(val.toString()).append(", ");
		}

		String finalStr = sb.toString().trim();
		finalStr = finalStr.substring(0, finalStr.length() - 1);

		return finalStr;
	}

	/**
	 * @param jsonDataFileMap
	 */
	public static void displayMapInnerMap(Map<String, SortedMap<String, String>> jsonDataFileMap) {
		String method = "displayMapInnerMap()" + Constants.DASHES;
		SortedMap<String, String> innerMap = null;
		String innerMapVal = null;

		for (String mapKey : jsonDataFileMap.keySet()) {
			innerMap = jsonDataFileMap.get(mapKey);
			for (String innerMapKey : innerMap.keySet()) {
				innerMapVal = innerMap.get(innerMapKey);
				LOG.info(method + "[" + mapKey + "] [" + innerMapKey + "] [" + innerMapVal + "]");
			}
		}

	}

	/**
	 * @param keySet
	 * @param currentString
	 * @return
	 */
	public static String adjustToMaxString(Set<String> keySet, String currentString) {
		/*
		 * calcul du maxLength
		 */
		int maxLength = 0;
		for (String key : keySet) {
			int keyLength = key.length();
			if (keyLength > maxLength) {
				maxLength = keyLength;
			}
		}

		String spaces = "";
		spaces = "";
		int strLength = currentString.length();
		if (strLength < maxLength) {
			spaces += addSpaces(maxLength - strLength);
		}

		String finalStr = currentString + spaces;
		return finalStr;
	}

	/**
	 * @param length
	 * @return
	 */
	public static String addSpaces(int length) {
		String spaces = "";
		length = Math.abs(length);

		for (int i = 0; i < length; i++) {
			spaces += " ";
		}

		return spaces;
	}

	/**
	 * @param threadName
	 * @return
	 */
	public static Thread getThreadByName(String threadName) {

		Set threadSet = Thread.getAllStackTraces().keySet();
		Iterator<Thread> i = threadSet.iterator();
		while (i.hasNext()) {
			Thread t = i.next();
			if (t.getName().equals(threadName))
				return t;
		}
		return null;
		// for (Thread t : ) {
		// if (t.getName().equals(threadName)) return t;
		// }
		// return null;
		// }
	}

	/**
	 * @param logger
	 * @param e
	 * @param someText
	 */
	public static void logCaughtException(Logger logger, Exception e, String someText) {
		String errorMsg = e.getClass().getName() + " -- " + e.getMessage();
		if (someText != null)
			errorMsg = someText + Constants.DASHES + errorMsg;

		LOG.error(errorMsg, e);
	}

	/**
	 * @param classToLoadList
	 */
	public static void loadClassList(List<String> classToLoadList) {
		String method = "loadClassList()" + Constants.DASHES;

		for (String classToLoad : classToLoadList) {
			try {
				Class.forName(classToLoad);
			} catch (ClassNotFoundException e) {
				logCaughtException(LOG, e, null);
			}
		}

	}

	/**
	 * @param pkgPrefixAr
	 */
	public static void showClassLoaderClasses(String[] pkgPrefixAr, ClassLoader classLoader) {
		String method = "showCurrentThreadClassLoaderClasses(" + display(pkgPrefixAr) + ") ";

		LOG.info(Constants.Y_LINE + method + "==== BEGIN");
		ClassLoader myCL = classLoader;
		while (myCL != null) {
			Vector<Class> classVector = listAllClassesInClassLoader(myCL);
			LOG.info(
					"Vector<Class> classVector [" + classVector.hashCode() + "] has [" + classVector.size() + "] elts");

			for (int i = 0; i < classVector.size(); i++) {

				Class clazz = (Class) classVector.get(i);

				for (String pkgPrefix : pkgPrefixAr) {
					if (clazz.getName().startsWith(pkgPrefix))
						LOG.info("\t" + clazz);
				}
			}
			myCL = myCL.getParent();
		}

		LOG.info(Constants.Y_LINE + method + "==== END");
	}

	/**
	 * @param CL
	 * @return
	 */
	public static Vector<Class> listAllClassesInClassLoader(ClassLoader CL) {
		Class CL_class = CL.getClass();
		while (CL_class != java.lang.ClassLoader.class) {
			CL_class = CL_class.getSuperclass();
		}
		Vector<Class> classes = null;
		try {
			java.lang.reflect.Field ClassLoader_classes_field = CL_class.getDeclaredField("classes");
			ClassLoader_classes_field.setAccessible(true);
			classes = (Vector) ClassLoader_classes_field.get(CL);
		} catch (Exception e) {
			LOG.error(e.getClass().getSimpleName() + e.getMessage(), e);
		}

		return classes;
	}

	/**
	 * @param zipFileSource
	 *            the zip which will receives the files to add
	 * @param filesToAddAr
	 * @param targetDirInJar
	 * @throws IOException
	 */
	public static void addFilesToZip(File zipFileSource, File[] filesToAddAr, String targetDirInJar)
			throws IOException {
		String method = "addFilesToZip(zip|jarFile, [" + filesToAddAr.length + "]class files, targetDirInJar)"
				+ Constants.DASHES;
		ZipOutputStream zout = null;
		ZipInputStream oldZip_zis = null;
		InputStream fileToAdd_fis = null;
		File tempZip = null;
		String newZipPath = zipFileSource.getAbsolutePath();
		File newZipFile = new File(newZipPath);

		try {
			// Create an empty temp file to get a fileName
			tempZip = File.createTempFile(zipFileSource.getName(), null);
			// LOG.info("tempZip " + tempZip.getName() + " and is at :" +
			// tempZip.getPath());
			tempZip.delete();

			// rename zipFileSource wih new name
			if (!zipFileSource.renameTo(tempZip)) {
				throw new IOException("Could not make temp file (" + tempZip.getName() + ")");
			}
			LOG.info(method + "Will add [" + filesToAddAr.length + "] class files to zipFileSource ["
					+ zipFileSource.getName() + "]");

			/*
			 * write each file to add into a new zipFile (zout = zipFileSource)
			 */
			byte[] buffer = new byte[4096];
			zout = new ZipOutputStream(new FileOutputStream(newZipFile));

			for (int i = 0; i < filesToAddAr.length; i++) {
				/*
				 * Read each File to add
				 */
				fileToAdd_fis = new FileInputStream(filesToAddAr[i]);

				/*
				 * write to zout (= zipFileSource)
				 */
				zout.putNextEntry(new ZipEntry(targetDirInJar + filesToAddAr[i].getName()));
				for (int read = fileToAdd_fis.read(buffer); read > -1; read = fileToAdd_fis.read(buffer)) {
					zout.write(buffer, 0, read);
				}
				zout.closeEntry();
				fileToAdd_fis.close();
			}

			/*
			 * add zipEntries from oldZip into the newZip file EXCEPT if
			 * zipEntry exists in filesToAddAr => It does not overwrite files
			 * added by file[] filesToAddAr !!!
			 */
			oldZip_zis = new ZipInputStream(new FileInputStream(tempZip));
			List<String> zipEntryList = new ArrayList<String>();
			for (ZipEntry ze = oldZip_zis.getNextEntry(); ze != null; ze = oldZip_zis.getNextEntry()) {
				// Sometimes 2 ZipEntry (in a zip file) have the same name in
				// the same directory !
				if (zipEntryList.contains(ze.getName()))
					continue;

				if (!zipEntryMatch(ze.getName(), filesToAddAr, targetDirInJar)) {
					zout.putNextEntry(ze);
					for (int read = oldZip_zis.read(buffer); read > -1; read = oldZip_zis.read(buffer)) {
						zout.write(buffer, 0, read);
					}
					zout.closeEntry();
					zipEntryList.add(ze.getName());
				}
			}

		} finally {
			if (zout != null) {
				zout.flush();
				zout.close();
			}
			if (oldZip_zis != null) {
				oldZip_zis.close();
			}
			if (fileToAdd_fis != null) {
				fileToAdd_fis.close();
			}

			tempZip.delete();
		}
	}

	/**
	 * Says if the zeName existe in fileAr
	 * 
	 * @param zeName
	 * @param fileAr
	 * @param path
	 * @return
	 */
	public static boolean zipEntryMatch(String zeName, File[] fileAr, String path) {

		for (int i = 0; i < fileAr.length; i++) {

			String zeShort = zeName;
			if (zeShort.length() > 60)
				zeShort = zeName.substring(60);

			String fileToAdd = path + fileAr[i].getName();
			fileToAdd = fileToAdd.replaceAll("\\\\", "/");

			LOG.info("Comparing zeName [" + zeName + "] to [" + fileToAdd + "]");
			if (fileToAdd.equals(zeName)) {
				LOG.info("return true;");
				return true;
			}
		}
		return false;
	}

	/**
	 * @param min
	 * @param max
	 * @return
	 */
	public static int generateRandomInt(int min, int max) {
		int retVal = ThreadLocalRandom.current().nextInt(min, max);
		return retVal;
	}

	/**
	 * @param value
	 * @param places
	 * @return
	 */
	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	// =========================================== private methods
}
