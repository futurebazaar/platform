package com.fb.platform.payback.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
	

	/*private void setProperty(String key, String value) throws FileNotFoundException, IOException, URISyntaxException {
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		URL url = loader.getResource("payback.properties");
		InputStream inStream = url.openStream();
		Properties props = new Properties();
		props.load(inStream);
		FileOutputStream fos = new FileOutputStream(new File(url.toURI()));
		props.setProperty(key, value);
		props.store(fos,null);
		fos.flush();
		fos.close();
		inStream.close();
	}*/



}
