package io.code.dao;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *@author songkejun
 */
@Service
public interface SysGeneratorDao {
	
	List<Map<String, Object>> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	Map<String, String> queryTable(String tableName);
	
	List<Map<String, String>> queryColumns(String tableName);
}
