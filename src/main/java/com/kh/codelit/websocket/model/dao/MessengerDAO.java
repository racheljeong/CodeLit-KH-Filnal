package com.kh.codelit.websocket.model.dao;

import java.util.List;
import java.util.Map;

import com.kh.codelit.websocket.model.vo.Messenger;

public interface MessengerDAO {

	int insertMsg(Messenger msg);

	int insertMsgCurrval(Messenger msg);

	List<Map<String, String>> selectAuth(String auth);

	List<Messenger> alarmList(String name);

}
