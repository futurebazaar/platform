/**
 * 
 */
package com.fb.platform.order;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author vinayak
 *
 */
@Path("/order")
@Component
@Scope("request")
public class OrderResource {

	@GET
	@Produces("text/xml")
	public Object getOrder() {
		return "<order></order>";
	}
}
