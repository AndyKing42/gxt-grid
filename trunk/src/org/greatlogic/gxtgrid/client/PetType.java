package org.greatlogic.gxtgrid.client;

public class PetType {
//--------------------------------------------------------------------------------------------------
private final String _petTypeDesc;
private final int    _petTypeId;
private final String _petTypeShortDesc;
//--------------------------------------------------------------------------------------------------
public PetType(final int petTypeId, final String petTypeDesc, final String petTypeShortDesc) {
  _petTypeId = petTypeId;
  _petTypeDesc = petTypeDesc;
  _petTypeShortDesc = petTypeShortDesc;
}
//--------------------------------------------------------------------------------------------------
public int getPetTypeId() {
  return _petTypeId;
}
//--------------------------------------------------------------------------------------------------
}