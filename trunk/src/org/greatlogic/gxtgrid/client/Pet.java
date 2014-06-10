package org.greatlogic.gxtgrid.client;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import com.sencha.gxt.core.client.ValueProvider;

public class Pet {
//--------------------------------------------------------------------------------------------------
private BigDecimal _adoptionFee;
private Date       _fosterDate;
private Date       _intakeDate;
private final int  _petId;
private String     _petName;
private final int  _petTypeId;
private String     _sex;
protected boolean  _trained;
//--------------------------------------------------------------------------------------------------
public static ValueProvider<Pet, BigDecimal> getAdoptionFeeValueProvider() {
  return new ValueProvider<Pet, BigDecimal>() {
    @Override
    public String getPath() {
      return "Pet.AdoptionFee";
    }
    @Override
    public BigDecimal getValue(final Pet pet) {
      return pet._adoptionFee;
    }
    @Override
    public void setValue(final Pet pet, final BigDecimal value) {
      pet._adoptionFee = value.setScale(2, RoundingMode.HALF_UP);
    }
  };
}
//--------------------------------------------------------------------------------------------------
public static ValueProvider<Pet, Date> getFosterDateValueProvider() {
  return new ValueProvider<Pet, Date>() {
    @Override
    public String getPath() {
      return "Pet.FosterDate";
    }
    @Override
    public Date getValue(final Pet pet) {
      return pet._fosterDate;
    }
    @Override
    public void setValue(final Pet pet, final Date value) {
      pet._fosterDate = value;
    }
  };
}
//--------------------------------------------------------------------------------------------------
public static ValueProvider<Pet, Date> getIntakeDateValueProvider() {
  return new ValueProvider<Pet, Date>() {
    @Override
    public String getPath() {
      return "Pet.IntakeDate";
    }
    @Override
    public Date getValue(final Pet pet) {
      return pet._intakeDate;
    }
    @Override
    public void setValue(final Pet pet, final Date value) {
      pet._intakeDate = value;
    }
  };
}
//--------------------------------------------------------------------------------------------------
public static ValueProvider<Pet, String> getPetNameValueProvider() {
  return new ValueProvider<Pet, String>() {
    @Override
    public String getPath() {
      return "Pet.PetName";
    }
    @Override
    public String getValue(final Pet pet) {
      return pet._petName;
    }
    @Override
    public void setValue(final Pet pet, final String value) {
      pet._petName = value;
    }
  };
}
//--------------------------------------------------------------------------------------------------
public static ValueProvider<Pet, String> getSexValueProvider() {
  return new ValueProvider<Pet, String>() {
    @Override
    public String getPath() {
      return "Pet.Sex";
    }
    @Override
    public String getValue(final Pet pet) {
      return pet._sex;
    }
    @Override
    public void setValue(final Pet pet, final String value) {
      pet._sex = value;
    }
  };
}
//--------------------------------------------------------------------------------------------------
public static ValueProvider<Pet, Boolean> getTrainedFlagValueProvider() {
  return new ValueProvider<Pet, Boolean>() {
    @Override
    public String getPath() {
      return "Pet.TrainedFlag";
    }
    @Override
    public Boolean getValue(final Pet pet) {
      return pet._trained;
    }
    @Override
    public void setValue(final Pet pet, final Boolean value) {
      pet._trained = value;
    }
  };
}
//--------------------------------------------------------------------------------------------------
public Pet(final int petId, final String petName, final int petTypeId, final String sex,
           final boolean trained, final Date intakeDate, final Date fosterDate,
           final BigDecimal adoptionFee) {
  _petId = petId;
  _petName = petName;
  _petTypeId = petTypeId;
  _sex = sex;
  _trained = trained;
  _intakeDate = intakeDate;
  _fosterDate = fosterDate;
  _adoptionFee = adoptionFee.setScale(2, RoundingMode.HALF_UP);
}
//--------------------------------------------------------------------------------------------------
public int getPetId() {
  return _petId;
}
//--------------------------------------------------------------------------------------------------
}