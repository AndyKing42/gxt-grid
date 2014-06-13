package org.greatlogic.gxtgrid.client;

import java.util.Date;
import com.google.gwt.i18n.client.DateTimeFormat;

public class Plant {

private final DateTimeFormat df      = DateTimeFormat.getFormat("MM/dd/y");
private static int           AUTO_ID = 0;

private int                  id;
private String               name;
private String               light;
private double               price;
private Date                 available;
private boolean              indoor;
private String               color;
private int                  difficulty;
private double               progress;

public Plant() {
  id = AUTO_ID++;

  difficulty = (int)(Math.random() * 100);
  progress = Math.random();

}

public Plant(final String name1, final String light1, final double price1, final String available1,
             final boolean indoor1) {
  this();
  setName(name1);
  setLight(light1);
  setPrice(price1);
  setAvailable(df.parse(available1));
  setIndoor(indoor1);
}

public double getProgress() {
  return progress;
}

public void setProgress(final double progress1) {
  this.progress = progress1;
}

public String getColor() {
  return color;
}

public int getDifficulty() {
  return difficulty;
}

public void setDifficulty(final int difficulty1) {
  this.difficulty = difficulty1;
}

public void setColor(final String color1) {
  this.color = color1;
}

public Date getAvailable() {
  return available;
}

public int getId() {
  return id;
}

public String getLight() {
  return light;
}

public String getName() {
  return name;
}

public double getPrice() {
  return price;
}

public boolean isIndoor() {
  return indoor;
}

public void setAvailable(final Date available1) {
  this.available = available1;
}

public void setId(final int id1) {
  this.id = id1;
}

public void setIndoor(final boolean indoor1) {
  this.indoor = indoor1;
}

public void setLight(final String light1) {
  this.light = light1;
}

public void setName(final String name1) {
  this.name = name1;
}

public void setPrice(final double price1) {
  this.price = price1;
}

@Override
public String toString() {
  return name != null ? name : super.toString();
}

}
