package no.acntech.seaturtle.receiver.resource;

import no.acntech.seaturtle.receiver.message.Heartbeat;
import no.acntech.seaturtle.receiver.storage.HeartbeatMessageBuffer;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/heartbeat")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HeartbeatResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatResource.class);
    private static final HeartbeatMessageBuffer MESSAGE_BUFFER = new HeartbeatMessageBuffer();

    @GET
    public List<Heartbeat> listEvents() {
        return MESSAGE_BUFFER.list();
    }

    @POST
    public Response event(@NotEmpty String event, @Context HttpServletRequest request) throws InterruptedException {
        LOGGER.info("Heartbeat event received: {}", event);
        String remote = getRemote(request);
        Heartbeat heartbeat = new Heartbeat(remote, event);
        MESSAGE_BUFFER.put(heartbeat);
        return Response.status(201).entity(heartbeat).build();
    }

    private String getRemote(HttpServletRequest request) {
        if (request == null) {
            return "N/A";
        } else {
            String host = request.getRemoteHost();
            String address = request.getRemoteAddr();
            int port = request.getRemotePort();
            return String.format("%s (%s:%s)", host, address, port);
        }
    }
}