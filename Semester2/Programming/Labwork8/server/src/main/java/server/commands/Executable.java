package server.commands;

import common.network.requests.Request;
import common.network.responses.Response;

public interface Executable {
  Response apply(Request request);
}
