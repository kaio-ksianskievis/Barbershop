import { useState } from 'react';

const Login = ({ onLoginSuccess, onNavigateToRegister }) => {
  // Estados para os campos do formulário (devem bater com seu LoginRequestRecord)
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    setError(''); // Limpa erros anteriores

    try {
      // Chamada para o seu backend Spring Boot
      const response = await fetch('http://localhost:8080/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        // O corpo segue exatamente o seu LoginRequestRecord
        body: JSON.stringify({ email, password }),
      });

      if (response.ok) {
        // Recebe o LoginResponseRecord que contém o token
        const data = await response.json();

        // Salva no localStorage para persistência
        localStorage.setItem('token', data.token);

        // Avisa o App.jsx que o login deu certo
        onLoginSuccess(data.token);
      } else {
        // Tratamento de erro baseado nas respostas do seu GlobalHandler
        setError('E-mail ou senha inválidos. Verifique se sua conta foi verificada por e-mail.');
      }
    } catch (err) {
      setError('Não foi possível conectar ao servidor. Verifique se o backend está rodando.');
    }
  };

  return (
    <div className="login-box">
      <h2>Entrar na Barbearia</h2>

      {error && <p style={{ color: 'red', fontSize: '0.9rem' }}>{error}</p>}

      <form onSubmit={handleLogin}>
        <div className="input-group">
          <label>E-mail</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            placeholder="seu@email.com"
          />
        </div>

        <div className="input-group">
          <label>Senha</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            placeholder="********"
          />
        </div>

        <button type="submit" className="login-btn">Acessar Agenda</button>
      </form>

      <div className="auth-footer">
        <p>Ainda não tem uma conta?</p>
        <button onClick={onNavigateToRegister} className="secondary-btn">
          Criar conta agora
        </button>
      </div>
    </div>
  );
};

export default Login;