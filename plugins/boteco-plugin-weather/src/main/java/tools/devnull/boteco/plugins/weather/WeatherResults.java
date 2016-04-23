/*
 * The MIT License
 *
 * Copyright (c) 2016 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
 *
 * Permission  is hereby granted, free of charge, to any person obtaining
 * a  copy  of  this  software  and  associated  documentation files (the
 * "Software"),  to  deal  in the Software without restriction, including
 * without  limitation  the  rights to use, copy, modify, merge, publish,
 * distribute,  sublicense,  and/or  sell  copies of the Software, and to
 * permit  persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * The  above  copyright  notice  and  this  permission  notice  shall be
 * included  in  all  copies  or  substantial  portions  of the Software.
 *
 * THE  SOFTWARE  IS  PROVIDED  "AS  IS",  WITHOUT  WARRANTY OF ANY KIND,
 * EXPRESS  OR  IMPLIED,  INCLUDING  BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN  NO  EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM,  DAMAGES  OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT  OR  OTHERWISE,  ARISING  FROM,  OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE   OR   THE   USE   OR   OTHER   DEALINGS  IN  THE  SOFTWARE.
 */

package tools.devnull.boteco.plugins.weather;

import com.google.gson.annotations.SerializedName;

public class WeatherResults {

  private Query query;

  public Query getQuery() {
    return query;
  }

  public void setQuery(Query query) {
    this.query = query;
  }

  public boolean hasResult() {
    return query.count > 0;
  }

  public String text() {
    return query.results.channel.item.title;
  }

  public String condition() {
    return query.results.channel.item.condition.text;
  }

  public int temperatureInCelsius() {
    return query.results.channel.item.condition.temperature;
  }

  public int temperatureInFahrenheits() {
    return (int) (temperatureInCelsius() * 1.8 + 32);
  }

  public static class Query {

    private int count;

    @SerializedName("lang")
    private String language;

    private Results results;

    public int getCount() {
      return count;
    }

    public void setCount(int count) {
      this.count = count;
    }

    public String getLanguage() {
      return language;
    }

    public void setLanguage(String language) {
      this.language = language;
    }

    public Results getResults() {
      return results;
    }

    public void setResults(Results results) {
      this.results = results;
    }
  }

  public static class Results {

    private Channel channel;

    public Channel getChannel() {
      return channel;
    }

    public void setChannel(Channel channel) {
      this.channel = channel;
    }

  }

  public static class Channel {

    private Item item;

    public Item getItem() {
      return item;
    }

    public void setItem(Item item) {
      this.item = item;
    }

  }

  public static class Item {

    private String title;
    @SerializedName("lat")

    private double latitude;

    @SerializedName("long")
    private double longitude;

    private String link;

    private Condition condition;

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public double getLatitude() {
      return latitude;
    }

    public void setLatitude(double latitude) {
      this.latitude = latitude;
    }

    public double getLongitude() {
      return longitude;
    }

    public void setLongitude(double longitude) {
      this.longitude = longitude;
    }

    public String getLink() {
      return link;
    }

    public void setLink(String link) {
      this.link = link;
    }

    public Condition getCondition() {
      return condition;
    }

    public void setCondition(Condition condition) {
      this.condition = condition;
    }
  }

  public static class Condition {

    private int code;

    @SerializedName("temp")
    private int temperature;

    private String text;

    public int getCode() {
      return code;
    }

    public void setCode(int code) {
      this.code = code;
    }

    public int getTemperature() {
      return temperature;
    }

    public void setTemperature(int temperature) {
      this.temperature = temperature;
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }
  }

}
