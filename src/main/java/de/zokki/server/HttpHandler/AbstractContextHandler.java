package de.zokki.server.HttpHandler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.zokki.server.Utils.Constants;
import de.zokki.server.Utils.ContentType;

public abstract class AbstractContextHandler implements HttpHandler {

    protected HttpExchange httpExchange;

    private File tempFile = new File(Constants.TEMP_DIR + "tempFile");

    protected String getDataFromStream() throws IOException {
	tempFile.delete();
	boolean n = false;
	BufferedInputStream input = new BufferedInputStream(httpExchange.getRequestBody());
	ByteArrayOutputStream received = new ByteArrayOutputStream();
	byte[] buffer = new byte[32768];
	int length;
	while ((length = input.read(buffer)) != -1) {
	    received.write(buffer, 0, length);
	    if (received.size() > 2500000) {
		n = true;
		appendToFile(received.toString(Constants.RECEIVED_CHARSET).getBytes());
		received.reset();
	    }
	}
	received.close();
	input.close();
	if (n) {
	    appendToFile(received.toString(Constants.RECEIVED_CHARSET).getBytes());
	    return null;
	}
	return received.toString(Constants.RECEIVED_CHARSET);
    }

    protected void sendDataWithCode(int code, String response, ContentType contentType) throws IOException {
	sendDataWithCode(code, response.getBytes(Constants.SEND_CHARSET), contentType);
    }

    protected void sendDataWithCode(int code, byte[] response, ContentType contentType) throws IOException {
	httpExchange.getResponseHeaders().add("content-type",
		contentType.getContent() + "; charset=" + Constants.SEND_CHARSET);
	httpExchange.sendResponseHeaders(code, response.length);

	BufferedOutputStream output = new BufferedOutputStream(httpExchange.getResponseBody());
	output.write(response);
	output.flush();
	output.close();
    }

    protected void handleError(Error error) {
	error.printStackTrace();
	try {
	    sendDataWithCode(HttpURLConnection.HTTP_INTERNAL_ERROR, error.toString(), ContentType.TEXT_PLAIN);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    protected void handleException(Exception exception) {
	exception.printStackTrace();
	try {
	    sendDataWithCode(HttpURLConnection.HTTP_INTERNAL_ERROR, exception.toString(), ContentType.TEXT_PLAIN);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void appendToFile(byte[] toWrite) throws IOException {
	BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(tempFile, true));
	writer.write(toWrite);
	writer.close();
    }
}
