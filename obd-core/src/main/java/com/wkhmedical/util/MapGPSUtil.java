/**
 * 
 */
package com.wkhmedical.util;

import java.math.BigDecimal;

/**
 * @author Administrator
 */
public class MapGPSUtil {
	private static double Pi = 3.14159265358979324;
	private static double A = 6378245.0;
	private static double Ee = 0.00669342162296594323;

	public static BigDecimal[] Transform(double wgLat, double wgLon) {
		BigDecimal[] latlng = null;
		try {
			latlng = new BigDecimal[2];
			if (OutOfChina(wgLat, wgLon)) {
				latlng[0] = new BigDecimal(wgLat).setScale(10, BigDecimal.ROUND_HALF_UP);
				latlng[1] = new BigDecimal(wgLon).setScale(10, BigDecimal.ROUND_HALF_UP);
				return latlng;
			}
			double dLat = TransformLat(wgLon - 105.0, wgLat - 35.0);
			double dLon = TransformLon(wgLon - 105.0, wgLat - 35.0);
			double radLat = wgLat / 180.0 * Pi;
			double magic = Math.sin(radLat);
			magic = 1 - Ee * magic * magic;
			double sqrtMagic = Math.sqrt(magic);
			dLat = (dLat * 180.0) / ((A * (1 - Ee)) / (magic * sqrtMagic) * Pi);
			dLon = (dLon * 180.0) / (A / sqrtMagic * Math.cos(radLat) * Pi);

			BigDecimal bdWgLat = new BigDecimal(wgLat);
			BigDecimal bddLat = new BigDecimal(dLat);
			latlng[0] = bdWgLat.add(bddLat).setScale(10, BigDecimal.ROUND_HALF_UP);

			BigDecimal bdWgLon = new BigDecimal(wgLon);
			BigDecimal bddLon = new BigDecimal(dLon);
			latlng[1] = bdWgLon.add(bddLon).setScale(10, BigDecimal.ROUND_HALF_UP);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return latlng;
	}

	private static boolean OutOfChina(double lat, double lon) {
		if (lon < 72.004 || lon > 137.8347) return true;
		if (lat < 0.8293 || lat > 55.8271) return true;
		return false;
	}

	private static double TransformLat(double x, double y) {
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * Pi) + 20.0 * Math.sin(2.0 * x * Pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(y * Pi) + 40.0 * Math.sin(y / 3.0 * Pi)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(y / 12.0 * Pi) + 320 * Math.sin(y * Pi / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	private static double TransformLon(double x, double y) {
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * Pi) + 20.0 * Math.sin(2.0 * x * Pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(x * Pi) + 40.0 * Math.sin(x / 3.0 * Pi)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(x / 12.0 * Pi) + 300.0 * Math.sin(x / 30.0 * Pi)) * 2.0 / 3.0;
		return ret;
	}
}
