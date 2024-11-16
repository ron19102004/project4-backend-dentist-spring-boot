package com.hospital.core.services.def;

public interface IWriteService<Entity, CreateDTO, UpdateDTO, DataTypeOfPrimaryKey> {
    Entity create(final CreateDTO dto);
    void update(final DataTypeOfPrimaryKey id, final UpdateDTO dto);
    void delete(final DataTypeOfPrimaryKey id);
}
