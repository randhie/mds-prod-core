package com.md.studio.json;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.view.AbstractView;

public class JsonView extends AbstractView{
	private static final String CONTENT_TYPE = "Content-type";
	 private static final String CHAR_ENCODING = ";charset=\"UTF-8\"";
	protected JsonData jsonData;
	
	public JsonView(){}
	
	public JsonView(JsonData jsonData){
		this.jsonData = jsonData;
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> arg0,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		response.setHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON.toString() + CHAR_ENCODING);
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.print(jsonData.toJsonString());
			writer.flush();	
		}
		catch (final IOException e){
			throw new IllegalStateException(e);
		}
		finally {
			if (writer != null) {
				writer.close();
			}
		}
		
	}

}
