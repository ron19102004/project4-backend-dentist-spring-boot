package com.hospital.app.services.def;

import java.util.List;

public interface IReaderService<Entity,DataTypeOfPrimaryKey> {
    List<Entity> getAll();
    Entity getById(final DataTypeOfPrimaryKey id);
}
