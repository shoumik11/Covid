package utils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import object.WorldDataTuple;

// Shuvradeb Barman Srijon

public class Utils {
	public static String createRandomString(int len, boolean do_capitalize) {
		int leftLimit = 'a';
		int rightLimit = 'z';
		Random random = new Random();

		int targetStringLength = (int) (3 + random.nextInt(len - 3 + 1));
		StringBuilder buffer = new StringBuilder(targetStringLength);

		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + random.nextInt(rightLimit - leftLimit + 1);
			if(do_capitalize && i == 0)
				randomLimitedInt -= 32;
			buffer.append((char) randomLimitedInt);
		}
		return buffer.toString();
	}
	public static float getRandomLat() {
		float loc = (float) (19.50139 + Math.random() * (64.85694 - 19.50139));
		return loc;
	}
	public static float getRandomLong() {
		float loc = (float) (-161.75583 + Math.random() * (-68.01197 - -161.75583));
		return loc;
	}
	public static int getEpochFromDateString(String dateString)
	{
		if(dateString == null) return 0;
		try 
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dt = sdf.parse(dateString);
			long epoch = dt.getTime();
			return (int)(epoch/1000);
		} catch(ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
	public static String getDateStringFromEpoch(int epoch, boolean... dateOnly)
	{
		try
		{
			//Date date = new Date(((long)epoch) * 1000);
			LocalDateTime localDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.of("GMT"));	
			String formatStr = "yyyy-MM-dd";
			if(dateOnly != null && dateOnly.length > 0)
				formatStr = "dd";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatStr);
			//SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
			return formatter.format(localDate);
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getRandomDateAsString()
	{
		LocalDate from = LocalDate.of(2020, 1, 1);
		LocalDate to = LocalDate.now();
		long days = from.until(to, ChronoUnit.DAYS);
		long randomDays = ThreadLocalRandom.current().nextLong(days + 1);
		LocalDate randomDate = from.plusDays(randomDays);
		return randomDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
	}
	public static String getRandomLastTestDateAsString(String infectedDate) {
		String[] d = infectedDate.split("-");
		LocalDate infDate = LocalDate.of(Integer.parseInt(d[0]),
				Integer.parseInt(d[1]), Integer.parseInt(d[2]));
		LocalDate to = LocalDate.now();
		long days = infDate.until(to, ChronoUnit.DAYS);
		long randomDays = ThreadLocalRandom.current().nextLong(days + 1);
		LocalDate randomDate = infDate.plusDays(randomDays);
		return randomDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
	}
	public static String getRandomRecoveryDateAsString(String infectedDate) {
		String[] d = infectedDate.split("-");
		LocalDate infDate = LocalDate.of(Integer.parseInt(d[0]),
				Integer.parseInt(d[1]), Integer.parseInt(d[2]));
		LocalDate from = LocalDate.of(2020, 1, 1);
		LocalDate to = LocalDate.now();
		long days = from.until(to, ChronoUnit.DAYS);
		long randomDays = ThreadLocalRandom.current().nextLong(days + 1);
		LocalDate randomDate = from.plusDays(randomDays);
		Period period = Period.between(from, randomDate);
		if(randomDate.compareTo(infDate) < 0 || period.getDays() < 14)
			return "";
		return randomDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
	}
	public static int getRandomTestCount() {
		int c = (int) (1 + Math.random() * (4 - 1));
		return c;
	}
	public static double getRandomRate(double min, double max) {
		return min + Math.random() * (max - min + 1);
	}
	public static double getRandomRate(double avg) {
		double min = avg - 0.002;
		double max = avg + 0.002;
		return min + Math.random() * (max - min);
	}
	public static double getMaxLimit(List<WorldDataTuple> w)
	{
		double max = 0;
		for(int i = 0; i < w.size(); i++)
		{
			WorldDataTuple t = w.get(i);
			if(t.getDeathCount() > max)
				max = t.getDeathCount();
			if(t.getRecoveredCount() > max)
				max = t.getRecoveredCount();
			if(t.getInfectedCount() > max)
				max = t.getInfectedCount();
			if(t.getInfectionRate() > max)
				max = t.getInfectionRate();
		}
		return max;
	}
	public static float round(float d, int decimalPlace)
	{
		String fStr = Float.toString(d);
		if(fStr==null||fStr.isEmpty()||fStr.equals("NaN")||fStr.equals("Infinity"))
			return 0.000f;
	    BigDecimal bd = new BigDecimal(Float.toString(d));
	    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
	    return bd.floatValue();
	}
	public static String encryptString(String input)
	{ 
		try { 
			MessageDigest md = MessageDigest.getInstance("SHA-512");  
			byte[] messageDigest = md.digest(input.getBytes()); 
			BigInteger no = new BigInteger(1, messageDigest);  
			String hashtext = no.toString(16); 
			while (hashtext.length() < 32) { 
				hashtext = "0" + hashtext; 
			} 
			return hashtext; 
		}
		catch (NoSuchAlgorithmException e) { 
			throw new RuntimeException(e); 
		} 
	}
}
