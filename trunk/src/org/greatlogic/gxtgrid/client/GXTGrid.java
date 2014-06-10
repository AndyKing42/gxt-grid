package org.greatlogic.gxtgrid.client;
/*
 * Copyright 2006-2014 Andy King (GreatLogic.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
import java.util.Random;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.info.DefaultInfoConfig;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.info.InfoConfig;

public class GXTGrid implements EntryPoint {
//--------------------------------------------------------------------------------------------------
private static Random _random;
//--------------------------------------------------------------------------------------------------
static {
  _random = new Random(System.currentTimeMillis());
}
//--------------------------------------------------------------------------------------------------
/**
 * Returns a random value between 0 and the maxValue minus one.
 * @param maxValue The maximum value plus one for the random value.
 * @return A random integer between 0 (inclusive) and the specified value minus one.
 */
public static int getRandomInt(final int maxValue) {
  return _random.nextInt(maxValue);
}
//--------------------------------------------------------------------------------------------------
/**
 * Returns a random value between a minimum and maximum value minus one.
 * @param minValue The minimum value for the random value.
 * @param maxValue The maximum value plus one for the random value.
 * @return A random integer between the minimum and maximum value minus one.
 */
public static int getRandomInt(final int minValue, final int maxValue) {
  return _random.nextInt(maxValue - minValue) + minValue;
}
//--------------------------------------------------------------------------------------------------
public static void info(final int seconds, final String message) {
  final InfoConfig infoConfig = new DefaultInfoConfig("", message);
  infoConfig.setDisplay(seconds * 1000);
  final Info info = new Info();
  info.show(infoConfig);
}
//--------------------------------------------------------------------------------------------------
@Override
public void onModuleLoad() {
  final PetGridWidget petGrid = new PetGridWidget();
  RootPanel.get().add(petGrid);
  final ListStore<PetType> petTypeStore = new ListStore<PetType>(new ModelKeyProvider<PetType>() {
    @Override
    public String getKey(final PetType petType) {
      return Integer.toString(petType.getPetTypeId());
    }
  });
  TestData.loadPetTypeTestData(petTypeStore);
  TestData.loadPetTestData(petGrid.getListStore(), petTypeStore);
}
//--------------------------------------------------------------------------------------------------
}