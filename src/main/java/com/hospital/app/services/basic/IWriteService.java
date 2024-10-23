package com.hospital.app.services.basic;

public interface IWriteService<Entity, CreateDTO, UpdateDTO, DataTypeOfPrimaryKey> {
    Entity create(CreateDTO createDTO);
    void update(UpdateDTO updateDTO);
    void delete(DataTypeOfPrimaryKey id);
}
