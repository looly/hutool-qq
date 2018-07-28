package cn.hutool.qq.util;

/**
 * Hash工具
 * 
 * @author looly
 *
 */
public class QQHashUtil {
	/**
	 * ptqrtoken令牌hash计算
	 * 
	 * @param qrsig 二维码令牌
	 * @return hash值
	 */
	public static int hash33(String qrsig) {
		int e = 0;
		int n = qrsig.length();
		for (int i = 0; i < n; i++)
			e += (e << 5) + qrsig.charAt(i);
		return 2147483647 & e;
	}

	/**
	 * 用户IDhash值计算
	 * 
	 * @param uin 用户ID
	 * @param ptwebqq ptwebqq，在登录时获取
	 * @return hash值
	 */
	public static String hashUid(long uin, String ptwebqq) {
		int[] N = new int[4];
		for (int T = 0; T < ptwebqq.length(); T++) {
			N[T % 4] ^= ptwebqq.charAt(T);
		}
		String[] U = { "EC", "OK" };
		long[] V = new long[4];
		V[0] = uin >> 24 & 255 ^ U[0].charAt(0);
		V[1] = uin >> 16 & 255 ^ U[0].charAt(1);
		V[2] = uin >> 8 & 255 ^ U[1].charAt(0);
		V[3] = uin & 255 ^ U[1].charAt(1);

		long[] U1 = new long[8];

		for (int T = 0; T < 8; T++) {
			U1[T] = T % 2 == 0 ? N[T >> 1] : V[T >> 1];
		}

		String[] N1 = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
		String V1 = "";
		for (long aU1 : U1) {
			V1 += N1[(int) ((aU1 >> 4) & 15)];
			V1 += N1[(int) (aU1 & 15)];
		}
		return V1;
	}
}
