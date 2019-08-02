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
