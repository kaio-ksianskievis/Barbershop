package kaio.ksianskievis.barbershop;

import kaio.ksianskievis.barbershop.Exception.RegraDeNegocioException;
import kaio.ksianskievis.barbershop.Model.Agendamento;
import kaio.ksianskievis.barbershop.Repository.AgendamentoRepository;
import kaio.ksianskievis.barbershop.Services.AgendamentoServices;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;

import  static  org.mockito.Mockito.*;
import  static  org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BarbershopApplicationTests {

	@Mock
	private AgendamentoRepository repository;

	@InjectMocks
	private AgendamentoServices services;

	@ParameterizedTest
	@ValueSource(strings = {"2026-01-03T09:00","2026-01-04T09:00"})
	@DisplayName("Deve retornar  uma exceção de RegraDeNegocio pois hoje é sabado")
	void lancaUmaExecaoPorSerFinalDeSemana(String dataHora){

		Agendamento agendamento = new Agendamento();

		LocalDateTime data = LocalDateTime.parse(dataHora);

		agendamento.setDataHora(data);

		assertThrows(RegraDeNegocioException.class,()->services.addAgendamento(agendamento));

		verify(repository,Mockito.never()).save(any());
	}

	@ParameterizedTest
	@ValueSource(strings = {"2026-01-02T07:00","2026-01-02T19:00"})
	@DisplayName("Deve retornar  uma exceção de RegraDeNegocio pois o horario é fora do expediente")
	void lancaUmaExecaoPorSerForaDoHorario(String dataHora){

		Agendamento agendamento = new Agendamento();

		LocalDateTime foraDoExpediente = LocalDateTime.parse(dataHora);

		agendamento.setDataHora(foraDoExpediente);

		assertThrows(RegraDeNegocioException.class,()->services.addAgendamento(agendamento));

		verify(repository,Mockito.never()).save(any());
	}

}
