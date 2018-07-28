package cn.hutool.qq.test;

import java.util.Map.Entry;
import java.util.Set;

import cn.hutool.setting.Setting;

public enum QADict {
	INSTANCE;
	
	private Setting setting = new Setting("qa.setting");
	
	public String getAnswer(String question) {
		return setting.getStr(question);
	}
	
	/**
	 * 关键字包含查找答案
	 * @param question
	 * @return
	 */
	public String getAnswerLike(String question) {
		Set<Entry<String,String>> entrySet = setting.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String keys = entry.getKey();
			String[] keyArray = keys.split(",");
			for (String key : keyArray) {
				if(question.contains(key)){
					return entry.getValue();
				}
			}
		}
		return null;
	}
}
