package kaio.ksianskievis.barbershop.Repository;

import kaio.ksianskievis.barbershop.Model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, UUID> {

    List<Agendamento> findByDataHoraBetweenOrderByDataHoraAsc(LocalDateTime inicio, LocalDateTime fim);

    boolean existsByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

}
