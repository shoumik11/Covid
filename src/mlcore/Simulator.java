
package mlcore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import database.Config;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.Utils;

public class Simulator {
	public final static String AttrName_DATE = "Date";
	public final static String AttrName_CONFIRMED = "Confirmed";
	public final static String AttrName_RECOVERED = "Recovered";
	public final static String AttrName_DEATHS = "Deaths";
	public final static String AttrName_INCREASERATE = "Increase rate";
	public final static String AttrName_POPULATION = "pop";
	public final static int WORLD_POPULATION = 77936456;
	private static boolean isRateFlattened = false;
	private static boolean isModelOnly = false;
	public static void setModelOnly(boolean state) { Simulator.isModelOnly = state; }
	public static boolean getModelState() { return Simulator.isModelOnly; }
	private static boolean isRateZero(List<Double> rateList, int count)
	{
		if(count < 5 || rateList.size() < count)
			return false;
		double val;
		int i = rateList.size() - count;
		val = rateList.get(i);
		i++;
		for( ; i < rateList.size(); i++)
		{
			if(val != rateList.get(i))
				return false;
		}
		System.out.println("Rate = 0");
		isRateFlattened = true;
		return true;
	}
	public static int[] getPrediction(int fromDateEpoch, int dayCount, String attrName) throws Exception
	{
		ArrayList<Integer> ret = new ArrayList<Integer>();
		String modelPath;
		switch (attrName) {
		case AttrName_RECOVERED:
			modelPath = "MPRecovered.model";
			break;
		case AttrName_DEATHS:
			modelPath = "MPDeaths.model";
			break;
		default:
			modelPath = "MPConfirmed.model";
			break;
		}
		Classifier cls = (Classifier)weka.core.SerializationHelper.read("model/"+modelPath);
		final Attribute attributeDate = new Attribute(AttrName_DATE);
		final Attribute attributeUnpredicted = new Attribute(attrName);
		final Attribute attributePop = new Attribute(AttrName_POPULATION);

		ArrayList<Attribute> attributeList = new ArrayList<Attribute>(2) {
			private static final long serialVersionUID = 1L;

			{
				add(attributeDate);
				add(attributePop);
				add(attributeUnpredicted);
			}
		};
		// unpredicted data sets (reference to sample structure for new instances)
		Instances dataUnpredicted = new Instances("TestInstances", attributeList, 1);
		// last feature is target variable
		dataUnpredicted.setClassIndex(dataUnpredicted.numAttributes() - 1); 

		for(int i = 0; i < dayCount; i++)
		{
			int date = fromDateEpoch + i*Config.daySec;

			DenseInstance newInstanceConfirmed = new DenseInstance(dataUnpredicted.numAttributes()) {
				private static final long serialVersionUID = 1L;
				{
					setValue(attributeDate, date);
					setValue(attributePop, WORLD_POPULATION);
				}
			};
			DenseInstance newInstance = newInstanceConfirmed;
			newInstance.setDataset(dataUnpredicted);

			try
			{
				int result = (int)cls.classifyInstance(newInstance);
				System.out.println(date +":" + result);
				ret.add(result < 0 ?0:result);
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return ret.stream().filter(Objects::nonNull).mapToInt(Integer::intValue).toArray();
	}
	//	public static List<Integer> getPredictionList(int fromDateEpoch, int dayCount, String attrName) throws Exception
	//	{
	//		double factor = WORLD_POPULATION, rate = 0.0, current = 1;
	//		ArrayList<Integer> ret = new ArrayList<Integer>();
	//		ArrayList<Double> rateList = new ArrayList<Double>();
	//		String modelPath;
	//		switch (attrName) {
	//		case AttrName_RECOVERED:
	//			modelPath = "MPRecovered.model";
	//			break;
	//		case AttrName_DEATHS:
	//			modelPath = "MPDeaths.model";
	//			break;
	//		default:
	//			modelPath = "MPConfirmed.model";
	//			break;
	//		}
	//		Classifier cls = (Classifier)weka.core.SerializationHelper.read("model/"+modelPath);
	//		final Attribute attributeDate = new Attribute(AttrName_DATE);
	//		final Attribute attributeUnpredicted = new Attribute(attrName);
	//		final Attribute attributePop = new Attribute(AttrName_POPULATION);
	//
	//		ArrayList<Attribute> attributeList = new ArrayList<Attribute>(2) {
	//			private static final long serialVersionUID = 1L;
	//
	//			{
	//				add(attributeDate);
	//				add(attributePop);
	//				add(attributeUnpredicted);
	//			}
	//		};
	//		// unpredicted data sets (reference to sample structure for new instances)
	//		Instances dataUnpredicted = new Instances("TestInstances", attributeList, 1);
	//		// last feature is target variable
	//		dataUnpredicted.setClassIndex(dataUnpredicted.numAttributes() - 1); 
	//		factor = Config.getInfectionRate() < 0? Config.getInfectionRate():0;
	//		int p = 0;
	//		boolean r = false;
	//		for(int i = 0; i < dayCount; i++)
	//		{
	//			if(p == 0)
	//				r = isRateZero(rateList, dayCount/50);
	//			int date = (int) (fromDateEpoch + (int)(i*Config.daySec));
	//			DenseInstance newInstanceConfirmed = new DenseInstance(dataUnpredicted.numAttributes()) {
	//				private static final long serialVersionUID = 1L;
	//				{
	//					setValue(attributeDate, date);
	//					setValue(attributePop, WORLD_POPULATION);
	//				}
	//			};
	//			DenseInstance newInstance = newInstanceConfirmed;
	//			newInstance.setDataset(dataUnpredicted);
	//
	//			try
	//			{
	//				int result = (int)cls.classifyInstance(newInstance);
	//				System.out.println(date +":" + result);
	//				ret.add(result < 0 ?0:(int)(result*(1+factor)));
	//				if(current > 0)
	//				{
	//					rate = (result - current)/current;
	//					if(r)
	//						p = rateList.size();
	//					if(p==0)
	//						rateList.add(rate);
	//				}
	//				
	//				current = result;
	//			}
	//			catch (Exception e)
	//			{
	//				e.printStackTrace();
	//			}
	//			if(p > 0)
	//			{
	//				p--;
	//				factor = -rateList.get(p);
	//			}
	////			if(rate <= 0)
	////				factor = 1+Config.getInfectionRate();
	//			//factor = factor * (1-Config.getInfectionRate());
	//		}
	//		return ret;
	//	}
	public static List<Integer> getPredictionList(int fromDateEpoch, int dayCount, String attrName) throws Exception
	{
		int current;
		ArrayList<Integer> ret = new ArrayList<Integer>();
		String modelPath;
		switch (attrName) {
		case AttrName_RECOVERED:
			modelPath = "MPRecovered.model";
			break;
		case AttrName_DEATHS:
			modelPath = "MPDeaths.model";
			break;
		default:
			modelPath = "MPConfirmed.model";
			break;
		}
		Classifier cls = (Classifier)weka.core.SerializationHelper.read("model/"+modelPath);
		final Attribute attributeDate = new Attribute(AttrName_DATE);
		final Attribute attributeUnpredicted = new Attribute(attrName);
		final Attribute attributePop = new Attribute(AttrName_POPULATION);

		ArrayList<Attribute> attributeList = new ArrayList<Attribute>(2) {
			private static final long serialVersionUID = 1L;

			{
				add(attributeDate);
				add(attributePop);
				add(attributeUnpredicted);
			}
		};
		// unpredicted data sets (reference to sample structure for new instances)
		Instances dataUnpredicted = new Instances("TestInstances", attributeList, 1);
		// last feature is target variable
		dataUnpredicted.setClassIndex(dataUnpredicted.numAttributes() - 1); 

		if(isModelOnly)
		{

			for(int i = 0; i < dayCount; i++)
			{
				int date = fromDateEpoch + i*Config.daySec;

				DenseInstance newInstanceConfirmed = new DenseInstance(dataUnpredicted.numAttributes()) {
					private static final long serialVersionUID = 1L;
					{
						setValue(attributeDate, date);
						setValue(attributePop, WORLD_POPULATION);
					}
				};
				DenseInstance newInstance = newInstanceConfirmed;
				newInstance.setDataset(dataUnpredicted);

				try
				{
					int result = (int)cls.classifyInstance(newInstance);
					//System.out.println(date +":" + result);
					ret.add(result < 0 ?0:result);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			isRateFlattened = false;
			ArrayList<Double> rateList = new ArrayList<Double>();
			int currDate = fromDateEpoch;
			for(int i = 0; i < dayCount; i++)
			{
				int date= currDate + (int)(Config.daySec*(isRateFlattened?(Config.getInfectionRate()*5):Config.getInfectionRate()));
				int pop = (int) WORLD_POPULATION;
				if(attrName.equals(AttrName_RECOVERED))
				{
					pop = pop + (int)(Config.daySec*(Config.getInfectionRate() < 0?(20*(1-Config.getInfectionRate())):1));
				}
				int cc = pop;
				DenseInstance newInstanceConfirmed = new DenseInstance(dataUnpredicted.numAttributes()) {
					private static final long serialVersionUID = 1L;
					{
						setValue(attributeDate, date);
						setValue(attributePop, cc);
					}
				};
				DenseInstance newInstance = newInstanceConfirmed;
				newInstance.setDataset(dataUnpredicted);

				try
				{
					int result = (int)cls.classifyInstance(newInstance);
					//System.out.println(date +":" + result);
					if(ret.size() > 0)
					{
						if(attrName.equals(AttrName_CONFIRMED))
						{
							current = ret.get(ret.size()-1);
							double rate = Utils.roundDouble((double)(result-current)/current, 4);
							if(Math.abs(rate) > Math.abs(Config.getInfectionRate()*1.5))
							{
								//result = (int) (current+Config.getInfectionRate()*1.5);
								result = (int) (current* (1+Config.getInfectionRate()*1.5));
							}
						}
					}
					ret.add(result < 0 ?0:result);
					//if(attrName.equals(AttrName_CONFIRMED))
					{
						if(ret.size() > 1 && !isRateFlattened)
						{
							current = ret.get(ret.size()-2);
							double rate = Utils.roundDouble((double)(result-current)/current, 4);
							rateList.add(rate);
							isRateZero(rateList, dayCount/20);
						}
					}
					currDate = date;
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

}
