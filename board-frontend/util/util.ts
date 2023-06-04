export interface UserCookie {
  id: number;
  username: string;
  email: string;
}

export const saveUserCookie = (cookie: UserCookie) => {
  localStorage.setItem("user", JSON.stringify(cookie));
};

export const loadUserCookie = (): UserCookie | null => {
  const item = localStorage.getItem("user");

  if (item) return JSON.parse(item) as UserCookie;
  else return null;
};

export const getServers = () => "http://localhost:8080";
