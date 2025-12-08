package com.app.contabilidad.infrastructure.adapters.persistence;

import com.app.contabilidad.domain.entities.Movimiento;
import com.app.contabilidad.domain.ports.MovimientoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia que implementa el puerto MovimientoRepositoryPort
 * Traduce entre el dominio y JPA
 */
@Component
@RequiredArgsConstructor
public class MovimientoRepositoryAdapter implements MovimientoRepositoryPort {
    private final MovimientoJpaRepository jpaRepository;

    @Override
    public Movimiento guardar(Movimiento movimiento) {
        MovimientoEntity entity = toEntity(movimiento);
        MovimientoEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Movimiento> obtenerPorId(Long id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Movimiento> obtenerTodos() {
        return jpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Movimiento actualizar(Movimiento movimiento) {
        MovimientoEntity entity = toEntity(movimiento);
        MovimientoEntity updatedEntity = jpaRepository.save(entity);
        return toDomain(updatedEntity);
    }

    @Override
    public void eliminar(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Movimiento> obtenerPorTipo(Movimiento.TipoMovimiento tipo) {
        MovimientoEntity.TipoMovimiento tipoEntity = MovimientoEntity.TipoMovimiento.valueOf(tipo.name());
        return jpaRepository.findByTipo(tipoEntity)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Movimiento> obtenerPorCategoria(String categoria) {
        return jpaRepository.findByCategoria(categoria)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    /**
     * Convierte una entidad JPA a una entidad de dominio
     */
    private Movimiento toDomain(MovimientoEntity entity) {
        return Movimiento.builder()
                .id(entity.getId())
                .descripcion(entity.getDescripcion())
                .cantidad(entity.getCantidad())
                .tipo(Movimiento.TipoMovimiento.valueOf(entity.getTipo().name()))
                .fecha(entity.getFecha())
                .categoria(entity.getCategoria())
                .notas(entity.getNotas())
                .build();
    }

    /**
     * Convierte una entidad de dominio a una entidad JPA
     */
    private MovimientoEntity toEntity(Movimiento domainEntity) {
        return MovimientoEntity.builder()
                .id(domainEntity.getId())
                .descripcion(domainEntity.getDescripcion())
                .cantidad(domainEntity.getCantidad())
                .tipo(MovimientoEntity.TipoMovimiento.valueOf(domainEntity.getTipo().name()))
                .fecha(domainEntity.getFecha())
                .categoria(domainEntity.getCategoria())
                .notas(domainEntity.getNotas())
                .build();
    }
}
