import { NextPage } from "next";

interface Props {
  value: string;
  onClick(): void;
}

const AuthButton: NextPage<Props> = ({ value, onClick }) => {
  return (
    <button className="auth-button" onClick={onClick}>
      {value}
    </button>
  );
};

export default AuthButton;
