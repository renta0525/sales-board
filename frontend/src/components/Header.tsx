import React from 'react';

interface User {
  sub: string;
}

interface HeaderProps {
  user: User | null;
}

const Header: React.FC<HeaderProps> = ({ user }) => {
  const username = user?.sub;

  return (
    <header style={{ textAlign: 'right', padding: '1rem', color: 'white', backgroundColor: '#1976d2' }}>
      {username ? `こんにちは${username}さん` : ''}
    </header>
  );
};

export default Header;
