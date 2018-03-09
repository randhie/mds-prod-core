package com.md.studio.dao.impl.typehandler;

import java.sql.SQLException;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;
import com.md.studio.domain.enums.SiteUserStatus;

public class SiteUserStatusTypeHandlerCallback implements TypeHandlerCallback {

	@Override
	public Object getResult(ResultGetter getter) throws SQLException {
		return SiteUserStatus.valueOf(getter.getString());
	}

	@Override
	public void setParameter(ParameterSetter setter, Object param)	throws SQLException {
		if (param == null) {
			setter.setString(null);
		}
		else {
			setter.setString( ((SiteUserStatus)param).toString() );
		}

	}

	@Override
	public Object valueOf(String s) {
		return s;
	}

}
