package com.kh.codelit.community.notice.model.service;


import java.util.List;
import java.util.Map;

import com.kh.codelit.attachment.model.vo.Attachment;
import com.kh.codelit.community.notice.model.vo.Notice;

public interface NoticeService {

	int insertBoard(Notice notice);

	int getListCount();

	List<Notice> noticeList(Map<String, Object> param);

	Notice selectOneNotice(int noticeNo);

	int delete(int noticeNo);

	int update(Notice notice);

	int updateCnt(int noticeNo);

	int insertAttachment(Attachment attach);

	Attachment selectOneAttach(int noticeNo);

	int deleteAttach(int noticeNo);

}
