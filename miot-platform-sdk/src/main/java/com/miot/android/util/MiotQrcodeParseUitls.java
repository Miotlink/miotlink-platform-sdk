package com.miot.android.util;

public class MiotQrcodeParseUitls {

	public static String getQrcode(String qrcode) {
		String result = "";

		try {
			if (qrcode.length() < 11) {
				return "";
			}
			if (qrcode.startsWith("http")) {
				String[] strings = qrcode.split("&");
				if (strings != null) {
					for (String qrcodes : strings) {
						if (qrcodes.startsWith("qrcode")
								|| qrcodes.startsWith("QRCode")
								|| qrcodes.startsWith("qrCode")) {
							String[] results = qrcodes.split("=");
							if (results != null && results.length > 1) {
								if (checkMiotQRCode(results[1])) {
									result = results[1];
									return result;
								}
							}

						}else if(qrcode.indexOf("qrcode")>0||qrcode.indexOf("QRCode")>0||qrcode.indexOf("qrCode")>0){
							String [] eqs=qrcodes.split("=");
							if (eqs.length>0) {
								if (checkMiotQRCode(eqs[1])) {
									result = eqs[1];
									return result;
								}
							}
						}
					}

				}

			}
			if (qrcode.length() >= 11) {
				String qr=qrcode.substring(0, 11);
				if (checkMiotQRCode(qr)) {
					result=qrcode;
				}
			}
		} catch (Exception e) {
		

		}
		return result;
	}

	/**
	 * 
	 * @param qrcode
	 * @return
	 */
	private static boolean checkMiotQRCode(String qrcode) {
		try {
			String checkCode = qrcode.substring(10, 11);
			String content = qrcode.substring(0, 10);
			Character c = getLastIDNum(content);
			return (checkCode.equals(String.valueOf(c)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 二维码校验和
	 * 
	 * @param preIds
	 * @return
	 */

	public static Character getLastIDNum(String preIds) {
		Character lastId = null; // 当传入的字符串没有17位的时候，则无法计算，直接返回
		if (preIds == null) { // && preIds.length()<17) {
			return null;
		}
		int[] weightArray = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4,
				2 };// 权数数组
		String vCode = "10X98765432";// 校验码字符串
		int sumNum = 0;// 前17为乘以权然后求和得到的数
		// 循环乘以权，再求和
		int maxI = preIds.length() > 17 ? 17 : preIds.length();
		for (int i = 0; i < maxI; i++) {
			int index = Integer.parseInt(preIds.charAt(i) + "");
			sumNum = sumNum + index * weightArray[i];// 乘以权数，再求和
		}
		int modNum = sumNum % 11;// 求模
		lastId = vCode.charAt(modNum);// 从验证码中找出对应的数
		if (lastId == 'X')
			return '0';
		else
			return lastId;
	}

}
