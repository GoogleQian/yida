package utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import entity.adverstising.Advertising;
import entity.dev.DevConnInfo;
import entity.dev.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;

/**
 * @author hzh
 * @date 2018/11/28
 */
public class MqttUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(MqttUtil.class);

	/**
	 * 获取字符传长度
	 *
	 * @param s 要操作的字符串
	 */
	private static int getLength(String s) {
		int length = 0;
		for (int i = 0; i < s.length(); i++) {
			int ascii = Character.codePointAt(s, i);
			if (ascii >= 0 && ascii <= 255) {
				length++;
			} else {
				length += 2;
			}
		}
		return length;
	}

	/**
	 * 数字小于10返回01,02，03，否则返回本身字符串
	 *
	 * @param s 要操作的字符串
	 */
	private static String strLen(String s) {
		int length = getLength(s);
		String change;
		if (length < ConstantUtil.STRING_LENGTH) {
			change = "0" + length;
		} else {
			change = String.valueOf(length);
		}
		return change;
	}


	public static byte[] adUnicode(String pushMessage) throws UnsupportedEncodingException {
		//截取广告前的字符串。
		String[] split = pushMessage.split(",@");
		String str1 = split[0] + ",";
		LOGGER.info("广告前的字符串：" + str1 + "长度为:" + str1.length());
		byte[] gbBytes = str1.getBytes(ConstantUtil.GB2312);
		//截取第四个逗号后的字符串
		String str2 = split[1];
		LOGGER.info("广告的字符串：" + str2);
		byte[] unicodeBytes = str2.getBytes("Unicode");
		int str1Length = gbBytes.length;
		int str2length = unicodeBytes.length;
		LOGGER.info("广告编码后个数：" + str2length);
		//数组扩容
		gbBytes = Arrays.copyOf(gbBytes, str1Length + str2length - 2);
		//将unicodeBytes 第二个位置开始 复制到 gbBytes
		System.arraycopy(unicodeBytes, 2, gbBytes, str1Length, str2length - 2);
		return gbBytes;
	}


	/**
	 * 组合下发广告类容
	 *
	 * @param ad 广告实体类
	 * @return 组合结果
	 */
	public static String getAdvMsg(Advertising ad, String prefix) {
		if (ad == null) {
			return null;
		}

		int id = ad.getId();

		byte[] targets = new byte[4];
		targets[3] = (byte) (id);
		targets[2] = (byte) ((id >> 8) & 0xff);
		targets[1] = (byte) ((id >> 16) & 0xff);
		targets[0] = (byte) ((id >> 24) & 0xff);

		String res = new String(targets);

		String sendTime = DateUtil.format(new Date());
		String updateTime = DateUtil.format(ad.getUpdateTime());
		return prefix + sendTime + ConstantUtil.MQTT_COMMA + updateTime + ConstantUtil.MQTT_COMMA + res + ConstantUtil.MQTT_COMMA + MqttUtil.strLen(ad.getTitle()) + ConstantUtil.MQTT_COMMA

				+ MqttUtil.strLen(ad.getSolgan()) + ConstantUtil.MQTT_COMMA + "@" + ad.getTitle() + ad.getSolgan();
	}


	/**
	 * 组合下发时间
	 *
	 * @return 组合结果
	 */
	public static String getTimeMsg() {
		return ConstantUtil.MQTT_TIME_PREFIX + DateUtil.format(new Date());
	}

	/**
	 * 组合时间
	 *
	 * @return 组合结果
	 */
	public static String getActiveTime() {
		return ConstantUtil.MQTT_ACTIVE_PREFIX + DateUtil.format(new Date());
	}

	/**
	 * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。
	 */
	public static int bytesToInt(byte[] src, int offset) {
		byte[] targetByte = new byte[4];
		System.arraycopy(src, 0, targetByte, 0, 4);
		int value;
		value = (((targetByte[offset] & 0xFF) << 24)
				| ((targetByte[offset + 1] & 0xFF) << 16)
				| ((targetByte[offset + 2] & 0xFF) << 8)
				| (targetByte[offset + 3] & 0xFF));
		return value;
	}

	/**
	 * 获取设备在线状态
	 *
	 * @param topic 主题
	 */
	public static boolean getDevSubTopicStatus(String topic, String connectUrl, String appid, String appsecret) {
		boolean exist = false;
		String topic2 = topic.replace("/", "%");
		JSONObject jsonObject = HttpClientUtil.doGet(connectUrl + topic2, null, appid, appsecret);
		if (jsonObject != null) {
			JSONArray jsonObject1 = jsonObject.getJSONArray("data");
			if (jsonObject1 != null && jsonObject1.size() != 0) {
				exist = true;
			}
		}
		return exist;
	}
}
