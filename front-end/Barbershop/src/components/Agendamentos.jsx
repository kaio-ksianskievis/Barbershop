import { useState, useEffect } from 'react';

const Agendamentos = () => {
  const [meusAgendamentos, setMeusAgendamentos] = useState([]);
  const [dataSelecionada, setDataSelecionada] = useState('');
  const [horariosLivres, setHorariosLivres] = useState([]);
  const [loading, setLoading] = useState(false);
  const token = localStorage.getItem('token');

  // 1. Carrega os agendamentos do usuário logado
  const carregarMeusAgendamentos = async () => {
    try {
      const response = await fetch('http://localhost:8080/agendamentos/me', {
        headers: { 'Authorization': `Bearer ${token}` } //
      });
      if (response.ok) {
        const data = await response.json();
        setMeusAgendamentos(data);
      }
    } catch (err) {
      console.error("Erro ao carregar agendamentos:", err);
    }
  };

  useEffect(() => {
    if (token) carregarMeusAgendamentos();
  }, [token]);

  // 2. Busca horários disponíveis na data escolhida
  const buscarHorariosLivres = async (data) => {
    setDataSelecionada(data);
    if (!data) return;

    setLoading(true);
    try {
      const response = await fetch(`http://localhost:8080/horarios/${data}`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      if (response.ok) {
        const horarios = await response.json();
        setHorariosLivres(horarios);
      }
    } catch (err) {
      console.error("Erro ao buscar horários livres:", err);
    } finally {
      setLoading(false);
    }
  };

  // 3. Função de Agendamento Corrigida (Formato LocalDateTime)
  const agendarHorario = async (hora) => {
    // Garante que a hora tenha o formato HH:mm:ss para o Java
    const horaComSegundos = hora.length === 5 ? `${hora}:00` : hora;
    const dataHoraISO = `${dataSelecionada}T${horaComSegundos}`;

    try {
      const response = await fetch('http://localhost:8080/agendamentos', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        // Envia o JSON esperado pelo seu Record
        body: JSON.stringify({ dataHora: dataHoraISO })
      });

      if (response.ok) {
        alert("Horário agendado com sucesso!");
        carregarMeusAgendamentos(); // Atualiza a lista do usuário
        buscarHorariosLivres(dataSelecionada); // Remove o horário que acabou de ser ocupado
      } else {
        const erro = await response.json();
        // Exibe a mensagem de erro da sua Regra de Negócio (ex: Barbearia fechada)
        alert("Erro: " + (erro.erro || "Falha ao agendar"));
      }
    } catch (err) {
      alert("Erro ao conectar com o servidor.");
    }
  };

  // 4. Cancelar Agendamento
  const cancelarAgendamento = async (id) => {
    if (!confirm("Deseja realmente cancelar este horário?")) return;

    try {
      const response = await fetch(`http://localhost:8080/agendamentos/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${token}` }
      });

      if (response.ok) {
        alert("Agendamento cancelado.");
        carregarMeusAgendamentos();
        if (dataSelecionada) buscarHorariosLivres(dataSelecionada);
      }
    } catch (err) {
      console.error("Erro ao cancelar:", err);
    }
  };

  return (
    <div className="agendamento-container">
      <div className="card-secao">
        <h2>Novo Agendamento</h2>
        <p>Selecione uma data para ver os horários disponíveis:</p>
        <input
          type="date"
          className="date-picker"
          value={dataSelecionada}
          onChange={(e) => buscarHorariosLivres(e.target.value)}
          min={new Date().toISOString().split('T')[0]} // Impede agendar no passado
        />

        {loading ? (
          <p>Consultando agenda...</p>
        ) : (
          <div className="grid-horarios">
            {horariosLivres.map(hora => (
              <button
                key={hora}
                className="hora-btn"
                onClick={() => agendarHorario(hora)}
              >
                {hora.substring(0, 5)}
              </button>
            ))}
            {dataSelecionada && horariosLivres.length === 0 && !loading && (
              <p className="aviso">Não há horários disponíveis para esta data.</p>
            )}
          </div>
        )}
      </div>

      <div className="card-secao">
        <h2>Meus Horários</h2>
        <div className="lista-agendamentos">
          {meusAgendamentos.length === 0 && <p>Você ainda não possui agendamentos.</p>}
          {meusAgendamentos.map(ag => (
            <div key={ag.id} className="agendamento-item">
              <div>
                <strong>{new Date(ag.dataHora).toLocaleDateString('pt-BR')}</strong>
                <span> às {new Date(ag.dataHora).toLocaleTimeString('pt-BR', {hour: '2-digit', minute:'2-digit'})}</span>
              </div>
              <button
                className="btn-cancelar"
                onClick={() => cancelarAgendamento(ag.id)}
              >
                Cancelar
              </button>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Agendamentos;