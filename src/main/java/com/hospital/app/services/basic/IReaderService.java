package com.hospital.app.services.basic;

import java.util.List;

public interface IReaderService<Entity,DataTypeOfPrimaryKey> {
    List<Entity> getAll();
    Entity getById(DataTypeOfPrimaryKey id);
}
