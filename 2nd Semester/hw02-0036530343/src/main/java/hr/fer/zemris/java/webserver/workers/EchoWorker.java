package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Set;

public class EchoWorker implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {
        context.setMimeType("text/html");
        Set<String> namesSet = context.getParameterNames();

        context.write("<html><body>");
        context.write("<table><thead>");
        context.write("<tr><th>Param name</th><th>Param value</th></tr>");
        context.write("</thead");

        context.write("<tbody>");

        for(String name: namesSet) {
            String value = context.getParameter(name);

            context.write("<tr><td>" + name + "</td><td>" + value + "</td></tr>");
        }

        context.write("</tbody>");
        context.write("</table>");
        context.write("</body>");
        context.write("</html>");

    }
}
