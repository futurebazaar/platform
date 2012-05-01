package com.fb.platform.payback.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;
import java.util.StringTokenizer;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class PointsUtil {
	
	public String getPreviousDayDate(){
		DateTime datetime = new DateTime();
		datetime = datetime.minusDays(14);
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		return fmt.print(datetime);
	}
	
	public String convertDateToFormat(String settlementDate, String string) {
		DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTime datetime = format.parseDateTime(settlementDate);
		format = DateTimeFormat.forPattern(string);
		String newSettlementDate = format.print(datetime);
		return newSettlementDate;
	}
	
	public Properties getProperties(String fileName) throws IOException{
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("payback.properties");
		Properties props = new Properties();
		props.load(inStream);
		inStream.close();
		return props;
	}
	
	public int getEarnPoints(BigDecimal amount, DateTime datetime, String clientName, String code){
		try {
			Properties props = getProperties("points.properties");
			String day = datetime.dayOfWeek().getAsText();
			BigDecimal earnRatio = new BigDecimal(props.getProperty(clientName.toUpperCase() + "_" + code + "_POINTS"));
			String earnFactorMap = props.getProperty(clientName.toUpperCase() + "_" + code +"_FACTOR");
			//String [] earnFactorList = earnFactorMap.split(";");
			StringTokenizer earnFactorIterator = new StringTokenizer(earnFactorMap, ",");
			int earnFactor = Integer.parseInt(earnFactorIterator.nextToken());
			while(earnFactorIterator.hasMoreTokens()){
				String dayFactorMap = earnFactorIterator.nextToken();
				String[] dayFactorList = dayFactorMap.split(",");
				if (day.equals(dayFactorList[0]) && dayFactorList[1] != null && !dayFactorList[1].equals("")){
					earnFactor = earnFactor * Integer.parseInt(dayFactorList[1]);
					break;
				}			
			}
			earnRatio = earnRatio.multiply(new BigDecimal(earnFactor));
			BigDecimal txnPoints = amount.multiply(earnRatio);
			return txnPoints.ROUND_HALF_EVEN;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
