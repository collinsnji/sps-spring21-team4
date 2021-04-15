package com.sps.app.servlets;

import com.sps.utils.PoemUtils;
import com.sps.utils.PoemTextToSpeech;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/read-poem")
public class PoemToSpeech extends HttpServlet {
    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        String poemText = new PoemUtils().convertRequestToJson(request).get("poemText").getAsString();
        ServletOutputStream stream = response.getOutputStream();
        byte[] poemUtterance = new PoemTextToSpeech().speakPoem(poemText);

        response.setContentType("application/octet-stream");
        response.setContentLength(poemUtterance.length);
        response.setStatus(200);
        stream.flush();
        stream.write(poemUtterance);
        stream.flush();
        stream.close();
    }
}
