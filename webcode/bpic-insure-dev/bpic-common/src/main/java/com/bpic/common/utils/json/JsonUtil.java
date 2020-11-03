package com.bpic.common.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonUtil {

	private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
	private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<Map<String, Object>> toListMap(String json){
        List<Object> list = JSON.parseArray(json);
        List< Map<String,Object>> listw = new ArrayList<Map<String,Object>>();
        for (Object object : list){
            Map <String,Object> ret = (Map<String, Object>) object;//取出list里面的值转为map
            listw.add(ret);
        }
        return listw;

    }


	/***
	 * List 转为 JSON
	 * 
	 * @param list
	 * @return
	 */
	public static <T> String list2Json(List<T> list) {
		try {
			if (null != list && list.size() > 0) {
				return JSON.toJSONString(list);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	/***
	 * JSON 转换为 List
	 * 
	 * @param jsonStr
	 *            [{"age":12,"createTime":null,"id":"","name":"wxw", "registerTime":null,"sex":1},{...}]
	 * @param objectClass
	 * @return
	 */
	public static <T> List<T> json2List(String jsonStr, Class<T> objectClass) {
		try {
			if (StringUtils.isNotBlank(jsonStr)) {
				List<T> list = (List<T>) JSON.parseArray(jsonStr, objectClass);
				return list;
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	/***
	 * Object 转为 JSON
	 * 
	 * @param object
	 * @return
	 */
	public static String object2Json(Object object) {
		try {
			if (null != object) {
				if (object instanceof String) {
					return String.valueOf(object);
				} else {
					return JsonMapper.toJsonString(object);
				}
			}
		} catch (Exception e) {
			logger.error("object2Json Exception", e);
			return String.valueOf(object);
		}
		return "";
	}
	/***
	 * 将String转换为JsonObject
	 * 
	 * @param string
	 * @return
	 */
	public static JSONObject stringJsonObject(String string) {
		JSONObject json = new JSONObject();
		try {
			if (null != string) {
				if (string instanceof String) {
					json = JSON.parseObject(string.toString());
				} else {
					String jsonStr = JSON.toJSONString(string);
					json = JSON.parseObject(jsonStr);
				}
			}
		} catch (Exception e) {
			logger.error("stringJsonObject Exception", e);
		}
		return json;
	}

	/***
	 * 
	 * JSON 转 Object
	 * 
	 * @param jsonStr
	 *            [{"age":12,"createTime":null,"id":"","name":"wxw", "registerTime":null,"sex":1}]
	 * @param objectClass
	 * @return
	 */
	public static <T> T json2Ojbect(String jsonStr, Class<T> objectClass) {
		try {
			if (null != jsonStr) {
				String leftStr = jsonStr.substring(0, 2);
				String rightStr = jsonStr.substring(jsonStr.length() - 2, jsonStr.length());
				if (leftStr.equals("[{")) {
					jsonStr = jsonStr.substring(1, jsonStr.length());
				}
				if (rightStr.equals("}]")) {
					jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
				}
				return (T) JSON.parseObject(jsonStr, objectClass);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	/***
	 * JsonArray 转为 JSON
	 * 
	 * @param jsonArray
	 * @return
	 */
	public static String jsonArrayToJSONString(JSONArray jsonArray) {
		try {
			if (null != jsonArray) {
				return jsonArray.toString();
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return "";
	}

	/***
	 * JsonObject 转为 JSON
	 * 
	 * @param jsonObject
	 * @return
	 */
	public static String jsonObjectToJSONString(JSONObject jsonObject) {
		try {
			if (null != jsonObject) {
				return jsonObject.toString();
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return "";
	}

	/***
	 * 将Object转换为JsonObject
	 * 
	 * @param object
	 * @return
	 */
	public static JSONObject object2JsonObject(Object object) {
		JSONObject json = new JSONObject();
		try {
			if (null != object) {
				if (object instanceof String) {
					json = JSON.parseObject(object.toString());
				} else {
					String jsonStr = JSON.toJSONString(object);
					json = JSON.parseObject(jsonStr);
				}
			}
		} catch (Exception e) {
			logger.error("object2JsonObject Exception", e);
		}
		return json;
	}

	public static String getValue(JSONObject json, String key) {
		String value = "";
		if (json.containsKey(key) && json.get(key) != null) {
			value = json.get(key) + "";
		}
		return value;
	}

	/**
	 * 去重复index_id项合并value值
	 * 
	 * @param args
	 */
	public static JSONArray delRepeatIndexid(JSONArray array) {
		JSONArray arrayTemp = new JSONArray();

		int num = 0;
		for (int i = 0; i < array.size(); i++) {
			if (num == 0) {
				arrayTemp.add(array.get(i));
			} else {
				int numJ = 0;
				for (int j2 = 0; j2 < arrayTemp.size(); j2++) {
					JSONObject newJsonObjectI = (JSONObject) array.get(i);
					JSONObject newJsonObjectJ = (JSONObject) arrayTemp.get(j2);
					String codeI = newJsonObjectI.get("code") == null ? "" : newJsonObjectI.get("code").toString();
					String nameI = newJsonObjectI.get("name").toString();
					String typeI = newJsonObjectI.get("type").toString();

					String codeJ = newJsonObjectJ.get("code") == null ? "" : newJsonObjectJ.get("code").toString();
					if (codeI.equals(codeJ)) {
						arrayTemp.remove(j2);
						JSONObject newObject = new JSONObject();
						newObject.put("code", codeI);
						newObject.put("name", nameI);
						newObject.put("type", typeI);
						arrayTemp.add(newObject);
						break;
					}
					numJ++;
				}
				if (numJ - 1 == arrayTemp.size() - 1) {
					arrayTemp.add(array.get(i));
				}
			}
			num++;
		}
		return arrayTemp;
	}

	public static String getJSONString(Object object) throws Exception {
		String jsonString = object2Json(object);
		return jsonString == null ? "{}" : jsonString;
	}

	/**
	 * 将对象转换成json字符串。
	 * <p>Title: pojoToJson</p>
	 * <p>Description: </p>
	 * @param data
	 * @return
	 *
	 */
	public static String objectToJson(Object data) {
		try {
			String string = objectMapper.writeValueAsString(data);
			return string;
		} catch (JsonProcessingException e) {
			logger.error("[业务时间{}]+[{}方法]+[捕获异常]", LocalDateTime.now(),Thread.currentThread().getStackTrace()[1].getMethodName(),e);
//            log.error("[业务时间:"+ LocalDateTime.now()
//                    +"线程ID:"+Thread.currentThread().getId()
//                    +"方法描述:json转换"+"主要参数:data"
//                    +JsonUtils.objectToJson(data)+"]异常详情:",e);
		}
		return null;
	}
}