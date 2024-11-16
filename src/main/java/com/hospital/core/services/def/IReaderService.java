package com.hospital.core.services.def;

import java.util.List;

public interface IReaderService<Entity,DataTypeOfPrimaryKey> {
    List<Entity> getAll();
    Entity getById(final DataTypeOfPrimaryKey id);
}
