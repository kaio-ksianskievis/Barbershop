import { useState } from 'react';
import Login from './components/Login';
import Register from './components/Register';
import Agendamentos from './components/Agendamentos';
import './App.css';

function App() {
  // Estado para armazenar o token JWT gerado pelo seu TokenService
  const [token, setToken] = useState(localStorage.getItem('token'));

  // Estado para alternar entre tela de Login e Cadastro
  const [isRegistering, setIsRegistering] = useState(false);

  // Função para deslogar
  const handleLogout = () => {
    localStorage.removeItem('token');
    setToken(null);
  };

  // 1. Se o usuário já tem um token, mostramos a área logada (Dashboard)
  if (token) {
    return (
      <div className="dashboard">
        <header>
                <h1>💈 Barbershop</h1>
                <button onClick={handleLogout}>Sair</button>
              </header>
              <main>
                <Agendamentos />
              </main>
      </div>
    );
  }

  // 2. Se não estiver logado, alternamos entre Login e Cadastro
  return (
    <div className="auth-container">
      {isRegistering ? (
        // Passamos uma função para o Register conseguir voltar para o Login
        <Register onNavigateToLogin={() => setIsRegistering(false)} />
      ) : (
        // Passamos a função setToken para o Login salvar o token quando a API responder
        <Login
          onLoginSuccess={(newToken) => setToken(newToken)}
          onNavigateToRegister={() => setIsRegistering(true)}
        />
      )}
    </div>
  );
}

export default App;