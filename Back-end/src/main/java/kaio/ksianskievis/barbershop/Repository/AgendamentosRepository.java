package kaio.ksianskievis.barbershop.Repository;

import kaio.ksianskievis.barbershop.Model.Agendamentos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AgendamentosRepository  extends JpaRepository<Agendamentos, UUID> {

    List<Agendamentos> findByNomeClientes(String nomeCliente);

    List<Agendamentos> findByData(LocalDate data);
    
}
