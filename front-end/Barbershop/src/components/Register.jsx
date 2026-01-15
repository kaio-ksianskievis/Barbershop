import { useState } from 'react';

function Register({ onNavigateToLogin }) {
  // 1. Estados para o formulário de cadastro (deve bater com RegisterRequestRecord)
  const [formData, setFormData] = useState({
    nome: '',
    email: '',
    password: '',
    role: 'USER' // Padrão definido no seu enum UserRole
  });

  // 2. Estados para controlar o fluxo de verificação
  const [isVerifying, setIsVerifying] = useState(false);
  const [codigoVerificacao, setCodigoVerificacao] = useState('');
  const [error, setError] = useState('');

  // Função para criar a conta
  const handleRegister = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const response = await fetch('http://localhost:8080/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        // Envia os dados para o seu UserController
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        // Se o cadastro deu certo, o e-mail já foi enviado pelo backend
        setIsVerifying(true);
      } else {
        const data = await response.json();
        setError(data.erro || 'Falha ao realizar cadastro.');
      }
    } catch (err) {
      setError('Erro de conexão com o servidor.');
    }
  };

  // Função para enviar o código de 6 dígitos
  const handleVerify = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const response = await fetch('http://localhost:8080/verify', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        // Envia o código para o seu DTO VerificationCode
        body: JSON.stringify({ code: codigoVerificacao })
      });

      if (response.ok) {
        alert("E-mail verificado com sucesso! Agora você pode fazer login.");
        onNavigateToLogin(); // Volta para a tela de login
      } else {
        setError('Código inválido ou expirado.');
      }
    } catch (err) {
      setError('Erro ao verificar código.');
    }
  };

  // Renderização Condicional: Se estiver verificando, mostra campo de código. Senão, mostra cadastro.
  return (
    <div className="register-container">
      {isVerifying ? (
        <div className="verify-box">
          <h2>Verifique seu E-mail</h2>
          <p>Enviamos um código de 6 dígitos para <strong>{formData.email}</strong></p>

          {error && <p style={{ color: 'red' }}>{error}</p>}

          <form onSubmit={handleVerify}>
            <div className="input-group">
              <label>Código de Verificação</label>
              <input
                type="text"
                maxLength="6"
                placeholder="000000"
                value={codigoVerificacao}
                onChange={(e) => setCodigoVerificacao(e.target.value)}
                required
              />
            </div>
            <button type="submit" className="login-btn">Ativar Conta</button>
          </form>
        </div>
      ) : (
        <div className="form-box">
          <h2>Criar Conta</h2>

          {error && <p style={{ color: 'red' }}>{error}</p>}

          <form onSubmit={handleRegister}>
            <div className="input-group">
              <label>Nome Completo</label>
              <input
                type="text"
                value={formData.nome}
                onChange={(e) => setFormData({...formData, nome: e.target.value})}
                required
              />
            </div>

            <div className="input-group">
              <label>E-mail</label>
              <input
                type="email"
                value={formData.email}
                onChange={(e) => setFormData({...formData, email: e.target.value})}
                required
              />
            </div>

            <div className="input-group">
              <label>Senha</label>
              <input
                type="password"
                value={formData.password}
                onChange={(e) => setFormData({...formData, password: e.target.value})}
                required
                minLength={8} // Bate com a validação do seu Record
              />
            </div>

            <button type="submit" className="login-btn">Cadastrar</button>
          </form>

          <div className="auth-footer">
            <button onClick={onNavigateToLogin} className="link-btn">
              Já tem conta? Faça Login
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default Register;