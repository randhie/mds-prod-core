package com.md.studio.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.md.studio.dao.EventInfoDao;
import com.md.studio.domain.EventInfo;

public class EventInfoDaoImpl_Ibatis extends SqlMapClientDaoSupport implements EventInfoDao {
	private static final String STMT_INSERT = "EventInfo.insert";
	private static final String STMT_UPDATE = "EventInfo.update";
	private static final String STMT_DELETE = "EventInfo.delete";
	private static final String STMT_SELECT_BYID = "EventInfo.selectById";
	private static final String STMT_SELECT_BYNAME = "EventInfo.selectByName";
	private static final String STMT_SELECT_ALL = "EventInfo.selectAll";
	private static final String STMT_SELECT_EXPIRED = "EventInfo.selectExpired";
	private static final String STMT_SELECT_VALID = "EventInfo.selectValidEvents";
	private static final String PARAM_LIMIT = "limit";
	private static final String PARAM_OFFSET = "offset";

	@Override
	public EventInfo selectEventInfo(long eventId) {
		return (EventInfo) getSqlMapClientTemplate().queryForObject(STMT_SELECT_BYID, eventId);
	}

	@Override
	public EventInfo selectEventInfo(String eventName) {
		return (EventInfo) getSqlMapClientTemplate().queryForObject(STMT_SELECT_BYNAME, eventName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EventInfo> selectAllEventInfo(Integer limit, Integer offset) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put(PARAM_LIMIT, limit);
		params.put(PARAM_OFFSET, offset);
		return getSqlMapClientTemplate().queryForList(STMT_SELECT_ALL, params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EventInfo> selectExpiredEvents(Integer limit, Integer offset) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put(PARAM_LIMIT, limit);
		params.put(PARAM_OFFSET, offset);
		return getSqlMapClientTemplate().queryForList(STMT_SELECT_EXPIRED, params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EventInfo> selectValidEvents(Integer limit, Integer offset) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put(PARAM_LIMIT, limit);
		params.put(PARAM_OFFSET, offset);
		return getSqlMapClientTemplate().queryForList(STMT_SELECT_VALID, params);
	}

	@Override
	public void createEvent(EventInfo eventInfo) {
		getSqlMapClientTemplate().insert(STMT_INSERT, eventInfo);
	}

	@Override
	public void updateEvent(EventInfo eventInfo) {
		getSqlMapClientTemplate().update(STMT_UPDATE, eventInfo);
	}

	@Override
	public void deleteEvent(EventInfo eventInfo) {
		getSqlMapClientTemplate().delete(STMT_DELETE, eventInfo);
	}

}
