package de.zokki.server.HttpHandler;

import java.net.HttpURLConnection;
import java.util.Date;

import com.sun.net.httpserver.HttpExchange;

import de.zokki.server.Utils.ContentType;

public class MainContextHandler extends AbstractContextHandler {

    @Override
    public void handle(HttpExchange httpExchange) {
	this.httpExchange = httpExchange;
	try {
	    String receivedString = getDataFromStream();
	    if(receivedString == null) {
		System.out.println("FILE");
	    } else if (!receivedString.isBlank()) {
	    }
	    System.out.println(receivedString);

	    String response = "<b>" + new Date() + "</b> for " + httpExchange.getRequestURI();
	    sendDataWithCode(HttpURLConnection.HTTP_OK, response, ContentType.TEXT_HTML);
	} catch (Exception e) {
	    handleException(e);
	} catch (Error e) {
	    handleError(e);
	}
    }
}
