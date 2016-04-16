package tools.devnull.boteco.rest;

public class AvailableChannel {

  private final String id;
  private final String name;

  public AvailableChannel(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

}
