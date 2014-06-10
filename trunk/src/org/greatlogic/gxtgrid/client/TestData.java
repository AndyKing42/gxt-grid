package org.greatlogic.gxtgrid.client;

import java.math.BigDecimal;
import java.util.Date;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.sencha.gxt.data.shared.ListStore;

public class TestData {
//--------------------------------------------------------------------------------------------------
private static final String[] PetNamesAndSex = new String[] {"Angel,F", "Ashley,F", "Bandit,M",
    "Beau,M", "Bella,F", "Bo,M", "Boomer,M", "Bubba,M", "Buddy,M", "Buster,M", "Callie,F",
    "Calvin,M", "Cassie,F", "Champ,M", "Chelsea,F", "Chester,M", "Cleopatra,F", "Cody,M",
    "Cookie,F", "Cooper,M", "Dexter,M", "Diesel,M", "Dixie,F", "Duke,M", "Duncan,M", "Dusty,M",
    "Emily,F", "Emma,F", "Felix,M", "Female,F", "Fred,M", "Grace,F", "Guinness,M", "Haley,F",
    "Harry,M", "Hunter,M", "Invisible Pink Unicorn,U", "Isabella,F", "Jack,M", "Jasmine,F",
    "Joey,M", "Junior,M", "Kitty,F", "Kobe,M", "Leo,M", "Loki,U", "Lucy,F", "Maddie,F", "Mandy,F",
    "Marley,M", "Max,M", "Maximus,M", "Maxwell,M", "Maya,F", "Merlin,M", "Mia,F", "Mickey,M",
    "Mikey,M", "Millie,F", "Milo,U", "Misty,F", "Mocha,U", "Moose,M", "Morgan,U", "Murphy,M",
    "Nala,U", "Nikki,F", "Olivia,F", "Oreo,U", "Pebbles,F", "Penny,F", "Rex,M", "Roxie,F",
    "Rufus,M", "Sabrina,F", "Sadie,F", "Sam,U", "Samantha,F", "Sarah,F", "Sassy,U", "Scooter,M",
    "Sebastian,M", "Sheba,F", "Simba,M", "Snowball,U", "Socks,U", "Sophia,F", "Sophie,F",
    "Spencer,M", "Sunny,F", "Sylvester,M", "Taz,M", "Thomas,M", "Tinkerbell,F", "Toby,M",
    "Tommy,M", "Tucker,M", "Winston,M", "Ziggy,M", "Zoe,F", "Zoey,F"};
@SuppressWarnings("deprecation")
public static void loadPetTestData(final ListStore<Pet> listStore,
                                   final ListStore<PetType> petTypeListStore) {
  listStore.clear();
  int nextPetId = 1;
  for (final String petNameAndSex : PetNamesAndSex) {
    final String[] nameAndSex = petNameAndSex.split(",");
    final int petTypeId = GXTGrid.getRandomInt(petTypeListStore.size()) + 1;
    final int year = 2012 + GXTGrid.getRandomInt(2) - 1900;
    final int month = GXTGrid.getRandomInt(12);
    final int day = GXTGrid.getRandomInt(1, 29);
    final int hour = GXTGrid.getRandomInt(8, 18);
    final Date intakeDate = new Date(year, month, day, hour, 0);
    final Date fosterDate = new Date(year, month, day);
    CalendarUtil.addDaysToDate(fosterDate, GXTGrid.getRandomInt(3, 60));
    final Pet pet = new Pet(nextPetId, nameAndSex[0], petTypeId, nameAndSex[1], //
                            GXTGrid.getRandomInt(2) == 0, intakeDate, fosterDate, //
                            new BigDecimal(GXTGrid.getRandomInt(3000, 10000) / 100.0));
    listStore.add(pet);
    ++nextPetId;
  }
}
//--------------------------------------------------------------------------------------------------
private static final String[] PetTypeDefs = new String[] {"Cat,Cat", "Dog,Dog",
    "Hedgehog,Hedgehog", "Invisible Pink Unicorn,IPU"};
public static void loadPetTypeTestData(final ListStore<PetType> listStore) {
  listStore.clear();
  int nextPetTypeId = 1;
  for (final String petTypeDef : PetTypeDefs) {
    final String[] petTypeFields = petTypeDef.split(",");
    final PetType petType = new PetType(nextPetTypeId, petTypeFields[0], petTypeFields[1]);
    listStore.add(petType);
    ++nextPetTypeId;
  }
}
//--------------------------------------------------------------------------------------------------
}