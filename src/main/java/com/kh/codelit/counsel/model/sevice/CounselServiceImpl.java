package com.kh.codelit.counsel.model.sevice;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.codelit.counsel.model.dao.CounselDAO;


@Service
public class CounselServiceImpl implements CounselService {

	@Autowired
	private CounselDAO dao;

//	@Override
//	public List<counsel> counselList(Map<String, Object> param) {
//	
//		return dao.counselList(param);
//	}
//
//	@Override
//	public int getListCount() {
//	
//		return dao.getListCount();
//	}
//	
//




}
