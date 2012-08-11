package com.fb.platform.promotion.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import com.fb.commons.to.PlatformMessage;

/**
 * @author nehaga
 *
 */
public class MessageTranslatorUtility implements MessageSourceAware{
	
	private MessageSource messageSource = null;

	public String translate(List<PlatformMessage> codeList) {
		List<String> messageList = new ArrayList<String>();
		for(PlatformMessage code : codeList) {
			messageList.add(messageSource.getMessage(code.getCode(), code.getArgumentsList(), Locale.US));
		}
		return StringUtils.join(messageList.toArray(), ",");
	}
	
	public String getMessage(PlatformMessage code) {
		return messageSource.getMessage(code.getCode(), code.getArgumentsList(), Locale.US);
	}
	
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
		
	}
}
