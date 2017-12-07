package nl.yogh.aerius.server.servlet;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import nl.yogh.aerius.server.registry.ServiceRegistry;

/**
 * The Dispatcher servlet is responsible for encoding/decoding rpc calls and
 * forwarding them to the correct service.
 *
 * TODO Setup dependency injection to avoid having to subclass the dispatcher
 * servlet to set the service registry.
 */
public abstract class GWTDispatcherServlet extends RemoteServiceServlet {
  private static final Logger LOG = LoggerFactory.getLogger(GWTDispatcherServlet.class);

  private static final long serialVersionUID = 8733982574355761682L;

  protected ServiceRegistry registry;

  @Override
  public String processCall(final String payload) throws SerializationException {
    try {
      final HttpServletRequest request = getThreadLocalRequest();

      final Object service = registry.findService(request);

      if (service == null) {
        return RPC.encodeResponseForFailure(null,
            new NullPointerException("No service found for <" + request.getRequestURL() + ">."));
      }

      final RPCRequest rpcRequest = RPC.decodeRequest(payload, service.getClass(), this);
      onAfterRequestDeserialized(rpcRequest);

      return RPC.invokeAndEncodeResponse(service, rpcRequest.getMethod(), rpcRequest.getParameters(),
          rpcRequest.getSerializationPolicy());
    } catch (final IncompatibleRemoteServiceException ex) {
      LOG.error("Incompatible remote version for request: {}", payload, ex);
      return RPC.encodeResponseForFailure(null, ex);
    }
  }
}
