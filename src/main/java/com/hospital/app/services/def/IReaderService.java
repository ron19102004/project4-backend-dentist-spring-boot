package com.hospital.app.services.def;

import java.io.IOException;
import java.util.List;

public interface IReaderService<Entity,DataTypeOfPrimaryKey> {
    List<Entity> getAll();
    Entity getById(final DataTypeOfPrimaryKey id);
}
