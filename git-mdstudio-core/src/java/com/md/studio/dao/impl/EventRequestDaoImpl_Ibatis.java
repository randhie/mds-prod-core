package com.md.studio.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.md.studio.dao.EventRequestDao;
import com.md.studio.domain.EventRequest;

public class EventRequestDaoImpl_Ibatis extends SqlMapClientDaoSupport implements EventRequestDao {
	private static final String STMT_INSERT = "EventRequest.insert";
	private static final String STMT_UPDATE_HASCONTACTED = "EventRequest.updateHasContacted";
	private static final String STMT_UPDATE_HASACCOMPLISHED = "EventRequest.updateHasAccomplished";
	private static final String STMT_UPDATE = "EventRequest.update";
	private static final String STMT_UPDATE_REQ_CANCELLED = "EventRequest.updateReqCancelled";
	private static final String STMT_SELECT_BYID = "EventRequest.selectById";
	private static final String STMT_SELECT_BYEMAIL = "EventRequest.selectByEmail";
	private static final String STMT_SELECT_HASCONTACTED = "EventRequest.selectHasContacted";
	private static final String STMT_SELECT_HASACCOMPLISHED = "EventRequest.selectHasAccomplished";
	private static final String STMT_SELECTALL = "EventRequest.selectAll";
	
	@Override
	public void insert(EventRequest eventRequest) {
		getSqlMapClientTemplate().insert(STMT_INSERT, eventRequest);

	}

	@Override
	public void update(EventRequest eventRequest) {
		getSqlMapClientTemplate().update(STMT_UPDATE, eventRequest);
	}

	@Override
	public void updateHasContacted(EventRequest eventRequest) {
		getSqlMapClientTemplate().update(STMT_UPDATE_HASCONTACTED, eventRequest);
	}

	@Override
	public void updateHasAccomplished(EventRequest eventRequest) {
		getSqlMapClientTemplate().update(STMT_UPDATE_HASACCOMPLISHED, eventRequest);
	}

	
	
	
	@Override
	public void updateCancelEvent(EventRequest eventRequest) {
		getSqlMapClientTemplate().update(STMT_UPDATE_REQ_CANCELLED, eventRequest);
	}

	@Override
	public EventRequest selectById(long eventId) {
		return (EventRequest) getSqlMapClientTemplate().queryForObject(STMT_SELECT_BYID, eventId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EventRequest> selectByEmail(String emailAddress) {
		return getSqlMapClientTemplate().queryForList(STMT_SELECT_BYEMAIL, emailAddress);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EventRequest> selectHasContacted(Boolean hasContacted) {
		return getSqlMapClientTemplate().queryForList(STMT_SELECT_HASCONTACTED, hasContacted);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EventRequest> selectHasAccomplished(Boolean hasAccomplished) {
		return getSqlMapClientTemplate().queryForList(STMT_SELECT_HASACCOMPLISHED, hasAccomplished);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EventRequest> selectAll() {
		return getSqlMapClientTemplate().queryForList(STMT_SELECTALL);
	}
}

