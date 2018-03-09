package com.md.studio.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.md.studio.dao.UserTestimonialDao;
import com.md.studio.domain.SiteUserRate;
import com.md.studio.domain.Testimonials;

public class UserTestimonialDaoImpl_Ibatis extends SqlMapClientDaoSupport implements UserTestimonialDao {
	private static final String STMT_INSERT = "Testimonials.insert";
	private static final String STMT_DELETE = "Testimonials.delete";
	private static final String STMT_UPDATE = "Testimonials.update";
	private static final String STMT_DELETEBYEMAIL = "Testimonials.deleteByEmail";
	private static final String STMT_SELECTBYID = "Testimonials.selectById";
	private static final String STMT_SELECTBYEMAIL = "Testimonials.selectByEmail";
	private static final String STMT_SELECTALLBYFILTER = "Testimonials.selectAllByFilter";
	private static final String STMT_SELECTCOUNT_ALLTESTI_RATE = "Testimonials.selectCountAll";
	
	private static final String PARAM_LIMIT = "limit";
	private static final String PARAM_OFFSET = "offset";
	
	
	@Override
	public void insert(Testimonials testimonial) {
		getSqlMapClientTemplate().insert(STMT_INSERT, testimonial);
	}

	@Override
	public void update(Testimonials testimonial) {
		getSqlMapClientTemplate().update(STMT_UPDATE, testimonial);
	}

	@Override
	public void delete(String testimonialId) {
		getSqlMapClientTemplate().delete(STMT_DELETE, testimonialId);
	}

	@Override
	public void deleteAllByEmail(String emailAddress) {
		getSqlMapClientTemplate().delete(STMT_DELETEBYEMAIL, emailAddress);
	}

	@Override
	public Testimonials getTestimonial(long testimonialId) {
		return (Testimonials) getSqlMapClientTemplate().queryForObject(STMT_SELECTBYID, testimonialId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Testimonials> getTestimonial(String emailAddress) {
		return getSqlMapClientTemplate().queryForList(STMT_SELECTBYEMAIL, emailAddress);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Testimonials> getAllTestimonials(int limit, int offset) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put(PARAM_LIMIT, limit);
		params.put(PARAM_OFFSET, offset);
		return getSqlMapClientTemplate().queryForList(STMT_SELECTALLBYFILTER, params);
	}

	@Override
	public List<SiteUserRate> getAllTestimonialRates() {
		return getSqlMapClientTemplate().queryForList(STMT_SELECTCOUNT_ALLTESTI_RATE);
	}
}
