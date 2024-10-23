package com.hospital.app.services.def;

public interface IWriteService<Entity, CreateDTO, UpdateDTO, DataTypeOfPrimaryKey> {
    Entity create(CreateDTO dto);
    void update(DataTypeOfPrimaryKey id, UpdateDTO dto);
    void delete(DataTypeOfPrimaryKey id);
}
